package com.psycho.signature;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.sign_tv)
    TextView signTv;
    @Bind(R.id.sign_iv)
    ImageView signIv;
    @Bind(R.id.sign_ll)
    RelativeLayout signLl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initEvent();
    }

    private void initEvent() {

        File file = new File("/sdcard/" + getResources().getString(R.string.app_name) + "/");
        if (!file.exists()) {
            file.mkdirs();
        }

    }

    @OnClick(R.id.sign_ll)
    public void onViewClicked() {
        Intent intent = new Intent(this, SignActivity.class);
        startActivityForResult(intent,100);
        overridePendingTransition(R.anim.push_up_in,R.anim.layout_null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 10011:
                Log.e("TAG", "onActivityResult: "+ data.getStringExtra("path"));
                Glide.with(this).load(new File(data.getStringExtra("path"))).into(signIv);
                break;
        }
    }
}
