package com.psycho.worfpicdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.psycho.worfpicdemo.utils.FileUtils;
import com.psycho.worfpicdemo.utils.WordUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button button;

    // 模板文集地址
    private static final String demoPath = "/mnt/sdcard/doc2/test.doc";
    // 创建生成的文件地址
    private static final String newPath = "/mnt/sdcard/doc2/testS.doc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.btn);

        initEvent();
    }

    private void initEvent() {
        try {
            InputStream inputStream = getAssets().open("muban.doc");
            FileUtils.writeFile(new File(demoPath), inputStream);

            doScan();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void doScan() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("${Name}", "huangqiqing");
        param.put("${Address}", "信息管理与信息系统");
        param.put("${Person}", "男");
        param.put("${LawPerson}", "山东财经大学");
        param.put("${Time}", new Date().toString());
        param.put("${Content}", "huangqiqing");
        param.put("${Result}", "信息管理与信息系统");
        param.put("${Sign}", "男");
        param.put("${PSign}", "山东财经大学");
        param.put("${Number}", new Date().toString());
        param.put("${Remark}", new Date().toString());

        Map<String, Object> header = new HashMap<String, Object>();
        header.put("width", 100);
        header.put("height", 150);
        header.put("type", "jpg");
        header.put("content", WordUtil.inputStream2ByteArray(new FileInputStream(demoPath), true));
        param.put("${Picture}", header);

        CustomXWPFDocument doc = WordUtil.generateWord(param, demoPath);
        FileOutputStream fopts = new FileOutputStream(newPath);
        doc.write(fopts);
        fopts.close();
    }
}
