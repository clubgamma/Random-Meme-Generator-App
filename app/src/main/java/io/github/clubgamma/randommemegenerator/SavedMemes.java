package io.github.clubgamma.randommemegenerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.github.clubgamma.randommemegenerator.databinding.ActivitySavedMemesBinding;

public class SavedMemes extends AppCompatActivity {

    ActivitySavedMemesBinding activitySavedMemesBinding;
    private RecyclerView recyclerView;
    SavedMemesAdapter myAdapter;

    SharedPreferences sharedPreferences;
    FloatingActionButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_memes);
        activitySavedMemesBinding = ActivitySavedMemesBinding.inflate(getLayoutInflater());
        setContentView(activitySavedMemesBinding.getRoot());
        getSupportActionBar().hide();
//        // Define ActionBar object
//        ActionBar actionBar;
//        actionBar = getSupportActionBar();
//        // Define ColorDrawable object and parse color
//        // using parseColor method
//        // with color hash code as its parameter
//        ColorDrawable colorDrawable
//                = new ColorDrawable(Color.parseColor("#5c5cd6"));
//
//        // Set BackgroundDrawable
//        ((ActionBar) actionBar).setBackgroundDrawable(colorDrawable);


        back=findViewById(R.id.back);
        recyclerView = findViewById(R.id.savedMemesRecyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView

        sharedPreferences=getSharedPreferences("USERS",MODE_PRIVATE);
        if(sharedPreferences.contains("username")){
            String userName=sharedPreferences.getString("username","");

            getList(userName,new MyCallback() {
                @Override
                public void onCallback(ArrayList<SavedMemesModel> memesArrayList) {
                    myAdapter = new SavedMemesAdapter(memesArrayList, getApplicationContext());
                    recyclerView.setAdapter(myAdapter);
                }
            });
        }else {
            Toast.makeText(this, "Please Login ", Toast.LENGTH_SHORT).show();
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

    }
    private interface MyCallback{
         void onCallback(ArrayList<SavedMemesModel> memesArrayList);
    }
    private void getList(String userName,MyCallback callback){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("SavedMemes");
        Query query = reference.child(userName);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<SavedMemesModel> list = new ArrayList<SavedMemesModel>();

                if(snapshot.exists()){
                    for(DataSnapshot ds:snapshot.getChildren()){
                        list.add(new SavedMemesModel(ds.getKey(),ds.getValue().toString()));
                        Log.e("ds",ds.getValue().toString());
                    }
                    callback.onCallback(list);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}