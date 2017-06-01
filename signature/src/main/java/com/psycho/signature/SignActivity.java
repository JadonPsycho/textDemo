package com.psycho.signature;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static me.xiaopan.android.widget.ToastUtils.toastS;

public class SignActivity extends AppCompatActivity {

    @Bind(R.id.id_sign_view)
    LinePathView idSignView;
    @Bind(R.id.sure)
    Button sure;
    @Bind(R.id.clear)
    Button clear;

    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        ButterKnife.bind(this);

    }

    private void initEvent() {
        String str = null;
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");//获取当前时间，进一步转化为字符串
        date = new Date();
        str = format.format(date);
        path = "/sdcard/" + getResources().getString(R.string.app_name) + "/" + str + ".jpg";
    }

    @OnClick({R.id.sure, R.id.clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sure:
                initEvent();
                if (idSignView.getTouched()){
                    try {
                        idSignView.save(path, false,10);
                    } catch (IOException e) {
                        Log.e("TAG","签名文件保存失败");
                        e.printStackTrace();
                    }
                    Intent intent = new Intent();
                    intent.putExtra("path",path);
                    setResult(10011,intent);
                    finish();
                    overridePendingTransition(R.anim.layout_null, R.anim.slide_down_out);
                }else{
                    toastS(this,"您没有签名~");
                }

                break;
            case R.id.clear:
                idSignView.clear();
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
            overridePendingTransition(R.anim.layout_null, R.anim.slide_down_out);
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
