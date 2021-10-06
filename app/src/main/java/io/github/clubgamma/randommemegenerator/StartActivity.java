package io.github.clubgamma.randommemegenerator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {
    Button button;
    ConstraintLayout nointernet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getSupportActionBar().hide();
        nointernet=findViewById(R.id.noInternetConst);
        button=findViewById(R.id.button1);

        if(!isConnected()){
            nointernet.setVisibility(View.VISIBLE);
            button.setVisibility(View.GONE);
            Toast.makeText(this, "No Internet Access", Toast.LENGTH_SHORT).show();
        }else{
            nointernet.setVisibility(View.GONE);
            button.setVisibility(View.VISIBLE);
        }
    }
    public void openActivity(android.view.View v){
        Toast.makeText(this, "Opening Meme Page. Enjoy!!", Toast.LENGTH_SHORT).show();
        Intent intent= new Intent( this, MainActivity.class);
        startActivity(intent);

    }
    private boolean isConnected(){
        ConnectivityManager connectivityManager=(ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo()!=null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}