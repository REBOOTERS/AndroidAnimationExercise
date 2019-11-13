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


import androidx.annotation.NonNull;

import java.util.List;

/**
 * Results of running a command in a shell. Results contain stdout, stderr, and the exit status.
 */
public class CommandResult implements ShellExitCode {

  private static String toString(List<String> lines) {
    StringBuilder sb = new StringBuilder();
    if (lines != null) {
      String emptyOrNewLine = "";
      for (String line : lines) {
        sb.append(emptyOrNewLine).append(line);
        emptyOrNewLine = "\n";
      }
    }
    return sb.toString();
  }

  @NonNull
  public final List<String> stdout;
  @NonNull public final List<String> stderr;
  public final int exitCode;

  public CommandResult(@NonNull List<String> stdout, @NonNull List<String> stderr, int exitCode) {
    this.stdout = stdout;
    this.stderr = stderr;
    this.exitCode = exitCode;
  }

  /**
   * Check if the exit code is 0.
   *
   * @return {@code true} if the {@link #exitCode} is equal to {@link ShellExitCode#SUCCESS}.
   */
  public boolean isSuccessful() {
    return exitCode == SUCCESS;
  }

  /**
   * Get the standard output.
   *
   * @return The standard output as a string.
   */
  public String getStdout() {
    return toString(stdout);
  }

  /**
   * Get the standard error.
   *
   * @return The standard error as a string.
   */
  public String getStderr() {
    return toString(stderr);
  }

  @Override
  public String toString() {
    return getStdout();
  }

}
