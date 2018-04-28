package com.example.srv.mp3play;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView musicListView;

    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};//권한 설정 변수
    private static final int MULTIPLE_PERMISSIONS = 101;//권한 동의 여부 문의 후 callback함수에 쓰일 변수

    private MediaPlayer mMediaPlayer = new MediaPlayer();
    private ArrayList<Music> musicList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        musicListView = (ListView)findViewById(R.id.musicListView);

        if(checkPermissions()) {
            musicList = new FileLoad(this).getMusicList();

            final MusicListAdapter adapter = new MusicListAdapter(this, musicList);

            musicListView.setAdapter(adapter);

            /*
            musicListView.setOnItemClickListener(new ListView.OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                   // Uri musicURI = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "" + adapter.getMusicId(position));

                  // playMusic(musicURI);

                    Intent intent = new Intent(MainActivity.this, MusicActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("playlist", musicList);
                    startActivity(intent);

                }
            });
            */
            musicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    Intent intent = new Intent(MainActivity.this, MusicActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("playlist", musicList);
                    startActivity(intent);


                    //Toast.makeText(getApplicationContext(), ""+position, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void playMusic(Uri uri) {
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(this, uri);
            mMediaPlayer.prepare();
            mMediaPlayer.start();

            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    // TODO
                    // Do something when playing is completed
                }
            });

        } catch (IOException e) {
            Log.v("SimplePlayer", e.getMessage());
        }
    }
    private boolean checkPermissions() {//사용권한 묻는 함수
        int result;
        List<String> permissionList = new ArrayList<>();
        for (String pm : permissions) {
            result = ContextCompat.checkSelfPermission(this, pm);//현재 컨텍스트가 pm 권한을 가졌는지 확인
            if (result != PackageManager.PERMISSION_GRANTED) {//사용자가 해당 권한을 가지고 있지 않을 경우
                permissionList.add(pm);//리스트에 해당 권한명을 추가한다
            }
        }
        if (!permissionList.isEmpty()) {//권한이 추가되었으면 해당 리스트가 empty가 아니므로, request 즉 권한을 요청한다.
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option, menu);

        return true;
    }

    @Override
    public MenuInflater getMenuInflater() {
        return super.getMenuInflater();
    }
}
