package com.ligaoqi.finalpractice.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ligaoqi.finalpractice.R;
import com.ligaoqi.finalpractice.adapter.GameAdapter;
import com.ligaoqi.finalpractice.adapter.HomeGameAdapter;
import com.ligaoqi.finalpractice.entity.BaseBean;
import com.ligaoqi.finalpractice.entity.GameInfoBean;
import com.ligaoqi.finalpractice.entity.GamePageBean;
import com.ligaoqi.finalpractice.entity.HomeGameBean;
import com.ligaoqi.finalpractice.entity.HomeGameListBean;
import com.ligaoqi.finalpractice.entity.NetCallBack;
import com.ligaoqi.finalpractice.entity.SingleLineGameList;
import com.ligaoqi.finalpractice.gson.GsonUtil;
import com.ligaoqi.finalpractice.http.HttpUtil;
import com.ligaoqi.finalpractice.listener.TextViewFragmentOnClickListener;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainFragment extends Fragment {

    private TextView textView;
    public Context mainContext;
    private RecyclerView recyclerView;
    private HomeGameAdapter homeGameAdapter;
    private SmartRefreshLayout smartRefreshLayout;

    private Handler handler = new Handler(Looper.getMainLooper());

    private ArrayList<SingleLineGameList> mDataList = new ArrayList<>();

    private ArrayList<GameInfoBean> gameInfoBuffer = new ArrayList<>();

    private int currentPage = 1;
    private int size = 2;

    public MainFragment() {
        super(R.layout.activity_main_fragment);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainContext = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView= view.findViewById(R.id.edit_text_main);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mainContext instanceof TextViewFragmentOnClickListener){
                    TextViewFragmentOnClickListener listener = (TextViewFragmentOnClickListener) mainContext;
                    listener.onClick();
                }

            }
        });

        Bundle bundle = requireArguments();
        String homeGameBean = bundle.getString("home_game_bean");
        HomeGameBean homePageBean = GsonUtil.getGson().fromJson(homeGameBean, HomeGameBean.class);

        generateAdapterData(homePageBean, mDataList);

        homeGameAdapter = new HomeGameAdapter(mDataList, getContext());
        recyclerView = view.findViewById(R.id.recycler_view_main_game);
        recyclerView.setAdapter(homeGameAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        smartRefreshLayout = view.findViewById(R.id.refresh_game_main);
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(mainContext));
        smartRefreshLayout.setRefreshFooter(new ClassicsFooter(mainContext));

        //下拉刷新
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mDataList.clear();
                currentPage = 1;
                Log.i("page", "page num is: " + currentPage);

                getHttpMainDataUtil(currentPage, size, mDataList);

                smartRefreshLayout.finishRefresh(2000);
            }
        });

        //上拉加载
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                currentPage++;
                Log.i("page", "page num is: " + currentPage);

                getHttpMainDataUtil(currentPage, size, mDataList);

                smartRefreshLayout.finishLoadMore(2000);
            }
        });

    }

    private void getHttpMainDataUtil(int currentPage, int size, ArrayList<SingleLineGameList> homeGameListBeanList) {
        HashMap<String, String> param = new HashMap<>();
        param.put("current", String.valueOf(currentPage));
        param.put("size", String.valueOf(size));
        HttpUtil.getInstance().get("quick-game/game/homePage", param, new NetCallBack<BaseBean<GamePageBean>>() {
            @Override
            public void onSuccess(BaseBean<GamePageBean> data) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String homeGameBean = GsonUtil.getGson().toJson(data.data);
                        HomeGameBean homePageBean = GsonUtil.getGson().fromJson(homeGameBean, HomeGameBean.class);
                        ArrayList<SingleLineGameList> newList = new ArrayList<>();
                        homeGameListBeanList.addAll(generateAdapterData(homePageBean, newList));
                        homeGameAdapter.notifyDataSetChanged();
                    }
                });
            }
            @Override
            public void onFailure(int code, String errMsg) {

            }
        });
    }

    //数据处理
    public ArrayList<SingleLineGameList> generateAdapterData(HomeGameBean homePageBean, ArrayList<SingleLineGameList> mDataList){
        for(int i = 0; i < 2; i++) {

            for (int j = 0; j < homePageBean.records.size(); j++) {

                HomeGameListBean homeGameListBean = homePageBean.records.get(j);

                SingleLineGameList homeGameListTheme = new SingleLineGameList();

                homeGameListTheme.setTheme(true);
                homeGameListTheme.setGameInfoList(null);
                homeGameListTheme.setModuleCode(homeGameListBean.getModuleCode());
                homeGameListTheme.setModuleName(homeGameListBean.getModuleName());

                if(!(homeGameListBean.getStyle() == 3 && i == 1)) {
                    mDataList.add(homeGameListTheme);
                }

                if (homeGameListBean.getStyle() == 1) {
                    if(i == 0){
                        for(int k = 0; k < 2; k++){
                            SingleLineGameList singleLineGameList = new SingleLineGameList();
                            singleLineGameList.setGameInfoList(homeGameListBean.getGameInfoList().subList(k * 3, (k + 1) * 3));

                            //配置信息
                            singleLineGameList.setTheme(false);
                            singleLineGameList.setStyle(1);

                            mDataList.add(singleLineGameList);
                        }
                    }else if(i == 1){
                        for(int k = 0; k < 2; k++){
                            SingleLineGameList singleLineGameList = new SingleLineGameList();
                            singleLineGameList.setGameInfoList(homeGameListBean.getGameInfoList().subList((k + 2) * 3, (k + 3) * 3));

                            //配置信息
                            singleLineGameList.setTheme(false);
                            singleLineGameList.setStyle(1);

                            mDataList.add(singleLineGameList);
                        }
                    }

                } else if (homeGameListBean.getStyle() == 2) {

                    if(i == 0){

                        for(int k = 0; k < 2; k++){
                            SingleLineGameList singleLineGameList = new SingleLineGameList();
                            singleLineGameList.setGameInfoList(homeGameListBean.getGameInfoList().subList(k * 4, (k + 1) * 4));

                            //配置信息
                            singleLineGameList.setTheme(false);
                            singleLineGameList.setStyle(2);

                            mDataList.add(singleLineGameList);
                        }

                    }else if(i == 1){
                        SingleLineGameList singleLineGameList = new SingleLineGameList();
                        singleLineGameList.setGameInfoList(homeGameListBean.getGameInfoList().subList(8, 12));

                        //配置信息
                        singleLineGameList.setTheme(false);
                        singleLineGameList.setStyle(2);

                        mDataList.add(singleLineGameList);
                    }
                }else if (homeGameListBean.getStyle() == 3){

                    if(i == 0) {

                        for (int k = 0; k < 12; k++) {
                            SingleLineGameList singleLineGameList = new SingleLineGameList();
                            ArrayList<GameInfoBean> newList = new ArrayList<>();
                            newList.add(homeGameListBean.getGameInfoList().get(k));
                            singleLineGameList.setGameInfoList(newList);

                            //配置信息
                            singleLineGameList.setTheme(false);
                            singleLineGameList.setStyle(3);

                            mDataList.add(singleLineGameList);
                        }
                    }
                }
            }
        }
        return mDataList;
    }

}
