package frangsierra.popularmoviesudacity.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import frangsierra.popularmoviesudacity.R;
import frangsierra.popularmoviesudacity.data.model.Movie;

import static frangsierra.popularmoviesudacity.utils.MovieUtils.buildPosterUri;

/**
 * Custom {@link RecyclerView.ViewHolder} used for {@link MovieGridAdapter} to create a view for every
 * {@link Movie} from the adapter.
 */
class MovieViewHolder extends RecyclerView.ViewHolder {
   private final View view;
   private ImageView movieThumbnail;

   MovieViewHolder(View itemView) {
      super(itemView);
      view = itemView;
      movieThumbnail = (ImageView) itemView.findViewById(R.id.movie_thumbnail_image);
   }

   /**
    * Set the current {@link #movieThumbnail} with a given url using {@link Picasso}.
    */
   void setImageThumbnail(String url, final Context context) {
      Uri uri = buildPosterUri(url);
      Picasso.with(context).load(uri).into(movieThumbnail);
   }

   /**
    * Set a {@link OnClickListener} to the current view linked to a specific item
    * position in the {@link MovieGridAdapter}.
    */
   void setOnClickListener(final MovieGridAdapter.MovieAdapterListener mOnMovieClickListener,
                           final int position) {
      view.setOnClickListener(v -> mOnMovieClickListener.onMovieClick(position));
   }
}
