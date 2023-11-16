package com.example.appmovie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    //link json intent https://run.mocky.io/v3/5ce6edcb-8eed-4c46-b69b-b5b7fa2e125f
    private static String JSON_URL = "https://run.mocky.io/v3/5ce6edcb-8eed-4c46-b69b-b5b7fa2e125f";

    List<MovieModelClass> movieList;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycelView);

        GetData getData = new GetData();
        getData.execute();
    }

    public class GetData extends AsyncTask<String, String, String >{


        @Override
        protected String doInBackground(String... strings) {

            String current="";

            try {
                URL url;
                HttpURLConnection urlConnection = null;

                try {
                    url = new URL(JSON_URL);
                    urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream is = urlConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);

                    int data = isr.read();
                    while (data != -1){
                        current+=(char) data;
                        data = isr.read();
                    }
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }finally {
                    if (urlConnection != null){
                        urlConnection.disconnect();
                    }
                }
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
            return current;

        }

        @Override
        protected void onPostExecute(String s) {

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("movie");

                for (int i = 0; i< jsonArray.length(); i++){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    MovieModelClass model = new MovieModelClass();
                    model.setName(jsonObject1.getString("name"));
                    model.setTime(jsonObject1.getString("time"));
                    model.setImg(jsonObject1.getString("image"));

                    movieList.add(model);
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            PutDataIntoRecycelView(movieList);
        }
    }

    private void PutDataIntoRecycelView(List<MovieModelClass> movieList){
        Adaptery adaptery = new Adaptery(this, movieList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(adaptery);
    }
}

