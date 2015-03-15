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
package com.msaure.iphotodb.checks;

import com.msaure.iphotodb.IPhotoLibraryUtils;
import com.msaure.iphotodb.ImageInfo;
import com.msaure.iphotodb.Library;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MissingImagesCheck {
    
    public List<ImageInfo> findImagesWithMissingFiles(Library library) {
        List<ImageInfo> imagesWithMissingFiles = new ArrayList<>();
        
        for (ImageInfo imageInfo: library.getAllImages()) {
            if (!checkImagePath(imageInfo, library.getIphotoPackageDirectory())) {
                imagesWithMissingFiles.add(imageInfo);
            }
        }
        
        return imagesWithMissingFiles;
    }
    
    private boolean checkImagePath(ImageInfo image, File iphotoPackageDirectory) {
        final String imageFilePath = IPhotoLibraryUtils.relocatePath(iphotoPackageDirectory.getAbsolutePath(), image.getImagePath());
        final File imageFile = new File(imageFilePath);
        
        if (!imageFile.exists()) {
            return false;
        }
        
        final String thumbFilePath = IPhotoLibraryUtils.relocatePath(iphotoPackageDirectory.getAbsolutePath(), image.getThumbPath());
        final File thumbFile = new File(thumbFilePath);
        
        return thumbFile.exists();
    }

}
