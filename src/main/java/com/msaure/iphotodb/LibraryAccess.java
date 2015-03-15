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

import com.dd.plist.NSArray;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSObject;
import com.dd.plist.PropertyListParser;
import static com.msaure.iphotodb.IPhotoLibraryUtils.stripPath;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LibraryAccess {
    
    private static final String KEY_ALBUM_LIST = "List of Albums";
    private static final String KEY_IMAGE_LIST = "Master Image List";
    
    //private static final String KEY_MAJOR_VERSION = "Major Version";
    //private static final String KEY_MINOR_VERSION = "Minor Version";
    
    public boolean isValidLibrary(File iphotoPackageDirectory) {
        // TODO implement me
        return true;
    }
    
    public Library read(File iphotoPackageDirectory) throws Exception {
        
        final File plistFile = new File(iphotoPackageDirectory, "AlbumData.xml");
        final NSDictionary rootDict = (NSDictionary)PropertyListParser.parse(plistFile);
        
        final Library library = new Library();
        library.setIphotoPackageDirectory(iphotoPackageDirectory);
        library.setAllImages(getImageList(rootDict));
        library.setAlbums(getAlbums(library, rootDict));
        
        return library;
    }

    private List<ImageInfo> getImageList(NSDictionary rootDict) throws NumberFormatException, Exception {
        List<ImageInfo> imagesFound = new ArrayList<>();
        
        final NSDictionary imageDict = (NSDictionary) rootDict.get(KEY_IMAGE_LIST);
        
        for (Map.Entry<String,NSObject> imageInfo: imageDict.getHashMap().entrySet()) {
            NSDictionary imageInfoAsDict = (NSDictionary) imageInfo.getValue();
            
            final ImageInfo image = new ImageInfo();
            image.setId(Integer.parseInt(imageInfo.getKey()));
            if (imageInfoAsDict.containsKey("ImagePath")) {
                image.setImagePath(stripPath(imageInfoAsDict.get("ImagePath").toString()));
            }
            if (imageInfoAsDict.containsKey("ThumbPath")) {
                image.setThumbPath(stripPath(imageInfoAsDict.get("ThumbPath").toString()));
            }
            
            imagesFound.add(image);
        }
        
        return imagesFound;
    }

    private List<Album> getAlbums(Library library, NSDictionary rootDict) {
        NSArray albumsArray = (NSArray) rootDict.get(KEY_ALBUM_LIST);
        List<Album> albumsFound = new ArrayList<>();
        
        for (int i=0; i<albumsArray.count(); ++i) {
            NSDictionary albumDict = (NSDictionary) albumsArray.objectAtIndex(i);
            
            Album album = new Album();
            
            album.setName(albumDict.objectForKey("AlbumName").toString());
            if (albumDict.containsKey("Album Type")) {
                album.setType(mapAlbumType(albumDict.objectForKey("Album Type").toString()));
            }
            
            album.setId(Integer.parseInt(albumDict.objectForKey("AlbumId").toString()));
            
            Set<Integer> imageIds = new HashSet<>();
            if (albumDict.containsKey("KeyList")) {
                NSArray keyListIn = (NSArray) albumDict.objectForKey("KeyList");
                for (int j=0; j<keyListIn.count(); ++j) {
                    imageIds.add(Integer.valueOf(keyListIn.objectAtIndex(j).toString()));
                }
            }
            album.setImageIds(imageIds);
            
            albumsFound.add(album);
        }
        
        return albumsFound;
    }

    private Album.Type mapAlbumType(String toString) {
        if (null != toString) {
            switch (toString) {
            case "Flagged":
                return Album.Type.FLAGGED;
            case "Special Month":
                return Album.Type.SPECIAL_MONTH;
            case "Event":
                return Album.Type.EVENT;
            case "99":
                return Album.Type.MASTER;
            case "Slideshow":
                return Album.Type.SLIDESHOW;
            }
        }
        
        return Album.Type.UNKNWON;
    }
    
}
