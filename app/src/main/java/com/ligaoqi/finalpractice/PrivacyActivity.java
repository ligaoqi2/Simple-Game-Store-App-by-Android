package com.ligaoqi.finalpractice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ligaoqi.finalpractice.dialog.CustomDialog;
import com.tencent.mmkv.MMKV;

/**
 * 隐私页面
 */

public class PrivacyActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        sharedPreferences = getSharedPreferences("agree_privacy", MODE_PRIVATE);

        if(sharedPreferences.getString("agree_privacy", null) != null && sharedPreferences.getString("agree_privacy", null).equals("agree")){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(PrivacyActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }, 2000);
        }else{
            showPrivacyDialog();
        }
    }

    private void showPrivacyDialog(){
        final CustomDialog privacyDialog = new CustomDialog(this);
        privacyDialog.setContentView(R.layout.activity_privacy_dialog);
        Button agreeButton = privacyDialog.findViewById(R.id.agree);
        TextView disAgreeButton = privacyDialog.findViewById(R.id.disagree);
        TextView privacyText = privacyDialog.findViewById(R.id.text_privacy);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        agreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                privacyDialog.dismiss();
                editor.putString("agree_privacy", "agree");
                editor.apply();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(PrivacyActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }, 2000);
            }
        });

        disAgreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("agree_privacy", "disagree");
                editor.apply();
                finishAffinity();
            }
        });

        privacyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PrivacyActivity.this, "请授予相关权限", Toast.LENGTH_SHORT).show();
            }
        });

        privacyDialog.show();
    }
}