package com.ligaoqi.finalpractice.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ligaoqi.finalpractice.DetailActivity;
import com.ligaoqi.finalpractice.R;
import com.ligaoqi.finalpractice.entity.BaseBean;
import com.ligaoqi.finalpractice.entity.GameInfoBean;
import com.ligaoqi.finalpractice.entity.GamePageBean;
import com.ligaoqi.finalpractice.entity.HomeGameListBean;
import com.ligaoqi.finalpractice.entity.NetCallBack;
import com.ligaoqi.finalpractice.entity.SingleLineGameList;
import com.ligaoqi.finalpractice.gson.GsonUtil;
import com.ligaoqi.finalpractice.http.HttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeGameAdapter extends BaseMultiItemQuickAdapter<SingleLineGameList, BaseViewHolder> {

    private Context mContext;
    private ArrayList<HomeGameListBean> mDataList = new ArrayList<>();

    private int themeImgId;

    public HomeGameAdapter(List<SingleLineGameList> data, Context context) {
        super(data);
        mContext = context;
        addItemType(HomeGameListBean.TYPE_LAYOUT0, R.layout.home_fragment_item_view_zero);
        addItemType(HomeGameListBean.TYPE_LAYOUT1, R.layout.home_fragment_item_view_one);
        addItemType(HomeGameListBean.TYPE_LAYOUT2, R.layout.home_fragment_item_view_two);
        addItemType(HomeGameListBean.TYPE_LAYOUT3, R.layout.home_fragment_item_view_three);
    }

    @Override
    protected void convert(BaseViewHolder helper, SingleLineGameList singleLineGameList) {

        Log.d("helperviewtype", String.valueOf(helper.getItemViewType()));

        switch(helper.getItemViewType()){
            case HomeGameListBean.TYPE_LAYOUT0:
                switch(singleLineGameList.getModuleCode()){
                    case "BWXY":
                    case "BSBW":
                        themeImgId = R.drawable.icon_theme_1;
                        break;
                    case "DJDZW":
                        themeImgId = R.drawable.icon_theme_2;
                        break;
                }
                Glide.with(mContext).load(themeImgId).into((ImageView) helper.getView(R.id.recycler_game_img_theme));
                helper.setText(R.id.recycler_category_game_name, singleLineGameList.getModuleName());
                break;

            case HomeGameListBean.TYPE_LAYOUT1:

                Glide.with(mContext).load(singleLineGameList.getGameInfoList().get(0).getIcon()).into((ImageView) helper.getView(R.id.recycler_game_img_1));
                Glide.with(mContext).load(singleLineGameList.getGameInfoList().get(1).getIcon()).into((ImageView) helper.getView(R.id.recycler_game_img_2));
                Glide.with(mContext).load(singleLineGameList.getGameInfoList().get(2).getIcon()).into((ImageView) helper.getView(R.id.recycler_game_img_3));

                helper.setText(R.id.recycler_game_view_1_layout_1, singleLineGameList.getGameInfoList().get(0).getGameName());
                helper.setText(R.id.recycler_game_view_2_layout_1, singleLineGameList.getGameInfoList().get(1).getGameName());
                helper.setText(R.id.recycler_game_view_3_layout_1, singleLineGameList.getGameInfoList().get(2).getGameName());

                helper.setText(R.id.recycler_game_brief_1_layout_1, singleLineGameList.getGameInfoList().get(0).getPlayNumFormat() + "人正在玩");
                helper.setText(R.id.recycler_game_brief_2_layout_1, singleLineGameList.getGameInfoList().get(1).getPlayNumFormat() + "人正在玩");
                helper.setText(R.id.recycler_game_brief_3_layout_1, singleLineGameList.getGameInfoList().get(2).getPlayNumFormat() + "人正在玩");

                // 游戏详情
                helper.getView(R.id.recycler_game_img_1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getHttpUtilGameInformation(singleLineGameList.getGameInfoList().get(0).getId());
                    }
                });

                helper.getView(R.id.recycler_game_img_2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getHttpUtilGameInformation(singleLineGameList.getGameInfoList().get(1).getId());
                    }
                });

                helper.getView(R.id.recycler_game_img_3).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getHttpUtilGameInformation(singleLineGameList.getGameInfoList().get(2).getId());
                    }
                });

                break;

            case HomeGameListBean.TYPE_LAYOUT2:

                Glide.with(mContext).load(singleLineGameList.getGameInfoList().get(0).getIcon()).into((ImageView) helper.getView(R.id.recycler_game_img_1_layout_2));
                Glide.with(mContext).load(singleLineGameList.getGameInfoList().get(1).getIcon()).into((ImageView) helper.getView(R.id.recycler_game_img_2_layout_2));
                Glide.with(mContext).load(singleLineGameList.getGameInfoList().get(2).getIcon()).into((ImageView) helper.getView(R.id.recycler_game_img_3_layout_2));
                Glide.with(mContext).load(singleLineGameList.getGameInfoList().get(3).getIcon()).into((ImageView) helper.getView(R.id.recycler_game_img_4_layout_2));

                helper.setText(R.id.recycler_game_view_1_layout_2, singleLineGameList.getGameInfoList().get(0).getGameName());
                helper.setText(R.id.recycler_game_view_2_layout_2, singleLineGameList.getGameInfoList().get(1).getGameName());
                helper.setText(R.id.recycler_game_view_3_layout_2, singleLineGameList.getGameInfoList().get(2).getGameName());
                helper.setText(R.id.recycler_game_view_4_layout_1, singleLineGameList.getGameInfoList().get(3).getGameName());

                helper.setText(R.id.recycler_game_brief_1_layout_2, singleLineGameList.getGameInfoList().get(0).getTags());
                helper.setText(R.id.recycler_game_brief_2_layout_2, singleLineGameList.getGameInfoList().get(1).getTags());
                helper.setText(R.id.recycler_game_brief_3_layout_2, singleLineGameList.getGameInfoList().get(2).getTags());
                helper.setText(R.id.recycler_game_brief_4_layout_2, singleLineGameList.getGameInfoList().get(3).getTags());

                // 游戏详情
                helper.getView(R.id.recycler_game_img_1_layout_2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getHttpUtilGameInformation(singleLineGameList.getGameInfoList().get(0).getId());
                    }
                });

                helper.getView(R.id.recycler_game_img_2_layout_2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getHttpUtilGameInformation(singleLineGameList.getGameInfoList().get(1).getId());
                    }
                });

                helper.getView(R.id.recycler_game_img_3_layout_2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getHttpUtilGameInformation(singleLineGameList.getGameInfoList().get(2).getId());
                    }
                });

                helper.getView(R.id.recycler_game_img_4_layout_2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getHttpUtilGameInformation(singleLineGameList.getGameInfoList().get(3).getId());
                    }
                });

                // 按钮事件
                helper.getView(R.id.recycler_view_1_play_layout_2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "即将进入游戏，欢迎使用 ~ ", Toast.LENGTH_SHORT).show();
                    }
                });

                helper.getView(R.id.recycler_view_2_play_layout_2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "即将进入游戏，欢迎使用 ~ ", Toast.LENGTH_SHORT).show();
                    }
                });

                helper.getView(R.id.recycler_view_3_play_layout_2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "即将进入游戏，欢迎使用 ~ ", Toast.LENGTH_SHORT).show();
                    }
                });

                helper.getView(R.id.recycler_view_4_play_layout_2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "即将进入游戏，欢迎使用 ~ ", Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case HomeGameListBean.TYPE_LAYOUT3:

                Glide.with(mContext).load(singleLineGameList.getGameInfoList().get(0).getIcon()).into((ImageView) helper.getView(R.id.recycler_game_img_1_layout_3));

                helper.setText(R.id.recycler_game_name_layout_3, singleLineGameList.getGameInfoList().get(0).getGameName());
                helper.setText(R.id.recycler_game_tags_layout_3, singleLineGameList.getGameInfoList().get(0).getTags());
                helper.setText(R.id.recycler_game_brief_layout_3, singleLineGameList.getGameInfoList().get(0).getBrief());

                //游戏详情
                helper.getView(R.id.recycler_game_img_1_layout_3).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getHttpUtilGameInformation(singleLineGameList.getGameInfoList().get(0).getId());
                    }
                });

                // 按钮事件
                helper.getView(R.id.recycler_view_second_play).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "即将进入游戏，欢迎使用 ~ ", Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            default:
                break;
        }
    }

    private void getHttpUtilGameInformation(int id){
        HttpUtil.getInstance().getGameInformation("quick-game/game/" + String.valueOf(id), new NetCallBack<BaseBean<GameInfoBean>>() {
            @Override
            public void onSuccess(BaseBean<GameInfoBean> data) {
                String gameInfoBeanJson = GsonUtil.getGson().toJson(data.data);
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("game_information", gameInfoBeanJson);
                mContext.startActivity(intent);
            }
            @Override
            public void onFailure(int code, String errMsg) {
                Toast.makeText(mContext, "获取游戏详情失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
