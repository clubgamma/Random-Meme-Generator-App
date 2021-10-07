package io.github.clubgamma.randommemegenerator;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import io.github.clubgamma.randommemegenerator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;
    String memeUrl = null,memeTitle=null;
    String url = "https://meme-api.herokuapp.com/gimme";
    RequestQueue queue;
    public static final int PERMISSION_WRITE = 0;

    // Declaring statements for the share functionality
    BitmapDrawable drawable;
    Bitmap bitmap;
    ImageView img;
    Button shareBtn;
    Button nextMeme;
    ProgressBar proBar;
    FloatingActionButton downloadBtn;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        getSupportActionBar().hide();
        getMemeImage();

        // Define ShareButton and image object here
        shareBtn = findViewById(R.id.share);
        img = findViewById(R.id.imageView);
        nextMeme = findViewById(R.id.next);
        downloadBtn = findViewById(R.id.downloadBtn);
        progressDialog = new ProgressDialog(this);

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
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission()){
                    new Downloading().execute(memeUrl);
                }
            }
        });

        activityMainBinding.back.setOnClickListener(v -> {
            onBackPressed();
        });


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
                    memeTitle = response.getString("title");
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
    //runtime storage permission
    public boolean checkPermission() {
        int READ_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if((READ_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_WRITE);
            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==PERMISSION_WRITE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //After click on accept download image
            Toast.makeText(this, "Permission Accepted", Toast.LENGTH_SHORT).show();
            new Downloading().execute(memeUrl);
        }
    }


    public class Downloading extends AsyncTask<String, Integer, String> {

        @Override
        public void onPreExecute() {
            super .onPreExecute();
            //show progressDialog untill image download
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected String doInBackground(String... url) {
            //create DownloadManager instances
            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(url[0]);
            //create request from DownloadManager for download
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
                //set request for downloading
                request.setAllowedNetworkTypes(
                        DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false)
                        .setTitle("Downloading")
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, memeTitle + ".jpg");
         manager.enqueue(request);
            return Environment.DIRECTORY_PICTURES + memeTitle + ".jpg";
        }

        @Override
        public void onPostExecute(String s) {
            super .onPostExecute(s);
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Image Saved", Toast.LENGTH_SHORT).show();
        }
    }
}