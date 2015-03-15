package com.msaure.iphotodb.tool;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.msaure.iphotodb.Library;
import com.msaure.iphotodb.LibraryAccess;
import com.msaure.iphotodb.LibraryExporter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Backup {

    public static void main(String[] args) {
        CmdOptions options = new CmdOptions();
        JCommander cmd = new JCommander(options, args);
        cmd.setProgramName(Backup.class.getName());
        
        if (options.help) {
            cmd.usage();
            System.exit(0);
        }
        
        if (null != options.librarydir) {
            File libraryRootPath = new File(options.librarydir);
            
            LibraryAccess validator = new LibraryAccess();
            if (validator.isValidLibrary(libraryRootPath)) {
                try {
                    Library library = validator.read(libraryRootPath);
                    
                    LibraryExporter exporter = new LibraryExporter();
                    exporter.setTargetDirectory(options.targetdir);
                    exporter.exportLibrary(library);
                    
                } catch (Exception ex) {
                    Logger.getLogger(Backup.class.getName()).log(Level.SEVERE, "failed to read album property list", ex);
                }
            }
            else {
                System.err.println(args[0] + " is not a valid iPhoto Library");
                System.exit(1);
            }
        }
        else {
            System.err.println("usage: java com.msaure.iphotodb.Main <directory>");
            System.exit(1);
        }
    }
    
    public static class CmdOptions {
        @Parameter
        final List<String> args = new ArrayList<>();
        
        @Parameter(names = "-targetdir", description = "directory to where the images should be copied")
        String targetdir;
        
        @Parameter(names = "-librarydir", description = "full path to iPhoto library directory")
        String librarydir;
        
        @Parameter(names = "-help", description = "show information about available command line options", help = true)
        boolean help;
    }
}
