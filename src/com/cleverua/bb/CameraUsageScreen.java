package com.cleverua.bb;

import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.MainScreen;

import com.cleverua.bb.action.LaunchCameraAction;
import com.cleverua.bb.utils.IOUtils;

public class CameraUsageScreen extends MainScreen implements IImageListener {
    private static final String TAKE_PICTURE_BUTTON_LABEL = "Take picture";
    
    private static final int DESIRED_PICTURE_HEIGHT = 200;
    private static final int DESIRED_PICTURE_WIDTH = 200;
    
    private BitmapField pictureTaken;
    private ButtonField getPictureBtn;

    public CameraUsageScreen() {
        super();
        initUI();
    }
    
    protected boolean onSavePrompt() {
        return true;
    }

    private void initUI() {
        
        setTitle(Constants.CAMERA_USAGE_SCREEN_TITLE);
        pictureTaken = new BitmapField(null, DrawStyle.HCENTER);
        pictureTaken.setMargin(20, 0, 20, 0);
        add(pictureTaken);
        
        getPictureBtn = new ButtonField(TAKE_PICTURE_BUTTON_LABEL, FIELD_HCENTER);
        getPictureBtn.setMargin(5, 0, 5, 0);
        getPictureBtn.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) {
                pictureTaken.setBitmap(null);
                new LaunchCameraAction().perform();
            }
        });
        setStatus(getPictureBtn);
    }

    public void afterImageProcessed(String imageFileFullPath) {
        try {
            pictureTaken.setBitmap(IOUtils.resizeImage(imageFileFullPath, DESIRED_PICTURE_WIDTH, DESIRED_PICTURE_HEIGHT).getBitmap());
        } catch (Exception e) {
            Dialog.alert("Error while setting the image captured: " + e);
        }
    }
}
