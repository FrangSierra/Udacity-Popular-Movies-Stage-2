package frangsierra.popularmoviesudacity.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import frangsierra.popularmoviesudacity.R;
import frangsierra.popularmoviesudacity.data.model.Review;

/**
 * Custom {@link RecyclerView.ViewHolder} used for {@link ReviewAdapter} to create a view for every
 * {@link Review} from the adapter.
 */
public class ReviewViewHolder extends RecyclerView.ViewHolder {
   private final View view;
   private TextView authorText;
   private TextView reviewText;

   ReviewViewHolder(View itemView) {
      super(itemView);
      view = itemView;
      authorText = (TextView) view.findViewById(R.id.review_author);
      reviewText = (TextView) view.findViewById(R.id.review_content);
   }

   /**
    * Set the current {@link #authorText} with a given text.
    */
   void setAuthorName(String text) {
      authorText.setText(text);
   }

   /**
    * Set the current {@link #reviewText} with a given text.
    */
   void setReview(String text) {
      reviewText.setText(text);
   }

   /**
    * Set a {@link View.OnClickListener} to the current view linked to a specific item
    * position in the {@link MovieGridAdapter}.
    */
   void setOnClickListener(final MovieGridAdapter.MovieAdapterListener mOnMovieClickListener,
                           final int position) {
      view.setOnClickListener(v -> mOnMovieClickListener.onMovieClick(position));
   }
}