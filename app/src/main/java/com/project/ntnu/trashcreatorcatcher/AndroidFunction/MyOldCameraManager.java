package com.project.ntnu.trashcreatorcatcher.AndroidFunction;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by SPLAB_Note on 2018/1/4.
 */

public class MyOldCameraManager extends MyCameraManager {

    private CameraPreview mCameraPreview = null;

    private Camera myCam = null;

    public MyOldCameraManager(Context context) {
        super(context);
    }

    @Override
    public boolean Initialize(Context context) {
        {
            if(!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
                return false;
            }
            try{
                myCam = Camera.open();
            }catch (Exception e) {
                return false;
            }
            mCameraPreview = new CameraPreview(context,myCam);
            isInitialize = true;
            return  true;
        }
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
//                Log.d(TAG, "Error creating media file, check storage permissions: " +
//                        e.getMessage());
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d(Tag, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(Tag, "Error accessing file: " + e.getMessage());
            }
        }
    };

    @Override
    public boolean takePicture() {
        return  mCameraPreview.takepicture(null,null,mPicture);
    }


}
