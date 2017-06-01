package com.psycho.docx;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.psycho.docx.util.ZipControl;
import com.psycho.docx.util.ZipExtractorTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static me.xiaopan.android.widget.ToastUtils.toastS;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.txt)
    EditText txt;
    @Bind(R.id.signature_rl)
    RelativeLayout signatureRl;
    @Bind(R.id.signature_img)
    ImageView signatureImg;
    @Bind(R.id.btn_build)
    Button btnBuild;
    @Bind(R.id.btn_pinter)
    Button btnPinter;

    private int CODE_FOR_WRITE_PERMISSION = 132;

    String signImgPath = "";

    String replaceImg = Config.getFiles() + "unzip/word/media";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        permissionApply();

    }

    private void permissionApply() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    CODE_FOR_WRITE_PERMISSION);
        } else {
            readDocx();
        }
    }

    /**
     * 读取res/raw下docx文件 并对其进行解压
     * 解压后放入SD卡中的docxDemo/unzip文件夹
     */
    private void readDocx() {
        try {
            File file = new File(Config.getFiles());
            if (!file.exists()) {
                file.mkdirs();
            }
            String oldFile = Config.getFiles() + "name.docx";
            File old = new File(oldFile);
            if (!old.exists()) {
                InputStream is = getResources().openRawResource(R.raw.name);
                FileOutputStream fos = null;
                fos = new FileOutputStream(oldFile);
                byte[] buffer = new byte[8192];
                int count = 0;
                // 开始复制data.db文件
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
                Log.i("mkdirs", "name.docx写入成功");

                ZipExtractorTask task = new ZipExtractorTask(Config.getFiles()
                        + "name.docx", Config.getFiles() + "unzip", true);
                task.execute();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @OnClick({R.id.signature_rl, R.id.signature_img, R.id.btn_build, R.id.btn_pinter})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.signature_rl:
                Intent intent = new Intent(this, SignActivity.class);
                startActivityForResult(intent, 100);
                overridePendingTransition(R.anim.push_up_in, R.anim.layout_null);
                break;
            case R.id.signature_img:
                if (!signImgPath.equals("")) {
                    new AlertDialog.Builder(this).setTitle("清除").setMessage("清除该签名")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Glide.clear(signatureImg);
                                    signImgPath = "";
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                }
                break;
            case R.id.btn_build:
                if (!signImgPath.equals("") && !txt.getText().toString().equals("")) {
                    replace();
                    writeTxt();
                    handler.sendEmptyMessage(0);
                } else {
                    toastS(this, "请完善内容");
                }
                break;
            case R.id.btn_pinter:
                break;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String path = Config.getFiles() + "new_doc.zip";
            String basePath = Config.getFiles() + "unzip/";
            String[] pathStr = {
                    basePath
            };

            try {
                ZipControl.writeByApacheZipOutputStream(pathStr,path,"");
            } catch (IOException e) {
                e.printStackTrace();
            }
            renameFile(Config.getFiles() + "new_doc.zip");
        }
    };


    /**
     * 替换docx中的图片
     */
    private void replace() {
        File file = new File(replaceImg + "/image1.png");
        if (file.exists()) {
            file.delete();
        }
        try {
            InputStream is = new FileInputStream(new File(signImgPath));
            FileOutputStream fos = null;
            fos = new FileOutputStream(replaceImg + "/image1.png");
            byte[] buffer = new byte[8192];
            int count = 0;
            // 开始复制data.db文件
            while ((count = is.read(buffer)) > 0) {
                fos.write(buffer, 0, count);
            }
            fos.close();
            is.close();
            Log.i("mkdirs", "SignImage替换写入成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 替换docx标识符的文本
     */
    private void writeTxt() {
        String docPath = Config.getFiles() + "unzip/word/document.xml";
        File f = new File(docPath);
        try {
            InputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            String str = new String(buffer, "UTF-8");
            Log.e("TAG", "writeTxt: ===== " + str);
            is.close();
            Log.i("mkdirs", "读取xml内容成功");
            f.delete();

            str = str.replace("{NAME}", txt.getText().toString());

            File file = new File(docPath);
            FileOutputStream fos = new FileOutputStream(file);
            byte[] bytes = str.getBytes();
            fos.write(bytes);
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 10011:
                Log.e("TAG", "onActivityResult: " + data.getStringExtra("path"));
                signImgPath = data.getStringExtra("path");
                Glide.with(this).load(new File(data.getStringExtra("path"))).into(signatureImg);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == CODE_FOR_WRITE_PERMISSION) {
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readDocx();
            } else {
                //用户不同意，自行处理即可
                //申请WRITE_EXTERNAL_STORAGE权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        CODE_FOR_WRITE_PERMISSION);
            }
        }
    }


    public static void zipFolder(String srcFilePath, String zipFilePath) throws Exception {
        // 创建Zip包
        java.util.zip.ZipOutputStream outZip = new java.util.zip.ZipOutputStream(new java.io.FileOutputStream(
                zipFilePath));

        // 打开要输出的文件
        java.io.File file = new java.io.File(srcFilePath);
        // 压缩
        zipFiles(file.getParent() + java.io.File.separator, file.getName(), outZip);
        // 完成,关闭
        outZip.finish();
        outZip.close();
    }

    private static void zipFiles(String folderPath, String filePath, java.util.zip.ZipOutputStream zipOut)
            throws Exception {
        if (zipOut == null) {
            return;
        }
        java.io.File file = new java.io.File(folderPath + filePath);
        // 判断是不是文件
        if (file.isFile()) {
            java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(filePath);
            java.io.FileInputStream inputStream = new java.io.FileInputStream(file);
            zipOut.putNextEntry(zipEntry);
            int len;
            byte[] buffer = new byte[100000];
            while ((len = inputStream.read(buffer)) != -1) {
                zipOut.write(buffer, 0, len);
            }
            inputStream.close();
            zipOut.closeEntry();
        } else {
            // 文件夹的方式,获取文件夹下的子文件
            String fileList[] = file.list();
            // 如果没有子文件, 则添加进去即可
            if (fileList.length <= 0) {
                java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(filePath + java.io.File.separator);
                zipOut.putNextEntry(zipEntry);
                zipOut.closeEntry();
            }
            // 如果有子文件, 遍历子文件
            for (int i = 0; i < fileList.length; i++) {
                zipFiles(folderPath, filePath + java.io.File.separator + fileList[i], zipOut);
            }
        }
    }

    private void renameFile(String oldPath){
        File file = new File(oldPath);
        file.renameTo(new File(Config.getFiles()+"new_doc.docx"));
    }

}
