package io.github.clubgamma.randommemegenerator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import java.io.File;
import java.io.FileOutputStream;

import android.util.Log;
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
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import io.github.clubgamma.randommemegenerator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;
    String memeUrl = null,memeTitle=null;
    String url = "https://meme-api.herokuapp.com/gimme";
    RequestQueue queue;
    public static final int PERMISSION_WRITE = 0;

    SharedPreferences sharedPreferences ;
    // Declaring statements for the share functionality
    BitmapDrawable drawable;
    Bitmap bitmap;
    ImageView img;
    FloatingActionButton shareBtn;
//    Button nextMeme;
    ProgressBar proBar;
    FloatingActionButton downloadBtn,saveBtn,moreBtn;
    ProgressDialog progressDialog;

    @SuppressLint("ClickableViewAccessibility")
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
//        nextMeme = findViewById(R.id.next);
        downloadBtn = findViewById(R.id.downloadBtn);
        saveBtn=findViewById(R.id.saveBtn);
        moreBtn=findViewById(R.id.moreBtn);
        progressDialog = new ProgressDialog(this);

        sharedPreferences = getSharedPreferences("USERS",Context.MODE_PRIVATE);

        // Define ActionBar object
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        if(!checkInternetPermission()){
            Toast.makeText(this, R.string.On_internet, Toast.LENGTH_SHORT).show();
        }
        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#5c5cd6"));

        // Set BackgroundDrawable
        ((ActionBar) actionBar).setBackgroundDrawable(colorDrawable);

        //if user is logged in then make visible more button
        if(sharedPreferences.contains("username")) {
            moreBtn.setVisibility(View.VISIBLE);
        }else {
            moreBtn.setVisibility(View.GONE);
        }

        shareBtn.setOnClickListener(v -> shareImage());

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission() ){
                    if(checkInternetPermission()){
                        new Downloading().execute(memeUrl);
                    }else {
                        Toast.makeText(MainActivity.this, R.string.On_internet, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharedPreferences.contains("username")){
                    //if user is logged in then it will save image
                    saveImageToDatabase(memeUrl,sharedPreferences.getString("username",""));
                }else {
                    //if user is not logged in then it will ask for username
                    openDialogBox();
                }

            }

        });
  // Implemented Swipe listener here --

        // Implemented Swipe listener here --
        img.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this){


            public void onSwipeLeft(){

//                Toast.makeText(MainActivity.this, "Swiped", Toast.LENGTH_SHORT).show();
                getMemeImage();
            }


        });

        activityMainBinding.back.setOnClickListener(v -> {
            onBackPressed();
        });

        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(MainActivity.this,v);
                popupMenu.getMenuInflater().inflate(R.menu.more,popupMenu.getMenu());
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.savedMemes:
                                Intent intent=new Intent(MainActivity.this,SavedMemes.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                Toast.makeText(MainActivity.this, "saveBtn", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.logout:
                                sharedPreferences = getSharedPreferences("USERS", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.apply();
                                finish();
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                break;
                            default:
                                throw new IllegalStateException("Unexpected value: " + item.getItemId());
                        }
                        return true;
                    }
                });
            }
        });


    }

    // Method responsible for getting image from the imageView and sharing it.
    private void shareImage() {
//        if(checkInternetPermission()) {

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


    }

    public void getMemeImage() {
        if(checkInternetPermission()) {
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
        }else {
            Toast.makeText(this, R.string.On_internet, Toast.LENGTH_SHORT).show();
        }

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

    public boolean checkInternetPermission(){
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }else {
            return false;
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
    public interface checkUsernameCallback{
        Boolean onCallback(Boolean status);
    }
    public void checkUsernameInDatabase(String uname,checkUsernameCallback callback){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users");
        Query query=reference.orderByChild("username").equalTo(uname);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    callback.onCallback(true);
                }else {
                    callback.onCallback(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("database error",error.getMessage());
            }
        });
    }

    public void openDialogBox(){
        // Create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Login");

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.custom_dialog_login, null);
        builder.setView(customLayout);

        // add a button
        builder.setPositiveButton( "Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // send data from the
                // AlertDialog to the Activity
                EditText username = customLayout.findViewById(R.id.username);
                if(!username.getText().toString().isEmpty()){
                    final boolean[] checkUser = {false};

                    checkUsernameInDatabase(username.getText().toString(), new checkUsernameCallback() {
                        @Override
                        public Boolean onCallback(Boolean status) {
                            checkUser[0] =status;
                            if(status)
                            {   //if user exists then login user and save meme
                                saveImageToDatabase(memeUrl,username.getText().toString());
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.putString("username",username.getText().toString());
                                editor.commit();
                            }else {
                                //if user is new then add new user and save its meme
                                saveUserToDatabase(username.getText().toString());
                                Toast.makeText(MainActivity.this, username.getText().toString(), Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.putString("username",username.getText().toString());
                                editor.commit();

                            }
                            moreBtn.setVisibility(View.VISIBLE);
                            return status;
                        }
                    });
                }else {
                    Toast.makeText(MainActivity.this, "Please enter field", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // create and show
        // the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void saveImageToDatabase(String memeUrl,String uname) {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("SavedMemes");
        reference.child(uname).child(memeTitle).setValue(memeUrl);

        Toast.makeText(this, "Meme saved successfull", Toast.LENGTH_SHORT).show();
    }

    private void saveUserToDatabase(String uname) {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users");
        reference.child(uname).child("username").setValue(uname);
        saveImageToDatabase(memeUrl,uname);
        Toast.makeText(MainActivity.this, "Register Seccesfull", Toast.LENGTH_SHORT).show();

    }
}
