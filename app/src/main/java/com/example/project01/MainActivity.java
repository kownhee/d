package com.example.project01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    protected String mRecordingFile;
    SoundPool sound;
    int soundId;
    int sw=0;
    static final int GET_STRING=1;
    private static final String TAG = "ppp";
    String COUNT;
    String SW;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        sound = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        soundId = sound.load(this, R.raw.blop, 1);

    }

    public void myListener(View target) {
        Intent intent = new Intent(getApplicationContext(), followActivity.class);
        startActivity(intent);
    }

    public void myListener1(View target) {
        Intent intent = new Intent(getApplicationContext(), todayActivity.class);
        startActivity(intent);
    }
    public void myListener2(View target) {
        Intent in = new Intent(this, fitActivity.class);
        startActivityForResult(in,GET_STRING);
    }


    public void myListener3(View target) {
        if(sw == 0) sound.play(soundId,1f,1f,0,0,1f);
        Intent intent = new Intent(getApplicationContext(),PhotoCardActivity.class);
        startActivity(intent);
    }

    public void setting(View target){
        if(sw == 0) sound.play(soundId,1f,1f,0,0,1f);
        Intent intent = new Intent(this, settingDialog.class);
        startActivityForResult(intent,103);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 101 && resultCode== RESULT_OK){
            SW = data.getStringExtra("SW");
            Log.d(TAG,data.getStringExtra("SW")+"받음");
        }
            if(requestCode==GET_STRING && resultCode==RESULT_OK ){
                //Log.d(TAG,data.getStringExtra("Count")+"받음");
                COUNT = data.getStringExtra("Count");
            }

    }
    public void bt_graph(View v){
        Intent intent= new Intent(this, graphActivity.class);

        intent.putExtra("Count", COUNT);
        intent.putExtra("SW",1);
        startActivity(intent);
;       //sw 추가해서 한번만 값 들어가게 하기
        Log.d(TAG,COUNT+"값 출력");
        COUNT=null;
    }

}
