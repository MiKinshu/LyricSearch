package com.example.lyricsearch;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
public class Lyrics_Details extends AppCompatActivity {
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics__details);
        dialog=new ProgressDialog(Lyrics_Details.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        new RetriveLyrics().execute();
        new RetriveDetails().execute();
        Button BTNaddbookmark=findViewById(R.id.BTNaddbookmark);
        BTNaddbookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String artistnamews=getIntent().getStringExtra("artistnamedata");
                String tracknamews=getIntent().getStringExtra("tracknamedata");
                String artistname=artistnamews.replaceAll(" ","+");
                String trackname=tracknamews.replaceAll(" ","+");
                artistname=artistname.toUpperCase();
                trackname=trackname.toUpperCase();
                try {
                    FileOutputStream file = openFileOutput("data.txt",MODE_APPEND);
                    OutputStreamWriter outputfile = new OutputStreamWriter(file);
                    outputfile.write(trackname + " BY " + artistname+"\n");
                    outputfile.flush();
                    outputfile.close();
                    Toast toast = Toast.makeText(Lyrics_Details.this, "Successfully Bookmarked!",Toast.LENGTH_SHORT);
                    toast.show();
                }
                catch (IOException e){
                    Toast toast = Toast.makeText(Lyrics_Details.this, "Error in opening file! :/",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
    class RetriveDetails extends AsyncTask<Void,Void,String>{
        @SuppressLint("WrongThread")
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Void... voids) {
            String value=getString(R.string.norecord);
            try {
                String artistnamews=getIntent().getStringExtra("artistnamedata");
                String tracknamews=getIntent().getStringExtra("tracknamedata");
                String artistname=artistnamews.replaceAll(" ","+");
                String trackname=tracknamews.replaceAll(" ","+");
                URL audiodb = new URL("https://theaudiodb.com/api/v1/json/1/searchtrack.php?s="+artistname+"&t="+trackname);
                HttpURLConnection myConnection =(HttpURLConnection) audiodb.openConnection();
                InputStream stream = new BufferedInputStream(myConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();
                String inputString;
                while ((inputString = bufferedReader.readLine()) != null) {
                    builder.append(inputString);
                }
                JSONObject topLevel = new JSONObject(builder.toString());
                JSONArray main = topLevel.getJSONArray("track");
                JSONObject no= main.getJSONObject(0);
                value = String.valueOf(no.getString("strDescriptionEN"));
                myConnection.disconnect();
            }
            catch (IOException | JSONException e) {
                e.printStackTrace();
                return value;
            }
            return value;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(final String s) {//sharing details
            super.onPostExecute(s);
            dialog.dismiss();
            TextView TVdetails= findViewById(R.id.TVdetails);
            TVdetails.setMovementMethod(new ScrollingMovementMethod());
            if(s.equals("null")){
                TVdetails.setText(getString(R.string.detntaval));
            }
            else{
                TVdetails.setText(s);
                Button BTNShareDetails=findViewById(R.id.BTNShareDetails);
                BTNShareDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sharedetails=new Intent(Intent.ACTION_SEND);
                        sharedetails.setType("text/plain");
                        sharedetails.putExtra(Intent.EXTRA_TEXT,s);
                        startActivity(Intent.createChooser(sharedetails,"Share details"));
                    }
                });
            }
        }
    }
    class RetriveLyrics extends AsyncTask<Void,Void,String>{
        @SuppressLint("WrongThread")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle("Fetching data");
            dialog.setMessage("Getting Lyrics");
            dialog.show();
        }
        @Override
        protected String doInBackground(Void... voids) {
            String value=getString(R.string.norecord);
            try {
                String artistnamews=getIntent().getStringExtra("artistnamedata");
                String tracknamews=getIntent().getStringExtra("tracknamedata");
                String artistname=artistnamews.replaceAll(" ","+");
                String trackname=tracknamews.replaceAll(" ","+");
                URL audiodb = new URL("https://api.lyrics.ovh/v1/"+artistname+"/"+trackname);
                HttpURLConnection myConnection =(HttpURLConnection) audiodb.openConnection();
                InputStream stream = new BufferedInputStream(myConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();
                String inputString;
                while ((inputString = bufferedReader.readLine()) != null) {
                    builder.append(inputString);
                }
                JSONObject topLevel = new JSONObject(builder.toString());
                value = String.valueOf(topLevel.getString("lyrics"));
                myConnection.disconnect();
            }
            catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return value;
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(final String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            dialog.setMessage("Getting Song Details");
            dialog.show();
            TextView TVLyrics= findViewById(R.id.TVlyrics);
            TVLyrics.setMovementMethod(new ScrollingMovementMethod());
            if(s.equals("null")){
                TVLyrics.setText(getString(R.string.lyrntaval));
            }
            else{
                TVLyrics.setText(s);
                final Button BTNSharelyrics=findViewById(R.id.BTNShareLyrics);
                BTNSharelyrics.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sharelyrics=new Intent(Intent.ACTION_SEND);
                        sharelyrics.setType("text/plain");
                        sharelyrics.putExtra(Intent.EXTRA_TEXT,s);
                        startActivity(Intent.createChooser(sharelyrics,"Share LYRICS"));
                    }
                });
            }
        }
    }
}