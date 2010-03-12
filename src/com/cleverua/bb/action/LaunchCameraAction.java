package com.cleverua.bb.action;

import net.rim.blackberry.api.invoke.CameraArguments;
import net.rim.blackberry.api.invoke.Invoke;

import com.cleverua.bb.CameraUsageApplication;

public class LaunchCameraAction implements IAction {

    public void perform() {
        CameraUsageApplication.setFileSystemJournalListenerEnabled(true);
        Invoke.invokeApplication(Invoke.APP_TYPE_CAMERA, new CameraArguments());
    }

}
