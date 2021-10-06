package io.github.clubgamma.randommemegenerator;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import io.github.clubgamma.randommemegenerator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;
    String memeUrl = null;
    String url = "https://meme-api.herokuapp.com/gimme";
    RequestQueue queue;

    // Declaring statements for the share functionality
    private ProgressBar proBar;
    BitmapDrawable drawable;
    Bitmap bitmap;
    ImageView img;
    Button shareBtn;
    Button nextMeme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        getMemeImage();

        // Define ShareButton and image object here
        shareBtn = findViewById(R.id.share);
        img = findViewById(R.id.imageView);
        nextMeme = findViewById(R.id.next);

        // Define ActionBar object
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#5c5cd6"));

        // Set BackgroundDrawable
        ((ActionBar) actionBar).setBackgroundDrawable(colorDrawable);

        shareBtn.setOnClickListener(v -> shareImage());
        nextMeme.setOnClickListener(v -> getMemeImage());

    }

    // Method responsible for getting image from the imageView and sharing it.
    private void shareImage() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        drawable = (BitmapDrawable) img.getDrawable();
        bitmap = drawable.getBitmap();
        File file = new File(getExternalCacheDir()+"/"+"meme"+".png");
        Intent shareIntent;
        
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, memeUrl);
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        startActivity(Intent.createChooser(shareIntent, "Share using "));
        activityMainBinding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMemeImage();
            }
        });
    }

    public void getMemeImage() {
        queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    memeUrl = response.getString("url");
                    loadImageIntoImageView(memeUrl);
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(jsonObjectRequest);
    }

    public void loadImageIntoImageView(String url) {
         proBar = findViewById(R.id.progress_bar);
        proBar.setVisibility(View.VISIBLE);

        Glide.with(MainActivity.this).load(url).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                proBar.setVisibility(View.INVISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                proBar.setVisibility(View.INVISIBLE);
                return false;
            }
        }).into(activityMainBinding.imageView);
    }
}