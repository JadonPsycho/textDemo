package com.psycho;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static me.xiaopan.android.widget.ToastUtils.toastS;

public class TwoActivity extends AppCompatActivity {

    @Bind(R.id.btn_photo)
    TextView btnPhoto;
    @Bind(R.id.imageView)
    SquareImageView imageView;
    @Bind(R.id.activity_two)
    LinearLayout activityTwo;

    public final int MY_PERMISSIONS_REQUEST_CAMERA = 10011;
    private String imageDir;

    private final int PHOTO_ZOOM = 50000;
    private final int TAKE_PHOTO = 50001;
    private final int PHOTO_RESULT = 50002;
    private final String IMAGE_UNSPECIFIED = "image/*";

    protected String fileName;

    String avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        ButterKnife.bind(this);
        initEvent();
    }

    private void initEvent() {

    }

    @OnClick({R.id.btn_photo, R.id.imageView})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_photo:
                new AlertDialog.Builder(this)
                        .setCancelable(true)
                        .setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                permissionAgree(1);
                            }
                        })
                        .setNegativeButton("从手机相册选择", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                permissionAgree(2);
                            }
                        }).show();
                break;
            case R.id.imageView:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                if (requestCode == PHOTO_ZOOM) {
                    cropImageUri(data.getData(), 200, 200);
                }
                if (requestCode == TAKE_PHOTO) {
                    File picture = new File(Environment.getExternalStorageDirectory() + "/" + imageDir);
                    cropImageUri(Uri.fromFile(picture), 200, 200);
                }
                if (requestCode == PHOTO_RESULT) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Bitmap photo = extras.getParcelable("data");

                        avatar = SavePicInLocal(TwoActivity.this, photo);
                        Log.i("TAG", "onActivityResult: headImage==" + avatar);
                        handler.sendEmptyMessage(0);
                    }
                }
                break;
            default:
                break;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                GoApiWork();
            }catch (Exception e){
                e.printStackTrace();
                Logger.e(e.getMessage());
            }
        }
    };

    private void GoApiWork() {
        /**
         * 增加header
         */
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("Authorization", "CFBF5B19F2A05AA0AAECFFA77D1BC33EF7EFFF5F47DD6E09F7B179B9B07D95C718ECC6CC6C86894D8D67AD2F4C30FC98A563466C4930A6DEA812CD607E08D506")
//                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            }
        }).addNetworkInterceptor(new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.HEADERS));


        OkHttpClient client = httpClient.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://beadhouse.nbxuanma.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        ApiService api = retrofit.create(ApiService.class);

        File file = new File(avatar);

        // 创建 RequestBody，用于封装构建RequestBody
        final RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

// MultipartBody.Part  和后端约定好Key，这里的partName是用image
//        MultipartBody.Part body =
//                MultipartBody.Part.createFormData("image", file.getName(), requestFile);
//
//        Call<UploadEntity> call = api.Upload(body);
//        call.enqueue(new Callback<UploadEntity>() {
//            @Override
//            public void onResponse(Call<UploadEntity> call, Response<UploadEntity> response) {
//                UploadEntity entity = response.body();
//                Logger.json(new Gson().toJson(entity));
//                if(entity.getStatus()==1){
//                    Glide.with(TwoActivity.this).load(avatar).into(imageView);
//                }else {
//                    toastS(TwoActivity.this,entity.getResult());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UploadEntity> call, Throwable t) {
//
//            }
//        });
        /**
         * 这是分水岭，上面是单文件上传，下面是多文件上传
         */

        Map<String,RequestBody> params = new HashMap<>();
        params.put("file\"; filename=\""+ file.getName(), requestFile);

        Call<UploadEntity> call = api.Upload(params);
        call.enqueue(new Callback<UploadEntity>() {
            @Override
            public void onResponse(Call<UploadEntity> call, Response<UploadEntity> response) {
                UploadEntity entity = response.body();
                Logger.json(new Gson().toJson(entity));
                if(entity.getStatus()==1){
                    Glide.with(TwoActivity.this).load(avatar).into(imageView);
                }else {
                    toastS(TwoActivity.this,entity.getResult());
                }
            }

            @Override
            public void onFailure(Call<UploadEntity> call, Throwable t) {

            }
        });
    }


    private void permissionAgree(int i) {
        if (i == 1) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //请求权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                //判断是否需要 向用户解释，为什么要申请该权限
//                ActivityCompat.shouldShowRequestPermissionRationale(this,
//                        Manifest.permission.READ_CONTACTS);
            } else {
                pickImageFromCamera();
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //请求权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA);
                //判断是否需要 向用户解释，为什么要申请该权限
//                ActivityCompat.shouldShowRequestPermissionRationale(this,
//                        Manifest.permission.READ_CONTACTS);
            } else {
                pickImageFromAlbum();
            }
        }
    }


    /**
     * 打开相机拍照获取图片
     */
    public void pickImageFromCamera() {

        imageDir = "temp.jpg";
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(Environment.getExternalStorageDirectory(), imageDir)));
        startActivityForResult(intent, TAKE_PHOTO);
    }

    /**
     * 打开本地相册选取图片
     */
    public void pickImageFromAlbum() {

        //隐式调用，可能出现多种选择
        Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
        intent1.setType(IMAGE_UNSPECIFIED);
        Intent wrapperIntent = Intent.createChooser(intent1, null);
        startActivityForResult(wrapperIntent, PHOTO_ZOOM);
    }


    /**
     * 裁剪图片返回
     */
    public void cropImageUri(Uri uri, int outputX, int outputY) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, PHOTO_RESULT);

    }


    public static Bitmap getBitmapFromUri(Activity activity, Uri uri) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
    }

    @SuppressLint("SdCardPath")
    public String SavePicInLocal(final Activity activity, Bitmap bitmap) {
        FileOutputStream b = null;
        File file = new File("/sdcard/" + activity.getString(R.string.app_name) + "/");
        if (!file.exists()) {
            file.mkdirs();
        }
        String str = null;
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");//获取当前时间，进一步转化为字符串
        date = new Date();
        str = format.format(date);
        fileName = "/sdcard/" + activity.getString(R.string.app_name) + "/" + str + ".jpg";
        System.out.println("path------------------" + fileName);

        try {
            b = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }

    /**
     * 6.0请求权限的结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //权限申请结果
        if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA) {
            for (int index = 0; index < permissions.length; index++) {
                switch (permissions[index]) {
                    case Manifest.permission.CAMERA:
                        if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                            /**用户已经受权*/
                            pickImageFromCamera();
                        } else if (grantResults[index] == PackageManager.PERMISSION_DENIED) {
                            /**用户拒绝了权限*/
                            toastS(getApplication(), "应用没有拍照权限，请授权！");
                        } else {
                            toastS(getApplication(), "应用没有拍照权限，请授权！");
                        }
                        break;
                    case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                        if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                            /**用户已经受权*/
                            pickImageFromAlbum();
                        } else if (grantResults[index] == PackageManager.PERMISSION_DENIED) {
                            /**用户拒绝了权限*/
                            toastS(getApplication(), "应用没有访问相册的权限，请授权！");
                        } else {
                            toastS(getApplication(), "应用没有访问相册的权限，请授权！");
                        }

                        break;
                }
            }
        }
    }
}
