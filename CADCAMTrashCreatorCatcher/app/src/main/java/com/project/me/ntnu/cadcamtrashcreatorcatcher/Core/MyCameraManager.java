package com.project.me.ntnu.cadcamtrashcreatorcatcher.Core;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by SPLAB_Note on 2018/1/10.
 */

public abstract class MyCameraManager {

    protected ICameraAction iCameraAction = null;

    protected ArrayList<Bitmap> myList =new ArrayList<Bitmap>();

    protected Activity myActivity;

    protected boolean myIsActive = false;

    public boolean getIsActive(){
        return  myIsActive;
    }

    public  MyCameraManager(){}

    public MyCameraManager(Activity activity,ICameraAction iCameraAction){
        this.iCameraAction = iCameraAction;
        myActivity = activity;
        initialize(activity);
    }

    public Bitmap getImage(int index){
        return myList.get(index);
    }

    abstract protected void initialize(Context context);
    abstract public void takePicture();
    abstract public View getView();

}
