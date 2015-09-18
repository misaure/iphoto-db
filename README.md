[![Build Status](https://travis-ci.org/misaure/iphoto-db.svg?branch=master)]

Simple tool for exporting images from iPhoto libraries.

Note: The property list library used by this project cannot handle large libraries very well. I have implemented a new
library that only requires a minor percentage of the memory and time the old library uses. It should be ready for prime time
within the next few days.

NoteNote: The new property list library worked well for my personal iPhoto library. But then the tool and my wife's iPhoto
library met for the first time. Yeah, what can I say? I am completely rebuilding the plist parser again. It must become the
most memory efficient implementation that is possible. I am working on it. ;-)

