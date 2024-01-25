package com.ligaoqi.finalpractice.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ligaoqi.finalpractice.R;
import com.ligaoqi.finalpractice.listener.TextViewFragmentOnClickFreshEditText;
import com.nex3z.flowlayout.FlowLayout;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;

public class TagViewFragment extends Fragment {
    private Context mContext;
    private FlowLayout mFlowLayout;
    private ViewGroup.LayoutParams params;
    private ViewGroup.LayoutParams paramsImgView;

    private ImageView garbageImg;
    private ImageView imageView;

    private MMKV mmkv;

    public TagViewFragment(){
        super(R.layout.tagview_fragment);
    }

    private ArrayList<String> historyList;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mmkv = MMKV.defaultMMKV();
        mFlowLayout = view.findViewById(R.id.flow_layout);
        garbageImg = view.findViewById(R.id.garbage);

        garbageImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mmkv.removeValueForKey("search_history");
                if(mContext instanceof TextViewFragmentOnClickFreshEditText){
                    TextViewFragmentOnClickFreshEditText listener = (TextViewFragmentOnClickFreshEditText) mContext;
                    listener.onClick();
                }
            }
        });

        int heightInPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 26, mContext.getResources().getDisplayMetrics());
        int PaddingInPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, mContext.getResources().getDisplayMetrics());
        int PaddingInPixelsH = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, mContext.getResources().getDisplayMetrics());

        params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, heightInPixels);
        paramsImgView = new ViewGroup.LayoutParams(heightInPixels, heightInPixels);

        Bundle bundle = requireArguments();
        historyList = bundle.getStringArrayList("search_history_list");

        if(historyList != null){
            if(historyList.size() > 30){
                for(int i = 0; i < historyList.size() - 30; i++){
                    historyList.remove(historyList.size() - 1);
                }
            }
            for(int i = 0; i < historyList.size(); i++){
                TextView textView = new TextView(mContext);
                textView.setLayoutParams(params);
                textView.setText(historyList.get(i));
                textView.setTextSize(12);
                textView.setBackgroundResource(R.drawable.edittext_border);
                textView.setPadding(PaddingInPixels, PaddingInPixelsH, PaddingInPixels, PaddingInPixelsH);
                textView.setGravity(Gravity.CENTER);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mContext instanceof TextViewFragmentOnClickFreshEditText){
                            TextViewFragmentOnClickFreshEditText listener = (TextViewFragmentOnClickFreshEditText) mContext;
                            listener.onClickFreshEditText(textView.getText().toString());
                        }
                    }
                });
                mFlowLayout.addView(textView);
            }

            imageView = new ImageView(mContext);
            imageView.setLayoutParams(paramsImgView);
            imageView.setBackgroundResource(R.drawable.edittext_border);
            imageView.setImageResource(R.drawable.down);
            imageView.setPadding(PaddingInPixels, PaddingInPixels, PaddingInPixels, PaddingInPixels);
            mFlowLayout.addView(imageView);

        }
    }
}
