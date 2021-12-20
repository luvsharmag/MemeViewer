package com.example.practice;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    Button sharememe;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadmeme();
        sharememe = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);
        sharememe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
               Bitmap bitmap = bitmapDrawable.getBitmap();
               shareimage(bitmap);
            }
        });

    }

    private void shareimage(Bitmap bitmap) {
        Uri uri = getimageforfile(bitmap);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM,uri);
        intent.putExtra(Intent.EXTRA_TEXT,"meme");
        intent.setType("image/png");
        startActivity(Intent.createChooser(intent,"share via.."));
    }

    private Uri getimageforfile(Bitmap bitmap) {
        File file = new File(getCacheDir(),"images");
        Uri uri = null;
        try{
            file.mkdirs();
            File file1 = new File(file,"meme.png");
            FileOutputStream fileOutputStream = new FileOutputStream(file1);
            bitmap.compress(Bitmap.CompressFormat.PNG,90,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            uri = FileProvider.getUriForFile(this,"com.example.practice",file1);
        } catch (Exception e) {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return uri;
    }

    public void loadmeme() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://meme-api.herokuapp.com/gimme";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    String url1 = null;
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            url1 = response.getString("url");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ImageView imageView;
                        imageView = MainActivity.this.findViewById(R.id.imageView);
                        Glide.with(MainActivity.this).load(url1).into(imageView);
                    }
                }, error -> {
                });
        queue.add(jsonObjectRequest);
    }

    public void nextmeme(View view) {
        loadmeme();
    }

    public void sharememe(View view) {
    }
}