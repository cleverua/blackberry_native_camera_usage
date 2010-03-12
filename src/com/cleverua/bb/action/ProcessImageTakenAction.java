package com.cleverua.bb.action;

import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

import com.cleverua.bb.Constants;
import com.cleverua.bb.IImageListener;
import com.cleverua.bb.utils.IOUtils;

public class ProcessImageTakenAction implements IAction {

	private String choosenFile;
	private IImageListener imageListener;

	public ProcessImageTakenAction(String choosenFile, IImageListener imageListener) {
		this.choosenFile = choosenFile;
		this.imageListener = imageListener;
	}

	public void perform() {
		try {
			String filename = choosenFile.substring(choosenFile.lastIndexOf(Characters.SOLIDUS) + 1);
			String destinationPath = Constants.WORKING_DIR + "/" + filename;
			IOUtils.createDirIncludingAncestors(destinationPath);
			IOUtils.copyFile(choosenFile, destinationPath);
			if (imageListener != null) {
			    imageListener.afterImageProcessed(destinationPath);
			}
			
		} catch (Exception e) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Dialog.alert("Failed to process image you choose");
				}
			});
		}
	}

}
