package com.example.mymovieapp.data;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovieapp.R;

import java.util.ArrayList;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.ViewHolder> {

    private static final String TAG = "MovieReviewAdapter";

    private ArrayList<MovieReview> mMovieReviews;

    @NonNull
    @Override
    public MovieReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        //inflate the view
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.review_item_view, parent, false);
        //Parsing the view to the view holder
        return new MovieReviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieReviewAdapter.ViewHolder holder, int position) {
        final MovieReview movieReview = mMovieReviews.get(position);

        holder.bind(movieReview);

    }

    @Override
    public int getItemCount() {
        if (mMovieReviews == null) {
            return 0;
        } else {
            return mMovieReviews.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView reviewAuthorTv;
        final TextView reviewTv;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            //getting reference to the image view in the item layout
            reviewAuthorTv = itemView.findViewById(R.id.reviewer);
            reviewTv = itemView.findViewById(R.id.review_body);
        }

        private void bind(MovieReview movieReview) {
            String author = movieReview.getAuthor();
            String review = movieReview.getReviewContent();

            Log.v("MOVIE REVIEW AUTHOR IS ", author);
            reviewAuthorTv.setText(author);
            reviewTv.setText(review);

        }
    }

    public void setMovieTrailerData(ArrayList<MovieReview> movieReviews) {
        mMovieReviews = movieReviews;
        notifyDataSetChanged();
    }
}
