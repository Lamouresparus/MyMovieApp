package com.example.mymovieapp.data;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovieapp.R;

import java.util.ArrayList;

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.ViewHolder> {

    private static final String TAG = "MovieTrailerAdapter";

    private ArrayList<MovieTrailer> mMovieTrailers;
    private Context mContext;
    public MovieTrailerAdapter(Context context){
        mContext =context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        //inflate the view
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.trailer_item_view, parent, false);
        //Parsing the view to the view holder
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieTrailerAdapter.ViewHolder holder, int position) {

        final MovieTrailer movieTrailer = mMovieTrailers.get(position);

        holder.bind(movieTrailer);

        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri trailerUri = Uri.parse(movieTrailer.getTrailerStringUrl());
                Log.v(TAG, "Trailer Uri for youtube is "+ trailerUri);
                Intent intent = new Intent(Intent.ACTION_VIEW, trailerUri);
                if(intent.resolveActivity(mContext.getPackageManager()) != null){
                    mContext.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mMovieTrailers == null) {
            return 0;
        } else {
            return mMovieTrailers.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            //getting reference to the image view in the item layout
            mTextView = itemView.findViewById(R.id.movie_trailer_name_tv);
        }

        private void bind(MovieTrailer movieTrailer) {
            String moveTrailerName = movieTrailer.getTrailerName();

            Log.v("MOVIE TRAILER NAME IS ", moveTrailerName);
            mTextView.setText(moveTrailerName);

        }
    }

    public void setMovieTrailerData(ArrayList<MovieTrailer> movieTrailers) {
        mMovieTrailers = movieTrailers;
        notifyDataSetChanged();
    }
}

