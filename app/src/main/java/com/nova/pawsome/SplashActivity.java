package com.nova.pawsome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    private RecyclerView mRecycleView;
    private LoaderAdapter mExampleAdapter;
    private ArrayList<LoaderItem> mExampleList;
    private RequestQueue mRequestQueue;
    private EditText searchtxt;
    private ImageButton searchbtn,clearbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);


        searchtxt=findViewById(R.id.SearchText);
        searchbtn=findViewById(R.id.Searchbtn);

        mRecycleView=findViewById(R.id.recycler_view);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));

        mExampleList=new ArrayList<>();
        mRequestQueue= Volley.newRequestQueue(this);
        parseJSON();
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExampleList.clear();
                mExampleAdapter.notifyDataSetChanged();
                search();
            }
        });


    }

    private void parseJSON() {
        String url="https://api.thedogapi.com/v1/breeds";


        JsonArrayRequest request=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {


                    for(int i=0;i<response.length();i++){
                        JSONObject Breed=response.getJSONObject(i);
                        String breedName =Breed.getString("name");
                        int id =Breed.getInt("id");
                        JSONObject image=Breed.getJSONObject("image");
                        String imgUrl= image.getString("url");


                        mExampleList.add(new LoaderItem(breedName,imgUrl,id));
                    }

                    mExampleAdapter =new LoaderAdapter(SplashActivity.this,mExampleList);
                    mRecycleView.setAdapter(mExampleAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(SplashActivity.this,"Error",Toast.LENGTH_SHORT).show();
            }
        });

        mRequestQueue.add(request);

    }
    public void search(){
        String url="https://api.thedogapi.com/v1/breeds";


        JsonArrayRequest request=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    String searchBreed =searchtxt.getText().toString();

                    String empty=" ";

                    for(int i=0;i<response.length();i++){
                        JSONObject obj = response.getJSONObject(i);
                        String breedName = obj.getString("name");
                        int id = obj.getInt("id");
                        JSONObject image = obj.getJSONObject("image");
                        String imgUrl = image.getString("url");

                        if(!searchBreed.equals(empty)){
                            String BreedNameCapital = makeCaps(searchBreed);

                            if(breedName.equals(searchBreed) || breedName.equals(BreedNameCapital)  ){
                                mExampleList.add(new LoaderItem(breedName,imgUrl,id));
                                mExampleAdapter =new LoaderAdapter(SplashActivity.this,mExampleList);
                                mRecycleView.setAdapter(mExampleAdapter);
                            }

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });

        mRequestQueue.add(request);
    }
    public static String makeCaps(String str){
        String words[]=str.split("\\s");
        String capitalizeWord="";
        for(String w:words){
            String begin =w.substring(0,1);
            String begin2 =w.substring(1);
            capitalizeWord += begin.toUpperCase()+begin2+" ";
        }
        return capitalizeWord.trim();
    }
}