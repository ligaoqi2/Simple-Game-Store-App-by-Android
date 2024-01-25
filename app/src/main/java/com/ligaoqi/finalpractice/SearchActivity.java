package com.ligaoqi.finalpractice;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.gson.reflect.TypeToken;
import com.ligaoqi.finalpractice.entity.BaseBean;
import com.ligaoqi.finalpractice.entity.GamePageBean;
import com.ligaoqi.finalpractice.entity.NetCallBack;
import com.ligaoqi.finalpractice.fragment.NoResultFragment;
import com.ligaoqi.finalpractice.fragment.ResultFragment;
import com.ligaoqi.finalpractice.fragment.TagViewFragment;
import com.ligaoqi.finalpractice.gson.GsonUtil;
import com.ligaoqi.finalpractice.http.HttpUtil;
import com.ligaoqi.finalpractice.listener.TextViewFragmentOnClickFreshEditText;
import com.ligaoqi.finalpractice.listener.TextViewFragmentOnClickListener;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class SearchActivity extends FragmentActivity implements TextViewFragmentOnClickFreshEditText {

    private TextView searchTextView;
    private Button backButton;
    private EditText searchEdit;
    private FragmentManager fragmentManager;

    private MMKV mmkv;

    private HashSet<String> searchHistorySet;
    private ArrayList<String> searchHistoryList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        fragmentManager = getSupportFragmentManager();

        MMKV.initialize(SearchActivity.this);
        mmkv = MMKV.defaultMMKV();

        searchHistorySet = (HashSet<String>) mmkv.decodeStringSet("search_history");

        if(searchHistorySet != null) {
            searchHistoryList = new ArrayList<String>(searchHistorySet);
        }else{
            searchHistorySet = new HashSet<>();
            searchHistoryList = null;
        }

        Bundle bundle = new Bundle();
        bundle.putStringArrayList("search_history_list", searchHistoryList);

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container_search, TagViewFragment.class, bundle)
                .commit();

        backButton = findViewById(R.id.back);
        searchEdit = findViewById(R.id.edit_text);
        searchTextView = findViewById(R.id.search_text);

        searchTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = searchEdit.getText().toString();
                searchHistorySet.add(searchText);
                mmkv.encode("search_history", searchHistorySet);
                getHttpJump(searchText);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container_search);
                if(fragment instanceof ResultFragment || fragment instanceof NoResultFragment){
                    searchHistorySet = (HashSet<String>) mmkv.decodeStringSet("search_history");

                    if(searchHistorySet != null) {
                        searchHistoryList = new ArrayList<String>(searchHistorySet);
                    }else{
                        searchHistorySet = new HashSet<>();
                        searchHistoryList = null;
                    }

                    Bundle bundle2 = new Bundle();
                    bundle2.putStringArrayList("search_history_list", searchHistoryList);
                    fragmentManager.beginTransaction()
                                   .replace(R.id.fragment_container_search, TagViewFragment.class, bundle2)
                                   .commit();
                }else if(fragment instanceof TagViewFragment){
                    finish();
                }
            }
        });
    }


    private void getHttpJump(String gameName) {
        HashMap<String, String> param = new HashMap<>();
        param.put("search", gameName);
        param.put("current", "1");
        param.put("size", "10");
        HttpUtil.getInstance().get("quick-game/game/search", param, new NetCallBack<BaseBean<GamePageBean>>() {
            @Override
            public void onSuccess(BaseBean<GamePageBean> data) {
                String dataGamePageJson = GsonUtil.getGson().toJson(data.data);
                GamePageBean gamePageBean = GsonUtil.getGson().fromJson(dataGamePageJson, GamePageBean.class);

                if(gamePageBean.records.size() == 0) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_search, NoResultFragment.class, null)
                            .commit();
                }else if(gamePageBean.records.size() > 0){
                    Bundle bundle = new Bundle();
                    bundle.putString("gamesearch", gameName);
                    bundle.putString("dataGamePageJson", dataGamePageJson);
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_search, ResultFragment.class, bundle)
                            .commit();
                }
            }
            @Override
            public void onFailure(int code, String errMsg) {
                Toast.makeText(SearchActivity.this, "请检查您的网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(){
        searchHistorySet = (HashSet<String>) mmkv.decodeStringSet("search_history");

        if(searchHistorySet != null) {
            searchHistoryList = new ArrayList<String>(searchHistorySet);
        }else{
            searchHistorySet = new HashSet<>();
            searchHistoryList = null;
        }

        Bundle bundle3 = new Bundle();
        bundle3.putStringArrayList("search_history_list", searchHistoryList);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_search, TagViewFragment.class, bundle3).commit();
    }

    @Override
    public void onClickFreshEditText(String content) {
        searchEdit.setText("");
        searchEdit.setText(content);
    }
}
