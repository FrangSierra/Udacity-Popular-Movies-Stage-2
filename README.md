# Popular Movies Phase 2 

Second project for the Udacity Android Developer Fast Track supported by Google. The project meet the requested specifications and it's implemented using CLEAN architecture together with high level libraries as Dagger and RxJava2.

## PROJECT SPECIFICATION

### User Interface - Layout
-UI contains an element (e.g., a spinner or settings menu) to toggle the sort order of the movies by: most popular, highest rated.

-Movies are displayed in the main layout via a grid of their corresponding movie poster thumbnails.

-UI contains a screen for displaying the details for a selected movie.

-Movie Details layout contains title, release date, movie poster, vote average, and plot synopsis.

-Movie Details layout contains a section for displaying trailer videos and user reviews.

### User Interface - Function
-When a user changes the sort criteria (most popular, highest rated, and favorites) the main view gets updated correctly.

-When a movie poster thumbnail is selected, the movie details screen is launched.

-When a trailer is selected, app uses an Intent to launch the trailer.

-In the movies detail screen, a user can tap a button(for example, a star) to mark it as a Favorite.

### Network API Implementation
-In a background thread, app queries the `/movie/popular` or `/movie/top_rated` API for the sort criteria specified in the settings menu.

-App requests for related videos for a selected movie via the `/movie/{id}/videos` endpoint in a background thread and displays those details when the user selects a movie.

-App requests for user reviews for a selected movie via the `/movie/{id}/reviews` endpoint in a background thread and displays those details when the user selects a movie.

### Data Persistence
-The titles and ids of the user's favorite movies are stored in a `ContentProvider` backed by a SQLite database. This `ContentProvider` is updated whenever the user favorites or unfavorites a movie.

-When the "favorites" setting option is selected, the main view displays the entire favorites collection based on movie ids stored in the `ContentProvider`.

###General Project Guidelines
-App conforms to common standards found in the [Android Nanodegree General Project Guidelines](http://udacity.github.io/android-nanodegree-guidelines/core.html).
