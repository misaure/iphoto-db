/*
 * Copyright 2015 msaure.
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
package com.msaure.iphotodb.tool;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.msaure.iphotodb.LibraryAccess;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Validate {

    public static void main(String[] args) {
        Backup.CmdOptions options = new Backup.CmdOptions();
        JCommander cmd = new JCommander(options, args);
        cmd.setProgramName(Backup.class.getName());
        
        if (options.help) {
            cmd.usage();
            System.exit(0);
        }

        if (null != options.librarydir) {
            File libraryRootPath = new File(options.librarydir);
            
            final LibraryAccess validator = new LibraryAccess();
        
            if (validator.isValidLibrary(libraryRootPath)) {
                System.out.println("library at path " + libraryRootPath.getAbsolutePath() + " is valid");
            } else {
                System.out.println("library at path " + libraryRootPath.getAbsolutePath() + " failed to validate");
            }
        }
    }
    
    public static class CmdOptions {
        @Parameter
        final List<String> args = new ArrayList<>();
        
        @Parameter(names = "-librarydir", description = "full path to iPhoto library directory")
        String librarydir;
        
        @Parameter(names = "-help", description = "show information about available command line options", help = true)
        boolean help;
    }
}
