package com.zyh.activities;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.R.attr;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gyf.immersionbar.ImmersionBar;
import com.xuexiang.xui.XUI;
import com.zyh.R;
import com.zyh.beans.CourseList;
import com.zyh.beans.LoginBean;
import com.zyh.beans.SemesterBean;
import com.zyh.fragment.ExamFragment;
import com.zyh.fragment.GradeFragment;
import com.zyh.fragment.IndividualFragment;
import com.zyh.fragment.TimetableFragment;
import com.zyh.utills.Utills;
import com.zyh.utills.WebSocketUtils;

import org.litepal.LitePal;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private long exitTime = 0;
    private String token;
    private TextView topNmae;

    //默认为false，主题一旦改变则设置为true
    public static boolean change = false;

    //声明四个Tab的布局文件
    private LinearLayout mTabTimetable;
    private LinearLayout mTabGrade;
    private LinearLayout mTabExam;
    private LinearLayout mTabIndividual;

    //声明四个Tab的ImageButton
    private ImageButton mImgTimetable;
    private ImageButton mImgGrade;
    private ImageButton mImgExam;
    private ImageButton mImgIndividual;

    //声明四个Tab的TextView
    private TextView mTextTimetable;
    private TextView mTextGrade;
    private TextView mTextExam;
    private TextView mTextIndividual;

    //声明四个Tab分别对应的Fragment
    private Fragment mFragTimetable;
    private Fragment mFragGrade;
    private Fragment mFragExam;
    private Fragment mFragIndividual;

    private BottomNavigationView navigationView;

    private LinearLayout addFeedback;
    private RelativeLayout notice;
    private ImageView pot;
    private AppCompatImageView renovate;

    public LoginBean loginBean;
    public String username;
    public String[] semesters;

    private WebSocketUtils websocket;


    private SharedPreferences qdy;
    private SharedPreferences bjt;
    private SharedPreferences bjs;

    private RelativeLayout topbar;

    private boolean isPause;//解决点击更换主题进入到一个Activity后回退后的主题刷新问题

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        XUI.initTheme(this);
        //取消welcomActivity活动的多余通知
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.cancel(10);

        Intent intent = getIntent();
        loginBean = (LoginBean) intent.getSerializableExtra("loginBean");
        username = intent.getStringExtra("username");
        Log.d("MainActivity", "ActionBegin:Already get loginBean");
        token = loginBean.getData().getToken();
        postSemester(token);
        initViews();//初始化控件
        initEvents();//初始化事件
        selectTab(0);
        addFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FeedbackActivity.actionStart(MainActivity.this, token);
                Intent intent = new Intent(MainActivity.this, FeedbackActivity2.class);
                startActivity(intent);
            }
        });
        notice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NoticeActivity.class);
                startActivity(intent);
            }
        });
//        final GuideCaseView guideStep1 = new GuideCaseView.Builder(MainActivity.this)
//                .title("左右滑动切换周次")
//                .build();
//        new GuideCaseQueue()
//                .add(guideStep1)
//                .show();
        websocket = new WebSocketUtils(MainActivity.this, token, pot);


        qdy = this.getSharedPreferences("启动页", Activity.MODE_PRIVATE);
        bjt = this.getSharedPreferences("背景图", Activity.MODE_PRIVATE);
        bjs = this.getSharedPreferences("背景色", Activity.MODE_PRIVATE);

        Log.e("MainActivity2", "背景图为:" + bjt.toString());
        Log.e("MainActivity2", "背景色为:" + bjs.getString("背景色", "#5187F4"));

        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(bjs.getString("背景色", "#5187F4"))
                .navigationBarColor(R.color.backgroundColor)
                .autoDarkModeEnable(true)
                //.keyboardEnable(true)
                //.keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
                .init();
        topbar.setBackgroundColor(Color.parseColor(bjs.getString("背景色", "#5187F4")));

//        int[] colors = new int[]{-256, -16776961, -1, -16776961, -65536, -1};
        int color = Color.parseColor(bjs.getString("背景色", "#5187F4"));
//        (int normal, int pressed, int focused, int unable)
        int color2 = R.color.black;
//        Toast.makeText(this, "color的值是" + color, Toast.LENGTH_SHORT).show();
        int[] colors = new int[]{color, color2, color2, color2, color, color2};
//                             pressed focused normal   focused  unable normal

        int[][] states = new int[6][];
//        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[0] = new int[]{attr.state_selected, android.R.attr.state_enabled};
        states[1] = new int[]{attr.state_enabled, android.R.attr.state_focused};
        states[2] = new int[]{android.R.attr.state_enabled};
        states[3] = new int[]{android.R.attr.state_focused};
        states[4] = new int[]{android.R.attr.state_selected};
        states[5] = new int[]{};

        ColorStateList csl = new ColorStateList(states, colors);
        navigationView.setItemTextColor(csl);
        navigationView.setItemIconTintList(csl);
//        System.out.println(navigationView.getSelectedItemId());


    }


    public static void actionStart(Context context, LoginBean loginBean, String username) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("loginBean", loginBean);
        intent.putExtra("username", username);
        context.startActivity(intent);
        ((AppCompatActivity) context).finish();
    }

    private FragmentTransaction getTrasaction() {
        //获取FragmentManager对象
        FragmentManager manager = getSupportFragmentManager();
        //获取FragmentTransaction对象
        FragmentTransaction transaction = manager.beginTransaction();
        //先隐藏所有的Fragment
        hideFragments(transaction);

        return transaction;
    }

    @SuppressLint("NonConstantResourceId")
    private void initEvents() {
        navigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.menu_timetable:
                    selectTab(0);
                    break;
                case R.id.menu_grade:
                    selectTab(1);
                    break;
                case R.id.menu_exam:
                    selectTab(2);
                    break;
                case R.id.menu_individual:
                    selectTab(3);
                    break;
            }
            return true;
        });

        renovate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String semester = TimetableFragment.semester;
                LitePal.deleteAll(CourseList.class, "semester = ? and username = ?", semester, username);
                CourseList couList = LitePal.where("semester = ? and username = ?", semester, username).findFirst(CourseList.class);
                if (couList == null) {
                    Toast.makeText(MainActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "刷新失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initViews() {
        topNmae = (TextView) findViewById(R.id.top_name);
        addFeedback = (LinearLayout) findViewById(R.id.addFeedback);
        notice = findViewById(R.id.notice);
        pot = findViewById(R.id.notice_pot);
        renovate = (AppCompatImageView) findViewById(R.id.renovate);
        topbar = (RelativeLayout) findViewById(R.id.topbar);
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
    }

    private void selectTab(int i) {
        //获取FragmentManager对象
        FragmentManager manager = getSupportFragmentManager();
        //获取FragmentTransaction对象
        FragmentTransaction transaction = manager.beginTransaction();
        //先隐藏所有的Fragment
        hideFragments(transaction);
        switch (i) {
            //当选中点击的是微信的Tab时
            case 0:
                topNmae.setText("课程表");
                addFeedback.setVisibility(View.INVISIBLE);
                notice.setVisibility(View.VISIBLE);
                renovate.setVisibility(View.VISIBLE);
                //如果微信对应的Fragment没有实例化，则进行实例化，并显示出来
                if (mFragTimetable == null) {
                    mFragTimetable = new TimetableFragment();
                    transaction.add(R.id.id_content, mFragTimetable);
                } else {
                    //如果微信对应的Fragment已经实例化，则直接显示出来
                    transaction.show(mFragTimetable);
                }
                break;
            case 1:
                topNmae.setText("成绩");
                addFeedback.setVisibility(View.INVISIBLE);
                notice.setVisibility(View.INVISIBLE);
                renovate.setVisibility(View.INVISIBLE);
                if (mFragGrade == null) {
                    mFragGrade = new GradeFragment();
                    transaction.add(R.id.id_content, mFragGrade);
                } else {
                    transaction.show(mFragGrade);
                }
                break;
            case 2:
                topNmae.setText("考试");
                addFeedback.setVisibility(View.INVISIBLE);
                notice.setVisibility(View.INVISIBLE);
                renovate.setVisibility(View.INVISIBLE);
                if (mFragExam == null) {
                    mFragExam = new ExamFragment();
                    transaction.add(R.id.id_content, mFragExam);
                } else {
                    transaction.show(mFragExam);
                }
                break;
            case 3:
                topNmae.setText("我的");
                addFeedback.setVisibility(View.VISIBLE);
                notice.setVisibility(View.INVISIBLE);
                renovate.setVisibility(View.INVISIBLE);
                if (mFragIndividual == null) {
                    mFragIndividual = new IndividualFragment();
                    transaction.add(R.id.id_content, mFragIndividual);
                } else {
                    transaction.show(mFragIndividual);
                }
                break;
        }
        //不要忘记提交事务
        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (mFragTimetable != null) {
            transaction.hide(mFragTimetable);
        }
        if (mFragGrade != null) {
            transaction.hide(mFragGrade);
        }
        if (mFragExam != null) {
            transaction.hide(mFragExam);
        }
        if (mFragIndividual != null) {
            transaction.hide(mFragIndividual);
        }
    }

    private void postSemester(final String token) {
        Log.d("MainActivity", "ActionBegin:1Haven't get semesters");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://finalab.cn:8989/getAllSemester")
                            .addHeader("token", token)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    SemesterBean semesterBean = Utills.parseJSON(responseData, SemesterBean.class);
                    semesters = semesterBean.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }


    //跳转到其他页面时，记录isPause
    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
    }

    //重新返回本Activity时，刷新页面
    @Override
    protected void onResume() {
        WebSocketUtils.hasUnReadMessage(MainActivity.this, pot);
        WebSocketUtils.getUnReadMessage(token);
        super.onResume();

        if (isPause) {
//            actionStart(this, loginBean, username);
//            refresh(loginBean, username);
//            isPause = false;
        }
        if (change){
            refresh(loginBean, username);
            change = false;
        }
    }

    @Override
    protected void onDestroy() {
        websocket.close();
        super.onDestroy();
    }

    private void refresh(LoginBean loginBean, String username) {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.putExtra("loginBean", loginBean);
        intent.putExtra("username", username);
//        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
//        Transition enter = getWindow().getEnterTransition();
//        Transition exit = getWindow().getExitTransition();
//        getWindow().setEnterTransition(exit);
//        getWindow().setExitTransition(enter);
//        getWindow().setExitTransition(new Fade());
//        getWindow().setEnterTransition(new Fade());
    }

}

