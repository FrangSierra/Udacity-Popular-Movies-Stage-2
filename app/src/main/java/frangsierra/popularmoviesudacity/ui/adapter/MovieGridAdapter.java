package frangsierra.popularmoviesudacity.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import frangsierra.popularmoviesudacity.R;
import frangsierra.popularmoviesudacity.data.model.Movie;

/**
 * Custom {@link android.support.v7.widget.RecyclerView.Adapter} which works together with a list of
 * {@link Movie} objects and a {@link MovieViewHolder}.
 */
public class MovieGridAdapter extends RecyclerView.Adapter<MovieViewHolder> {
   private final List<Movie> movieList = new ArrayList<>();
   private MovieAdapterListener onMovieClickListener;
   private Context mContext;

   public MovieGridAdapter(MovieAdapterListener onMovieClick, Context context) {
      this.onMovieClickListener = onMovieClick;
      mContext = context;
   }

   @Override public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_view_item, null);
      return new MovieViewHolder(layoutView);
   }

   @Override public void onBindViewHolder(MovieViewHolder holder, int position) {
      final Movie movie = movieList.get(position);
      holder.setOnClickListener(onMovieClickListener, position);
      holder.setImageThumbnail(movie.getPosterPath(), mContext);
   }

   @Override public int getItemCount() {
      return movieList.size();
   }

   /**
    * Clear the adapter.
    */
   public void clear() {
      int size = getItemCount();
      movieList.clear();
      notifyItemRangeRemoved(0, size);
   }

   /**
    * Return the movie in the list situated in the given position.
    */
   public Movie getMovieFromPosition(int position) {
      return movieList.get(position);
   }

   public List<Movie> getList() {
      return movieList;
   }

   /**
    * Go through the movies on the list to update one of them with his new favored value.
    */
   public void updateMovieAsFavored(Long movieId, boolean fav) {
      for (int i = 0; i < movieList.size(); i++) {
         final Movie movie = movieList.get(i);
         if (movie.getId() != movieId) continue;
         movie.setFavMovie(fav);
         notifyItemChanged(i);
         break;
      }
   }


   /**
    * Add a list of {@link Movie movies} to the current adapter list.
    */
   public void addMovies(List<Movie> movies) {
      movieList.addAll(movies);
      notifyDataSetChanged();
   }

   /**
    * Custom interface used to implement {@link OnClickListener} method for each movie in {@link MovieViewHolder}.
    * It is implemented by {@link frangsierra.popularmoviesudacity.movies.BrowserActivity}
    */
   public interface MovieAdapterListener {
      /**
       * Method dispatched when the user click's in a movie. Retrieving the current position in the {@link MovieGridAdapter}.
       */
      void onMovieClick(int position);
   }
}
