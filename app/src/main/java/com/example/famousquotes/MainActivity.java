package com.example.famousquotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {



    ListView listView;
    ProgressBar progressber;

    ArrayList <HashMap<String, String>> arrayList = new ArrayList<>();
    HashMap<String, String> hashMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressber = findViewById(R.id.progressber);
        listView = findViewById(R.id.listView);

        ImageSlider imageSlider = findViewById(R.id.image_slider);
        ArrayList<SlideModel> imageList = new ArrayList<>(); // Create image list

        // imageList.add(new SlideModel("String Url" or R.drawable, "title")) You can add title
        imageList.add(new SlideModel(R.drawable.slidercover,"this is a title", ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel("https://bit.ly/2YoJ77H", "The animal population decreased by 58 percent in 42 years.", ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel("https://bit.ly/2BteuF2", "Elephants and tigers may become extinct.",ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel("https://bit.ly/3fLJf72", "And people do that.",ScaleTypes.CENTER_CROP));

        imageSlider.setImageList(imageList);




        String url = "https://dummyjson.com/quotes";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressber.setVisibility(View.GONE);


                        try {
                            JSONArray jsonArray = response.getJSONArray("quotes");

                            for (int x=0; x<jsonArray.length(); x++){
                                JSONObject jsonObjectRequest1 = jsonArray.getJSONObject(x);
                                String quote = jsonObjectRequest1.getString("quote");
                                String author = jsonObjectRequest1.getString("author");
                                String id = jsonObjectRequest1.getString("id");


                                hashMap = new HashMap<>();
                                hashMap.put("quote", quote);
                                hashMap.put("author", author);
                                hashMap.put("id", id);
                                arrayList.add(hashMap);



                            }

                            Myadapter myAdepter = new Myadapter();
                            listView.setAdapter(myAdepter);


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        progressber.setVisibility(View.GONE);

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(jsonObjectRequest);





    }

    public class Myadapter extends BaseAdapter{

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater   layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View myview = layoutInflater.inflate(R.layout.itemquatas,parent,false);

            TextView tvquotes = myview. findViewById(R.id.tvquotes);
            TextView tvauthor = myview. findViewById(R.id.tvauthor);
            LinearLayout tvFavorite = myview.findViewById(R.id.tvFavorite);
            LinearLayout tvCopy = myview.findViewById(R.id.tvCopy);
            LinearLayout tvShare = myview.findViewById(R.id.tvShare);



            hashMap = arrayList.get(position);
            String quote = hashMap.get("quote");
            String author = hashMap.get("author");
            String id = hashMap.get("id");


            tvquotes.setText(quote);
            tvauthor.setText(author);


            tvCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("copy text", quote);
                    clipboard.setPrimaryClip(clip);

                }
            });




            tvShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = quote;
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));

                }
            });









            return myview;
        }
    }



}