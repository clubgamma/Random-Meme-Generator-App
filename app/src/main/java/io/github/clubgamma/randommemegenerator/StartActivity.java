package io.github.clubgamma.randommemegenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getSupportActionBar().hide();
    }
    public void openActivity(android.view.View v){
        Toast.makeText(this, "Opening Meme Page. Enjoy!!", Toast.LENGTH_SHORT).show();
        Intent intent= new Intent( this, MainActivity.class);
        startActivity(intent);

    }
}