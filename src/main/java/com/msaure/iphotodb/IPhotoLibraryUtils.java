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
package com.msaure.iphotodb;

import org.apache.commons.io.FilenameUtils;

public class IPhotoLibraryUtils {
    
    private static final String DEFAULT_LIBRARY_DIRECTORY = "iPhoto Library";
    
    /**
     * Strip the original iPhoto Library path from an image file path.
     */
    public static String stripPath(String path) {
        int pos = path.indexOf(DEFAULT_LIBRARY_DIRECTORY);
        
        if (pos > 0) {
            return path.substring(pos + DEFAULT_LIBRARY_DIRECTORY.length() + 1);
        }
        
        return path;
    }
    
    public static String relocatePath(String newLibraryPath, String path) {
        return FilenameUtils.concat(newLibraryPath, stripPath(path));
    }
}
