package com.example.mymovieapp.data;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovieapp.MovieDetailActivity;
import com.example.mymovieapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    public MovieAdapter(){}

    public static final String KEY_MOVIE = "movie";

    private ArrayList<Movie> mMovie;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //get context
        Context context = parent.getContext();
        //inflate the view
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_view, parent, false);
        //Parsing the view to the view holder
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Movie movie = mMovie.get(position);

        holder.bind(movie);

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), MovieDetailActivity.class);

                intent.putExtra(KEY_MOVIE, movie);
                v.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        if (mMovie == null) {
            return 0;
        } else {
            return mMovie.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView mImageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            //getting reference to the image view in the item layout
            mImageView = itemView.findViewById(R.id.movie_image_view);
        }

        private void bind(Movie movie) {
            String imageUrl = movie.getMImageUrl();

            Log.v("IMAGE_URL", imageUrl);
            Picasso.get().load(imageUrl).into(mImageView);

        }
    }

    public void setMovieData(ArrayList<Movie> movies) {
        mMovie = movies;
        notifyDataSetChanged();
    }
}
