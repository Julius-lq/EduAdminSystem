package com.zyh.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zyh.R;
import com.zyh.beans.Version;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AboutActivity extends AppCompatActivity {
    private ImageView return_back;
    private TextView copy;
    private TextView verson_word;
    private Button reward;
    private Dialog dialog;
    private ImageView mImageView;

    private Toolbar toolbar;


    private SharedPreferences bjs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_about);
        return_back = findViewById(R.id.return_img);
        copy = findViewById(R.id.copy);
        verson_word = findViewById(R.id.version_word);
        verson_word.setText(Version.getVersion());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("关于我们");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        return_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager copy = (ClipboardManager) AboutActivity.this
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                copy.setText(getResources().getString(R.string.github_website));
                Toast.makeText(AboutActivity.this, "复制成功", Toast.LENGTH_SHORT).show();
            }
        });
        init();
        //小图的点击事件（弹出大图）
        reward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        bjs = this.getSharedPreferences("背景色", Activity.MODE_PRIVATE);

        Log.e("MainActivity2", "背景色为:" + bjs.getString("背景色","#5187F4"));
    }

    private void init() {
        reward = (Button) findViewById(R.id.reward);
        //大图所依附的dialog
        dialog = new Dialog(AboutActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        mImageView = getImageView();
        dialog.setContentView(mImageView);

        //大图的点击事件（点击让他消失）
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //大图的长按监听
        mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //弹出的“保存图片”的Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(AboutActivity.this);
                builder.setItems(new String[]{getResources().getString(R.string.save_picture)}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveImageToGallery(AboutActivity.this, ((BitmapDrawable) mImageView.getDrawable()).getBitmap());
                    }
                });
                builder.show();
                return true;
            }
        });

        TextView QQGroup = (TextView) findViewById(R.id.qqGroup);
        String str = getString(R.string.about_word);
        int startPosition = 69, endPosition = 77;
        //获取字符串中qq群号的字符串，便于以后的管理员修改
        for (int i = 0; i < str.length() - 1; i++) {
            final boolean digit1 = Character.isDigit(str.charAt(i));
            final boolean digit2 = Character.isDigit(str.charAt(i + 1));
            if (!digit1 && digit2) {
                startPosition = i + 1;
            }
            if (digit1 && !digit2) {
                endPosition = i + 1;
            }
        }
        //创建一个SpannableString用来存放要显示的字符串
        SpannableString spannableString = new SpannableString(str);
        //给QQ群号字符串设置点击事件
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                joinQQGroup("eJBaGRZGMiWjQdb9Nbzi3paKnZq3nu2d");
            }
        }, startPosition, endPosition, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //给QQ群号字符串设置文本颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            spannableString.setSpan(new ForegroundColorSpan(getColor(R.color.colorPrimaryDark)), startPosition, endPosition, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        //然后把spannableString放进到Text中,
        QQGroup.setText(spannableString);
        //最后设置可点击
        QQGroup.setMovementMethod(LinkMovementMethod.getInstance());
    }

    //保存文件到指定路径
    public boolean saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "dearxy";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                Toast.makeText(AboutActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //动态的ImageView
    private ImageView getImageView() {
        ImageView iv = new ImageView(this);
        //宽高
        iv.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //设置Padding
        iv.setPadding(20, 20, 20, 20);
        //imageView设置图片
        @SuppressWarnings("ResourceType")
        InputStream is = getResources().openRawResource(R.drawable.reward);
        Drawable drawable = BitmapDrawable.createFromStream(is, null);
        iv.setImageDrawable(drawable);
        return iv;
    }

    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
//            Toast.makeText(this, "返回", Toast.LENGTH_LONG).show();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
