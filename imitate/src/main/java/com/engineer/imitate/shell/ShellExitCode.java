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

@SuppressWarnings("unused")
public interface ShellExitCode {

  int SUCCESS = 0;

  int WATCHDOG_EXIT = -1;

  int SHELL_DIED = -2;

  int SHELL_EXEC_FAILED = -3;

  int SHELL_WRONG_UID = -4;

  int SHELL_NOT_FOUND = -5;

  int TERMINATED = 130;

  int COMMAND_NOT_EXECUTABLE = 126;

  int COMMAND_NOT_FOUND = 127;

}
