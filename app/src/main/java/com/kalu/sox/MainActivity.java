package com.kalu.sox;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import lib.kalu.sox.Sox;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        findViewById(R.id.main_sox_pcm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoPcm();
            }
        });

        findViewById(R.id.main_sox_mp3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoMp3();
            }
        });

        findViewById(R.id.main_sox_wav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoWav();
            }
        });
    }

    private void todoWav() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                // 1
                String path = getApplicationContext().getCacheDir().getAbsolutePath();
                String from = path + "/test3.wav";
                String to = path + "/test3_3.wav";
                // 2
                boolean noisered = Sox.noisered(from, to);
                Log.e("SOX", "noisered = " + noisered);
            }
        }).run();
    }

    private void todoMp3() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                // 1
                String path = getApplicationContext().getCacheDir().getAbsolutePath();
                String from = path + "/test2.mp3";
                String to = path + "/test2_2.mp3";
                // 2
                boolean noisered = Sox.noisered(from, to);
                Log.e("SOX", "noisered = " + noisered);
            }
        }).run();
    }

    private void todoPcm() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                // 1
                String path = getApplicationContext().getCacheDir().getAbsolutePath();
                String from = path + "/test1.pcm";
                String to = path + "/test1_1.pcm";
                // 2
                boolean noisered = Sox.noisered(from, to);
                Log.e("SOX", "noisered = " + noisered);
            }
        }).run();
    }

    private void init() {

        String absolutePath = getApplicationContext().getCacheDir().getAbsolutePath();
        List<String> list = Arrays.asList("test1.pcm", "test2.mp3", "test3.wav");
        for (int i = 0; i < list.size(); i++) {
            String s = list.get(i);
            boolean copyFile = copyFile(getApplicationContext(), s, absolutePath + "/" + s);
            if (!copyFile) {
                Toast.makeText(getApplicationContext(), "初始化资源文件 => 错误", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private boolean copyFile(Context context, String assetsPath, String savePath) {
        // assetsPath 为空时即 /assets
        try {
            InputStream is = context.getAssets().open(assetsPath);
            FileOutputStream fos = new FileOutputStream(new File(savePath));
            byte[] buffer = new byte[1024];
            int byteCount = 0;
            while ((byteCount = is.read(buffer)) != -1) {// 循环从输入流读取
                // buffer字节
                fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
            }
            fos.flush();// 刷新缓冲区
            is.close();
            fos.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
