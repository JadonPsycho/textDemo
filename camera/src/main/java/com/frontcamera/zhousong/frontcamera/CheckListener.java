package com.frontcamera.zhousong.frontcamera;

/**
 * Created by mr.psycho on 2017/2/16.
 */

public interface CheckListener {
    /**
     * 回调成功
     * @param str 回调内容
     */
    void CheckSuccessed(String str);


    void CheckFail();
}
