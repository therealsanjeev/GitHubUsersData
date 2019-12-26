package com.example.networking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    public static final String TAG="MainActivity";

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
        nAsync.execute("https://www.google.com","https://api.github.com/search/users?q=therealsanjeev");

    }
    class NetworkAsync extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            String stringUrl=strings[1];

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
            ArrayList<GithubUser> users= parseJson(s);
            GithubUsersAdapter githubUsersAdapter = new GithubUsersAdapter(users);
            RecyclerView recyclerView =findViewById(R.id.rvList);

            recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
            recyclerView.setAdapter(githubUsersAdapter);


//            TextView tvData = findViewById(R.id.tvData);
//            tvData.setText(s);
        }
    }
    ArrayList<GithubUser> parseJson(String s){
        ArrayList<GithubUser> githubUsers= new ArrayList<>();

        try {
            JSONObject root= new JSONObject(s);
            JSONArray item=root.getJSONArray("items");

            for(int i=0;i<item.length();i++){
                JSONObject object = item.getJSONObject(i);

                String login =object.getString("login");
                Integer id = object.getInt("id");
                String avatar = object.getString("avatar_url");
                Double score= object.getDouble("score");
                String html = object.getString("html_url");

                GithubUser githubUser= new GithubUser(login,id,html,score,avatar);
                githubUsers.add(githubUser);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }



        return githubUsers;
    }
}
