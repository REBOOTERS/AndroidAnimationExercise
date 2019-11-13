/*
 * Copyright (C) 2017 Jared Rummler
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.engineer.imitate.shell;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Class providing functionality to execute commands in a (root) shell
 */
@SuppressWarnings("unused")
public class Shell {

  static final String[] AVAILABLE_TEST_COMMANDS = new String[]{"echo -BOC-", "id"};

  /**
   * <p>Runs commands using the supplied shell, and returns the result.</p>
   *
   * <p>When in debug mode, the code will also excessively log the commands passed to and the output returned from
   * the shell.</p>
   *
   * <p>Though this function uses background threads to gobble stdout and stderr so a deadlock does not occur if the
   * shell produces massive output, the output is still stored in a List&lt;String&gt;, and as such doing something
   * like <em>'ls -lR /'</em> will probably have you run out of memory.</p>
   *
   * @param shell
   *     The shell to use for executing the commands
   * @param commands
   *     The commands to execute
   * @return Output of the commands, or null in case of an error
   */
  @WorkerThread
  public static CommandResult run(@NonNull String shell, @NonNull String... commands) {
    return run(shell, commands, null);
  }

  /**
   * <p>Runs commands using the supplied shell, and returns the result.</p>
   *
   * <p>When in debug mode, the code will also excessively log the commands passed to and the output returned from
   * the shell.</p>
   *
   * <p>Though this function uses background threads to gobble stdout and stderr so a deadlock does not occur if the
   * shell produces massive output, the output is still stored in a List&lt;String&gt;, and as such doing something
   * like <em>'ls -lR /'</em> will probably have you run out of memory.</p>
   *
   * @param shell
   *     The shell to use for executing the commands
   * @param commands
   *     The commands to execute
   * @param env
   *     List of all environment variables (in 'key=value' format) or null for defaults
   * @return Output of the commands, or null in case of an error
   */
  @WorkerThread
  public static CommandResult run(@NonNull String shell, @NonNull String[] commands, @Nullable String[] env) {
    List<String> stdout = Collections.synchronizedList(new ArrayList<String>());
    List<String> stderr = Collections.synchronizedList(new ArrayList<String>());
    int exitCode;

    try {
      // setup our process, retrieve stdin stream, and stdout/stderr gobblers
      Process process = runWithEnv(shell, env);
      DataOutputStream stdin = new DataOutputStream(process.getOutputStream());
      StreamGobbler stdoutGobbler = new StreamGobbler(process.getInputStream(), stdout);
      StreamGobbler stderrGobbler = new StreamGobbler(process.getErrorStream(), stderr);

      // start gobbling and write our commands to the shell
      stdoutGobbler.start();
      stderrGobbler.start();
      try {
        for (String write : commands) {
          stdin.write((write + "\n").getBytes("UTF-8"));
          stdin.flush();
        }
        stdin.write("exit\n".getBytes("UTF-8"));
        stdin.flush();
      } catch (IOException e) {
        //noinspection StatementWithEmptyBody
        if (e.getMessage().contains("EPIPE") || e.getMessage().contains("Stream closed")) {
          // Method most horrid to catch broken pipe, in which case we do nothing. The command is not a shell, the
          // shell closed stdin, the script already contained the exit command, etc. these cases we want the output
          // instead of returning null
        } else {
          // other issues we don't know how to handle, leads to returning null
          throw e;
        }
      }

      // wait for our process to finish, while we gobble away in the background
      exitCode = process.waitFor();

      // make sure our threads are done gobbling, our streams are closed, and the process is destroyed - while the
      // latter two shouldn't be needed in theory, and may even produce warnings, in "normal" Java they are required
      // for guaranteed cleanup of resources, so lets be safe and do this on Android as well
      try {
        stdin.close();
      } catch (IOException e) {
        // might be closed already
      }
      stdoutGobbler.join();
      stderrGobbler.join();
      process.destroy();
    } catch (InterruptedException e) {
      exitCode = ShellExitCode.WATCHDOG_EXIT;
    } catch (IOException e) {
      exitCode = ShellExitCode.SHELL_WRONG_UID;
    }

    return new CommandResult(stdout, stderr, exitCode);
  }

  /**
   * <p>This code is adapted from java.lang.ProcessBuilder.start().</p>
   *
   * <p>The problem is that Android doesn't allow us to modify the map returned by ProcessBuilder.environment(), even
   * though the JavaDoc indicates that it should. This is because it simply returns the SystemEnvironment object that
   * System.getenv() gives us. The relevant portion in the source code is marked as "// android changed", so
   * presumably it's not the case in the original version of the Apache Harmony project.</p>
   *
   * @param command
   *     The name of the program to execute. E.g. "su" or "sh".
   * @param environment
   *     List of all environment variables (in 'key=value' format) or null for defaults
   * @return new {@link Process} instance.
   * @throws IOException
   *     if the requested program could not be executed.
   */
  @WorkerThread
  public static Process runWithEnv(@NonNull String command, @Nullable String[] environment) throws IOException {
    if (environment != null) {
      Map<String, String> newEnvironment = new HashMap<>();
      newEnvironment.putAll(System.getenv());
      int split;
      for (String entry : environment) {
        if ((split = entry.indexOf("=")) >= 0) {
          newEnvironment.put(entry.substring(0, split), entry.substring(split + 1));
        }
      }
      int i = 0;
      environment = new String[newEnvironment.size()];
      for (Map.Entry<String, String> entry : newEnvironment.entrySet()) {
        environment[i] = entry.getKey() + "=" + entry.getValue();
        i++;
      }
    }
    return Runtime.getRuntime().exec(command, environment);
  }

  /**
   * <p>This code is adapted from java.lang.ProcessBuilder.start().</p>
   *
   * <p>The problem is that Android doesn't allow us to modify the map returned by ProcessBuilder.environment(), even
   * though the JavaDoc indicates that it should. This is because it simply returns the SystemEnvironment object that
   * System.getenv() gives us. The relevant portion in the source code is marked as "// android changed", so
   * presumably it's not the case in the original version of the Apache Harmony project.</p>
   *
   * @param command
   *     The name of the program to execute. E.g. "su" or "sh".
   * @param environment
   *     Map of all environment variables
   * @return new {@link Process} instance.
   * @throws IOException
   *     if the requested program could not be executed.
   */
  @WorkerThread
  public static Process runWithEnv(@NonNull String command, Map<String, String> environment) throws IOException {
    String[] env;
    if (environment != null && environment.size() != 0) {
      Map<String, String> newEnvironment = new HashMap<>();
      newEnvironment.putAll(System.getenv());
      newEnvironment.putAll(environment);
      int i = 0;
      env = new String[newEnvironment.size()];
      for (Map.Entry<String, String> entry : newEnvironment.entrySet()) {
        env[i] = entry.getKey() + "=" + entry.getValue();
        i++;
      }
    } else {
      env = null;
    }
    return Runtime.getRuntime().exec(command, env);
  }

  /**
   * See if the shell is alive, and if so, check the UID
   *
   * @param stdout
   *     Standard output from running AVAILABLE_TEST_COMMANDS
   * @param checkForRoot
   *     true if we are expecting this shell to be running as root
   * @return true on success, false on error
   */
  static boolean parseAvailableResult(List<String> stdout, boolean checkForRoot) {
    if (stdout == null) {
      return false;
    }

    // this is only one of many ways this can be done
    boolean echoSeen = false;

    for (String line : stdout) {
      if (line.contains("uid=")) {
        // id command is working, let's see if we are actually root
        return !checkForRoot || line.contains("uid=0");
      } else if (line.contains("-BOC-")) {
        // if we end up here, at least the su command starts some kind of shell, let's hope it has root privileges -
        // no way to know without additional native binaries
        echoSeen = true;
      }
    }

    return echoSeen;
  }

  /**
   * This class provides utility functions to easily execute commands using SH
   */
  public static class SH {

    private static volatile Console console;

    /**
     * <p>The {@link Console} is used to keep the shell open for long periods of time so a new shell does not need to
     * be created each time you run a command. Because this shell remains open, any commands sent to execute on it may
     * need to wait for previous commands to finish. If you need to execute long running commands consider building
     * your own interactive shell using {@link Builder} or {@link Console.Builder}.</p>
     *
     * @return The {@link Console} instance for running commands in a normal shell.
     * @throws ShellNotFoundException
     */
    @WorkerThread
    public static Console getConsole() throws ShellNotFoundException {
      if (console == null || console.isClosed()) {
        synchronized (SH.class) {
          if (console == null || console.isClosed()) {
            console = new Console.Builder().useSH().setWatchdogTimeout(30).build();
          }
        }
      }
      return console;
    }

    /**
     * Closes the console if open
     */
    public static void closeConsole() {
      if (console != null) {
        synchronized (SH.class) {
          if (console != null) {
            console.close();
            console = null;
          }
        }
      }
    }

    /**
     * <p>Runs commands and return output.</p>
     *
     * <p>A new shell is opened each time a command is run. If you want to keep the shell open then use
     * {@link Shell.Interactive}, {@link Console} or {@link #getConsole()}</p>
     *
     * @param commands
     *     The commands to run
     * @return Output of the commands, or null in case of an error
     */
    @WorkerThread
    public static CommandResult run(@NonNull String... commands) {
      return Shell.run("sh", commands);
    }

  }

  /**
   * This class provides utility functions to easily execute commands using SU (root shell), as well as detecting
   * whether or not root is available, and if so which version.
   */
  public static class SU {

    private static Boolean isSELinuxEnforcing = null;
    private static String[] suVersion = new String[]{null, null};
    private static volatile Console console;

    /**
     * <p>The {@link Console} is used to keep the shell open for long periods of time so a new shell does not need to
     * be created each time you run a command. Because this shell remains open, any commands sent to execute on it may
     * need to wait for previous commands to finish. If you need to execute long running commands consider building
     * your own interactive shell using {@link Builder} or {@link Console.Builder}.</p>
     *
     * @return The {@link Console} instance for running commands in a root shell.
     * @throws ShellNotFoundException
     */
    @WorkerThread
    public static Console getConsole() throws ShellNotFoundException {
      if (console == null || console.isClosed()) {
        synchronized (SH.class) {
          if (console == null || console.isClosed()) {
            console = new Console.Builder().useSU().setWatchdogTimeout(30).build();
          }
        }
      }
      return console;
    }

    /**
     * Closes the console if open
     */
    public static void closeConsole() {
      if (console != null) {
        synchronized (SU.class) {
          if (console != null) {
            console.close();
            console = null;
          }
        }
      }
    }

    /**
     * <p>Runs commands as root (if available) and return output.</p>
     *
     * <p>The commands are run in an interactive shell that remains open after the commands finish execution. This is
     * done so the superuser management app does not continually show toast messages for granting root access. If you
     * want to run commands in a shell that exits on completion use {@link Shell#run(String, String...)} or
     * {@link Shell#run(String, String[], String[])}. You can also open your own interactive shell using {@link
     * Builder} or {@link Console.Builder}.</p>
     *
     * @param commands
     *     The commands to run
     * @return Output of the commands, or null if root isn't available or in case of an error
     */
    @WorkerThread
    public static CommandResult run(@NonNull String... commands) {
      try {
        Console console = SU.getConsole();
        return console.run(commands);
      } catch (ShellNotFoundException e) {
        return new CommandResult(
            Collections.<String>emptyList(), Collections.<String>emptyList(), ShellExitCode.SHELL_NOT_FOUND);
      }
    }

    /**
     * Detects whether or not superuser access is available, by checking the output of the "id"
     * command if available, checking if a shell runs at all otherwise
     *
     * @return {@code true} if superuser access is available
     */
    @WorkerThread
    public static boolean available() {
      // this is only one of many ways this can be done
      CommandResult result = run(Shell.AVAILABLE_TEST_COMMANDS);
      return Shell.parseAvailableResult(result.stdout, true);
    }

    /**
     * <p>Detects the version of the su binary installed (if any), if supported by the binary. Most binaries support
     * two different version numbers, the public version that is displayed to users, and an internal version number
     * that is used for version number comparisons. Returns null if su not available or retrieving the version isn't
     * supported.</p>
     *
     * <p>Note that su binary version and GUI (APK) version can be completely different.</p>
     *
     * <p>This function caches its result to improve performance on multiple calls</p>
     *
     * @param internal
     *     Request human-readable version or application internal version
     * @return String containing the su version or null
     */
    @WorkerThread
    public static synchronized String version(boolean internal) {
      int idx = internal ? 0 : 1;
      if (suVersion[idx] == null) {
        String version = null;

        CommandResult result = Shell.run(internal ? "su -V" : "su -v", "exit");

        for (String line : result.stdout) {
          if (!internal) {
            if (!line.trim().equals("")) {
              version = line;
              break;
            }
          } else {
            try {
              if (Integer.parseInt(line) > 0) {
                version = line;
                break;
              }
            } catch (NumberFormatException e) {
              // should be parsable, try next line otherwise
            }
          }
        }

        suVersion[idx] = version;
      }
      return suVersion[idx];
    }

    /**
     * Attempts to deduce if the shell command refers to a su shell
     *
     * @param shell
     *     Shell command to run
     * @return Shell command appears to be su
     */
    public static boolean isSU(@NonNull String shell) {
      // Strip parameters
      int pos = shell.indexOf(' ');
      if (pos >= 0) {
        shell = shell.substring(0, pos);
      }

      // Strip path
      pos = shell.lastIndexOf('/');
      if (pos >= 0) {
        shell = shell.substring(pos + 1);
      }

      return shell.equals("su");
    }

    /**
     * Constructs a shell command to start a su shell using the supplied uid and SELinux context.
     * This is can be an expensive operation, consider caching the result.
     *
     * @param uid
     *     Uid to use (0 == root)
     * @param context
     *     (SELinux) context name to use or null
     * @return Shell command
     */
    public static String shell(int uid, @Nullable String context) {
      // su[ --context <context>][ <uid>]
      String shell = "su";

      if ((context != null) && isSELinuxEnforcing()) {
        String display = version(false);
        String internal = version(true);

        // We only know the format for SuperSU v1.90+ right now
        if ((display != null) &&
            (internal != null) &&
            (display.endsWith("SUPERSU")) &&
            (Integer.valueOf(internal) >= 190)) {
          shell = String.format(Locale.ENGLISH, "%s --context %s", shell, context);
        }
      }

      // Most su binaries support the "su <uid>" format, but in case they don't, lets skip it for
      // the default 0 (root) case
      if (uid > 0) {
        shell = String.format(Locale.ENGLISH, "%s %d", shell, uid);
      }

      return shell;
    }

    /**
     * Constructs a shell command to start a su shell connected to mount master daemon, to perform public mounts on
     * Android 4.3+ (or 4.2+ in SELinux enforcing mode)
     *
     * @return Shell command
     */
    public static String shellMountMaster() {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        return "su --mount-master";
      }
      return "su";
    }

    /**
     * Detect if SELinux is set to enforcing, caches result
     *
     * @return true if SELinux set to enforcing, or false in the case of permissive or not present
     */
    @WorkerThread
    public static synchronized boolean isSELinuxEnforcing() {
      if (isSELinuxEnforcing == null) {
        Boolean enforcing = null;

        // First known firmware with SELinux built-in was a 4.2 (17) leak
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
          // Detect enforcing through sysfs, not always present
          File f = new File("/sys/fs/selinux/enforce");
          if (f.exists()) {
            InputStream is = null;
            try {
              is = new FileInputStream("/sys/fs/selinux/enforce");
              enforcing = (is.read() == '1');
            } catch (Exception e) {
              // we might not be allowed to read, thanks SELinux
            } finally {
              if (is != null) {
                try {
                  is.close();
                } catch (IOException ignored) {
                }
              }
            }
          }

          // 4.4+ builds are enforcing by default, take the gamble
          if (enforcing == null) {
            try {
              Class<?> SELinux = Class.forName("android.os.SELinux");
              Method isSELinuxEnforced = SELinux.getMethod("isSELinuxEnforced");
              if (!isSELinuxEnforced.isAccessible()) isSELinuxEnforced.setAccessible(true);
              enforcing = (Boolean) isSELinuxEnforced.invoke(SELinux.newInstance());
            } catch (Exception e) {
              // 4.4+ release builds are enforcing by default, take the gamble
              enforcing = Build.VERSION.SDK_INT >= 19;
            }
          }
        }

        if (enforcing == null) {
          enforcing = false;
        }

        isSELinuxEnforcing = enforcing;
      }
      return isSELinuxEnforcing;
    }

    /**
     * <p>Clears results cached by isSELinuxEnforcing() and version(boolean internal) calls.</p>
     *
     * <p>Most apps should never need to call this, as neither enforcing status nor su version is
     * likely to change on a running device - though it is not impossible.</p>
     */
    public static synchronized void clearCachedResults() {
      isSELinuxEnforcing = null;
      suVersion[0] = null;
      suVersion[1] = null;
    }

  }

  /**
   * Command result callback, notifies the recipient of the completion of a command block, including the (last) exit
   * code, and the full output
   */
  public interface OnCommandResultListener extends ShellExitCode {

    /**
     * <p>Command result callback</p>
     *
     * <p>Depending on how and on which thread the shell was created, this callback may be executed on one of the
     * gobbler threads. In that case, it is important the callback returns as quickly as possible, as delays in this
     * callback may pause the native process or even result in a deadlock</p>
     *
     * <p>See {@link Shell.Interactive} for threading details</p>
     *
     * @param commandCode
     *     Value previously supplied to addCommand
     * @param exitCode
     *     Exit code of the last command in the block
     * @param output
     *     All output generated by the command block
     */
    void onCommandResult(int commandCode, int exitCode, List<String> output);
  }

  /**
   * Command per line callback for parsing the output line by line without buffering It also notifies the recipient
   * of the completion of a command block, including the (last) exit code.
   */
  public interface OnCommandLineListener extends ShellExitCode, StreamGobbler.OnLineListener {

    /**
     * <p>Command result callback</p>
     *
     * <p>Depending on how and on which thread the shell was created, this callback may be executed on one of the
     * gobbler threads. In that case, it is important the callback returns as quickly as possible, as delays in this
     * callback may pause the native process or even result in a deadlock</p>
     *
     * <p>See {@link Shell.Interactive} for threading details</p>
     *
     * @param commandCode
     *     Value previously supplied to addCommand
     * @param exitCode
     *     Exit code of the last command in the block
     */
    void onCommandResult(int commandCode, int exitCode);
  }

  /**
   * Internal class to store command block properties
   */
  private static class Command {

    private static int commandCounter = 0;

    private final String[] commands;
    private final int code;
    private final OnCommandResultListener onCommandResultListener;
    private final OnCommandLineListener onCommandLineListener;
    private final String marker;

    public Command(String[] commands, int code, OnCommandResultListener onCommandResultListener,
                   OnCommandLineListener onCommandLineListener) {
      this.commands = commands;
      this.code = code;
      this.onCommandResultListener = onCommandResultListener;
      this.onCommandLineListener = onCommandLineListener;
      this.marker = UUID.randomUUID().toString() + String.format("-%08x", ++commandCounter);
    }
  }

  /**
   * Builder class for {@link Shell.Interactive}
   */
  public static class Builder {

    private Map<String, String> environment = new HashMap<>();
    private List<Command> commands = new LinkedList<>();
    private StreamGobbler.OnLineListener onStdoutLineListener;
    private StreamGobbler.OnLineListener onStderrLineListener;
    private Handler handler;
    private boolean autoHandler = true;
    private boolean wantStderr;
    private String shell = "sh";
    private int watchdogTimeout;

    /**
     * <p>Set a custom handler that will be used to post all callbacks to</p>
     *
     * <p>See {@link Shell.Interactive} for further details on threading and handlers</p>
     *
     * @param handler
     *     Handler to use
     * @return This Builder object for method chaining
     */
    public Builder setHandler(Handler handler) {
      this.handler = handler;
      return this;
    }

    /**
     * <p>Automatically create a handler if possible ? Default to true</p>
     *
     * <p>See {@link Shell.Interactive} for further details on threading and handlers</p>
     *
     * @param autoHandler
     *     Auto-create handler ?
     * @return This Builder object for method chaining
     */
    public Builder setAutoHandler(boolean autoHandler) {
      this.autoHandler = autoHandler;
      return this;
    }

    /**
     * Set shell binary to use. Usually "sh" or "su", do not use a full path unless you have a good
     * reason to
     *
     * @param shell
     *     Shell to use
     * @return This Builder object for method chaining
     */
    public Builder setShell(String shell) {
      this.shell = shell;
      return this;
    }

    /**
     * Convenience function to set "sh" as used shell
     *
     * @return This Builder object for method chaining
     */
    public Builder useSH() {
      return setShell("sh");
    }

    /**
     * Convenience function to set "su" as used shell
     *
     * @return This Builder object for method chaining
     */
    public Builder useSU() {
      return setShell("su");
    }

    /**
     * Set if error output should be appended to command block result output
     *
     * @param wantStderr
     *     Want error output ?
     * @return This Builder object for method chaining
     */
    public Builder setWantStderr(boolean wantStderr) {
      this.wantStderr = wantStderr;
      return this;
    }

    /**
     * Add or update an environment variable
     *
     * @param key
     *     Key of the environment variable
     * @param value
     *     Value of the environment variable
     * @return This Builder object for method chaining
     */
    public Builder addEnvironment(String key, String value) {
      environment.put(key, value);
      return this;
    }

    /**
     * Add or update environment variables
     *
     * @param addEnvironment
     *     Map of environment variables
     * @return This Builder object for method chaining
     */
    public Builder addEnvironment(Map<String, String> addEnvironment) {
      environment.putAll(addEnvironment);
      return this;
    }

    /**
     * Add a command to execute
     *
     * @param command
     *     Command to execute
     * @return This Builder object for method chaining
     */
    public Builder addCommand(String command) {
      return addCommand(command, 0, null);
    }

    /**
     * <p>Add a command to execute, with a callback to be called on completion</p>
     *
     * <p>The thread on which the callback executes is dependent on various factors, see {@link Shell.Interactive}
     * for further details</p>
     *
     * @param command
     *     Command to execute
     * @param code
     *     User-defined value passed back to the callback
     * @param onCommandResultListener
     *     Callback to be called on completion
     * @return This Builder object for method chaining
     */
    public Builder addCommand(String command, int code, OnCommandResultListener onCommandResultListener) {
      return addCommand(new String[]{
          command
      }, code, onCommandResultListener);
    }

    /**
     * Add commands to execute
     *
     * @param commands
     *     Commands to execute
     * @return This Builder object for method chaining
     */
    public Builder addCommand(List<String> commands) {
      return addCommand(commands, 0, null);
    }

    /**
     * <p>Add commands to execute, with a callback to be called on completion (of all commands)</p>
     *
     * <p>The thread on which the callback executes is dependent on various factors, see {@link Shell.Interactive}
     * for further details</p>
     *
     * @param commands
     *     Commands to execute
     * @param code
     *     User-defined value passed back to the callback
     * @param onCommandResultListener
     *     Callback to be called on completion (of all commands)
     * @return This Builder object for method chaining
     */
    public Builder addCommand(List<String> commands, int code, OnCommandResultListener onCommandResultListener) {
      return addCommand(commands.toArray(new String[commands.size()]), code,
          onCommandResultListener);
    }

    /**
     * Add commands to execute
     *
     * @param commands
     *     Commands to execute
     * @return This Builder object for method chaining
     */
    public Builder addCommand(String[] commands) {
      return addCommand(commands, 0, null);
    }

    /**
     * <p>Add commands to execute, with a callback to be called on completion (of all commands)</p>
     *
     * <p>The thread on which the callback executes is dependent on various factors, see {@link Shell.Interactive}
     * for further details</p>
     *
     * @param commands
     *     Commands to execute
     * @param code
     *     User-defined value passed back to the callback
     * @param onCommandResultListener
     *     Callback to be called on completion
     *     (of all commands)
     * @return This Builder object for method chaining
     */
    public Builder addCommand(String[] commands, int code, OnCommandResultListener onCommandResultListener) {
      this.commands.add(new Command(commands, code, onCommandResultListener, null));
      return this;
    }

    /**
     * <p>Set a callback called for every line output to stdout by the shell</p>
     *
     * <p>The thread on which the callback executes is dependent on various factors, see {@link Shell.Interactive}
     * for further details</p>
     *
     * @param onLineListener
     *     Callback to be called for each line
     * @return This Builder object for method chaining
     */
    public Builder setOnStdoutLineListener(StreamGobbler.OnLineListener onLineListener) {
      this.onStdoutLineListener = onLineListener;
      return this;
    }

    /**
     * <p>Set a callback called for every line output to stderr by the shell</p>
     *
     * <p>The thread on which the callback executes is dependent on various factors, see {@link Shell.Interactive}
     * for further details</p>
     *
     * @param onLineListener
     *     Callback to be called for each line
     * @return This Builder object for method chaining
     */
    public Builder setOnStderrLineListener(StreamGobbler.OnLineListener onLineListener) {
      this.onStderrLineListener = onLineListener;
      return this;
    }

    /**
     * <p>Enable command timeout callback</p>
     *
     * <p>This will invoke the onCommandResult() callback with exitCode WATCHDOG_EXIT if a command takes longer than
     * watchdogTimeout seconds to complete.</p>
     *
     * <p>If a watchdog timeout occurs, it generally means that the Interactive session is out of sync with the shell
     * process. The caller should close the current session and open a new one.</p>
     *
     * @param watchdogTimeout
     *     Timeout, in seconds; 0 to disable
     * @return This Builder object for method chaining
     */
    public Builder setWatchdogTimeout(int watchdogTimeout) {
      this.watchdogTimeout = watchdogTimeout;
      return this;
    }

    /**
     * Construct a {@link Shell.Interactive} instance, and start the shell
     */
    @WorkerThread
    public Interactive open() {
      return new Interactive(this, null);
    }

    /**
     * Construct a {@link Shell.Interactive} instance, try to start the shell, and call onCommandResultListener to
     * report success or failure
     *
     * @param onCommandResultListener
     *     Callback to return shell open status
     */
    @WorkerThread
    public Interactive open(OnCommandResultListener onCommandResultListener) {
      return new Interactive(this, onCommandResultListener);
    }
  }

  /**
   * <p>An interactive shell - initially created with {@link Shell.Builder} - that executes blocks of commands you
   * supply in the background, optionally calling callbacks as each block completes.</p>
   *
   * <p>stderr output can be supplied as well, but due to compatibility with older Android versions, wantStderr is
   * not implemented using redirectErrorStream, but rather appended to the output. stdout and stderr are thus not
   * guaranteed to be in the correct order in the output.</p>
   *
   * <p>Note as well that the close() and waitForIdle() methods will intentionally crash when run in debug mode from
   * the main thread of the application. Any blocking call should be run from a background thread.</p>
   *
   * <p>When in debug mode, the code will also excessively log the commands passed to and the output returned from
   * the shell.</p>
   *
   * <p>Though this function uses background threads to gobble stdout and stderr so a deadlock does not occur if the
   * shell produces massive output, the output is still stored in a List, and as such doing something like <em>'ls
   * -lR /'</em> will probably have you run out of memory when using a {@link Shell.OnCommandResultListener}. A
   * work-around is to not supply this callback, but using (only)
   * {@link Shell.Builder#setOnStdoutLineListener(StreamGobbler.OnLineListener)}. This way, an internal buffer will
   * not be created and wasting your memory.</p>
   *
   * <h3>Callbacks, threads and handlers</h3>
   *
   * <p>On which thread the callbacks execute is dependent on your initialization. You can supply a custom Handler
   * using  {@link Shell.Builder#setHandler(Handler)} if needed. If you do not supply a custom Handler - unless you set
   * {@link Shell.Builder#setAutoHandler(boolean)} to false - a Handler will be auto-created if the thread used for
   * instantiation of the object has a Looper.</p>
   *
   * <p>If no Handler was supplied and it was also not auto-created, all callbacks will be called from either the
   * stdout or stderr gobbler threads. These are important threads that should be blocked as little as possible, as
   * blocking them may in rare cases pause the native process or even create a deadlock.</p>
   *
   * <p>The main thread must certainly have a Looper, thus if you call {@link Shell.Builder#open()} from the main
   * thread, a handler will (by default) be auto-created, and all the callbacks will be called on the main thread.
   * While this is often convenient and easy to code with, you should be aware that if your callbacks are 'expensive'
   * to execute, this may negatively impact UI performance.</p>
   *
   * <p>Background threads usually do <em>not</em> have a Looper, so calling {@link Shell.Builder#open()} from such a
   * background thread will (by default) result in all the callbacks being executed in one of the gobbler threads.
   * You will have to make sure the code you execute in these callbacks is thread-safe.</p>
   */
  @SuppressWarnings("unused")
  public static class Interactive {

    private final Handler handler;
    private final boolean autoHandler;
    final String shell;
    final boolean wantSTDERR;
    private final List<Command> commands;
    private final Map<String, String> environment;
    final StreamGobbler.OnLineListener onStdoutLineListener;
    final StreamGobbler.OnLineListener onStderrLineListener;
    private final Object idleSync = new Object();
    private final Object callbackSync = new Object();

    volatile String lastMarkerStdout;
    volatile String lastMarkerStderr;
    volatile Command command;
    private volatile List<String> buffer;
    private volatile boolean running;
    private volatile boolean idle = true; // read/write only synchronized
    private volatile boolean closed = true;
    private volatile int callbacks;
    private volatile int watchdogCount;
    volatile int lastExitCode;

    private Process process;
    private DataOutputStream stdin;
    private StreamGobbler stdout;
    private StreamGobbler stderr;
    private ScheduledThreadPoolExecutor watchdog;
    int watchdogTimeout;

    /**
     * The only way to create an instance: Shell.Builder::open()
     *
     * @param builder
     *     Builder class to take values from
     */
    Interactive(final Builder builder, final OnCommandResultListener onCommandResultListener) {
      autoHandler = builder.autoHandler;
      shell = builder.shell;
      wantSTDERR = builder.wantStderr;
      commands = builder.commands;
      environment = builder.environment;
      onStdoutLineListener = builder.onStdoutLineListener;
      onStderrLineListener = builder.onStderrLineListener;
      watchdogTimeout = builder.watchdogTimeout;

      // If a looper is available, we offload the callbacks from the gobbling threads to whichever thread created us.
      // Would normally do this in open(), but then we could not declare handler as final
      if ((Looper.myLooper() != null) && (builder.handler == null) && autoHandler) {
        handler = new Handler();
      } else {
        handler = builder.handler;
      }

      if (onCommandResultListener != null) {
        // Allow up to 60 seconds for SuperSU/Superuser dialog, then enable the user-specified timeout for all
        // subsequent operations
        watchdogTimeout = 60;
        commands.add(0, new Command(Shell.AVAILABLE_TEST_COMMANDS, 0, new OnCommandResultListener() {

          @Override
          public void onCommandResult(int commandCode, int exitCode, List<String> output) {
            if ((exitCode == ShellExitCode.SUCCESS) &&
                !Shell.parseAvailableResult(output, Shell.SU.isSU(shell))) {
              // shell is up, but it's brain-damaged
              exitCode = ShellExitCode.SHELL_EXEC_FAILED;
            }
            watchdogTimeout = builder.watchdogTimeout;
            onCommandResultListener.onCommandResult(0, exitCode, output);
          }
        }, null));
      }

      if (!open() && (onCommandResultListener != null)) {
        onCommandResultListener.onCommandResult(0, ShellExitCode.SHELL_WRONG_UID, null);
      }
    }

    /**
     * Add a command to execute
     *
     * @param commands
     *     Commands to execute
     */
    public void addCommand(@NonNull String... commands) {
      addCommand(commands, 0, (OnCommandResultListener) null);
    }

    /**
     * <p>Add a command to execute, with a callback to be called on completion</p>
     *
     * <p>The thread on which the callback executes is dependent on various factors, see {@link Shell.Interactive}
     * for further details</p>
     *
     * @param command
     *     Command to execute
     * @param code
     *     User-defined value passed back to the callback
     * @param resultListener
     *     Callback to be called on completion
     */
    public void addCommand(@NonNull String command, int code, @Nullable OnCommandResultListener resultListener) {
      addCommand(new String[]{command}, code, resultListener);
    }

    /**
     * <p>Add a command to execute, with a callback. This callback gobbles the output line by line without buffering
     * it and also returns the result code on completion.</p>
     *
     * <p>The thread on which the callback executes is dependent on various factors, see {@link Shell.Interactive}
     * for further details</p>
     *
     * @param command
     *     Command to execute
     * @param code
     *     User-defined value passed back to the callback
     * @param onCommandLineListener
     *     Callback
     */
    public void addCommand(@NonNull String command, int code, @Nullable OnCommandLineListener onCommandLineListener) {
      addCommand(new String[]{command}, code, onCommandLineListener);
    }

    /**
     * Add commands to execute
     *
     * @param commands
     *     Commands to execute
     */
    public void addCommand(@NonNull List<String> commands) {
      addCommand(commands, 0, (OnCommandResultListener) null);
    }

    /**
     * <p>Add commands to execute, with a callback to be called on completion (of all commands)</p>
     *
     * <p>The thread on which the callback executes is dependent on various factors, see {@link Shell.Interactive}
     * for further details</p>
     *
     * @param commands
     *     Commands to execute
     * @param code
     *     User-defined value passed back to the callback
     * @param onCommandResultListener
     *     Callback to be called on completion (of all commands)
     */
    public void addCommand(@NonNull List<String> commands, int code,
                           @Nullable OnCommandResultListener onCommandResultListener) {
      addCommand(commands.toArray(new String[commands.size()]), code, onCommandResultListener);
    }

    /**
     * <p>Add commands to execute, with a callback. This callback gobbles the output line by line without buffering
     * it and also returns the result code on completion.</p>
     *
     * <p>The thread on which the callback executes is dependent on various factors, see {@link Shell.Interactive}
     * for further details</p>
     *
     * @param commands
     *     Commands to execute
     * @param code
     *     User-defined value passed back to the callback
     * @param lineListener
     *     Callback
     */
    public void addCommand(@NonNull List<String> commands, int code,
                           @Nullable OnCommandLineListener lineListener) {
      addCommand(commands.toArray(new String[commands.size()]), code, lineListener);
    }

    /**
     * <p>Add commands to execute, with a callback to be called on completion (of all commands)</p>
     *
     * <p>The thread on which the callback executes is dependent on various factors, see {@link Shell.Interactive}
     * for further details</p>
     *
     * @param commands
     *     Commands to execute
     * @param code
     *     User-defined value passed back to the callback
     * @param resultListener
     *     Callback to be called on completion (of all commands)
     */
    public synchronized void addCommand(@NonNull String[] commands, int code,
                                        @Nullable OnCommandResultListener resultListener) {
      this.commands.add(new Command(commands, code, resultListener, null));
      runNextCommand();
    }

    /**
     * <p>Add commands to execute, with a callback. This callback gobbles the output line by line without buffering
     * it and also returns the result code on completion.</p>
     *
     * <p>The thread on which the callback executes is dependent on various factors, see {@link Shell.Interactive}
     * for further details</p>
     *
     * @param commands
     *     Commands to execute
     * @param code
     *     User-defined value passed back to the callback
     * @param onCommandLineListener
     *     Callback
     */
    public synchronized void addCommand(@NonNull String[] commands, int code,
                                        @Nullable OnCommandLineListener onCommandLineListener) {
      this.commands.add(new Command(commands, code, null, onCommandLineListener));
      runNextCommand();
    }

    /**
     * Run the next command if any and if ready, signals idle state if no commands left
     */
    private void runNextCommand() {
      runNextCommand(true);
    }

    /**
     * Called from a ScheduledThreadPoolExecutor timer thread every second when there is an outstanding command
     */
    synchronized void handleWatchdog() {
      final int exitCode;

      if (watchdog == null)
        return;
      if (watchdogTimeout == 0)
        return;

      if (!isRunning()) {
        exitCode = ShellExitCode.SHELL_DIED;
      } else if (watchdogCount++ < watchdogTimeout) {
        return;
      } else {
        exitCode = ShellExitCode.WATCHDOG_EXIT;
      }

      if (handler != null) {
        postCallback(command, exitCode, buffer);
      }

      // prevent multiple callbacks for the same command
      command = null;
      buffer = null;
      idle = true;

      watchdog.shutdown();
      watchdog = null;
      kill();
    }

    /**
     * Start the periodic timer when a command is submitted
     */
    private void startWatchdog() {
      if (watchdogTimeout == 0) {
        return;
      }
      watchdogCount = 0;
      watchdog = new ScheduledThreadPoolExecutor(1);
      watchdog.scheduleAtFixedRate(new Runnable() {

        @Override
        public void run() {
          handleWatchdog();
        }
      }, 1, 1, TimeUnit.SECONDS);
    }

    /**
     * Disable the watchdog timer upon command completion
     */
    private void stopWatchdog() {
      if (watchdog != null) {
        watchdog.shutdownNow();
        watchdog = null;
      }
    }

    /**
     * Run the next command if any and if ready
     *
     * @param notifyIdle
     *     signals idle state if no commands left ?
     */
    private void runNextCommand(boolean notifyIdle) {
      // must always be called from a synchronized method

      boolean running = isRunning();
      if (!running)
        idle = true;

      if (running && idle && (commands.size() > 0)) {
        Command command = commands.get(0);
        commands.remove(0);

        buffer = null;
        lastExitCode = 0;
        lastMarkerStdout = null;
        lastMarkerStderr = null;

        if (command.commands.length > 0) {
          try {
            if (command.onCommandResultListener != null) {
              // no reason to store the output if we don't have an OnCommandResultListener.
              // user should catch the output with an OnLineListener in this case.
              buffer = Collections.synchronizedList(new ArrayList<String>());
            }

            idle = false;
            this.command = command;
            startWatchdog();
            for (String write : command.commands) {
              stdin.write((write + "\n").getBytes("UTF-8"));
            }
            stdin.write(("echo " + command.marker + " $?\n").getBytes("UTF-8"));
            stdin.write(("echo " + command.marker + " >&2\n").getBytes("UTF-8"));
            stdin.flush();
          } catch (IOException e) {
            // stdin might have closed
          }
        } else {
          runNextCommand(false);
        }
      } else if (!running) {
        // our shell died for unknown reasons - abort all submissions
        while (commands.size() > 0) {
          postCallback(commands.remove(0), ShellExitCode.SHELL_DIED, null);
        }
      }

      if (idle && notifyIdle) {
        synchronized (idleSync) {
          idleSync.notifyAll();
        }
      }
    }

    /**
     * Processes a stdout/stderr line containing an end/exitCode marker
     */
    synchronized void processMarker() {
      if (command.marker.equals(lastMarkerStdout)
          && (command.marker.equals(lastMarkerStderr))) {
        postCallback(command, lastExitCode, buffer);
        stopWatchdog();
        command = null;
        buffer = null;
        idle = true;
        runNextCommand();
      }
    }

    /**
     * Process a normal stdout/stderr line
     *
     * @param line
     *     Line to process
     * @param listener
     *     Callback to call or null
     */
    synchronized void processLine(String line, StreamGobbler.OnLineListener listener) {
      if (listener != null) {
        if (handler != null) {
          final String fLine = line;
          final StreamGobbler.OnLineListener fListener = listener;

          startCallback();
          handler.post(new Runnable() {

            @Override
            public void run() {
              try {
                fListener.onLine(fLine);
              } finally {
                endCallback();
              }
            }
          });
        } else {
          listener.onLine(line);
        }
      }
    }

    /**
     * Add line to internal buffer
     *
     * @param line
     *     Line to add
     */
    synchronized void addBuffer(String line) {
      if (buffer != null) {
        buffer.add(line);
      }
    }

    /**
     * Increase callback counter
     */
    private void startCallback() {
      synchronized (callbackSync) {
        callbacks++;
      }
    }

    /**
     * Schedule a callback to run on the appropriate thread
     */
    private void postCallback(final Command fCommand, final int fExitCode, final List<String> fOutput) {
      if (fCommand.onCommandResultListener == null && fCommand.onCommandLineListener == null) {
        return;
      }
      if (handler == null/* || !handler.getLooper().getThread().isAlive()*/) {
        if ((fCommand.onCommandResultListener != null) && (fOutput != null))
          fCommand.onCommandResultListener.onCommandResult(fCommand.code, fExitCode, fOutput);
        if (fCommand.onCommandLineListener != null)
          fCommand.onCommandLineListener.onCommandResult(fCommand.code, fExitCode);
        return;
      }
      startCallback();

      handler.post(new Runnable() {

        @Override
        public void run() {
          try {
            if ((fCommand.onCommandResultListener != null) && (fOutput != null))
              fCommand.onCommandResultListener.onCommandResult(fCommand.code, fExitCode, fOutput);
            if (fCommand.onCommandLineListener != null)
              fCommand.onCommandLineListener.onCommandResult(fCommand.code, fExitCode);
          } finally {
            endCallback();
          }
        }
      });
    }

    /**
     * Decrease callback counter, signals callback complete state when dropped to 0
     */
    void endCallback() {
      synchronized (callbackSync) {
        callbacks--;
        if (callbacks == 0) {
          callbackSync.notifyAll();
        }
      }
    }

    /**
     * Internal call that launches the shell, starts gobbling, and starts executing commands. See
     * {@link Shell.Interactive}
     *
     * @return Opened successfully ?
     */
    private synchronized boolean open() {
      try {
        // setup our process, retrieve stdin stream, and stdout/stderr gobblers
        process = runWithEnv(shell, environment);
        stdin = new DataOutputStream(process.getOutputStream());
        stdout = new StreamGobbler(process.getInputStream(), new StreamGobbler.OnLineListener() {

          @Override
          public void onLine(String line) {
            synchronized (Interactive.this) {
              if (command == null) {
                return;
              }

              String contentPart = line;
              String markerPart = null;

              int markerIndex = line.indexOf(command.marker);
              if (markerIndex == 0) {
                contentPart = null;
                markerPart = line;
              } else if (markerIndex > 0) {
                contentPart = line.substring(0, markerIndex);
                markerPart = line.substring(markerIndex);
              }

              if (contentPart != null) {
                addBuffer(contentPart);
                processLine(contentPart, onStdoutLineListener);
                processLine(contentPart, command.onCommandLineListener);
              }

              if (markerPart != null) {
                try {
                  lastExitCode = Integer.valueOf(markerPart.substring(command.marker.length() + 1), 10);
                } catch (Exception e) {
                  // this really shouldn't happen
                  e.printStackTrace();
                }
                lastMarkerStdout = command.marker;
                processMarker();
              }
            }
          }
        });
        stderr = new StreamGobbler(process.getErrorStream(), new StreamGobbler.OnLineListener() {

          @Override
          public void onLine(String line) {
            synchronized (Interactive.this) {
              if (command == null) {
                return;
              }

              String contentPart = line;

              int markerIndex = line.indexOf(command.marker);
              if (markerIndex == 0) {
                contentPart = null;
              } else if (markerIndex > 0) {
                contentPart = line.substring(0, markerIndex);
              }

              if (contentPart != null) {
                if (wantSTDERR)
                  addBuffer(contentPart);
                processLine(contentPart, onStderrLineListener);
              }

              if (markerIndex >= 0) {
                lastMarkerStderr = command.marker;
                processMarker();
              }
            }
          }
        });

        // start gobbling and write our commands to the shell
        stdout.start();
        stderr.start();

        running = true;
        closed = false;

        runNextCommand();

        return true;
      } catch (IOException e) {
        // shell probably not found
        return false;
      }
    }

    /**
     * Close shell and clean up all resources. Call this when you are done with the shell. If the shell is not idle
     * (all commands completed) you should not call this method from the main UI thread because it may block for a
     * long time. This method will intentionally crash your app (if in debug mode) if you try to do this anyway.
     */
    public void close() {
      boolean idle = isIdle(); // idle must be checked synchronized

      synchronized (this) {
        if (!running)
          return;
        running = false;
        closed = true;
      }

      // This method should not be called from the main thread unless the shell is idle and can be cleaned up with
      // (minimal) waiting.

      if (!idle)
        waitForIdle();

      try {
        try {
          stdin.write(("exit\n").getBytes("UTF-8"));
          stdin.flush();
        } catch (IOException e) {
          //noinspection StatementWithEmptyBody
          if (e.getMessage().contains("EPIPE") || e.getMessage().contains("Stream closed")) {
            // we're not running a shell, the shell closed stdin,
            // the script already contained the exit command, etc.
          } else {
            throw e;
          }
        }

        // wait for our process to finish, while we gobble away in the background
        process.waitFor();

        // make sure our threads are done gobbling, our streams are closed, and the process is
        // destroyed - while the latter two shouldn't be needed in theory, and may even produce
        // warnings, in "normal" Java they are required for guaranteed cleanup of resources, so
        // lets be safe and do this on Android as well
        try {
          stdin.close();
        } catch (IOException e) {
          // stdin going missing is no reason to abort
        }
        stdout.join();
        stderr.join();
        stopWatchdog();
        process.destroy();
      } catch (InterruptedException | IOException e) {
        // various unforseen IO errors may still occur
      }
    }

    /**
     * Try to clean up as much as possible from a shell that's gotten itself wedged. Hopefully
     * the StreamGobblers will croak on their own when the other side of the pipe is closed.
     */
    public synchronized void kill() {
      running = false;
      closed = true;

      try {
        stdin.close();
      } catch (IOException e) {
        // in case it was closed
      }
      try {
        process.destroy();
      } catch (Exception e) {
        // in case it was already destroyed or can't be
      }
    }

    /**
     * Is our shell still running ?
     *
     * @return Shell running ?
     */
    public boolean isRunning() {
      if (process == null) {
        return false;
      }
      try {
        process.exitValue();
        return false;
      } catch (IllegalThreadStateException e) {
        // if this is thrown, we're still running
      }
      return true;
    }

    /**
     * Have all commands completed executing ?
     *
     * @return Shell idle ?
     */
    public synchronized boolean isIdle() {
      if (!isRunning()) {
        idle = true;
        synchronized (idleSync) {
          idleSync.notifyAll();
        }
      }
      return idle;
    }

    /**
     * <p>Wait for idle state. As this is a blocking call, you should not call it from the main UI thread. If you do
     * so and debug mode is enabled, this method will intentionally crash your app.</p>
     *
     * <p>If not interrupted, this method will not return until all commands have finished executing. Note that this
     * does not necessarily mean that all the callbacks have fired yet.</p>
     *
     * <p>If no Handler is used, all callbacks will have been executed when this method returns. If a Handler is
     * used, and this method is called from a different thread than associated with the Handler's Looper, all
     * callbacks will have been executed when this method returns as well. If however a Handler is used but this
     * method is called from the same thread as associated with the Handler's Looper, there is no way to know.</p>
     *
     * <p>In practice this means that in most simple cases all callbacks will have completed when this method
     * returns, but if you actually depend on this behavior, you should make certain this is indeed the case.</p>
     *
     * <p>See {@link Shell.Interactive} for further details on threading and handlers</p>
     *
     * @return True if wait complete, false if wait interrupted
     */
    public boolean waitForIdle() {
      if (isRunning()) {
        synchronized (idleSync) {
          while (!idle) {
            try {
              idleSync.wait();
            } catch (InterruptedException e) {
              return false;
            }
          }
        }

        if ((handler != null) &&
            (handler.getLooper() != null) &&
            (handler.getLooper() != Looper.myLooper())) {
          // If the callbacks are posted to a different thread than this one, we can wait until all callbacks have
          // called before returning. If we don't use a Handler at all, the callbacks are already called before we
          // get here. If we do use a Handler but we use the same Looper, waiting here would actually block the
          // callbacks from being called

          synchronized (callbackSync) {
            while (callbacks > 0) {
              try {
                callbackSync.wait();
              } catch (InterruptedException e) {
                return false;
              }
            }
          }
        }
      }

      return true;
    }

    /**
     * Are we using a Handler to post callbacks ?
     *
     * @return Handler used ?
     */
    public boolean hasHandler() {
      return (handler != null);
    }

  }

  /**
   * Class that creates an {@link Interactive} shell and handles the callbacks and threads for you.
   */
  public static class Console implements Closeable {

    private final OnCloseListener onCloseListener;
    private final Shell.Interactive shell;
    final HandlerThread callbackThread;
    private final boolean wantStderr;
    List<String> stdout;
    List<String> stderr;
    int exitCode;
    boolean isCommandRunning;
    private boolean closed;

    private final Shell.OnCommandResultListener commandResultListener = new Shell.OnCommandResultListener() {

      @Override
      public void onCommandResult(int commandCode, int exitCode, List<String> stdout) {
        Console.this.exitCode = exitCode;
        Console.this.stdout = stdout;
        synchronized (callbackThread) {
          isCommandRunning = false;
          callbackThread.notifyAll();
        }
      }
    };

    Console(Builder builder) throws ShellNotFoundException {
      try {
        onCloseListener = builder.onCloseListener;
        wantStderr = builder.wantStderr;

        callbackThread = new HandlerThread("Shell Callback");
        callbackThread.start();
        isCommandRunning = true;

        Shell.Builder shellBuilder = new Shell.Builder();
        shellBuilder.setShell(builder.shell);
        shellBuilder.setHandler(new Handler(callbackThread.getLooper()));
        shellBuilder.setWatchdogTimeout(builder.watchdogTimeout);
        shellBuilder.addEnvironment(builder.environment);
        shellBuilder.setWantStderr(false);

        if (builder.wantStderr) {
          shellBuilder.setOnStderrLineListener(new StreamGobbler.OnLineListener() {
            @Override
            public void onLine(String line) {
              if (stderr != null) {
                stderr.add(line);
              }
            }
          });
        }

        shell = shellBuilder.open(commandResultListener);

        waitForCommandFinished();
        if (exitCode != ShellExitCode.SUCCESS) {
          close();
          throw new ShellNotFoundException("Access was denied or this is not a shell");
        }
      } catch (Exception e) {
        throw new ShellNotFoundException("Error opening shell '" + builder.shell + "'", e);
      }
    }

    /**
     * Executes the commands in the shell and wait for its termination and return the result.
     *
     * @param commands
     *     The commands to execute.
     * @return The {@link CommandResult}.
     */
    @WorkerThread
    public synchronized CommandResult run(String... commands) {
      isCommandRunning = true;
      if (wantStderr) {
        stderr = Collections.synchronizedList(new ArrayList<String>());
      } else {
        stderr = Collections.emptyList();
      }
      shell.addCommand(commands, 0, commandResultListener);
      waitForCommandFinished();
      CommandResult result = new CommandResult(stdout, stderr, exitCode);
      stderr = null;
      stdout = null;
      return result;
    }

    /**
     * Closes all resources related to the shell.
     */
    @Override
    public synchronized void close() {
      try {
        shell.close();
      } catch (Exception ignored) {
      }
      synchronized (callbackThread) {
        callbackThread.notifyAll();
      }
      callbackThread.interrupt();
      callbackThread.quit();
      closed = true;
      if (onCloseListener != null) {
        onCloseListener.onClosed(this);
      }
    }

    /**
     * Check if the shell and callback thread have been closed.
     *
     * @return {@code true} if the {@link Interactive} shell has been closed.
     */
    public boolean isClosed() {
      return closed;
    }

    private void waitForCommandFinished() {
      synchronized (callbackThread) {
        while (isCommandRunning) {
          try {
            callbackThread.wait();
          } catch (InterruptedException ignored) {
          }
        }
      }
      if (exitCode == ShellExitCode.WATCHDOG_EXIT || exitCode == ShellExitCode.SHELL_DIED) {
        close();
      }
    }

    /**
     * Callback to be invoked when the shell closes.
     */
    public interface OnCloseListener {

      /**
       * Called when the shell is closed. A shell may close unexpectedly, in which case a new shell should be opened.
       *
       * @param console
       *     The {@link Console} holding the interactive shell that was closed.
       */
      void onClosed(Console console);
    }

    /**
     * Create a {@link Console} for running commands in an {@link Interactive} shell.
     */
    public static class Builder {

      Console.OnCloseListener onCloseListener;
      Map<String, String> environment = new HashMap<>();
      String shell = "sh";
      boolean wantStderr = true;
      int watchdogTimeout;

      /**
       * Set shell binary to use. Usually "sh" or "su", do not use a full path unless you have a good
       * reason to
       *
       * @param shell
       *     Shell to use
       * @return This Builder object for method chaining
       */
      public Builder setShell(String shell) {
        this.shell = shell;
        return this;
      }

      /**
       * Convenience function to set "sh" as used shell
       *
       * @return This Builder object for method chaining
       */
      public Builder useSH() {
        return setShell("sh");
      }

      /**
       * Convenience function to set "su" as used shell
       *
       * @return This Builder object for method chaining
       */
      public Builder useSU() {
        return setShell("su");
      }

      /**
       * Set if error output should be read and returned to the {@link CommandResult}.
       *
       * @param wantStderr
       *     {@code true} to read error output
       * @return This Builder object for method chaining
       */
      public Builder setWantStderr(boolean wantStderr) {
        this.wantStderr = wantStderr;
        return this;
      }

      /**
       * <p>Enable command timeout callback</p>
       *
       * <p>This will invoke the onCommandResult() callback with exitCode WATCHDOG_EXIT if a command takes longer than
       * watchdogTimeout seconds to complete.</p>
       *
       * <p>If a watchdog timeout occurs, it generally means that the Interactive session is out of sync with the shell
       * process. The caller should close the current session and open a new one.</p>
       *
       * @param watchdogTimeout
       *     Timeout, in seconds; 0 to disable
       * @return This Builder object for method chaining
       */
      public Builder setWatchdogTimeout(int watchdogTimeout) {
        this.watchdogTimeout = watchdogTimeout;
        return this;
      }

      /**
       * Add or update an environment variable
       *
       * @param key
       *     Key of the environment variable
       * @param value
       *     Value of the environment variable
       * @return This Builder object for method chaining
       */
      public Builder addEnvironment(String key, String value) {
        environment.put(key, value);
        return this;
      }

      /**
       * Add or update environment variables
       *
       * @param addEnvironment
       *     Map of environment variables
       * @return This Builder object for method chaining
       */
      public Builder addEnvironment(Map<String, String> addEnvironment) {
        environment.putAll(addEnvironment);
        return this;
      }

      /**
       * Set the listener that is called when the shell is closed.
       *
       * @param onCloseListener
       *     The {@link Console.OnCloseListener}
       * @return This Builder object for method chaining
       */
      public Builder setOnCloseListener(Console.OnCloseListener onCloseListener) {
        this.onCloseListener = onCloseListener;
        return this;
      }

      /**
       * Opens the console
       *
       * @return The {@link Console} with the opened interactive shell
       * @throws ShellNotFoundException
       *     If the shell is not an executable or could not be opened.
       */
      public Console build() throws ShellNotFoundException {
        return new Console(this);
      }

    }

  }

}