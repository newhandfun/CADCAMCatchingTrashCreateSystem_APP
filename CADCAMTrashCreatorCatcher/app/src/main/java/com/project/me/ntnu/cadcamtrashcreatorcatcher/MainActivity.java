package com.project.me.ntnu.cadcamtrashcreatorcatcher;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.project.me.ntnu.cadcamtrashcreatorcatcher.Core.CameraManagerOld;
import com.project.me.ntnu.cadcamtrashcreatorcatcher.Core.DocumentManager;
import com.project.me.ntnu.cadcamtrashcreatorcatcher.Core.ICameraAction;
import com.project.me.ntnu.cadcamtrashcreatorcatcher.Core.MyCameraManager;
import com.project.me.ntnu.cadcamtrashcreatorcatcher.UI.ITaskAction;
import com.project.me.ntnu.cadcamtrashcreatorcatcher.UI.PagerAdapterOption;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.zip.Inflater;


public class MainActivity extends AppCompatActivity implements ITaskAction,IMainActivityAction {

    /***
     *Main Handler
     * */
    Handler handler = new Handler();

    /***
     * Timer of send picture to teacher
     * ***/
    int limitTime = 10;        //limit time of saving photo
    int currentTime = 0;           //current time from picture was taking

    //data of image
    byte[] data;
    //name of pic
    String imageName;


    Runnable garbageRunnable = new Runnable() {
        @Override
        public void run() {
            currentTime += 1;
            int remaind = limitTime - currentTime;
            txt_runningHour.setText(Integer.toString(remaind / 3600));
            txt_runningMin.setText(Integer.toString((remaind /60) % 60));
            txt_runningSec.setText(Integer.toString(remaind % 60));

            if(currentTime>=limitTime) {
                if (!userEmailPassword.equals("")){
                    BackgroundMail.newBuilder(MainActivity.this)
                            .withUsername(userEmailName)
                            .withPassword(userEmailPassword)
                            .withSenderName("Garbage Catcher")
                            .withMailTo(masterEmailName)
                            .withMailCc(userEmailName)
                            .withMailBcc(userEmailName)
                            .withType(BackgroundMail.TYPE_PLAIN)
                            .withSubject("Some one throw garbage.Please take a look.")
                            .withBody("picture in attachment.")
                            .withAttachments(documentManager.root + "/" + pictureDirName + "/" + imageName)
                            .withProcessVisibility(false)
                            .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(MainActivity.this, "Send Success!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                                @Override
                                public void onFail() {
                                    Toast.makeText(MainActivity.this, "Send Fail!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .send();
                }
                stopSendingTimer();
                return;
            }
            resumeSendingTimer();
        }
    };
    /***
     * Command Combo System
     */
    int comboTime = 0;
    final int comboLimitTime = 400;
    Runnable comboRunnable = new Runnable() {
        @Override
        public void run() {
            comboTime += 50;
            if(comboTime>comboLimitTime){
                handler.removeCallbacks(comboRunnable);
                comboTime = 0;
            }
        }
    };

    /**
     * Timer
     * */
    void startSendingTimer(){
        currentTime = 0;
        resumeSendingTimer();
    }
    void resumeSendingTimer(){
        handler.postDelayed(garbageRunnable,1000);
    }
    void stopSendingTimer(){
        currentTime = 0;
        txt_runningSec.setText(Integer.toString(0));
        txt_runningMin.setText(Integer.toString(0));
        txt_runningHour.setText(Integer.toString(0));
        handler.removeCallbacks(garbageRunnable);
    }

     /***
     * File IO
     */
    DocumentManager documentManager = new DocumentManager(this);
    String pictureDirName = "Picture";
    String pictureDateFormat = "yyyy-MM-dd-HH:mm:ss";


    /***
     * UI
     * ***/
    TextView txt_runningHour,txt_runningMin,txt_runningSec;
    EditText txt_limitHour,txt_limitMin,txt_limitSec;
    FrameLayout flayout_cam = null;
    ViewPager viewpager_option;
    TabLayout tlaout_option;
    PagerAdapterOption pagerAdapterOption;

    /***
     *UI Events
     */
    //Input of Limit Time
    TextWatcher keyEvent_refreshLimitTime = new TextWatcher(){

        CharSequence originalValue;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            originalValue = s;
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
                s.replace(0, originalValue.length(), originalValue);
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

    void changeLimitText(){

    }

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
                DateFormat dateFormat = new SimpleDateFormat(pictureDateFormat);
                fileName = dateFormat.format(Calendar.getInstance().getTime());
                imageName = fileName+".jpg";
            }catch (Exception e){
                Log.e("Parse Image",e.toString());
                return;
            }
            if(!documentManager.writeToFile(pictureDirName,imageName,data)){
                Log.e("Save picture","Can't save");
                return;
            }else{
                //Take picture successfully,start count.
                startSendingTimer();
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
        viewpager_option = (ViewPager)findViewById(R.id.viewpager_option);
        tlaout_option = (TabLayout)findViewById(R.id.tlayout_option);
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
        txt_runningSec = (TextView)findViewById(R.id.txt_runningsec);

        //UI Event
        pagerAdapterOption = new PagerAdapterOption(this,getSupportFragmentManager());
        viewpager_option.setAdapter(pagerAdapterOption);
        tlaout_option.setupWithViewPager(viewpager_option);

        //UI number

    }

    @Override
    protected void onResume() {
        super.onResume();

        //Core Initial
        //Email Data
        loadEmailData();
        AlertDialog dialog;
        final AlertDialog.Builder alertDialog =new AlertDialog.Builder(this);
        alertDialog.setTitle("Enter Email Password");
        final View view = getLayoutInflater().inflate(R.layout.dialog_enter_password,null);
        alertDialog.setView(view);
        alertDialog.setPositiveButton(R.string.confirm,new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText text = (EditText) view.findViewById(R.id.txt_userpassword);
                userEmailPassword = text.getText().toString();
                pagerAdapterOption.refreshEmailData(userEmailName,userEmailPassword,masterEmailName);
                dialog.dismiss();
            }
        });
        dialog = alertDialog.create();
        dialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();

//        stopSendingTimer();
//        handler.removeCallbacks(garbageRunnable);
//        handler.removeCallbacks(comboRunnable);

    }


    /***
     * Event
     * */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN||keyCode==KeyEvent.KEYCODE_VOLUME_UP){
            getCommand();
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

    /***
     * Local Function
     * */
    //Data save
    static final String saperateChar = "%";
    String emailDirName = "Email";
    void loadEmailData() {
        byte[] data = documentManager.readByFile(emailDirName, emailFileName);
        String strData = "";
        try {
            strData = new String(data,0,data.length, "UTF-8");
        } catch (Exception e){
            e.printStackTrace();
        }
        String[] realData = strData.split(saperateChar);
        userEmailName = realData[0];
        if(realData.length>1) {
            userEmailPassword = realData[1];
            if (realData.length>2) {
                masterEmailName = realData[2];
            }
        }
        pagerAdapterOption.refreshEmailData(userEmailName,userEmailPassword,masterEmailName);
    }

    //Command
    int flag = -1;
    public void getCommand(){
        flag = (flag+1)%3;
        switch (flag){
            case 0:
                myCameraManager.takePicture();
                Toast.makeText(this,"Some one put garbage on desk.",Toast.LENGTH_SHORT).show();
                break;
            case 1:
                handler.postDelayed(comboRunnable,50);
                break;
            case 2:
                if(comboLimitTime<comboTime){
                    return;
                }
                stopSendingTimer();
                Toast.makeText(this,"Garbage allready removed.",Toast.LENGTH_SHORT).show();
                break;
        }
    }


    //Main
    String emailFileName = "emailData.txt";
    String userEmailName = "";
    String userEmailPassword = "";
    String masterEmailName = "";
    void sendMail() throws IOException {
    }

    @Override
    public void onEmailDataChange(String myName, String myPassword, String masterName){
        userEmailName = myName;
        userEmailPassword = myPassword;
        masterEmailName  = masterName;

        byte[] data = new byte[0];

        try {
            String strData = (userEmailName + saperateChar  + saperateChar + masterEmailName);
             data = strData.getBytes("UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        documentManager.writeToFile(emailDirName,emailFileName,data);
    }
}
