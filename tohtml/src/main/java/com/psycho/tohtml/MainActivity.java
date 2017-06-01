package com.psycho.tohtml;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.psycho.tohtml.utils.FileUtils;

import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private static final String demoPath = "/mnt/sdcard/docs/";

    private WebView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = (WebView) findViewById(R.id.web);

        File file = new File(demoPath);
        if (!file.exists()) {
            file.mkdirs();
        }

        try {
            InputStream inputStream = getAssets().open("a.html");
            FileUtils.writeFile(new File(demoPath+"a.html"), inputStream);

            InputStream inputStream1 = getAssets().open("a.css");
            FileUtils.writeFile(new File(demoPath+"a.css"), inputStream1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        view.loadUrl("file:///android_asset/a.html");

        try {
            htmlToWord2();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void htmlToWord2() throws Exception {

        InputStream bodyIs = new FileInputStream(demoPath+"a.html");
        InputStream cssIs = new FileInputStream(demoPath+"a.css");
        String body = this.getContent(bodyIs);
        String css = this.getContent(cssIs);

        Log.e("TAG", "htmlToWord2: "+css);
        Log.e("TAG", "htmlToWord2     : "+body);

        //拼一个标准的HTML格式文档
        String content = "<html><head><style>" + css + "</style></head><body>" + body + "</body></html>";
        InputStream is = new ByteArrayInputStream(content.getBytes("GBK"));
        OutputStream os = new FileOutputStream(demoPath + "a.doc");
        this.inputStreamToWord(is, os);
    }

    /**
     * 把is写入到对应的word输出流os中
     * 不考虑异常的捕获，直接抛出
     *
     * @param is
     * @param os
     * @throws IOException
     */
    private void inputStreamToWord(InputStream is, OutputStream os) throws IOException {
        POIFSFileSystem fs = new POIFSFileSystem();
        //对应于org.apache.poi.hdf.extractor.WordDocument
        fs.createDocument(is, "WordDocument");
        fs.writeFilesystem(os);
        os.close();
        is.close();
    }

    /**
     * 把输入流里面的内容以UTF-8编码当文本取出。
     * 不考虑异常，直接抛出
     *
     * @param ises
     * @return
     * @throws IOException
     */
    private String getContent(InputStream... ises) throws IOException {
        if (ises != null) {
            StringBuilder result = new StringBuilder();
            BufferedReader br;
            String line;
            for (InputStream is : ises) {
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
            }
            return result.toString();
        }
        return null;
    }

}
