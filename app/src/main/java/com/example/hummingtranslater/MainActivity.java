package com.example.hummingtranslater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private String filePathRecording = ""; // 録音用のファイルパス
    private final String filePathBase = Environment.getExternalStorageDirectory() + "/base.wav"; // 録音用のファイルパス

    private MediaRecorder mediarecorder; // 録音用のメディアレコーダークラス
    private MediaPlayer mediaPlayer;

    private boolean isRecording = false; // 録音中フラグ

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("permission:" + "許可されていない");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1000);
        }
        // ファイルパス
        filePathRecording = getExternalCacheDir().toString() + "/hummingtranslater.wav"; // 録音用のファイルパス
        System.out.println("filePathRecording:" + filePathRecording);
        System.out.println("filePathBase:" + filePathBase);

        // 録音ボタン
        Button buttonRecording = findViewById(R.id.button1);
        // 変換ボタン
        Button buttonTranslater = findViewById(R.id.button2);
        // 再生テストボタン
        Button buttonTest= findViewById(R.id.button3);

        // 録音ボタンリスナー
        buttonRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isRecording){
                    isRecording = false;
                    System.out.println("stopRecord:" + filePathRecording);
                    stopRecord();
                    return;
                }
                isRecording = true;
                System.out.println("startRecord:" + filePathRecording);
                startRecord();

            }
        });

        // テスト再生ボタンリスナー
        buttonTranslater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("startPlay:" + filePathRecording);
                startPlay();
            }
        });

        // テスト再生ボタンリスナー
        buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTestPlay();
            }
        });
    }

    // 録音開始
    private void startRecord(){
        try{
            File mediafile = new File(filePathRecording);
            if(mediafile.exists()) {
                System.out.println("exists:" + filePathRecording);
                //ファイルが存在する場合は削除する
                mediafile.delete();
            }
            mediafile = null;
            mediarecorder = new MediaRecorder();
            //マイクからの音声を録音する
            mediarecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //ファイルへの出力フォーマット DEFAULTにするとwavが扱えるはず
            mediarecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            //音声のエンコーダーも合わせてdefaultにする
            mediarecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            //ファイルの保存先を指定
            mediarecorder.setOutputFile(filePathRecording);
            //録音の準備をする
            mediarecorder.prepare();
            //録音開始
            mediarecorder.start();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    // 録音停止
    private void stopRecord(){
        if(mediarecorder == null){
            Toast.makeText(MainActivity.this, "mediarecorder = null", Toast.LENGTH_SHORT).show();
        }else{
            try{
                //録音停止
                mediarecorder.stop();
                mediarecorder.reset();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    // 再生
    private void startPlay() {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(filePathRecording);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 再生
    private void startTestPlay() {
            mediaPlayer = MediaPlayer.create(this, R.raw.base);
            //mediaPlayer.prepare();
            mediaPlayer.start();
    }
}
