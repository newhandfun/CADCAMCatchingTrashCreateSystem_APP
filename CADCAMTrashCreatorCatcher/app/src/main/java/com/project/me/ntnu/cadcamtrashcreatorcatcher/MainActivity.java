package com.project.me.ntnu.cadcamtrashcreatorcatcher;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.me.ntnu.cadcamtrashcreatorcatcher.Core.CameraManagerOld;
import com.project.me.ntnu.cadcamtrashcreatorcatcher.Core.ICameraAction;
import com.project.me.ntnu.cadcamtrashcreatorcatcher.Core.MyCameraManager;
import com.project.me.ntnu.cadcamtrashcreatorcatcher.UI.ITaskAction;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements ITaskAction {

    /***
     * Timer of send picture to teacher
     * ***/
    int limitTime = 3*60*60;        //limit time of saving photo
    int currentTime = 60;           //current time from picture was taking

    byte[] data;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            currentTime += 1;
            txt_runningHour.setText(currentTime / 3600);
            txt_runningMin.setText((currentTime /60) % 60);
            txt_runningHour.setText(currentTime % 60);

            if(currentTime>=limitTime){
//                try {
//                    GMailSender sender = new GMailSender("username@gmail.com", "password");
//                    sender.sendMail("This is Subject",
//                            "This is Body",
//                            "user@gmail.com",
//                            "user@yahoo.com");
//                } catch (Exception e) {
//                    Log.e("SendMail", e.getMessage(), e);
//                }
                currentTime = 0;
                handler.removeCallbacks(runnable);
            }
        }
    };

    /***
     * UI
     * ***/
    TextView txt_runningHour,txt_runningMin,txt_runningSec;
    EditText txt_limitHour,txt_limitMin,txt_limitSec;
    FrameLayout flayout_cam = null;

    /***
     *UI Events
     */
    //Input of Limit Time
    TextWatcher keyEvent_refreshLimitTime = new TextWatcher(){

        CharSequence orginValue ;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            orginValue = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            int number = 0;
//            try {
            number = Integer.parseInt(s.toString());
//            }catch (Exception e){
//                number = -1;
//            }
            if(number<0||number>60){
                s.replace(0,orginValue.length(),orginValue);
            }else{
                limitTime =
                        Integer.parseInt(txt_limitHour.getText().toString())*3600 +
                                Integer.parseInt(txt_limitMin.getText().toString())*60    +
                                Integer.parseInt(txt_limitSec.getText().toString())
                ;
                Toast.makeText(MainActivity.this,"Limit Time is "+limitTime+" seconds.",Toast.LENGTH_SHORT).show();
            }
        }
    };

    /***
     * Function Manager
     * ***/
    ///Camera
    MyCameraManager myCameraManager;                                //manager
    ICameraAction cameraAction = new ICameraAction() {              //interface
        @Override
        public void takePicture(byte[] data) {
            MainActivity.this.data = data;
            String fileName;
            try {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                dateFormat.format(date);
                fileName = date.toString();
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
                out.write(data);
                out.flush();
                out.close();

                handler.postDelayed(runnable,1000);

                System.gc();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /***
     *Life cycle
     * */
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
        if(flayout_cam==null)flayout_cam = (FrameLayout)findViewById(R.id.flayout_cam);
            //limit
        txt_limitHour = (EditText)findViewById(R.id.txt_limithour);
        txt_limitHour.addTextChangedListener(keyEvent_refreshLimitTime);
        txt_limitMin= (EditText)findViewById(R.id.txt_limitmin);
        txt_limitMin.addTextChangedListener(keyEvent_refreshLimitTime);
        txt_limitSec = (EditText)findViewById(R.id.txt_limitsec);
        txt_limitSec.addTextChangedListener(keyEvent_refreshLimitTime);
        //run
        txt_runningHour = (TextView)findViewById(R.id.txt_runnninghour);
        txt_runningMin = (TextView)findViewById(R.id.txt_runnningmin);
        txt_runningSec = (TextView)findViewById(R.id.txt_limitsec);
    }

    /***
     * Event
     * */
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

    /***
     * Un implement function
     */
    @Override
    public boolean onTimeTaskOver(int index) {

        return true;
    }

}
