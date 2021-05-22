package com.example.newspaper;


import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

public class ImagesArrayAdapter extends RecyclerView.Adapter<ImagesArrayAdapter.ViewHolder> {

    private static ClickListenner clickListenner;


    public interface ClickListenner {
        void onItemClick(int position, View view) ;
    }


    public void setClickListenner(ClickListenner clickListenner) {
        ImagesArrayAdapter.clickListenner = clickListenner;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Foto foto = DataModel.getInstance().getListPhotoTokens().get(position);
        File file = new File(foto.getFoto());
        holder.imageView.setImageURI(Uri.fromFile(file));
    }

    @Override
    public int getItemCount() {
        return DataModel.getInstance().getListPhotoTokens().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clickListenner == null)
                        return;

                    clickListenner.onItemClick(getAdapterPosition(), v);
                }
            });
        }




    }
}
