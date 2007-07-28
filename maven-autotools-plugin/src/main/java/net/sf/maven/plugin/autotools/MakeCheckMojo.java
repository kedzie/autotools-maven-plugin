/*
 * Copyright (C) 2006-2007 Holger Joest <hjoest@users.sourceforge.net>
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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;


/**
 * @goal check
 * @phase verify
 * @description run 'make check'
 */
public final class MakeCheckMojo
extends AbstractMojo {

    /**
     * The working directory.
     *
     * @parameter expression="${project.build.directory}/make-work"
     */
    private File workingDirectory;


    /**
     * {@inheritDoc}
     * @see org.apache.maven.plugin.AbstractMojo#execute()
     */
    public void execute()
    throws MojoExecutionException {
        try {
            workingDirectory.mkdirs();
            String[] makeCheckCommand = {
                    "make",
                    "check"
            };
            ProcessExecutor pe = new ProcessExecutor();
            pe.execProcess(makeCheckCommand, null, workingDirectory);
        } catch (MojoExecutionException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new MojoExecutionException("Failed to run \"make\"", ex);
        }
    }

}

