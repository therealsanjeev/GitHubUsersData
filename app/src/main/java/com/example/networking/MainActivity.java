package com.example.networking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    String input;

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
        Button btnEnter =findViewById(R.id.btnEnter);
        final EditText text = findViewById(R.id.editText2);
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input=text.getText().toString();

            }
        });
    }

    void makeNetworkCall(String url) throws IOException {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                Toast.makeText(MainActivity.this, "hello", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                String result  =response.body().string();

                ArrayList<GithubUser> users= parseJson(result);
                final GithubUsersAdapter githubUsersAdapter = new GithubUsersAdapter(users);

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RecyclerView recyclerView =findViewById(R.id.rvList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                        recyclerView.setAdapter(githubUsersAdapter);
                    }
                });

            }
        });


    }

    private void UpdateTextView() {
        //network call :
//        NetworkAsync nAsync = new NetworkAsync();
//        nAsync.execute("https://www.google.com","https://api.github.com/search/users?q="+input);
        try {
            makeNetworkCall("https://api.github.com/search/users?q="+input);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
