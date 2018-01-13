package com.project.me.ntnu.cadcamtrashcreatorcatcher.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.project.me.ntnu.cadcamtrashcreatorcatcher.Core.DocumentManager;
import com.project.me.ntnu.cadcamtrashcreatorcatcher.IMainActivityAction;
import com.project.me.ntnu.cadcamtrashcreatorcatcher.R;


public class FragmentOptionData extends Fragment {

    EditText txt_userName,txt_userPassword,txt_masterName;

    public IMainActivityAction iMainActivityAction = null;

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(iMainActivityAction!=null){
                iMainActivityAction.onEmailDataChange(
                        txt_userName.getText().toString()
                        ,txt_userPassword.getText().toString()
                        ,txt_masterName.getText().toString());
            }
        }
    };

    public FragmentOptionData() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_option_data, container, false);
        txt_userName =  (EditText) view.findViewById(R.id.txt_userEmailName);
        txt_userPassword =(EditText)view.findViewById(R.id.txt_userEmailPassword);
        txt_masterName = (EditText)view.findViewById(R.id.txt_masterEmailName);

        txt_userName.addTextChangedListener(textWatcher);
        txt_masterName.addTextChangedListener(textWatcher);
        txt_userPassword.addTextChangedListener(textWatcher);

        return view;
    }

    public void setUserMailName(String text){
        txt_userName.setText(text);
    }
    public void setUserMailPassword(String text){
        txt_userPassword.setText(text);
    }
    public void setMasterMailName(String text){
        txt_masterName.setText(text);
    }
    public String getUserMailName(){
        return txt_userName.getText().toString();
    }
    public String getUserMainPassword(){
        return txt_userPassword.getText().toString();
    }
    public String getMasterMainName(){
        return txt_masterName.getText().toString();
    }
}
