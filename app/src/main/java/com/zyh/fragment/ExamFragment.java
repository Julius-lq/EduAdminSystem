package com.zyh.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuexiang.xui.widget.picker.widget.OptionsPickerView;
import com.xuexiang.xui.widget.picker.widget.builder.OptionsPickerBuilder;
import com.xuexiang.xui.widget.picker.widget.listener.OnOptionsSelectListener;
import com.zyh.R;
import com.zyh.activities.MainActivity;
import com.zyh.beans.ExamBean;
import com.zyh.beans.LoginBean;
import com.zyh.adapter.ExamAdapter;
import com.zyh.utills.Utills;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ExamFragment extends Fragment {
    private LoginBean loginBean;
    private LinearLayout selectExamLinear;
    private TextView selectExam;
    private RecyclerView recyclerView;
    private LinearLayout noGrade;
    private int examSelectOption = 0;
    public String semester;
    private String[] datas;
    private List<ExamBean.Exam> examList;
    private MainActivity mainActivity;
    private Boolean isFinished;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exam, container, false);
        isFinished = false;
        recyclerView = view.findViewById(R.id.exam_recycler_view);
        noGrade = view.findViewById(R.id.no_exam);
        selectExamLinear = view.findViewById(R.id.select_exam_linear);
        selectExam = view.findViewById(R.id.select_exam);
        mainActivity = (MainActivity)getActivity();
        waitingAndSet();
        return view;
    }
    private void waitingAndSet(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    datas = mainActivity.semesters;
                    if (!(datas==null||datas.length==0)) {
                        Log.d("ExamFragment","ActionBegin: getting datas...");
                        break;
                    }
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        datas = mainActivity.semesters;
                        loginBean = mainActivity.loginBean;
                        semester = loginBean.getData().getNowXueqi();
                        selectExamLinear.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showPickerView();
                            }
                        });
                        for(int i=0;i<datas.length;i++){
                            if (datas[i].equals(semester)){
                                showExam(i);
                            }
                        }
                    }
                });
            }
        }).start();
    }
    private void showPickerView() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(mainActivity, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                showExam(options1);
            }
        })
                .setTitleText("选择学期")
                .setSelectOptions(examSelectOption)
                .build();
        pvOptions.setPicker(datas);
        pvOptions.show();
    }
    private void showExam(int options){
        selectExam.setText(datas[options]);
        examSelectOption = options;
        String semester = datas[options];
        new Thread(new Runnable() {
            @Override
            public void run() {
                postExam(semester);
                while(isFinished.equals(false)){
                    Log.d("ExamFragment","notFinished");
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (examList==null){
                            recyclerView.setVisibility(View.INVISIBLE);
                            noGrade.setVisibility(View.VISIBLE);
                            isFinished = false;
                        }else {
                            recyclerView.setVisibility(View.VISIBLE);
                            noGrade.setVisibility(View.INVISIBLE);
                            showExamRecyclerView();
                        }
                    }
                });
            }
        }).start();
    }
    private void postExam(final String semester) {
        final String cookie = loginBean.getData().getCookie();
        final String token = loginBean.getData().getToken();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("cookie",cookie)
                            .add("xueqi",semester)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://finalab.cn:8989/getKsap")
                            .post(requestBody)
                            .addHeader("token",token)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    /*
                    responseData = "{\"code\":200,\"msg\":\"请求成功\",\"data\":[{\"campus\":\"云塘" +
                            "校区\",\"courseName\":\"计算机组成原理\",\"teacher\":\"曾道建\",\"" +
                            "startTime\":\"2019-12-10 14:20\",\"endTime\":\"2019-12-10 16:20\",\"" +
                            "address\":\"云综教A-201\",\"seatNumber\":\"\",\"ticketNumber\":\"\"}," +
                            "{\"campus\":\"云塘校区\",\"courseName\":\"程序设计、算法与数据结构（一）实验\",\"teacher\":\"" +
                            "颜宏文\",\"startTime\":\"2019-12-23 09:20\",\"endTime\":\"" +
                            "2019-12-23 11:20\",\"address\":\"云综教A-104\",\"seatNumber\":\"\",\"" +
                            "ticketNumber\":\"\"},{\"campus\":\"云塘校区\",\"courseName\":\"UML建模\"," +
                            "\"teacher\":\"黄园媛\",\"startTime\":\"2019-12-25 14:20\",\"endTime\":" +
                            "\"2019-12-25 16:20\",\"address\":\"云综教C-305\",\"seatNumber\":\"\"," +
                            "\"ticketNumber\":\"\"},{\"campus\":\"云塘校区\",\"courseName\":\"计算机图形学\"," +
                            "\"teacher\":\"桂彦\",\"startTime\":\"2019-12-26 09:20\",\"endTime\":\"2019-12-26 11:20\"," +
                            "\"address\":\"云综教B-406\",\"seatNumber\":\"\",\"ticketNumber\":\"\"}]}";

                     */

                    ExamBean examBean = Utills.parseJSON(responseData,ExamBean.class);
                    examList = examBean.getData();
                    isFinished = true;
                }catch (Exception e) {
                    Log.d("okHttpError","okHttpError");
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void showExamRecyclerView(){
        RecyclerView recyclerView = (RecyclerView)getActivity().findViewById(R.id.exam_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mainActivity);
        recyclerView.setLayoutManager(layoutManager);
        ExamAdapter adapter = new ExamAdapter(examList);
        recyclerView.setAdapter(adapter);
        isFinished = false;
    }
}
