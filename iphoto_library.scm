(plist
; AlbumData.xml
 (dictionary
 ; Keys:
  (string "Application Version")
  (string "Archive Path")
  (string "ArchiveId")
  (integer "Major Version")
  (integer "Minor Version")
  (array "List of Albums"
    (dict
      (integer "AlbumId")
      (string "AlbumName")
      (string "Album Type")
      (string "GUID")
      (boolean "Master")
      (real "TransitionSpeed")
      (boolean "ShuffleSlides")
      (array "KeyList" string)
      (integer "PhotoCount")))
  (array "List of Rolls"
    (dict ...))
  (dict "List of Faces"
    (dict ...))
  (dict "Master Image List"
    (dict ...))   ; see below

=== Master Image List ===

(dict
  (string ImagePath)
  (string MediaType)
  (string ThumbPath))


=== List of Albums ===

* AlbumId
* AlbumName
* Album Type
* KeyList
* KeyPhotoKey (opt)
