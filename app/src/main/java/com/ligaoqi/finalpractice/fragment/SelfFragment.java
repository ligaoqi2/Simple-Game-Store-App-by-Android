package com.ligaoqi.finalpractice.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.ligaoqi.finalpractice.R;
import com.ligaoqi.finalpractice.entity.BaseBean;
import com.ligaoqi.finalpractice.entity.GamePageBean;
import com.ligaoqi.finalpractice.entity.NetCallBack;
import com.ligaoqi.finalpractice.entity.UserBean;
import com.ligaoqi.finalpractice.gson.GsonUtil;
import com.ligaoqi.finalpractice.http.HttpUtil;
import com.ligaoqi.finalpractice.listener.TextViewFragmentOnClickListener;
import com.tencent.mmkv.MMKV;

import java.util.HashMap;

public class SelfFragment extends Fragment {
    public Context mainContext;
    private ImageView imageView;
    private ImageView imageViewEmoji;
    private TextView textView;

    private TextView textviewLogOut;

    private MMKV mmkv;

    public SelfFragment() {
        super(R.layout.activity_self_fragment);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainContext = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MMKV.initialize(mainContext);
        mmkv = MMKV.defaultMMKV();

        imageView = view.findViewById(R.id.avatar_self_fragment);
        imageViewEmoji = view.findViewById(R.id.emoji);

        textView = view.findViewById(R.id.login_in);
        textviewLogOut = view.findViewById(R.id.log_out);

        textviewLogOut.setVisibility(View.GONE);

        // 点击登录
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainContext instanceof TextViewFragmentOnClickListener) {
                    TextViewFragmentOnClickListener listener = (TextViewFragmentOnClickListener) mainContext;
                    imageViewEmoji.setVisibility(View.GONE);
                    imageView.setBackgroundColor(Color.TRANSPARENT);
                    listener.onClickLogin();
                }
            }
        });

        // 退出登录
        textviewLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mmkv.removeValueForKey("user_token");
                imageViewEmoji.setVisibility(View.VISIBLE);
                imageView.setBackgroundResource(R.drawable.avatar_border);
                imageView.setImageResource(0);
                textviewLogOut.setVisibility(View.GONE);
                textView.setText("点击登录");
                textView.setEnabled(true);
            }
        });

        if(mmkv.decodeString("user_token")!= null){
            imageViewEmoji.setVisibility(View.GONE);
            imageView.setBackgroundColor(Color.TRANSPARENT);
            textView.setEnabled(false);
            textviewLogOut.setVisibility(View.VISIBLE);
            updateUserData(mmkv.decodeString("user_token"));
        }else{
            textView.setEnabled(true);
            textviewLogOut.setVisibility(View.GONE);
        }
    }

    public void updateUserData(String token){
        getHttpUtilUpdateUserInformation(token);
    }

    public void getHttpUtilUpdateUserInformation(String token){

        HttpUtil.getInstance().getUserInformation("quick-game/api/user/info", token, new NetCallBack<BaseBean<UserBean>>() {
            @Override
            public void onSuccess(BaseBean<UserBean> data) {
                String userBeanJson = GsonUtil.getGson().toJson(data.data);
                UserBean user = GsonUtil.getGson().fromJson(userBeanJson, UserBean.class);

                imageViewEmoji.setVisibility(View.GONE);
                textView.setText(user.getUsername());
                imageView.setBackgroundColor(Color.TRANSPARENT);
                textviewLogOut.setVisibility(View.VISIBLE);
                Glide.with(mainContext).load(user.getAvatar()).into(imageView);
            }
            @Override
            public void onFailure(int code, String errMsg) {

            }
        });
    }

}
