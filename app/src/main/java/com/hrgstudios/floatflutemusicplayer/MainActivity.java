package com.hrgstudios.floatflutemusicplayer;

//Programado por HeroRicky_Games

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final List<MusicList> musicLists = new ArrayList<>();
    private RecyclerView musicRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decodeView = getWindow().getDecorView();
        int options = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decodeView.setSystemUiVisibility(options);

        setContentView(R.layout.activity_main);

        final ImageView searchBtn = findViewById(R.id.searchBtn);
        final ImageView menuBtn = findViewById(R.id.menuBtn);
        musicRecyclerView = findViewById(R.id.musicRecyclerView);
        final CardView playPauseCard = findViewById(R.id.PlayPauseCard);
        final ImageView playPauseImg = findViewById(R.id.PlayPauseImg);
        final ImageView nextBtn = findViewById(R.id.nextBtn);
        final ImageView previusBtn = findViewById(R.id.PreviusBtn);

        musicRecyclerView.setHasFixedSize(true);
        musicRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            getMusicFiles();
        }
        else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 11);
            }
            else{
                getMusicFiles();
            }
        }
    }

    @SuppressLint("Range")
    private void getMusicFiles(){
        ContentResolver contentResolver = getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = contentResolver.query(uri, null,MediaStore.Audio.Media.DATA+" LIKE?", new String[]{"%.mp3%"}, null);

        if(cursor == null){
            Toast.makeText(this, "Algo de errado não está certo!!", Toast.LENGTH_SHORT).show();
        }
        else if (!cursor.moveToNext()){
            Toast.makeText(this, "Nenhuma musica Encontrada!", Toast.LENGTH_SHORT).show();
        }
        else{
            while (cursor.moveToNext()){
                @SuppressLint("Range")
                final String getMusicFileName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                final String getArtistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));

                @SuppressLint("Range")
                long cursorID = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));

                Uri musicFileUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, cursorID);
                String getDuration = "00:00";

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    getDuration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION));
                }

                final MusicList musicList = new MusicList(getMusicFileName, getArtistName, getDuration, false, musicFileUri);
                musicLists.add(musicList);
            }
            musicRecyclerView.setAdapter(new MusicAdapter(musicLists, MainActivity.this));
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getMusicFiles();
        } else {
            Toast.makeText(this, "A Permissão foi negada pelo Usuario! Acesse as configurações do seu dispositivo para permitir", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus){
            View decodeView = getWindow().getDecorView();
            int options = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            decodeView.setSystemUiVisibility(options);
        }
    }
}