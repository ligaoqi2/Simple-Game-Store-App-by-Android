package com.ligaoqi.finalpractice;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.ligaoqi.finalpractice.entity.BaseBean;
import com.ligaoqi.finalpractice.entity.GamePageBean;
import com.ligaoqi.finalpractice.entity.HomeGameBean;
import com.ligaoqi.finalpractice.entity.NetCallBack;
import com.ligaoqi.finalpractice.fragment.MainFragment;
import com.ligaoqi.finalpractice.fragment.NoResultFragment;
import com.ligaoqi.finalpractice.fragment.ResultFragment;
import com.ligaoqi.finalpractice.fragment.SelfFragment;
import com.ligaoqi.finalpractice.fragment.TagViewFragment;
import com.ligaoqi.finalpractice.gson.GsonUtil;
import com.ligaoqi.finalpractice.http.HttpUtil;
import com.ligaoqi.finalpractice.listener.TextViewFragmentOnClickListener;
import com.tencent.mmkv.MMKV;

import java.util.HashMap;

/**
 * 主页面
 */

public class MainActivity extends FragmentActivity implements TextViewFragmentOnClickListener {

    private ImageView recommendImgView;
    private ImageView SelfImgView;

    private TextView recommendTextView;
    private TextView SelfTextView;

    private SelfFragment selfFragment;

    private static final int LOGIN_ACTIVITY_REQUEST_CODE = 5217;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recommendImgView = findViewById(R.id.recommend_img);
        SelfImgView = findViewById(R.id.self_img);
        recommendTextView = findViewById(R.id.recommend_text);
        SelfTextView = findViewById(R.id.self_text);

        recommendImgView.setEnabled(false);
        recommendTextView.setEnabled(false);

        recommendImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelfImgView.setEnabled(true);
                SelfTextView.setEnabled(true);
                recommendImgView.setEnabled(false);
                recommendTextView.setEnabled(false);
                getHttpMainData();
            }
        });

        recommendTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelfImgView.setEnabled(true);
                SelfTextView.setEnabled(true);
                recommendImgView.setEnabled(false);
                recommendTextView.setEnabled(false);
                getHttpMainData();
            }
        });

        SelfImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelfImgView.setEnabled(false);
                SelfTextView.setEnabled(false);
                recommendImgView.setEnabled(true);
                recommendTextView.setEnabled(true);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_main, SelfFragment.class, null)
                        .commit();
            }
        });

        SelfTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelfImgView.setEnabled(false);
                SelfTextView.setEnabled(false);
                recommendImgView.setEnabled(true);
                recommendTextView.setEnabled(true);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_main, SelfFragment.class, null)
                        .commit();
            }
        });

        getHttpMainData();
    }

    private void getHttpMainData() {
        HashMap<String, String> param = new HashMap<>();
        param.put("current", "1");
        param.put("size", "2");
        HttpUtil.getInstance().get("quick-game/game/homePage", param, new NetCallBack<BaseBean<HomeGameBean>>() {
            @Override
            public void onSuccess(BaseBean<HomeGameBean> data) {
                String homeGameBeanJson = GsonUtil.getGson().toJson(data.data);
                Bundle bundle = new Bundle();
                bundle.putString("home_game_bean", homeGameBeanJson);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_main, MainFragment.class, bundle)
                        .commit();

            }
            @Override
            public void onFailure(int code, String errMsg) {
                Toast.makeText(MainActivity.this, "请检查您的网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick() {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClickLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivityForResult(intent, LOGIN_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOGIN_ACTIVITY_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                String resultData = data.getStringExtra("token_user");

                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container_main);

                if(currentFragment instanceof SelfFragment){
                    selfFragment = (SelfFragment) currentFragment;
                }
                selfFragment.updateUserData(resultData);
            }
        }
    }



}
