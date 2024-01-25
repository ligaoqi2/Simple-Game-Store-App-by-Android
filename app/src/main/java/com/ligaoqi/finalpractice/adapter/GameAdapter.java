package com.ligaoqi.finalpractice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ligaoqi.finalpractice.entity.GameInfoBean;
import com.ligaoqi.finalpractice.R;
import com.ligaoqi.finalpractice.entity.GameInfoBean;

import java.util.ArrayList;
import java.util.List;

public class GameAdapter extends BaseQuickAdapter<GameInfoBean, BaseViewHolder> {

    private Context mContext;
    private ArrayList<GameInfoBean> mDataList = new ArrayList<>();

    public GameAdapter(List<GameInfoBean> data, Context context) {
        super(R.layout.result_fragment_item_view, data);
        mContext = context;
    }

    public void setData(ArrayList<GameInfoBean> list){
        mDataList.clear();
        mDataList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, GameInfoBean gameInfoBean) {
        baseViewHolder.setText(R.id.recycler_game_name, gameInfoBean.getGameName());
        baseViewHolder.setText(R.id.recycler_game_brief, gameInfoBean.getBrief());
        baseViewHolder.setText(R.id.recycler_game_tags, gameInfoBean.getTags());
        baseViewHolder.getView(R.id.recycler_view_second_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "即将进入游戏，欢迎使用 ~ ", Toast.LENGTH_SHORT).show();
            }
        });
        Glide.with(mContext).load(gameInfoBean.getIcon()).into((ImageView) baseViewHolder.getView(R.id.recycler_game_img));
    }
}
