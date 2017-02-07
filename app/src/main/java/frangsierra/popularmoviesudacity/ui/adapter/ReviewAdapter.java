package frangsierra.popularmoviesudacity.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import frangsierra.popularmoviesudacity.R;
import frangsierra.popularmoviesudacity.data.model.Review;

/**
 * Adapter class used to work together with {@link RecyclerView} and {@link Review}.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder> {
   private ArrayList<Review> reviewsList;

   public ReviewAdapter(ArrayList<Review> reviews) {
      this.reviewsList = reviews;
   }

   @Override public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, null);
      return new ReviewViewHolder(layoutView);
   }

   @Override public void onBindViewHolder(ReviewViewHolder holder, int position) {
      final Review review = reviewsList.get(position);
      holder.setAuthorName(review.getAuthor());
      holder.setReview(review.getContent());
   }

   @Override public int getItemCount() {
      return reviewsList.size();
   }
}
