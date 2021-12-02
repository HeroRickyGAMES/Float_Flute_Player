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
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final List<MusicList> musicLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decodeView = getWindow().getDecorView();
        int options = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decodeView.setSystemUiVisibility(options);

        setContentView(R.layout.activity_main);

        final ImageView searchBtn = findViewById(R.id.searchBtn);
        final ImageView menuBtn = findViewById(R.id.menuBtn);
        final RecyclerView musicRecyclerView = findViewById(R.id.musicRecyclerView);
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

    private void getMusicFiles(){

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
}