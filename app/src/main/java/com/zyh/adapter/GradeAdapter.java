package com.zyh.adapter;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zyh.R;
import com.zyh.beans.GradeBean;
import com.zyh.beans.PscjBean;
import com.zyh.utills.Utills;

import java.util.List;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.ViewHolder> {
    private final List<GradeBean.Datas> mGrade;
    private final Activity activity;
    private final String token;
    private final String cookie;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView courseNameText;
        TextView scoreText;
        TextView xuefenText;
        TextView pointText;
        TextView methodText;
        TextView natureText;
        TextView course_name_title;
        TextView score_title;
        LinearLayout triangle_text;
        ImageView triangle;
        LinearLayout content;

        public ViewHolder(View view) {
            super(view);
            courseNameText = (TextView) view.findViewById(R.id.course_name);
            scoreText = (TextView) view.findViewById(R.id.score);
            xuefenText = (TextView) view.findViewById(R.id.xuefen);
            pointText = (TextView) view.findViewById(R.id.point);
            //methodText = (TextView)view.findViewById(R.id.method);
            natureText = (TextView) view.findViewById(R.id.nature);
            course_name_title = view.findViewById(R.id.course_name_title);
            score_title = view.findViewById(R.id.score_title);
            triangle_text = view.findViewById(R.id.triangle_text);
            triangle = view.findViewById(R.id.triangle);
            content = view.findViewById(R.id.content);
        }
    }

    public GradeAdapter(List<GradeBean.Datas> gradeList, Activity activity, String token, String cookie) {
        mGrade = gradeList;
        this.activity = activity;
        this.token = token;
        this.cookie = cookie;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.grade_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView courseNameText = holder.courseNameText;
        GradeBean.Datas grade = mGrade.get(position);
        String courseName = grade.getCourseName();
        int size = courseName.length();
//        if (size>=12){
//            courseNameText.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
//            LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) courseNameText.getLayoutParams();
//            linearParams.weight *= 0.6;
//            courseNameText.setLayoutParams(linearParams);
//        }else if(size>=9){
//            courseNameText.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
//        }
        courseNameText.setText(courseName);
        holder.scoreText.setText(grade.getScore());
        holder.xuefenText.setText(grade.getXuefen());
        holder.pointText.setText(grade.getPoint());
        //holder.methodText.setText(grade.getMethod());
        holder.natureText.setText(grade.getNature());
        holder.course_name_title.setText(courseName);
        holder.score_title.setText(grade.getScore());
        holder.triangle_text.setOnClickListener(view -> {
            if (holder.content.getVisibility() == View.GONE) {
                holder.triangle.setImageResource(R.drawable.triangle_down);
                holder.content.setVisibility(View.VISIBLE);
            } else {
                holder.triangle.setImageResource(R.drawable.triangle_right);
                holder.content.setVisibility(View.GONE);
            }
        });
        holder.score_title.setOnClickListener(v -> postPSGrade(grade.getCourseName(), grade.getPscjUrl()));
    }

    @Override
    public int getItemCount() {
        if (mGrade == null) {
            return 0;
        }
        return mGrade.size();
    }

    private void postPSGrade(final String courseName, final String pscjUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("cookie", cookie)
                            .add("pscjUrl", pscjUrl)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://finalab.cn:8989/queryPscj")
                            .post(requestBody)
                            .addHeader("token", token)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = Objects.requireNonNull(response.body()).string();
                    PscjBean pscjBean = Utills.parseJSON(responseData, PscjBean.class);
                    PscjBean.Datas pscjData = pscjBean.getData();
                    showGradeDialogOnUI(activity, courseName, pscjData);
                } catch (Exception e) {
                    Log.d("okHttpError", "okHttpError");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showGradeDialogOnUI(Activity activity, String courseName, PscjBean.Datas pscjData) {
        activity.runOnUiThread(() ->
                Utills.showGradeDialog(activity, courseName, pscjData.getPscj(), pscjData.getPscjBL(), pscjData.getQzcj(), pscjData.getQzcjBL(), pscjData.getQmcj(), pscjData.getQmcjBL()));
    }

}
