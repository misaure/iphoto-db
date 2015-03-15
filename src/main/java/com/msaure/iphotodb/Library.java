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
import java.util.List;

public class Library implements java.io.Serializable {
    
    private static final long serialVersionUID = 3555974658381952802L;
    
    private File iphotoPackageDirectory;
    private List<ImageInfo> allImages;
    private List<Album> albums;

    public List<ImageInfo> getAllImages() {
        return allImages;
    }

    public void setAllImages(List<ImageInfo> allImages) {
        this.allImages = allImages;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public File getIphotoPackageDirectory() {
        return iphotoPackageDirectory;
    }

    public void setIphotoPackageDirectory(File iphotoPackageDirectory) {
        this.iphotoPackageDirectory = iphotoPackageDirectory;
    }
    
    public File resolveImagePath(ImageInfo imageInfo) {
        return new File(this.iphotoPackageDirectory, imageInfo.getImagePath());
    }
}
