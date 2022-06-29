package com.zyh.fragment;


import static com.zyh.utills.Utils.isVPNConnected;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.baseokhttp.HttpRequest;
import com.kongzue.baseokhttp.listener.ResponseListener;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.tapadoo.alerter.Alerter;
import com.zyh.R;
import com.zyh.activities.MainActivity;
import com.zyh.beans.LoginBean;
import com.zyh.utills.QingFileUtil;
import com.zyh.utills.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class Bg2Fragment extends Fragment {

    private RecyclerView rv;
    private SmartRefreshLayout srl;
    private final HashMap<String, Object> map = new HashMap<>();

    private SharedPreferences bjt;
    private SharedPreferences bjs;

    public static Bg2Fragment newInstance() {
        return new Bg2Fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bg, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv = view.findViewById(R.id.rv);
        rv.setItemViewCacheSize(9999);
        srl = view.findViewById(R.id.srl);

        bjt = requireContext().getSharedPreferences("背景图", Activity.MODE_PRIVATE);
        bjs = requireContext().getSharedPreferences("背景色", Activity.MODE_PRIVATE);

        if (!isVPNConnected(getContext())) {
            HttpRequest.build(getContext(), "https://gitee.com/liuqing-keeping/resources/raw/master/color.json")
                    .addHeaders("Charset", "UTF-8")
                    .setResponseListener(new ResponseListener() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onResponse(String response, Exception error) {
                            srl.finishRefresh(false);
                            try {
                                HashMap<String, Object> map = new Gson().fromJson(response, new TypeToken<HashMap<String, Object>>() {
                                }.getType());
                                ArrayList<HashMap<String, Object>> listmap = new Gson().fromJson(new Gson().toJson(map.get("data")), new TypeToken<ArrayList<HashMap<String, Object>>>() {
                                }.getType());
                                TransitionManager.beginDelayedTransition(srl, new androidx.transition.AutoTransition());
                                rv.setAdapter(new Recyclerview1Adapter(listmap));
                                rv.getAdapter().notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).doGet();
        }
    }


    public class Recyclerview1Adapter extends RecyclerView.Adapter<Recyclerview1Adapter.ViewHolder> {
        ArrayList<HashMap<String, Object>> data;

        public Recyclerview1Adapter(ArrayList<HashMap<String, Object>> arr) {
            data = arr;
        }

        @Override
        public @NotNull
        ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.item_bgs, null);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (getResources().getDisplayMetrics().widthPixels - Utils.dp2px(getContext(), 20)) / 2);
            v.setLayoutParams(lp);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            View view = holder.itemView;

            final MaterialCardView cardview = view.findViewById(R.id.cardview1);

            cardview.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor(String.valueOf(data.get(position).get("color")))));
            cardview.setOnClickListener(view1 -> {
                try {
                    bjs.edit().putString("背景色", String.valueOf(data.get(position).get("color"))).apply();
                    QingFileUtil.deleteFile(QingFileUtil.getExternalStorageDir() + "/csust/.背景图.png");
                    Alerter.create((Activity) requireContext())
                            .setTitle("设置成功")
                            .setText("背景色设置成功")
                            .setBackgroundColorInt(getResources().getColor(R.color.success))
                            .show();
                    MainActivity.change = true;
//                    refresh();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View v) {
                super(v);
            }
        }

    }

}