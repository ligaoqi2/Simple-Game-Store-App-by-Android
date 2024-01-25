package com.ligaoqi.finalpractice;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.ligaoqi.finalpractice.entity.GameInfoBean;
import com.ligaoqi.finalpractice.gson.GsonUtil;

public class DetailActivity extends AppCompatActivity {

    private ImageView backMainFragment;

    private ImageView gameAvatar;

    private TextView updateIntro;

    private TextView gameName;
    private TextView gameVersion;
    private TextView appIntroduction;
    private TextView updateIntroduction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        backMainFragment = findViewById(R.id.introduction_back);
        gameAvatar = findViewById(R.id.game_img_detail);

        gameName = findViewById(R.id.game_name_detail);
        gameVersion = findViewById(R.id.game_version_detail);
        appIntroduction = findViewById(R.id.app_introduction);

        updateIntroduction = findViewById(R.id.update_introduction);
        updateIntroduction.setVisibility(View.GONE);

        updateIntro = findViewById(R.id.update_intro);
        updateIntro.setVisibility(View.GONE);

        String gameInformation = getIntent().getStringExtra("game_information");

        GameInfoBean game = GsonUtil.getGson().fromJson(gameInformation, GameInfoBean.class);

        Glide.with(DetailActivity.this).load(game.getIcon()).into(gameAvatar);
        gameName.setText(game.getGameName());
        gameVersion.setText(game.getVersionName());
        appIntroduction.setText(game.getIntroduction());

        backMainFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
