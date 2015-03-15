package com.msaure.iphotodb;

import org.junit.Test;
import static org.junit.Assert.*;

public class IPhotoLibraryUtilsTest {

    @Test
    public void testStripPath() {
        assertEquals(
                "Masters/2014/06/29/20140629-210004/DSC_0380.JPG",
                IPhotoLibraryUtils.stripPath("/Users/someuser/Pictures/iPhoto Library/Masters/2014/06/29/20140629-210004/DSC_0380.JPG"));
    }
    
    @Test
    public void testRelocatePath() {
        assertEquals(
                "/home/newuser/backup/iphoto/Masters/2014/06/29/20140629-210004/DSC_0380.JPG",
                IPhotoLibraryUtils.relocatePath(
                        "/home/newuser/backup/iphoto", 
                        "/Users/someuser/Pictures/iPhoto Library/Masters/2014/06/29/20140629-210004/DSC_0380.JPG"));
    }
}
