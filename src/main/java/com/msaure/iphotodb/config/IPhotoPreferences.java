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
package com.msaure.iphotodb.config;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSObject;
import com.dd.plist.PropertyListParser;
import java.io.File;
import java.net.URL;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;

/**
 * When trying to back up from the machine that is actually has the iPhoto instance installed, this class can be used to
 * determine the current iPhoto library folder by checking the iPhoto preferences.
 */
public class IPhotoPreferences {
    
    private static final String CONFIG_FILE_NAME = "com.apple.iApps.plist";
    private static final String LAST_DATABASE_KEY = "iPhotoLastOpenedDatabase";
    
    
    private final File configDirectory;

    public IPhotoPreferences() {
        final String userHome = System.getProperty("user.home");
        if (null == userHome) {
            throw new RuntimeException("system property user.home not accessible");
        }
        
        final String libraryDirectory = FilenameUtils.concat(userHome, "Library");
        File libraryFile = new File(libraryDirectory);
        if (!libraryFile.exists() || !libraryFile.isDirectory()) {
            throw new RuntimeException("user library directory " + libraryFile.getAbsolutePath() + " not accessible");
        }
        
        String preferencesDirectory = FilenameUtils.concat(libraryDirectory, "Preferences");
        File preferencesFile = new File(preferencesDirectory);
        if (!preferencesFile.exists() || !preferencesFile.isDirectory()) {
            throw new RuntimeException("user preferences directory " + preferencesFile.getAbsolutePath() + " not accessible");
        }
                
        this.configDirectory = preferencesFile;
    }
    
    protected File getConfigFile() {
        return new File(this.configDirectory, CONFIG_FILE_NAME);
    }
    
    public File getDefaultLibraryDirectory() throws Exception {
        final NSDictionary rootDict = (NSDictionary)PropertyListParser.parse(getConfigFile());
        NSObject albumUrlValue = rootDict.get(LAST_DATABASE_KEY);
        
        if (null != albumUrlValue) {
            URL albumUrl = new URL(albumUrlValue.toString());
            
            return new File(albumUrl.toURI());
        }
        
        return null;
    }
    
    public static void main(String[] args) {
        IPhotoPreferences m = new IPhotoPreferences();
        File configFile = m.getConfigFile();
        
        System.out.println(configFile.getAbsolutePath());
        if (configFile.canRead()) {
            System.out.println("readable: yes");
        } else {
            System.out.println("readable: no !");
        }
        
        try {
            System.out.println("album: " + m.getDefaultLibraryDirectory().getAbsolutePath());
            System.out.println();
            
            final NSDictionary rootDict = (NSDictionary)PropertyListParser.parse(configFile);
            for (Map.Entry<String,NSObject> entry: rootDict.getHashMap().entrySet()) {
               System.out.println(entry.getKey() + " = " + entry.getValue().toString());
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
