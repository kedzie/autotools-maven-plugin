/*
 * Copyright (C) 2006-2013 Holger Joest <holger@joest.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sf.maven.plugin.autotools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Running child processes.
 */
public class DefaultProcessExecutor
implements ProcessExecutor {

    private OutputStream stdout;

    private OutputStream stderr;


    /**
     * Set the standard output stream.
     *
     * @param stdout the standard output stream
     */
    public void setStdout(OutputStream stdout) {
        this.stdout = stdout;
    }


    /**
     * Set the standard error stream.
     *
     * @param stdout the standard error stream
     */
    public void setStderr(OutputStream stderr) {
        this.stderr = stderr;
    }


    /**
     * Executes the specified command in a child process passing stdout
     * and stderr to the specified output streams.
     *
     * @param command the command as a string array
     * @param env the environment variables
     * @param workingDirectory the working directory (getcwd)
     * @throws IOException if an I/O error occurs or if the child process
     *                     terminates with a non-zero code
     * @throws InterruptedException if the child process is interrupted
     */
    public void execProcess(
            String[] command,
            Map<String, String> env,
            File workingDirectory)
    throws IOException, InterruptedException {
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec(command, envp(env), workingDirectory);
        InputStream stdoutInput = process.getInputStream();
        InputStream stderrInput = process.getErrorStream();
        OutputStream stdoutResultStream = stdout;
        OutputStream stderrResultStream = stderr;
        if (stdoutResultStream == null) {
            stdoutResultStream = System.out;
        }
        if (stderrResultStream == null) {
            stderrResultStream = System.err;
        }
        StreamPump outStreamPump =
            new StreamPump(stdoutInput, stdoutResultStream);
        StreamPump errStreamPump =
            new StreamPump(stderrInput, stderrResultStream);
        outStreamPump.start();
        errStreamPump.start();
        int status = process.waitFor();
        if (status != 0) {
            throw new IOException(
                    "Child process \"" + command[0]
                    + "\" terminated with code " + status);
        }
        try {
            outStreamPump.stop();
        } finally {
            outStreamPump.waitFor();
        }
        try {
            errStreamPump.stop();
        } finally {
            errStreamPump.waitFor();
        }
    }


    /**
     * Returns the given environment variables as a string array
     * that may be passed to {@link Runtime#exec(String, String[])}.
     *
     * @param env the environment variables
     * @return a string array
     */
    private String[] envp(Map<String, String> env) {
        if (env == null) {
            return null;
        }
        List<String> result = new ArrayList<String>(env.size());
        for (String key : env.keySet()) {
            String value = env.get(key);
            result.add(key + "=" + value);
        }
        return result.toArray(new String[result.size()]);
    }

}

