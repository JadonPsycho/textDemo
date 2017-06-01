package com.psycho.freemarker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class MainActivity extends AppCompatActivity {

    private TextView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = (TextView) findViewById(R.id.text);
        Configuration configuration = new Configuration();
        /** 设置编码 **/
        configuration.setDefaultEncoding("utf-8");
        /** 我的ftl文件是放在assets**/
        String fileDirectory = "file:///android_asset/";

        try {
            /** 加载文件 **/
            configuration.setDirectoryForTemplateLoading(new File(fileDirectory));
            /** 加载模板 **/
            Template template = configuration.getTemplate("FreeMarker中word导出XML.ftl");

            /** 准备数据 **/
            Map<String,String> dataMap = new HashMap<>();
            /** 在ftl文件中有${textDeal}这个标签**/
            dataMap.put("QYMC","世游信息科技有限公司");
            /** 指定输出word文件的路径 **/
            String path = "/sdcard/" + getResources().getString(R.string.app_name) + "/";
            File docFile = new File(path);
            if (!docFile.exists()){
                docFile.mkdirs();
            }
            String outFile = path+ "myWord.doc";
            File docFile1 = new File(outFile);
            FileOutputStream fos = new FileOutputStream(docFile1);
            Writer out = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"),10240);
            template.process(dataMap,out);

            if(out != null){
                out.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }


    }

    private void initEvent() {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
