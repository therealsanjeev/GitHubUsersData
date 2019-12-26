package com.example.networking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnGetData = findViewById(R.id.btnGetData);
        btnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateTextView();
            }
        });
    }

    private void UpdateTextView() {
        //
        //network call :

        NetworkAsync nAsync = new NetworkAsync();
        nAsync.execute("https://www.google.com","https://www.github.com");

    }
    class NetworkAsync extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            String stringUrl=strings[0];

            try {
                URL url= new URL(stringUrl);
                HttpURLConnection httpsURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpsURLConnection.getInputStream();
                Scanner scanner = new Scanner(inputStream);
                scanner.useDelimiter("\\A");
                if(scanner.hasNext()){
                    String s= scanner.next();
                    return s;
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Field to load";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            TextView tvData = findViewById(R.id.tvData);
            tvData.setText(s);
        }
    }
}
