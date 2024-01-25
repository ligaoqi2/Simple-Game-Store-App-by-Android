package com.ligaoqi.finalpractice.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ligaoqi.finalpractice.R;
import com.ligaoqi.finalpractice.adapter.GameAdapter;
import com.ligaoqi.finalpractice.entity.BaseBean;
import com.ligaoqi.finalpractice.entity.GameInfoBean;
import com.ligaoqi.finalpractice.entity.GamePageBean;
import com.ligaoqi.finalpractice.entity.NetCallBack;
import com.ligaoqi.finalpractice.gson.GsonUtil;
import com.ligaoqi.finalpractice.http.HttpUtil;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ResultFragment extends Fragment {

    private Context mContext;
    private RecyclerView mRecyclerView;
//    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SmartRefreshLayout mSmartRefreshLayout;

    private Handler handler = new Handler(Looper.getMainLooper());

    private ArrayList<GameInfoBean> mDataList = new ArrayList<>();
    private GameAdapter gameAdapter;

    private int currentPage = 1;

    public ResultFragment(){
        super(R.layout.result_fragment);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recycler_view_game);

        Bundle bundle = requireArguments();
        String gameName = bundle.getString("gamesearch");
        String dataGamePageJson = bundle.getString("dataGamePageJson");
        GamePageBean gamePageBean = GsonUtil.getGson().fromJson(dataGamePageJson, GamePageBean.class);

        mDataList.addAll(gamePageBean.records);

        gameAdapter = new GameAdapter(mDataList, getContext());
        mRecyclerView.setAdapter(gameAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mSmartRefreshLayout = view.findViewById(R.id.refresh_game);
        mSmartRefreshLayout.setRefreshHeader(new ClassicsHeader(mContext));
        mSmartRefreshLayout.setRefreshFooter(new ClassicsFooter(mContext));

        //下拉刷新
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mDataList.clear();
                currentPage = 1;
                Log.i("page", "page num is: " + currentPage);

                getHttpUtil(gameName, currentPage, mDataList);

                mSmartRefreshLayout.finishRefresh(2000);
            }
        });

        //上拉加载
        mSmartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                currentPage++;
                Log.i("page", "page num is: " + currentPage);
                getHttpUtil(gameName, currentPage, mDataList);

                mSmartRefreshLayout.finishLoadMore(2000);
            }
        });
    }

    private void getHttpUtil(String gameName, int currentPage, ArrayList<GameInfoBean> GameDataList) {
        HashMap<String, String> param = new HashMap<>();
        param.put("search", gameName);
        param.put("current", String.valueOf(currentPage));
        param.put("size", "10");
        HttpUtil.getInstance().get("quick-game/game/search", param, new NetCallBack<BaseBean<GamePageBean>>() {
            @Override
            public void onSuccess(BaseBean<GamePageBean> data) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String dataGamePageJson = GsonUtil.getGson().toJson(data.data);
                        GamePageBean gamePageBean = GsonUtil.getGson().fromJson(dataGamePageJson, GamePageBean.class);
                        GameDataList.addAll(gamePageBean.records);
                        gameAdapter.notifyDataSetChanged();
                    }
                });
            }
            @Override
            public void onFailure(int code, String errMsg) {

            }
        });
    }

}

