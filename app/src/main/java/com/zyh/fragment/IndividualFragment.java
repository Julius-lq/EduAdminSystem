package com.zyh.fragment;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zyh.activities.AboutActivity;
import com.zyh.activities.LoginActivity;
import com.zyh.activities.MainActivity;
import com.zyh.beans.HeadPicBean;
import com.zyh.beans.LoginBean;
import com.zyh.utills.FileUtil;
import com.zyh.utills.PhotoPopupWindow;
import com.zyh.utills.Utills;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;


public class IndividualFragment extends Fragment {

    String token;
    String cookie;
    String name;
    String stuId;
    String college;
    String major;
    String className;
    TextView name_text;
    TextView stuId_text;
    TextView college_text;
    TextView marjor_text;
    TextView className_text;
    AppCompatImageView head_pic;
    Button logout;
    Button about;
    private PhotoPopupWindow mPhotoPopupWindow;
    public static File tempFile;
    private Uri imageUri;
    public static final int PHOTO_REQUEST_CAREMA = 1;// ??????
    public static final int CROP_PHOTO = 2; //??????
    public static final int SELECT_PHOTO = 3;//????????????


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_individual, container, false);
        final MainActivity mainActivity = (MainActivity) getActivity();

        LoginBean.Datas.StuInfo stuInfo = ((LoginBean.Datas) mainActivity.loginBean.getData()).getStuInfo();
        name = stuInfo.getName();
        stuId = stuInfo.getStuId();
        college = stuInfo.getCollege();
        major = stuInfo.getMajor();
        className = stuInfo.getClassName();
        token = mainActivity.loginBean.getData().getToken();
        cookie = mainActivity.loginBean.getData().getCookie();
        initView(view);
        getHeadPic();
        Log.d("IndividualFragment", "ActionBegin");
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, LoginActivity.class);
                startActivity(intent);
                mainActivity.finish();
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, AboutActivity.class);
                startActivity(intent);
            }
        });

        head_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoPopupWindow = new PhotoPopupWindow(getActivity(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // ??????????????????
                        openGallery();
                        mPhotoPopupWindow.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // ??????
                        openCamera();
                        mPhotoPopupWindow.dismiss();
                    }
                });
                View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.activity_main, null);
                mPhotoPopupWindow.showAtLocation(rootView,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
        return view;
    }


    private void initView(View view) {
        logout = (Button) view.findViewById(R.id.logout);
        head_pic = (AppCompatImageView) view.findViewById(R.id.head_pic);
        name_text = (TextView) view.findViewById(R.id.name);
        stuId_text = (TextView) view.findViewById(R.id.stu_id);
        college_text = (TextView) view.findViewById(R.id.college);
        marjor_text = (TextView) view.findViewById(R.id.major);
        className_text = (TextView) view.findViewById(R.id.class_name);
        about = view.findViewById(R.id.about);
        name_text.setText(name);
        stuId_text.setText(stuId);
        college_text.setText(college);
        marjor_text.setText(major);
        className_text.setText(className);
    }

    private void getHeadPic() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("cookie", cookie)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://finalab.cn:8081/getHeadImg")
                            .post(requestBody)
                            .addHeader("token", token)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    HeadPicBean headPicBean = Utills.parseJSON(responseData, HeadPicBean.class);
                    ShowHeadPic(headPicBean.getData());
                } catch (Exception e) {
                    Log.d("okHttpError", "okHttpError");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void ShowHeadPic(String base64) {
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        final Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                head_pic.setImageBitmap(decodedByte);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PHOTO_REQUEST_CAREMA:
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imageUri, "image/*");
                    intent.putExtra("scale", true);
                    intent.putExtra("aspectX", 1); // ????????????
                    intent.putExtra("aspectY", 1);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, CROP_PHOTO); // ??????????????????
                }
                break;
            case CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        //Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                        File picFile = uirToFile(imageUri);
                        Log.d("oringalSize", FileUtil.getFileOrFilesSize(picFile) + "");
                        picFile = FileUtil.compress(picFile);
                        Log.d("laterSize", FileUtil.getFileOrFilesSize(picFile) + "");
                        if (!FileUtil.isPicLegal(picFile, 7)) {
                            Toast.makeText(getActivity(), "??????????????????8MB,??????jpg,jpeg,png,gif??????", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        picFile = FileUtil.modifyFileName(picFile);
                        postSetHeadImg(picFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        if (data != null) {
                            Uri uri = data.getData();
                            if (uri.toString().contains("com.miui.gallery.open")) {
                                Log.d("miui", "miui");
                                uri = FileUtil.getImageContentUri(getActivity(), new File(FileUtil.getRealFilePath(getActivity(), uri)));
                            }
                            imageUri = uri;
                        }
                        Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver()
                                .openInputStream(imageUri));
                        //picture.setImageBitmap(bitmap);
                        Intent intent = new Intent("com.android.camera.action.CROP");
                        intent.setDataAndType(imageUri, "image/*");
                        intent.putExtra("scale", true);
                        intent.putExtra("aspectX", 1); // ????????????
                        intent.putExtra("aspectY", 1);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, CROP_PHOTO); // ??????????????????
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_PHOTO);
    }

    public void openCamera() {
        //??????????????????
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        // ????????????
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // ???????????????????????????????????????????????????
        if (hasSdcard()) {
            SimpleDateFormat timeStampFormat = new SimpleDateFormat(
                    "yyyy_MM_dd_HH_mm_ss");
            String filename = timeStampFormat.format(new Date());
            tempFile = new File(Environment.getExternalStorageDirectory(),
                    filename + ".jpg");
            if (currentapiVersion < 24) {
                // ??????????????????uri
                imageUri = Uri.fromFile(tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            } else {
                //??????android7.0 ???????????????????????????
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, tempFile.getAbsolutePath());
                //??????????????????????????????????????????
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //??????WRITE_EXTERNAL_STORAGE??????
                    Toast.makeText(getActivity(), "?????????????????????", Toast.LENGTH_SHORT).show();
                    return;
                }
                imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            }
        }
        // ??????????????????????????????Activity???????????????PHOTO_REQUEST_CAREMA
        startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
    }

    /*
     * ??????sdcard???????????????
     */
    public static boolean hasSdcard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public String bitmapToString(Bitmap bitmap) {
        //???Bitmap??????????????????
        String string = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        byte[] bytes = bStream.toByteArray();
        string = Base64.encodeToString(bytes, Base64.DEFAULT);
        return string;
    }

    private void postPicToServer(final String token, final String cookie, final File pic) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    MediaType MEDIA_TYPE_PNG = MediaType.parse("image");
                    RequestBody requestBody = MultipartBody.create(MEDIA_TYPE_PNG, pic);
                    // ??????????????????????????????
                    MultipartBody multipartBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("cookie", cookie)
                            .addFormDataPart("img", pic.getName(), requestBody)
                            .build();
//                    RequestBody requestBody = new FormBody.Builder()
//                            .add("cookie",cookie)
//                            .add("img",pic)
//                            .build();
                    Request request = new Request.Builder()
                            .url("http://finalab.cn:8081/setHeadImg")
                            .post(multipartBody)
                            .addHeader("token", token)
                            .build();
                    Log.d("setHeadPic", "ready");
                    Response response = client.newCall(request).execute();
                    Log.d("setHeadPic", "done");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getHeadPic();
                        }
                    });

                    String responseData = response.body().string();
                } catch (Exception e) {
                    Log.d("okHttpError", "okHttpError");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void postSetHeadImg(File pic) {
        postPicToServer(token, cookie, pic);
    }

    private File uirToFile(Uri uri) {
        String[] arr = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, arr, null, null, null);
        int imgIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String imgPath = cursor.getString(imgIndex);
        File file = new File(imgPath);
        return file;
    }

    public static String getImgMimeType(File imgFile) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgFile.getPath(), options);
        return options.outMimeType;
    }


}