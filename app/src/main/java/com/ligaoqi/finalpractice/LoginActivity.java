package com.ligaoqi.finalpractice;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ligaoqi.finalpractice.entity.BaseBean;
import com.ligaoqi.finalpractice.entity.NetCallBack;
import com.ligaoqi.finalpractice.gson.GsonUtil;
import com.ligaoqi.finalpractice.http.HttpUtil;
import com.tencent.mmkv.MMKV;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextPhoneNum;
    private EditText editTextVerifyNum;

    private TextView getVerifyNum;
    private TextView privacyContent;

    private ImageView privacyVerifyImg;
    private Button loginButton;

    private boolean verifyPrivacy = false;

    private int totalCount = 60;

    private Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if(msg.what == 100){
                totalCount--;
                getVerifyNum.setTextColor(getResources().getColor(R.color.gray));
                getVerifyNum.setText((totalCount) + "s");
                if(totalCount > 0){
                    Message obtain = Message.obtain();
                    obtain.what = 100;
                    handler.sendMessageDelayed(obtain, 1000);
                }else{
                    getVerifyNum.setTextColor(getResources().getColor(R.color.customorange));
                    getVerifyNum.setText("重新发送");
                    totalCount = 60;
                    getVerifyNum.setEnabled(true);
                }
            }
            return true;
        }
    });

    private MMKV mmkv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MMKV.initialize(LoginActivity.this);
        mmkv = MMKV.defaultMMKV();

        editTextPhoneNum = findViewById(R.id.phone_num);
        editTextVerifyNum = findViewById(R.id.verify_num);

        getVerifyNum = findViewById(R.id.get_verify_num);
        privacyContent = findViewById(R.id.verify_privacy_content);

        privacyVerifyImg = findViewById(R.id.verify_privacy_choose);
        loginButton = findViewById(R.id.login_account);

        getVerifyNum.setEnabled(false);

        editTextPhoneNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!verifyPrivacy){
                    Toast.makeText(LoginActivity.this, "请先同意用户协议和隐私政策", Toast.LENGTH_SHORT).show();
                    editTextPhoneNum.setFocusableInTouchMode(false);
                    editTextPhoneNum.clearFocus();
                    hideKeyBoard(editTextPhoneNum, true);
                }else{
                    editTextPhoneNum.setFocusableInTouchMode(true);
                    editTextPhoneNum.requestFocus();
                    editTextPhoneNum.requestFocusFromTouch();
                    hideKeyBoard(editTextPhoneNum, false);
                }
            }
        });

        editTextVerifyNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!verifyPrivacy){
                    Toast.makeText(LoginActivity.this, "请先同意用户协议和隐私政策", Toast.LENGTH_SHORT).show();
                    editTextVerifyNum.setFocusableInTouchMode(false);
                    editTextVerifyNum.clearFocus();
                    hideKeyBoard(editTextPhoneNum, true);
                }else{
                    editTextVerifyNum.setFocusableInTouchMode(true);
                    editTextVerifyNum.requestFocus();
                    editTextVerifyNum.requestFocusFromTouch();
                    hideKeyBoard(editTextVerifyNum, false);
                }
            }
        });

        editTextPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length() == 11){
                    getVerifyNum.setTextColor(getResources().getColor(R.color.customorange));
                    getVerifyNum.setEnabled(true);
                }else{
                    getVerifyNum.setTextColor(getResources().getColor(R.color.gray));
                    getVerifyNum.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextVerifyNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 6 && editTextPhoneNum.getText().length() == 11){
                    loginButton.setTextColor(Color.BLACK);
                    loginButton.setBackgroundResource(R.drawable.edittext_border_login_enable);
                }else{
                    loginButton.setTextColor(getResources().getColor(R.color.gray));
                    loginButton.setBackgroundColor(getResources().getColor(R.color.ssgray));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getVerifyNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postHttpUtilGetVerifyCode(editTextPhoneNum.getText().toString());
                Message obtain = Message.obtain();
                obtain.what = 100;
                handler.sendMessage(obtain);
                getVerifyNum.setEnabled(false);
            }
        });

        privacyContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "隐私协议内容...", Toast.LENGTH_SHORT).show();
            }
        });

        privacyVerifyImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!verifyPrivacy) {
                    privacyVerifyImg.setImageResource(R.drawable.verify_privacy_yes);
                    verifyPrivacy = true;
                }else{
                    privacyVerifyImg.setImageResource(R.drawable.verify_privacy_no);
                    verifyPrivacy = false;
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!verifyPrivacy){
                    Toast.makeText(LoginActivity.this, "请先同意用户协议和隐私政策", Toast.LENGTH_SHORT).show();
                }else{
                    if(editTextPhoneNum.getText().length() == 11){
                        if(editTextVerifyNum.getText().length() == 6){
                            postHttpUtilVerifyAuth(editTextPhoneNum.getText().toString(), editTextVerifyNum.getText().toString());
                        }else{
                            Toast.makeText(LoginActivity.this, "请确认输入6位验证码", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(LoginActivity.this, "请确认输入11位电话号码", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void postHttpUtilGetVerifyCode(String phoneNum){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("phone", phoneNum);
        String json = GsonUtil.getGson().toJson(hashMap);

        MediaType jsonMedia = MediaType.parse("application/json;charset=utf-8");
        RequestBody requestBody = RequestBody.create(json, jsonMedia);

        HttpUtil.getInstance().post("quick-game/api/auth/sendCode", requestBody, new NetCallBack<BaseBean<String>>(){

            @Override
            public void onSuccess(BaseBean<String> data) {

            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(LoginActivity.this, "验证码发送失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postHttpUtilVerifyAuth(String phoneNum, String verifyNum){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("phone", phoneNum);
        hashMap.put("smsCode", verifyNum);
        String json = GsonUtil.getGson().toJson(hashMap);

        MediaType jsonMedia = MediaType.parse("application/json;charset=utf-8");
        RequestBody requestBody = RequestBody.create(json, jsonMedia);

        HttpUtil.getInstance().post("quick-game/api/auth/login", requestBody, new NetCallBack<BaseBean<String>>(){

            @Override
            public void onSuccess(BaseBean<String> data) {
                String token = data.data;
                mmkv.encode("user_token", token);

                Intent intent = new Intent();
                intent.putExtra("token_user", token);

                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(LoginActivity.this, "用户验证失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hideKeyBoard(View view,boolean b) {
        if(b){
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);//关闭输入法
        }else if(!b) {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                    .showSoftInput(view,0);
        }

    }

}
