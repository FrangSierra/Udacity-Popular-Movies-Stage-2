package frangsierra.popularmoviesudacity.data;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Class which contains the values for sorting.
 */
public final class MovieSorting {
   public static final String DEFAULT_FILTER = "popular";
   public static final String SORT_BY_POPULAR = "popular";
   public static final String SORT_BY_TOP_RATED = "top_rated";
   public static final String SORT_BY_FAVORITE = "Favorite movies";

   /**
    * Custom annotation used to prevent set wrong string values as sorting values.
    */
   @StringDef({DEFAULT_FILTER, SORT_BY_POPULAR, SORT_BY_TOP_RATED, SORT_BY_FAVORITE})
   @Retention(RetentionPolicy.SOURCE)
   public @interface MovieSortingValue {

   }
}
