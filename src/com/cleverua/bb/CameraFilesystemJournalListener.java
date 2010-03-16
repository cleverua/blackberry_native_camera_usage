package com.cleverua.bb;

import net.rim.device.api.io.file.FileSystemJournal;
import net.rim.device.api.io.file.FileSystemJournalEntry;
import net.rim.device.api.io.file.FileSystemJournalListener;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.EventInjector;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.UiApplication;

public class CameraFilesystemJournalListener implements FileSystemJournalListener {

    private static final String FILE_PREFIX = "file://";
    private static long myStoredUSN = 0;
    private String file = null;
    private static final String[] IMAGE_EXTENSIONS = {".jpg", ".jpeg", "png"};

    public CameraFilesystemJournalListener() {
        // myStoredUSN = 0;
    }

    public void fileJournalChanged() {
        long nextUSN = FileSystemJournal.getNextUSN();
        for (long lookUSN = nextUSN - 1; lookUSN >= myStoredUSN; lookUSN--) {
            FileSystemJournalEntry entry = FileSystemJournal.getEntry(lookUSN);

            if (entry == null) {
                // we didn't find an entry
                break;
            }

            String path = FILE_PREFIX + entry.getPath();

            if (path != null) {
                if (isValidExtension(path)) {
                    switch (entry.getEvent()) {
                        case FileSystemJournalEntry.FILE_ADDED:
                            if(!path.startsWith(Constants.WORKING_DIR)) {
                                // picture was taken or added
                                file = path;

                                // emulateEscapeKeyPressed();

                                UiApplication.getUiApplication().requestForeground();
                            }
                            break;
                        case FileSystemJournalEntry.FILE_DELETED:
                            // picture was removed
                            if(path.equals(file)) {
                                file = null;
                            }
                            break;
                    }
                }
            }
        }
        myStoredUSN = nextUSN;
    }

    public String getFile() {
        return file;
    }

    public void resetFile() {
        file = null;
    }

    private boolean isValidExtension(String filename) {
        for(int i = 0; i < IMAGE_EXTENSIONS.length; i++) {
            if(filename.toLowerCase().endsWith(IMAGE_EXTENSIONS[i])) {
                return true;
            }
        }
        return false;
    }

    private void emulateEscapeKeyPressed() {
        // going to emulate Escape key pressed - this closes camera viewfinder screen
        EventInjector.invokeEvent(new EventInjector.KeyEvent(EventInjector.KeyCodeEvent.KEY_DOWN, Characters.ESCAPE, Keypad.status(Keypad.KEY_ESCAPE)));
        EventInjector.invokeEvent(new EventInjector.KeyEvent(EventInjector.KeyCodeEvent.KEY_UP, Characters.ESCAPE, Keypad.status(Keypad.KEY_ESCAPE)));

        // going to emulate Escape key pressed again - this now closes camera preview screen
        EventInjector.invokeEvent(new EventInjector.KeyEvent(EventInjector.KeyCodeEvent.KEY_DOWN, Characters.ESCAPE, Keypad.status(Keypad.KEY_ESCAPE)));
        EventInjector.invokeEvent(new EventInjector.KeyEvent(EventInjector.KeyCodeEvent.KEY_UP, Characters.ESCAPE, Keypad.status(Keypad.KEY_ESCAPE)));
    }
}