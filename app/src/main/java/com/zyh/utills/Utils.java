package com.zyh.utills;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.PictureDrawable;
import android.graphics.drawable.RippleDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.google.android.material.button.MaterialButton;
//import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.kongzue.baseokhttp.HttpRequest;
import com.kongzue.baseokhttp.listener.ResponseListener;
import com.tapadoo.alerter.Alerter;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.io.FileDescriptor;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Utils {


    /*
    public static void upData_home(Context context) {
        HttpRequest.build(context, "https://gitee.com/x1602965165/DaiMeng/raw/master/config.json")
                .addHeaders("Charset", "UTF-8")
                .setResponseListener(new ResponseListener() {
                    @Override
                    public void onResponse(String response, Exception error) {
                        try {
                            HashMap<String, Object> map = new Gson().fromJson(response, new TypeToken<HashMap<String, Object>>() {
                            }.getType());
                            if (!String.valueOf(map.get("官方验证")).equals("关闭")) {
                                if (!new SignCheck(context, "EA:07:11:21:D8:E5:ED:F4:80:62:21:8F:65:00:A8:AB:71:0E:F2:15").check()) {
                                    final AlertDialog mDialog = new MaterialAlertDialogBuilder(context)
                                            .setPositiveButton("下载", null)
                                            .setNegativeButton("退出", null)
                                            .create();
                                    mDialog.setTitle("警告");
                                    mDialog.setMessage("此软件非官方正版App，请前往官方渠道下载正版App，感谢你的支持！");
                                    mDialog.setCancelable(false);
                                    mDialog.setOnShowListener(dialog -> {
                                        Button positiveButton = mDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                        Button negativeButton = mDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                                        positiveButton.setOnClickListener(v -> {
                                            try {
                                                Uri uri = Uri.parse(map.get("蓝奏云地址").toString());
                                                Intent intent1 = new Intent();
                                                intent1.setAction("android.intent.action.VIEW");
                                                intent1.setData(uri);
                                                context.startActivity(intent1);
                                            } catch (Exception e) {
                                            }
                                        });
                                        negativeButton.setOnClickListener(v -> ((Activity) context).finishAffinity());
                                    });
                                    mDialog.show();
                                    WindowManager.LayoutParams layoutParams = mDialog.getWindow().getAttributes();
                                    layoutParams.width = context.getResources().getDisplayMetrics().widthPixels / 10 * 9;
                                    mDialog.getWindow().setAttributes(layoutParams);
                                }
                            }
                            if (!String.valueOf(map.get("弹窗广告标题")).equals("")) {
                                final AlertDialog mDialog = new MaterialAlertDialogBuilder(context)
                                        .create();
                                mDialog.setTitle(String.valueOf(map.get("弹窗广告标题")));
                                mDialog.setMessage(Html.fromHtml(String.valueOf(map.get("弹窗广告内容"))));
                                View contentView = View.inflate(context, R.layout.dialog_advertise, null);
                                mDialog.setView(contentView);
                                MaterialButton b1 = contentView.findViewById(R.id.button1);
                                MaterialButton b2 = contentView.findViewById(R.id.button2);

                                b1.setText("不了不了");
                                b1.setBackgroundColor(context.getResources().getColor(R.color.itemBackColor));
                                b2.setText("朕去看看");
                                b2.setBackgroundColor(context.getResources().getColor(R.color.zts));
                                b1.setOnClickListener(v1 -> {
                                    mDialog.dismiss();
                                });
                                Uri uri = Uri.parse(String.valueOf(map.get("弹窗广告地址")));
                                b2.setOnClickListener(v1 -> {
                                    mDialog.dismiss();
                                    Intent intent = new Intent();
                                    intent.setAction("android.intent.action.VIEW");
                                    intent.setData(uri);
                                    context.startActivity(intent);
                                });
                                mDialog.show();
                                WindowManager.LayoutParams layoutParams = mDialog.getWindow().getAttributes();
                                layoutParams.width = context.getResources().getDisplayMetrics().widthPixels / 10 * 9;
                                mDialog.getWindow().setAttributes(layoutParams);
                            }
                            if (!String.valueOf(map.get("最新版本")).equals("1.2.0")) {
                                final AlertDialog mDialog = new MaterialAlertDialogBuilder(context)
                                        .create();
                                View contentView = View.inflate(context, R.layout.dialog_update, null);
                                mDialog.setTitle((CharSequence) map.get("更新标题"));
                                mDialog.setMessage(Html.fromHtml(String.valueOf(map.get("更新内容"))));
                                mDialog.setView(contentView);
                                if (String.valueOf(map.get("强制更新")).equals("开启")) {
                                    mDialog.setCancelable(false);
                                } else {
                                    mDialog.setCancelable(true);
                                }
                                mDialog.show();
                                final MaterialButton button1 = contentView.findViewById(R.id.button1);
                                final MaterialButton button2 = contentView.findViewById(R.id.button2);
                                final ProgressBar progressBar = contentView.findViewById(R.id.jdt);
                                button1.setText("蓝奏云");
                                button1.setBackgroundColor(context.getResources().getColor(R.color.itemBackColor));
                                button2.setText("更新");
                                button2.setBackgroundColor(context.getResources().getColor(R.color.zts));
                                button1.setOnClickListener(v11 -> {
                                    try {
                                        Uri uri = Uri.parse(map.get("蓝奏云地址").toString());
                                        Intent intent1 = new Intent();
                                        intent1.setAction("android.intent.action.VIEW");
                                        intent1.setData(uri);
                                        context.startActivity(intent1);
                                    } catch (Exception e) {
                                    }
                                });
                                button2.setOnClickListener(v11 -> {
                                    if (!XXPermissions.isGrantedPermission(context, Permission.MANAGE_EXTERNAL_STORAGE)) {
                                        final AlertDialog mDialog1 = new MaterialAlertDialogBuilder(context)
                                                .setPositiveButton("申请", null)
                                                .setNegativeButton("拒绝", null)
                                                .create();
                                        mDialog1.setTitle("申请权限");
                                        mDialog1.setMessage(Html.fromHtml("储存权限"));
                                        mDialog1.setOnShowListener(dialog -> {
                                            Button positiveButton1 = mDialog1.getButton(AlertDialog.BUTTON_POSITIVE);
                                            Button negativeButton1 = mDialog1.getButton(AlertDialog.BUTTON_NEGATIVE);
                                            positiveButton1.setOnClickListener(v -> {
                                                mDialog1.dismiss();
                                                XXPermissions.with(context)
                                                        .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                                                        .request(new OnPermissionCallback() {
                                                            @Override
                                                            public void onGranted(List<String> permissions, boolean all) {
                                                                if (all) {
                                                                    //toast("获取权限成功");
                                                                    if (button2.getText().toString().equals("更新")) {
                                                                        button2.setText("请稍等");
                                                                        progressBar.setVisibility(View.VISIBLE);
                                                                        int downloadIdOne = PRDownloader.download(map.get("更新地址").toString(), QingFileUtil.getExternalStorageDir().concat("/简助手/"), "简助手.apk")
                                                                                .build()
                                                                                .setOnStartOrResumeListener(() -> {
                                                                                })
                                                                                .setOnPauseListener(() -> {
                                                                                })
                                                                                .setOnCancelListener(() -> {
                                                                                })
                                                                                .setOnProgressListener(progress -> {
                                                                                    long progressPercent = progress.currentBytes * 100 / progress.totalBytes;
                                                                                    button2.setText("下载中");
                                                                                    progressBar.setIndeterminate(false);
                                                                                    progressBar.setProgress((int) progressPercent);
                                                                                })
                                                                                .start(new OnDownloadListener() {
                                                                                    @Override
                                                                                    public void onDownloadComplete() {
                                                                                        button2.setText("安装");
                                                                                        progressBar.setVisibility(View.GONE);
                                                                                        try {
                                                                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                                                                            if (Build.VERSION.SDK_INT >= 24) {
                                                                                                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                                                                Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", new File(QingFileUtil.getExternalStorageDir().concat("/简助手/简助手.apk")));
                                                                                                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                                                                                            } else {
                                                                                                intent.setDataAndType(Uri.fromFile(new File(QingFileUtil.getExternalStorageDir().concat("/简助手/简助手.apk"))), "application/vnd.android.package-archive");
                                                                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                                            }
                                                                                            //打开安装应用界面
                                                                                            context.startActivity(intent);
                                                                                        } catch (Exception e) {
                                                                                        }
                                                                                    }

                                                                                    @Override
                                                                                    public void onError(Error error) {
                                                                                    }
                                                                                });

                                                                    }
                                                                    if (button2.getText().toString().equals("安装")) {
                                                                        //try {
                                                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                                                        if (Build.VERSION.SDK_INT >= 24) {
                                                                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                                            Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", new File(QingFileUtil.getExternalStorageDir().concat("/简助手/简助手.apk")));
                                                                            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                                                                        } else {
                                                                            intent.setDataAndType(Uri.fromFile(new File(QingFileUtil.getExternalStorageDir().concat("/简助手/简助手.apk"))), "application/vnd.android.package-archive");
                                                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                        }
                                                                        //打开安装应用界面
                                                                        context.startActivity(intent);
                                                                        //} catch(Exception e) {
                                                                        //}
                                                                    }
                                                                }
                                                            }

                                                            @Override
                                                            public void onDenied(List<String> permissions, boolean never) {
                                                                if (never) {
                                                                    //toast("被永久拒绝授权，请手动授予权限");
                                                                    // 如果是被永久拒绝就跳转到应用权限系统设置页面
                                                                    XXPermissions.startPermissionActivity(context, permissions);
                                                                } else {
                                                                    //toast("获取权限失败");
                                                                }
                                                            }
                                                        });
                                            });
                                            negativeButton1.setOnClickListener(v -> {
                                                mDialog1.dismiss();
                                            });
                                        });
                                        mDialog1.show();
                                        WindowManager.LayoutParams layoutParams1 = mDialog1.getWindow().getAttributes();
                                        layoutParams1.width = context.getResources().getDisplayMetrics().widthPixels / 10 * 9;
                                        mDialog1.getWindow().setAttributes(layoutParams1);
                                    } else {
                                        if (button2.getText().toString().equals("更新")) {
                                            button2.setText("请稍等");
                                            progressBar.setVisibility(View.VISIBLE);
                                            int downloadIdOne = PRDownloader.download(map.get("更新地址").toString(), QingFileUtil.getExternalStorageDir().concat("/简助手/"), "简助手.apk")
                                                    .build()
                                                    .setOnStartOrResumeListener(() -> {
                                                    })
                                                    .setOnPauseListener(() -> {
                                                    })
                                                    .setOnCancelListener(() -> {
                                                    })
                                                    .setOnProgressListener(progress -> {
                                                        long progressPercent = progress.currentBytes * 100 / progress.totalBytes;
                                                        button2.setText("下载中");
                                                        progressBar.setIndeterminate(false);
                                                        progressBar.setProgress((int) progressPercent);
                                                    })
                                                    .start(new OnDownloadListener() {
                                                        @Override
                                                        public void onDownloadComplete() {
                                                            button2.setText("安装");
                                                            progressBar.setVisibility(View.GONE);
                                                            try {
                                                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                                                if (Build.VERSION.SDK_INT >= 24) {
                                                                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                                    Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", new File(QingFileUtil.getExternalStorageDir().concat("/简助手/简助手.apk")));
                                                                    intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                                                                } else {
                                                                    intent.setDataAndType(Uri.fromFile(new File(QingFileUtil.getExternalStorageDir().concat("/简助手/简助手.apk"))), "application/vnd.android.package-archive");
                                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                }
                                                                //打开安装应用界面
                                                                context.startActivity(intent);
                                                            } catch (Exception e) {
                                                            }
                                                        }

                                                        @Override
                                                        public void onError(Error error) {
                                                        }
                                                    });

                                        }
                                        if (button2.getText().toString().equals("安装")) {
                                            //try {
                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                            if (Build.VERSION.SDK_INT >= 24) {
                                                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", new File(QingFileUtil.getExternalStorageDir().concat("/简助手/简助手.apk")));
                                                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                                            } else {
                                                intent.setDataAndType(Uri.fromFile(new File(QingFileUtil.getExternalStorageDir().concat("/简助手/简助手.apk"))), "application/vnd.android.package-archive");
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            }
                                            //打开安装应用界面
                                            context.startActivity(intent);
                                            //} catch(Exception e) {
                                            //}
                                        }
                                    }
                                });
                                WindowManager.LayoutParams layoutParams = mDialog.getWindow().getAttributes();
                                layoutParams.width = context.getResources().getDisplayMetrics().widthPixels / 10 * 9;
                                mDialog.getWindow().setAttributes(layoutParams);
                            }
                        } catch (Exception e) {
                        }
                    }
                }).doGet();
    }

     */

    //秒转分

    /**
     * Android 音乐播放器应用里，读出的音乐时长为 long 类型以毫秒数为单位，例如：将 234736 转化为分钟和秒应为 03:55 （包含四舍五入）
     *
     * @param duration 音乐时长
     * @return
     */
    public static String timeParse(long duration) {
        String time = "";
        long minute = duration / 60000;
        long seconds = duration % 60000;
        long second = Math.round((float) seconds / 1000);
        if (minute < 10) {
            time += "0";
        }
        time += minute + ":";
        if (second < 10) {
            time += "0";
        }
        time += second;
        return time;
    }

    public static Bitmap pictureDrawable2Bitmap(PictureDrawable pictureDrawable) {
        //图片背景的画笔
        Paint paint = new Paint();
        paint.setColor(Color.TRANSPARENT);
        //创建bitmap对象
        Bitmap bitmap = Bitmap.createBitmap(pictureDrawable.getIntrinsicWidth(), pictureDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(0, 0, bitmap.getWidth() + 50, bitmap.getHeight() + 50, paint);
        pictureDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        pictureDrawable.draw(canvas);
        return bitmap;
    }

    //下载文件
    /*
    public static void Download(Context context, String title, String content, String url, String path, String name) {
        final AlertDialog mDialog = new MaterialAlertDialogBuilder(context)
                .create();
        View contentView = View.inflate(context, R.layout.dialog_download, null);
        mDialog.setTitle(title);
        mDialog.setMessage(content);
        mDialog.setView(contentView);
        mDialog.show();
        final MaterialButton button1 = contentView.findViewById(R.id.button1);
        final MaterialButton button2 = contentView.findViewById(R.id.button2);
        final ProgressBar progressBar = contentView.findViewById(R.id.jdt);
        final TextInputEditText textInputEditText = contentView.findViewById(R.id.textInputEditText);
        final TextInputLayout textInputLayout = contentView.findViewById(R.id.textInputLayout);
        textInputEditText.setText(name);
        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                textInputLayout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        button1.setText("取消");
        button1.setBackgroundColor(context.getResources().getColor(R.color.itemBackColor));
        button2.setText("保存");
        button2.setBackgroundColor(context.getResources().getColor(R.color.zts));
        button1.setOnClickListener(v11 -> {
            mDialog.dismiss();
        });
        button2.setOnClickListener(v11 -> {
            if (TextUtils.isEmpty(textInputEditText.getText())) {
                textInputLayout.setError("请输入文件名称");
                textInputLayout.setErrorEnabled(true);
            } else {
                button2.setText("请稍等");
                progressBar.setVisibility(View.VISIBLE);
                if (!QingFileUtil.isExistFile(QingFileUtil.getExternalStorageDir().concat(path))) {
                    QingFileUtil.makeDir(QingFileUtil.getExternalStorageDir().concat(path));
                }
                int downloadIdOne = PRDownloader.download(url, QingFileUtil.getExternalStorageDir().concat(path), String.valueOf(textInputEditText.getText()))
                        .build()
                        .setOnStartOrResumeListener(() -> {
                        })
                        .setOnPauseListener(() -> {
                        })
                        .setOnCancelListener(() -> {
                        })
                        .setOnProgressListener(progress -> {
                            long progressPercent = progress.currentBytes * 100 / progress.totalBytes;
                            button2.setText("下载中"); // + progressPercent + "％");
                            progressBar.setIndeterminate(false);
                            progressBar.setProgress((int) progressPercent);
                        })
                        .start(new OnDownloadListener() {
                            @Override
                            public void onDownloadComplete() {
                                progressBar.setVisibility(View.GONE);
                                mDialog.dismiss();
                                android.media.MediaScannerConnection.scanFile((Activity) context, new String[]{QingFileUtil.getExternalStorageDir().concat(path) + textInputEditText.getText()}, null, new android.media.MediaScannerConnection.OnScanCompletedListener() {
                                    public void onScanCompleted(String str, Uri uri) {
                                        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                                        intent.setData(uri);
                                        ((Activity) context).sendBroadcast(intent);
                                        Alerter.create((Activity) context)
                                                .setTitle("保存成功")
                                                .setText("已保存到" + path + textInputEditText.getText())
                                                .setBackgroundColorInt(context.getResources().getColor(R.color.success))
                                                .show();
                                    }
                                });
                            }

                            @Override
                            public void onError(Error error) {
                            }

                        });
            }
        });
        WindowManager.LayoutParams layoutParams = mDialog.getWindow().getAttributes();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels / 10 * 9;
        mDialog.getWindow().setAttributes(layoutParams);
    }
     */

    /*
    //复制弹窗
    public static void CopyDialog(Context context, String little, String content) {
        final AlertDialog mDialog = new MaterialAlertDialogBuilder(context)
                .setPositiveButton("复制", null)
                .setNegativeButton("取消", null)
                .create();
        mDialog.setTitle(little);
        mDialog.setMessage(content);
        mDialog.setOnShowListener(dialog -> {
            Button positiveButton = mDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeButton = mDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            positiveButton.setOnClickListener(v -> {
                mDialog.dismiss();
                ((ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("clipboard", content));
                Alerter.create((Activity) context)
                        .setTitle("复制成功")
                        .setText("已成功将内容复制到剪切板")
                        .setBackgroundColorInt(context.getResources().getColor(R.color.success))
                        .show();
            });
            negativeButton.setOnClickListener(v -> mDialog.dismiss());
        });
        mDialog.show();
        WindowManager.LayoutParams layoutParams = mDialog.getWindow().getAttributes();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels / 10 * 9;
        mDialog.getWindow().setAttributes(layoutParams);
    }
     */

    //QQ群
    public static boolean joinQQGroup(Context context, String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

    //裁剪图片
    @SuppressLint("WrongConstant")
    public static String startUCrop(Context context, String sourceFilePath, float aspectRatioX, float aspectRatioY) {
        Uri sourceUri = Uri.fromFile(new File(sourceFilePath));
        File outDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!outDir.exists()) {
            outDir.mkdirs();
        }
        File outFile = new File(outDir, ".Crop.jpg");
        //裁剪后图片的绝对路径
        String cameraScalePath = outFile.getAbsolutePath();
        Uri destinationUri = Uri.fromFile(outFile);
        //初始化，第一个参数：需要裁剪的图片；第二个参数：裁剪后图片
        UCrop uCrop = UCrop.of(sourceUri, destinationUri);
        //初始化UCrop配置
        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //是否隐藏底部容器，默认显示
        options.setHideBottomControls(false);
        //设置toolbar颜色
        //options.setToolbarColor(ActivityCompat.getColor(activity, R.color.appbarColor));
        //设置状态栏颜色
        //options.setStatusBarColor(ActivityCompat.getColor(activity, R.color.appbarColor));
        //是否能调整裁剪框
        //options.setFreeStyleCropEnabled(true);
        //UCrop配置
        uCrop.withOptions(options);
        //设置裁剪图片的宽高比，比如16：9
        uCrop.withAspectRatio(aspectRatioX, aspectRatioY);
        //uCrop.useSourceImageAspectRatio();
        //跳转裁剪页面
        uCrop.start((Activity) context);
        return cameraScalePath;
    }

    //手机信息
    public static String getPhoneInfo(Context context) {
        StringBuffer sb = new StringBuffer();
        sb.append("主板： " + Build.BOARD + "\n\n");
        sb.append("系统启动程序版本号： " + Build.BOOTLOADER + "\n\n");
        sb.append("系统定制商： " + Build.BRAND + "\n\n");

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            sb.append("cpu指令集：" + Build.CPU_ABI + "\n\n");
            sb.append("cpu指令集2:" + Build.CPU_ABI2 + "\n\n");
        } else {

            if (Build.SUPPORTED_32_BIT_ABIS.length != 0) {
                sb.append("cpu指令集:");
                sb.append(" [ 32位 ] ");
                sb.append("[ ");
                for (int i = 0; i < Build.SUPPORTED_32_BIT_ABIS.length; i++) {

                    if (i == Build.SUPPORTED_32_BIT_ABIS.length - 1) {
                        sb.append(Build.SUPPORTED_32_BIT_ABIS[i]);
                    } else {
                        sb.append(Build.SUPPORTED_32_BIT_ABIS[i] + " , ");
                    }

                }
                sb.append(" ]");
                sb.append("\n\n");
            }

            if (Build.SUPPORTED_64_BIT_ABIS.length != 0) {
                sb.append("cpu指令集:");
                sb.append(" [ 64位 ] ");
                sb.append("[ ");
                for (int i = 0; i < Build.SUPPORTED_64_BIT_ABIS.length; i++) {

                    if (i == Build.SUPPORTED_64_BIT_ABIS.length - 1) {
                        sb.append(Build.SUPPORTED_64_BIT_ABIS[i]);
                    } else {
                        sb.append(Build.SUPPORTED_64_BIT_ABIS[i] + " , ");
                    }

                }
                sb.append(" ]");
                sb.append("\n\n");
            }

        }
        sb.append("设置参数： " + Build.DEVICE + "\n\n");
        sb.append("显示屏参数： " + Build.DISPLAY + "\n\n");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            sb.append("无线电固件版本： " + Build.getRadioVersion() + "\n\n");
        }
        sb.append("硬件识别码： " + Build.FINGERPRINT + "\n\n");
        sb.append("硬件名称： " + Build.HARDWARE + "\n\n");
        sb.append("HOST: " + Build.HOST + "\n\n");
        sb.append("修订版本列表： " + Build.ID + "\n\n");
        sb.append("硬件制造商： " + Build.MANUFACTURER + "\n\n");
        sb.append("版本： " + Build.MODEL + "\n\n");
        sb.append("硬件序列号：" + Build.SERIAL + "\n\n");
        sb.append("手机制造商：" + Build.PRODUCT + "\n\n");
        sb.append("描述Build的标签：" + Build.TAGS + "\n\n");
        sb.append("TIME: " + Build.TIME + "\n\n");
        sb.append("builder类型：" + Build.TYPE + "\n\n");
        sb.append("USER: " + Build.USER);

        return sb.toString();
    }

    //保存图片
    public static String SaveImage(Context context, Bitmap bitmap, String path, String name) {
        if (bitmap == null) {
            return null;
        }
        @SuppressLint("SimpleDateFormat") final String time = new SimpleDateFormat("HH-mm-ss").format(new Date());
        if (!QingFileUtil.isExistFile(QingFileUtil.getExternalStorageDir().concat(path))) {
            QingFileUtil.makeDir(QingFileUtil.getExternalStorageDir().concat(path));
        }
        File appDir = new File(QingFileUtil.getExternalStorageDir().concat(path));
        File file = new File(appDir, name);
        java.io.FileOutputStream fos = null;
        try {
            fos = new java.io.FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /*
    //手机壁纸
    public static Bitmap getWallpaper_1(Context context) {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        }
        ParcelFileDescriptor mParcelFileDescriptor = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mParcelFileDescriptor = wallpaperManager.getWallpaperFile(WallpaperManager.FLAG_SYSTEM);
        }
        FileDescriptor fileDescriptor = mParcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        try {
            mParcelFileDescriptor.close();
        } catch (Exception e) {
        }
        return image;
    }

    //锁屏壁纸
    public static Bitmap getWallpaper_2(Context context) {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        }
        ParcelFileDescriptor mParcelFileDescriptor = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mParcelFileDescriptor = wallpaperManager.getWallpaperFile(WallpaperManager.FLAG_LOCK);
        }
        FileDescriptor fileDescriptor = mParcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        try {
            mParcelFileDescriptor.close();
        } catch (Exception e) {
        }
        return image;
    }

     */

    /**
     * Base64加密字符串
     *
     * @param content     -- 代加密字符串
     * @param charsetName -- 字符串编码方式
     * @return
     */
    public static String base64Encode(Context context, String content, String charsetName) {
        if (TextUtils.isEmpty(charsetName)) {
            charsetName = "UTF-8";
        }
        try {
            byte[] contentByte = content.getBytes(charsetName);
            return Base64.encodeToString(contentByte, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    //判断vpn
    public static boolean isVPNConnected(Context context) {
        List<String> networkList = new ArrayList<>();
        try {
            for (java.net.NetworkInterface networkInterface : java.util.Collections.list(java.net.NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface.isUp()) {
                    networkList.add(networkInterface.getName());
                }
            }
        } catch (Exception ex) {
        }
        return networkList.contains("tun0") || networkList.contains("ppp0");
    }

//    public static AlertDialog loadDialog;
//
//    //加载弹窗
//    public static void LoadingDialog(Context context) {
//        try {
//            ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        loadDialog = new MaterialAlertDialogBuilder(context)
//                .create();
//        final View contentView = View.inflate(context, R.layout.loading, null);
//        loadDialog.setView(contentView);
//        loadDialog.show();
//        loadDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
//        WindowManager.LayoutParams layoutParams = loadDialog.getWindow().getAttributes();
//        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels / 5 * 4;
//        layoutParams.height = context.getResources().getDisplayMetrics().widthPixels / 5 * 4;
//        loadDialog.getWindow().setAttributes(layoutParams);
//    }


    //侧滑水波纹
    public static void setRipple(Context context, View view, int bgColor, int rippleColor, int left_top, int right_top, int left_bottom, int right_bottom) {
        GradientDrawable GD = new GradientDrawable();
        GD.setColor(bgColor);
        GD.setCornerRadii(new float[]{dp2px(context, left_top), dp2px(context, left_top), dp2px(context, right_top), dp2px(context, right_top), dp2px(context, right_bottom), dp2px(context, right_bottom), dp2px(context, left_bottom), dp2px(context, left_bottom)});
        RippleDrawable RE = new RippleDrawable(new ColorStateList(new int[][]{new int[]{}}, new int[]{rippleColor}), GD, null);
        view.setBackground(RE);
    }

    //dp转px
    public static int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    //截取字符串
    public static String JieQu(Context context, String str1, String str2, String str3) {
        if (!str1.contains(str2) || !str1.contains(str3)) {
            return "";
        }
        String substring = str1.substring(str1.indexOf(str2) + str2.length());
        return substring.substring(0, substring.indexOf(str3));
    }

}
