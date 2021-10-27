package io.github.clubgamma.randommemegenerator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SavedMemesAdapter extends RecyclerView.Adapter<SavedMemesAdapter.SavedMemeViewHolder> {


    ArrayList<SavedMemesModel> list;
    Context context;

    public SavedMemesAdapter(ArrayList<SavedMemesModel> memesArrayList, Context applicationContext) {
        list=memesArrayList;
        context=applicationContext;
    }

    @Override
    public SavedMemeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.savedmemes_layout,parent,false);
        return new SavedMemeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull  SavedMemesAdapter.SavedMemeViewHolder holder, int position) {

        SavedMemesModel orders=list.get(position);

        holder.title.setText(orders.getMemeTitle());
        Glide.with(holder.imageView.getContext()).load(orders.getMemeUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SavedMemeViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;

        public SavedMemeViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.savememeImg);
            title=(TextView)itemView.findViewById(R.id.savedmemeTitle);
        }
    }
}
