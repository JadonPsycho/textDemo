package com.frontcamera.zhousong.frontcamera;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements CheckListener{
    Context context = MainActivity.this;
    MySurfaceView surfaceView;
    CameraSurfaceHolder mCameraSurfaceHolder = new CameraSurfaceHolder();
    private TextView mTipContent;

    ViewCheckListener listener = new ViewCheckListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    public void initView() {
        mTipContent = (TextView) findViewById(R.id.id_camera_tv_tip_content);
        surfaceView = (MySurfaceView) findViewById(R.id.surfaceView1);
        mCameraSurfaceHolder.setCameraSurfaceHolder(context, surfaceView,listener);
        listener.setListener(this);
    }

    @Override
    public void CheckSuccessed(final String str) {
        Log.e("Str",str);
        Message msg=new Message();
        msg.what=1;
        msg.obj=str;
        handler.sendMessage(msg);
    }

    @Override
    public void CheckFail() {

    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if(msg.what==1){
                mTipContent.setText(msg.obj.toString());
            }
        }
    };
}
