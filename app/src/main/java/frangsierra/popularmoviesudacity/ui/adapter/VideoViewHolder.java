package frangsierra.popularmoviesudacity.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import frangsierra.popularmoviesudacity.R;
import frangsierra.popularmoviesudacity.data.model.Video;

/**
 * Custom {@link RecyclerView.ViewHolder} used for {@link VideoAdapter} to create a view for every
 * {@link Video} from the adapter.
 */
public class VideoViewHolder extends RecyclerView.ViewHolder {
   private final View view;
   private TextView videoTrailerText;

   VideoViewHolder(View itemView) {
      super(itemView);
      view = itemView;
      videoTrailerText = (TextView) view.findViewById(R.id.video_name);
   }

   /**
    * Set the current {@link #videoTrailerText} with a given text.
    */
   void setTrailerText(String text) {
      videoTrailerText.setText(text);
   }

   /**
    * Set a {@link View.OnClickListener} to the current view linked to a specific item
    * position in the {@link VideoAdapter}.
    */
   void setOnClickListener(VideoAdapter.VideoAdapterListener mVideoAdapterListener, final int position) {
      view.setOnClickListener(v -> mVideoAdapterListener.onVideoClick(position));
   }
}