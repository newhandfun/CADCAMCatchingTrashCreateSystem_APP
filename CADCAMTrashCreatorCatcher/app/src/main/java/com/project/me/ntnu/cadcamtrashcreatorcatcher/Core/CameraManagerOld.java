package com.project.me.ntnu.cadcamtrashcreatorcatcher.Core;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by SPLAB_Note on 2018/1/10.
 */

public class CameraManagerOld extends MyCameraManager {

    private Camera myCam =null;
    private CameraPreview myCamPre = null;

    public CameraManagerOld(Activity activity,ICameraAction iCameraAction){
        super(activity,iCameraAction);

        if (activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            myIsActive =  true;

            myCamPre = new CameraPreview(activity,getInstance());

        } else {
            // no camera on this device
            myIsActive =  false;
        }
    }

    @Override
    public void takePicture() {
        if(myCam!=null)
            myCam.takePicture(null,null,mPicture);
    }

    @Override
    protected void initialize(Context context){
    }

    @Override
    public View getView(){
        return  myCamPre;
    }


    private Camera getInstance(){
        if(myCam==null){
            try {

                myCam = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);; // attempt to get a Camera instance
            }
            catch (Exception e){
                // Camera is not available (in use or does not exist)
                Log.e("Camera Initial".toString(),"Camera is null!");
            }
        }
        return myCam;
    }


    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            camera.startPreview();

//            Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
//            bitmap.recycle();
//            System.gc();
//            Log.i("Take Pic","!");
            if(iCameraAction!=null){
                iCameraAction.takePicture(data);
            }
//            myList.add(bitmap);

        }
    };



}
