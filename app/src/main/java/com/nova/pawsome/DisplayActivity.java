package com.nova.pawsome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DisplayActivity extends AppCompatActivity {
    ImageView ShareButton;
    String breedGroup;
    String origin;
    String bredFor;
    String breedLifeSpan;
    String breedTemperament;
    TextView doggoWeight;
    TextView doggoHeight;
    TextView doggoOrigin;
    TextView doggoName;
    TextView lifespan1;
    TextView doggoBreedGroup;
    TextView doggoTemperament1;
    TextView doggoDescription1;
    private RequestQueue mRequestQueue;
    int doggoID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_display);

        String ImageUrl=getIntent().getStringExtra("ImgUrl");
        ImageView imageView=findViewById(R.id.image_view);
        Picasso.with(DisplayActivity.this).load(ImageUrl).fit().centerCrop().into(imageView);



        doggoWeight = findViewById(R.id.breed_weight);
        doggoHeight = findViewById(R.id.breed_height);
        doggoOrigin = findViewById(R.id.breed_origin);
        doggoName = findViewById(R.id.breed_name);
        lifespan1 = findViewById(R.id.breed_life_span);
        doggoBreedGroup = findViewById(R.id.breed_group);
        doggoDescription1 = findViewById(R.id.breed_description1);
        doggoTemperament1 = findViewById(R.id.breed_temperament);
        ShareButton = findViewById(R.id.Share_btn);


        ShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable drawable= (BitmapDrawable) imageView.getDrawable();
                Bitmap bitmap= drawable.getBitmap();
                String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(),bitmap, "title",null);
                Uri uri = Uri.parse(bitmapPath);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/png");
                intent.putExtra(Intent.EXTRA_STREAM,uri);
                startActivity(Intent.createChooser(intent,"Share"));

            }
        });


        mRequestQueue= Volley.newRequestQueue(this);
        doggoID =getIntent().getIntExtra("ID",0);
        parseJason(doggoID);

    }

    private void parseJason( int id1) {
        String url="https://api.thedogapi.com/v1/breeds";


        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject obj1 = response.getJSONObject(i);
                        String breedName = obj1.getString("name");
                        int id = obj1.getInt("id");
                        try {


                            bredFor = obj1.getString("bred_for");
                            breedGroup = obj1.getString("breed_group");
                            breedLifeSpan = obj1.getString("life_span");
                            breedTemperament = obj1.getString("temperament");
                            origin = obj1.getString("origin");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JSONObject weight = obj1.getJSONObject("weight");
                        String weight_in_metric = weight.getString("metric");

                        JSONObject height = obj1.getJSONObject("height");
                        String height_in_metric=height.getString("metric");

                        JSONObject image = obj1.getJSONObject("image");
                        String imgUrl = image.getString("url");

                        if (id == id1) {
                            DisplayActivity.this.doggoName.setText(breedName);
                            doggoWeight.setText(weight_in_metric);
                            doggoHeight.setText(height_in_metric);
                            try {
                                doggoOrigin.setText(origin);


                                doggoBreedGroup.setText(breedGroup);

                                doggoDescription1.setText(bredFor);

                                lifespan1.setText(breedLifeSpan);


                            doggoTemperament1.setText(breedTemperament);
                        } catch (Exception e) {
                                e.printStackTrace();
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
}

