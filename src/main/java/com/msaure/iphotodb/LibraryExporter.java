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

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;

/**
 * Copy all images from an iPhoto library to a given target directory.
 * 
 * Only the
 * images itself will be copied. Thumbnails will be ignored. The exporter makes 
 * some primitive attempts to create a more intuitive directory structure than
 * iPhoto originally uses.
 */
public class LibraryExporter {

    private File targetDirectory;

    /**
     * By default, iPhoto will assign a date as the title of a new Album.
     * 
     * This 
     * will most probably be represented in different formats, depending the a
     * user's locale settings. The pattern defined here works for my Mac, which
     * is configured to use DE_de.
     * 
     * TODO allow selection of date patterns by specifying a localization attribute
     */
    private static final String DEFAULT_EVENT_DATE_FORMAT = "dd.MM.yyyy";
    private static final String DEFAULT_EVENT_DATE_PATTERN = "[0123][0-9]\\.\\s*[012][0-9]\\.\\s*[12][0-9]{3}";

    private final Pattern eventDateRegex;
    private final DateFormat dateFormatIn;
    private final DateFormat dateFormatOut;
    
    public LibraryExporter() {
        this.eventDateRegex = Pattern.compile(DEFAULT_EVENT_DATE_PATTERN);
        this.dateFormatIn = new SimpleDateFormat(DEFAULT_EVENT_DATE_FORMAT);
        this.dateFormatOut = new SimpleDateFormat("yyyy-MM-dd");
    }
    
    public void exportLibrary(Library library) throws IOException {
        createEventDirectories(library, targetDirectory);
    }

    private void createEventDirectories(Library library, File targetDirectory) throws IOException {
        // store all images in a map, so that they can be assigned to the album image lists
        final Map<Integer,ImageInfo> images = new HashMap<>();
        for (ImageInfo image: library.getAllImages()) {
            images.put(image.getId(), image);
        }
        
        // create target directories for events and copy images to that directories
        FileUtils.forceMkdir(targetDirectory);
        for (Album album: library.getAlbums()) {
            if (Album.Type.EVENT == album.getType()) {
                File eventDirectory = createEventDirectory(album, targetDirectory);
                FileUtils.forceMkdir(eventDirectory);
                
                copyAssignedImages(library, album, images, eventDirectory);
            }
        }
        
        // copy remaining images to 'unsorted' directory
        File unsortedDirectory = new File(targetDirectory.getAbsolutePath(), "unsorted");
        FileUtils.forceMkdir(unsortedDirectory);
        
        for (Map.Entry<Integer,ImageInfo> entry: images.entrySet()) {
            ImageInfo image = entry.getValue();
            File imageFile = library.resolveImagePath(image);
            
            copyFileToDirectory(imageFile, unsortedDirectory);
        }
    }

    private void copyAssignedImages(Library library, Album album, Map<Integer,ImageInfo> images, File eventDirectory) throws IOException {
        for (Integer assignedImageId: album.getImageIds()) {
            if (images.containsKey(assignedImageId)) {
                
                // copy image to event directory
                ImageInfo image = images.get(assignedImageId);
                File sourceFile = library.resolveImagePath(image);
                copyFileToDirectory(sourceFile, eventDirectory);
                
                // remove image from map so that it won't get copied again
                images.remove(assignedImageId);
            }
        }
    }

    private void copyFileToDirectory(File fileToCopy, File targetDirectory) throws IOException {
        FileUtils.copyFileToDirectory(fileToCopy, targetDirectory, true);
        System.out.println(fileToCopy.getAbsolutePath() + " -> " + targetDirectory.getAbsolutePath());
    }
    
    private File createEventDirectory(Album album, File targetDirectory) {
        return new File(targetDirectory, cleanFileName(album.getName()));
    }
    
    private String cleanFileName(String originalFileName) {
        Matcher m = this.eventDateRegex.matcher(originalFileName);
        if (m.matches()) {
            try {
                Date d = this.dateFormatIn.parse(originalFileName);
                return this.dateFormatOut.format(d);
            }
            catch (ParseException ex) {
                return replaceSpecialCharacters(originalFileName);
            }
        }
        return replaceSpecialCharacters(originalFileName);
    }

    private String replaceSpecialCharacters(String originalFileName) {
        return originalFileName
                .replaceAll(":", " ")
                .replaceAll("/", " ")
                .replaceAll("[!,']", " ")
                .replaceAll("\\s+", " ");
    }

    public File getTargetDirectory() {
        return targetDirectory;
    }

    public void setTargetDirectory(File targetDirectory) {
        this.targetDirectory = targetDirectory;
    }
    
}
