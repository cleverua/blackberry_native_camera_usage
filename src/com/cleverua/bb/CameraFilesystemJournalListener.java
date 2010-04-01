package com.cleverua.bb;

import net.rim.device.api.io.file.FileSystemJournal;
import net.rim.device.api.io.file.FileSystemJournalEntry;
import net.rim.device.api.io.file.FileSystemJournalListener;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.EventInjector;
import net.rim.device.api.ui.Keypad;

public class CameraFilesystemJournalListener implements FileSystemJournalListener {

    private static final String FILE_PREFIX = "file://";
    
    private static final String[] IMAGE_EXTENSIONS = { ".jpg", ".jpeg" };
    
    private long storedUSN = 0;
    private String fileName = null;

    public CameraFilesystemJournalListener() {
    }

    public void fileJournalChanged() {
        if (fileName != null) { return; } // we've already detected one, so no need to do it again
        
        long nextUSN = FileSystemJournal.getNextUSN();
        for (long lookUSN = nextUSN - 1; lookUSN >= storedUSN; lookUSN--) {
            FileSystemJournalEntry entry = FileSystemJournal.getEntry(lookUSN);

            if (entry == null) {
                // we didn't find an entry
                break;
            }

            String path = FILE_PREFIX + entry.getPath();

            if (path != null) {
                if (isValidExtension(path)) {
                    if (entry.getEvent() == FileSystemJournalEntry.FILE_ADDED) {
                        if (!path.startsWith(Constants.WORKING_DIR)) {
                            // picture was taken or added
                            fileName = path;
                            break;
                        }
                    }
                }
            }
        }
        
        storedUSN = nextUSN;
        
        if (fileName != null) { // means image file has just been detected
            closeCamera();
        }
    }
    
    /**
     * Returns the file name of image taken 
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Resets the file name of image taken 
     */
    public void resetFileName() {
        fileName = null;
    }
    
    public void resetStoredUSN() {
        storedUSN = FileSystemJournal.getNextUSN();
    }

    private boolean isValidExtension(String filename) {
        for (int i = 0; i < IMAGE_EXTENSIONS.length; i++) {
            if (filename.toLowerCase().endsWith(IMAGE_EXTENSIONS[i])) {
                return true;
            }
        }
        return false;
    }

    private void closeCamera() {
        emulateEscKeyPressed(); // this closes camera viewfinder screen
        emulateEscKeyPressed(); // now this closes camera preview screen
    }
    
    private void emulateEscKeyPressed() {
        postEscKeyEvent(EventInjector.KeyCodeEvent.KEY_DOWN);
        postEscKeyEvent(EventInjector.KeyCodeEvent.KEY_UP);
    }

    private void postEscKeyEvent(int keyCodeEvent) {
        EventInjector.invokeEvent(
            new EventInjector.KeyEvent(
                keyCodeEvent, Characters.ESCAPE, Keypad.status(Keypad.KEY_ESCAPE)
            )
        );
    }
}