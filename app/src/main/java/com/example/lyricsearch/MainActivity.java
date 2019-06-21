package com.example.lyricsearch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText ETtrackname=findViewById(R.id.ETtrackname);
        final EditText ETartistname=findViewById(R.id.ETartistname);
        Button BTNsubmit=findViewById(R.id.BTNsubmit);
        BTNsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ETartistname.getText().toString().isEmpty()||ETartistname.getText().toString().isEmpty()) {
                    Toast toast = Toast.makeText(MainActivity.this, "Please Fill in both the Fields!",Toast.LENGTH_SHORT);
                    toast.show();
                } else{
                    String trackname=ETtrackname.getText().toString().trim();
                    String artistname=ETartistname.getText().toString().trim();
                    Intent intent=new Intent(MainActivity.this,com.example.lyricsearch.Lyrics_Details.class);
                    intent.putExtra("artistnamedata",artistname);
                    intent.putExtra("tracknamedata",trackname);
                    startActivity(intent);
                }
            }
        });
        Button BTNupdatefav=findViewById(R.id.BTNupdatefav);
        BTNupdatefav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String favartist=ETartistname.getText().toString().trim();
                String favtrack=ETtrackname.getText().toString().trim();
                SharedPreferences.Editor faveditor=getSharedPreferences("com.example.lyricsearch.favs",MODE_PRIVATE).edit();
                faveditor.putString("favartist",favartist);
                faveditor.putString("favtrack",favtrack);
                faveditor.apply();
                Toast toast = Toast.makeText(MainActivity.this, "Favourites updated!",Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        Button BTNsetfav=findViewById(R.id.BTNsetfav);
        BTNsetfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences faveditor=getSharedPreferences("com.example.lyricsearch.favs",MODE_PRIVATE);
                String favartist=faveditor.getString("favartist",null);
                String favtrack=faveditor.getString("favtrack",null);
                if(favartist==null||favtrack==null||favartist.equals("")||favtrack.equals("")){
                    Toast toast = Toast.makeText(MainActivity.this, "Favourites not set yet!",Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    ETartistname.setText(favartist);
                    ETtrackname.setText(favtrack);
                }
            }
        });
        Button BTNBookmarks=findViewById(R.id.BTNBookmarks);
        BTNBookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,com.example.lyricsearch.Bookmarks.class);
                startActivity(intent);
            }
        });
    }
}
