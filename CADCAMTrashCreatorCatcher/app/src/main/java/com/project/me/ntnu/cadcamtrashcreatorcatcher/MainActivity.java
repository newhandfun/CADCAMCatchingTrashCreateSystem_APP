package com.project.me.ntnu.cadcamtrashcreatorcatcher;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import com.project.me.ntnu.cadcamtrashcreatorcatcher.Core.CameraManagerOld;
import com.project.me.ntnu.cadcamtrashcreatorcatcher.Core.ICameraAction;
import com.project.me.ntnu.cadcamtrashcreatorcatcher.Core.MyCameraManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static java.lang.System.out;

public class MainActivity extends AppCompatActivity {

    FrameLayout flayout_cam = null;

    MyCameraManager myCameraManager;

    ICameraAction cameraAction = new ICameraAction() {
        @Override
        public void takePicture(Bitmap bitmap) {
            String fileName = "1995-01-01-01-01-01";
            try {
                new SimpleDateFormat("yyyy-MM-DD-hh-mm-ss", Locale.TRADITIONAL_CHINESE).parse(fileName);
            }catch (Exception e){
                Log.e("Parse Image",e.toString());
                return;
            }
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/req_images");
            myDir.mkdirs();
            String fname = fileName + ".jpg";
            File file = new File(myDir, fname);
            Log.i("Save image", "" + file);
            if (file.exists())
                file.delete();
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 50);

        //core
        if(myCameraManager==null)
            myCameraManager = new CameraManagerOld(this,cameraAction);

        //ui
        flayout_cam = (FrameLayout)findViewById(R.id.flayout_cam);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN||keyCode==KeyEvent.KEYCODE_VOLUME_UP){
            myCameraManager.takePicture();
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.w("Camera set","granted!");
            //intial core
            if(flayout_cam==null) {
                flayout_cam = (FrameLayout)findViewById(R.id.flayout_cam);
            }
            if(myCameraManager==null) {
                myCameraManager = new CameraManagerOld(this, cameraAction);
            }
            flayout_cam.addView(myCameraManager.getView());
            Log.w("Camera set","already setting!");
        }else{
            finish();
        }
    }


}
