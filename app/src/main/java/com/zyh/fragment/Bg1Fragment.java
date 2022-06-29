package com.zyh.fragment;

//import static com.zyh.utills.Utils.LoadingDialog;

import static com.zyh.utills.Utils.SaveImage;
import static com.zyh.utills.Utils.isVPNConnected;
//import static com.zyh.utills.Utils.loadDialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.baseokhttp.HttpRequest;
import com.kongzue.baseokhttp.listener.ResponseListener;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.tapadoo.alerter.Alerter;
import com.zyh.R;
import com.zyh.activities.MainActivity;
import com.zyh.utills.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Bg1Fragment extends Fragment {

    private RecyclerView recyclerView;
    private SmartRefreshLayout refreshLayout;
    private HashMap<String, Object> map = new HashMap<>();

    //sp存储分别保存背景图片和背景颜色
    private SharedPreferences back_pic;
    private SharedPreferences back_color;

    public static Bg1Fragment newInstance() {
        return new Bg1Fragment();
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

        recyclerView = view.findViewById(R.id.rv);
        recyclerView.setItemViewCacheSize(9999);
        refreshLayout = view.findViewById(R.id.srl);

//        back_pic = requireContext().getSharedPreferences("背景图", Activity.MODE_PRIVATE);
        back_pic = requireContext().getSharedPreferences("背景图", Context.MODE_MULTI_PROCESS);
        back_color = requireContext().getSharedPreferences("背景色", Activity.MODE_PRIVATE);

        if (!isVPNConnected(getContext())) {
            HttpRequest.build(getContext(), "https://gitee.com/liuqing-keeping/resources/raw/master/list.json")
                    .addHeaders("Charset", "UTF-8")
                    .setResponseListener(new ResponseListener() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onResponse(String response, Exception error) {
                            refreshLayout.finishRefresh(false);
                            try {
                                HashMap<String, Object> map = new Gson().fromJson(response, new TypeToken<HashMap<String, Object>>() {
                                }.getType());
                                ArrayList<HashMap<String, Object>> listmap = new Gson().fromJson(new Gson().toJson(map.get("data")), new TypeToken<ArrayList<HashMap<String, Object>>>() {
                                }.getType());
                                TransitionManager.beginDelayedTransition(refreshLayout, new androidx.transition.AutoTransition());
                                recyclerView.setAdapter(new Recyclerview1Adapter(listmap));
                                Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
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
            @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.item_bgt, null);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (getResources().getDisplayMetrics().widthPixels - Utils.dp2px(getContext(), 20)) / 2);
            v.setLayoutParams(lp);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            View view = holder.itemView;

            final MaterialCardView cardview = (MaterialCardView) view.findViewById(R.id.cardview1);
            final TextView txt1 = (TextView) view.findViewById(R.id.txt1);
            final ImageView tp1 = (ImageView) view.findViewById(R.id.tp1);

            txt1.setText((CharSequence) data.get(position).get("title"));
            Glide.with(requireContext()).load(data.get(position).get("url")).thumbnail(0.1f).fitCenter().priority(Priority.IMMEDIATE).into(tp1);

            cardview.setOnClickListener(_view1 -> {
                try {
//                    LoadingDialog(requireContext());
                    back_color.edit().putString("背景色", String.valueOf(data.get(position).get("color"))).apply();
                    Glide.with(requireContext())
                            .asBitmap()
                            .load(data.get((int) position).get("url").toString())
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                                    new Thread((Runnable) () -> {
                                        @SuppressLint("SimpleDateFormat")
                                        String savedFile = SaveImage(requireContext(), bitmap, "/csust/", ".背景图.png");
                                        if (savedFile != null) {
                                            MediaScannerConnection.scanFile((Activity) requireContext(), new String[]{savedFile}, null, (str, uri) -> {
                                                Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                                                intent.setData(uri);
                                                ((Activity) requireContext()).sendBroadcast(intent);
//                                                loadDialog.dismiss();
                                                Alerter.create((Activity) requireContext())
                                                        .setTitle("设置成功")
                                                        .setText("背景设置成功")
                                                        .setBackgroundColorInt(getResources().getColor(R.color.success))
                                                        .show();
                                                MainActivity.change = true;
                                            });
                                        } else {
//                                            loadDialog.dismiss();
                                        }
                                    }).start();
                                }
                            });
                } catch (Exception e) {
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