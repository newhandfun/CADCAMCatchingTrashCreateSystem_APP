package com.project.me.ntnu.cadcamtrashcreatorcatcher.Core;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by SPLAB_Note on 2018/1/12.
 */

public class DocumentManager {

    public String root;

    public DocumentManager(){
        Initialize();
    }

    public DocumentManager(Context context){
        Initialize();
    }
    private void Initialize(){
        root = Environment.getExternalStorageDirectory().getAbsolutePath() ;
        File directory = new File(root,"GarbageThrowerCater");
        root = directory.getAbsolutePath();
        directory.mkdirs();
        if(!directory.exists()){
            Log.e("File Create","Can't build");
        }
    }


    public boolean writeToFile(String dirName, String fileName, byte[] data){
        return ChangeFile(dirName,fileName,data,0);
    }

    public byte[] readByFile(String dirName, String fileName){
        File file = CheckDirectory(new String[]{dirName});
        if(file==null){
            return null;
        }
        file = new File(file,fileName);
        if(!file.exists()){
            writeToFile(dirName,fileName,new byte[]{});
        }
        byte[] data;
        try{
            data = new byte[(int)file.length()];
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            bufferedInputStream.read(data,0,data.length);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return  data;
    }

    private File CheckDirectory(String[] dirName){
        try {
            File dir = new File(root);
            for(int i=0;i<dirName.length;i++) {
                dir = new File(dir,dirName[i]);
                dir.mkdirs();
            }
            return dir;
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
    }

    private boolean ChangeFile(String dirName,String fileName,byte[] data,int type){
        CheckDirectory(new String[]{dirName});
        File file = new File(root + "/" + dirName + "/"+ fileName);
        if(file.exists()) {
            switch (type) {
                case 0:
                    file.delete();
                    break;
            }
        }
        try {
            Log.i("Write data to document",fileName);
            FileOutputStream out = new FileOutputStream(file);
            out.write(data);
            out.flush();
            out.close();
        }catch (Exception e){
            e.printStackTrace();
            file = null;
            return  false;
        }
        return  true;
    }

}
