package frangsierra.popularmoviesudacity.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import frangsierra.popularmoviesudacity.R;
import frangsierra.popularmoviesudacity.data.model.Video;
import frangsierra.popularmoviesudacity.movies.MovieDetailActivity;

/**
 * Adapter class used to work together with {@link RecyclerView} and {@link Video}.
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoViewHolder> {
   private final ArrayList<Video> videoList;
   private VideoAdapterListener videoAdapterListener;

   public VideoAdapter(ArrayList<Video> videoList, VideoAdapterListener videoAdapterListener) {
      this.videoList = videoList;
      this.videoAdapterListener = videoAdapterListener;
   }

   @Override public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, null);
      return new VideoViewHolder(layoutView);
   }

   @Override public void onBindViewHolder(VideoViewHolder holder, int position) {
      final Video video = videoList.get(position);
      holder.setTrailerText(video.getName());
      holder.setOnClickListener(videoAdapterListener, position);
   }

   @Override public int getItemCount() {
      return videoList.size();
   }

   /**
    * Retrieve a {@link Video} item from the adapter list based on a given position.
    */
   public Video getVideoFromPosition(int position) {
      return videoList.get(position);
   }

   /**
    * Custom interface used to implement {@link View.OnClickListener} method for each movie in {@link MovieViewHolder}.
    * It is implemented by {@link MovieDetailActivity}
    */
   public interface VideoAdapterListener {
      /**
       * Method dispatched when the user click's in a video. Retrieving the current position in the {@link VideoAdapter}.
       */
      void onVideoClick(int position);
   }
}
