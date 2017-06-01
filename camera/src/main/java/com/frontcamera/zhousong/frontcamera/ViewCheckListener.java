package com.frontcamera.zhousong.frontcamera;

/**
 * Created by mr.psycho on 2017/2/16.
 */

public class ViewCheckListener {
    CheckListener listener;

    public void setListener(CheckListener listener) {
        this.listener = listener;
    }

    public void onStartListener(String str){
        listener.CheckSuccessed(str);
    }

}
