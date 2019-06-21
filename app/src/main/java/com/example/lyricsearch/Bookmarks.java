package com.example.lyricsearch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Bookmarks extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);
        TextView TVbookmarks = findViewById(R.id.TVbookmarks);
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput("data.txt")));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();
            br.close();
            TVbookmarks.setText(everything);
        }
        catch(IOException e){
            Toast toast = Toast.makeText(Bookmarks.this, "Error in opening file! :/", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
