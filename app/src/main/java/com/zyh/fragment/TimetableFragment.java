package com.zyh.fragment;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuexiang.xui.widget.picker.widget.OptionsPickerView;
import com.xuexiang.xui.widget.picker.widget.builder.OptionsPickerBuilder;
import com.xuexiang.xui.widget.picker.widget.listener.OnOptionsSelectListener;
import com.zyh.activities.MainActivity;
import com.zyh.beans.Account;
import com.zyh.beans.CourseBean;
import com.zyh.beans.LoginBean;
import com.zyh.utills.Utills;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TimetableFragment extends Fragment {
    public static boolean isThisSemester = false;
    public static int thisWeek;
    public LoginBean loginBean;
    private LinearLayout selectTimetableLinear;
    private TextView selectTimetable;
    private int timetableSelectOption = 0;
    private String[] datas;
    public static String semester;
    public String nowWeek;
    public String selectedWeek;
    public String originalSemester;
    public Activity thisMainActivity;
    private MainActivity mainActivity;
    public String week = "ssss";
    public String[] context = {"abc", "def", "ghi", "klm"};
    public int getTimetableNum = 0;
    public List<String> timetableList = Arrays.asList(null, null, null, null, null, null, null, null, null
            , null, null, null, null, null, null, null, null, null, null, null, null);
    //    ("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"
//            , "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21")
    public List<List<List<CourseBean.Course>>> courseLists = Arrays.asList(null, null, null, null, null, null, null, null, null   //21???
            , null, null, null, null, null, null, null, null, null, null, null, null);
    private TextView weekText;
    private TextView isNowWeek;
    //??????ViewPager
    private ViewPager mViewPager;
    //?????????
    private FragmentPagerAdapter mAdapter;
    //??????Fragment?????????
    private List<Fragment> mFragments;
    public Boolean[] isFinished = {false, false, false, false, false, false, false, false, false, false, false
            , false, false, false, false, false, false, false, false, false, false};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.id_viewpager1);
        weekText = (TextView) view.findViewById(R.id.text_week);
        isNowWeek = (TextView) view.findViewById(R.id.text_isnowweek);

        selectTimetableLinear = view.findViewById(R.id.select_timetable_linear);
        selectTimetable = view.findViewById(R.id.select_timetable);
        mainActivity = (MainActivity) getActivity();
        thisMainActivity = mainActivity;
        waitingAndSet();
        Log.d("TimetableFragment", "ActionBegin");
        return view;
    }

    private void waitingAndSet() {
        loginBean = mainActivity.loginBean;
        Log.d("nowWeek", loginBean.getData().getNowWeek());
        semester = loginBean.getData().getNowXueqi();

        originalSemester = semester;
        nowWeek = loginBean.getData().getNowWeek();
        thisWeek = Integer.valueOf(nowWeek);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    datas = mainActivity.semesters;
                    if (!(datas == null || datas.length == 0)) {
                        Log.d("GradeFragment", "ActionBegin: getting datas...");
                        break;
                    }
                }
                if (datas == null) {
                    Log.d("GradeFragment", "ActionBegin: datas equals null!!!");
                }
                Log.d("GradeFragment", datas.length + " 0.0");
                updateLoginDB();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //initDatas();//???????????????
                        selectTimetableLinear.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showPickerView();
                            }
                        });
                        for (int i = 0; i < datas.length; i++) {
                            if (datas[i].equals(semester)) {
                                showTimetable(i);
                            }
                        }
                    }
                });
            }
        }).start();
    }

    private void updateLoginDB() {
        //?????????????????????????????????????????????????????????
        SimpleDateFormat sdf = new SimpleDateFormat();// ???????????????
        sdf.applyPattern("yyyy-MM-dd");// a???am/pm?????????
        Date date = new Date();// ??????????????????
        String username = ((MainActivity) getActivity()).username;
        Account account = new Account();
        account.setSemester(semester);
        account.setWeek(nowWeek);
        account.setTime(sdf.format(date));
        account.updateAll("username = ?", username);
    }

    private void showPickerView() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(mainActivity, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                showTimetable(options1);
            }
        })
                .setTitleText("????????????")
                .setSelectOptions(timetableSelectOption)
                .build();
        pvOptions.setPicker(datas);
        pvOptions.show();
    }

    private void showTimetable(int options) {
        selectTimetable.setText(datas[options]);
        timetableSelectOption = options;
        Utills.clear();
        semester = datas[options];
        weekText.setText("1");
        for (int i = 0; i < isFinished.length; i++) {
            isFinished[i] = false;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Utills.postAllTimetable(loginBean, TimetableFragment.this, semester, 1);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initDatas();
                        //mAdapter.notifyDataSetChanged();
                        Utills.showIsNowWeek(datas[timetableSelectOption], originalSemester, isNowWeek, selectedWeek, nowWeek);
                    }
                });
            }
        }).start();
    }

    private void initDatas() {
        mFragments = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            mFragments.add(TimetableFragmentItem.newInstance(i));
        }
        //??????????????????
        mAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            private int mChildCount = 0;

            @Override
            public Fragment getItem(int position) {//?????????????????????????????????Fragment
                week = String.valueOf(position);
                return mFragments.get(position);
            }

            @Override
            public int getCount() {//???????????????Fragment?????????
                return mFragments.size();
            }

            @Override
            public int getItemPosition(@NonNull Object object) {
                if (mChildCount > 0) {
                    mChildCount--;
                    return POSITION_NONE;
                }
                return super.getItemPosition(object);
            }

            @Override
            public void notifyDataSetChanged() {
                mChildCount = getCount();
                super.notifyDataSetChanged();
            }
        };
        if (mViewPager == null) {
            Log.d("null", "mViewPager==null");
        }
        //??????????????????ViewPager????????????
        mViewPager.setAdapter(mAdapter);
        //??????????????????
        selectedWeek = "1";
        if (!nowWeek.equals("-1") && semester.equals(originalSemester)) {
            selectedWeek = nowWeek;
            weekText.setText(String.valueOf(Integer.parseInt(nowWeek)));
            mViewPager.setCurrentItem(Integer.parseInt(nowWeek) - 1);
        }
        //??????ViewPager???????????????
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            //??????????????????
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //??????????????????
            @Override
            public void onPageSelected(int position) {
                int thisWeek = position + 1;
                selectedWeek = thisWeek + "";
                weekText.setText(String.valueOf(thisWeek));
                Utills.showIsNowWeek(datas[timetableSelectOption], originalSemester, isNowWeek, selectedWeek, nowWeek);
                mViewPager.setCurrentItem(position);
            }

            @Override
            //??????????????????????????????
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onResume() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
        super.onResume();
    }
}