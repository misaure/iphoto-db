package com.msaure.iphotodb.tool;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.msaure.iphotodb.Library;
import com.msaure.iphotodb.LibraryAccess;
import com.msaure.iphotodb.LibraryExporter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Backup {

    private final JCommander cmd;
    
    public Backup(JCommander cmd)
    {
        this.cmd = cmd;
    }
    
    public static void main(String[] args)
    {
        final CmdOptions options = new CmdOptions();
        JCommander cmd = new JCommander(options, args);
        cmd.setProgramName(Backup.class.getName());
        
        final Backup backupTool = new Backup(cmd);
        backupTool.run(options);
    }
    
    public void run(CmdOptions options)
    {
        if (options.help) {
            cmd.usage();
            System.exit(0);
        }
        
        if (null != options.librarydir && null != options.targetdir) {
            File libraryRootPath = new File(options.librarydir);
            File targetDirectory = new File(options.targetdir);
            
            try {
                copyLibrary(libraryRootPath, targetDirectory);
            }
            catch(RuntimeException e) {
                System.err.println("error while trying to copy iPhoto library:");
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }
        else {
            cmd.usage();
            System.exit(1);
        }
    }
    
    public void copyLibrary(File libraryRootPath, File targetDirectory) 
    {
        final LibraryAccess validator = new LibraryAccess();
        
        if (validator.isValidLibrary(libraryRootPath)) {
            try {
                final Library library = validator.read(libraryRootPath);

                final LibraryExporter exporter = new LibraryExporter();
                exporter.setTargetDirectory(targetDirectory);
                exporter.exportLibrary(library);

            }
            catch (Exception ex) {
                throw new RuntimeException("failed to read album property list", ex);
            }
        }
        else {
            throw new RuntimeException("invalid photo library at path " + libraryRootPath.getAbsolutePath());
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
