package com.cleverua.bb;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

import com.cleverua.bb.action.ProcessImageTakenAction;
import com.cleverua.bb.utils.StringUtils;

public class CameraUsageApplication extends UiApplication {
    private static final String NOT_IMPLEMENTED_MESSAGE = "Not implemented yet";

    private static CameraFilesystemJournalListener filesystemListener;
    private static CameraUsageApplication application;
    private static CameraUsageScreen cameraUsageScreen;

    public static CameraUsageApplication instance() {
        return application;
    }
    
    public static void main(String[] args) {
        application = new CameraUsageApplication();
        application.pushScreen(cameraUsageScreen);
        application.enterEventDispatcher();
    }
    
    public static void exit() {
        System.exit(0);
    }
    
    public static void setFileSystemJournalListenerEnabled(boolean enabled) {
        if (enabled) {
            filesystemListener.resetFile();
            filesystemListener.resetStoredUSN();
            UiApplication.getUiApplication().addFileSystemJournalListener(
                    filesystemListener);
        } else {
            UiApplication.getUiApplication().removeFileSystemJournalListener(
                    filesystemListener);
        }
    }
    
    public void initializeImage(String imageFileFullPath) {
        
    }
    
    public void activate() {

        if (filesystemListener != null /*
                                         * happens when it renders the splash
                                         * screen
                                         */) {
            try {
                setFileSystemJournalListenerEnabled(false);
            } catch (Exception e) { /* n/a */
            }

            try {
                // activate: going to see if we have image taken
                String image = filesystemListener.getFile();
                
                if (StringUtils.isNotBlank(image)) {
                    // perform some action with image taken
                    new ProcessImageTakenAction(image, cameraUsageScreen).perform();
                }
            } finally {
                filesystemListener.resetFile();
            }
        }
        repaint();
        super.activate();
    }
    
    public static void showNotImplementedAlert() {
        Dialog.alert(NOT_IMPLEMENTED_MESSAGE);
    }
    
    private CameraUsageApplication() {
        filesystemListener = new CameraFilesystemJournalListener();
        cameraUsageScreen = new CameraUsageScreen();
    }
}
