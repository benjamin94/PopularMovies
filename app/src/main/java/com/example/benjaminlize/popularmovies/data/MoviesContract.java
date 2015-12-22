package com.example.benjaminlize.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by benjamin.lize on 02/11/2015.
 */
public class MoviesContract {

    public static final String BASE_PASS_URI = "http://api.themoviedb.org/3/discover/movie?";
    public static final String BASE_PASS_URI_DESCRIPTION = "http://api.themoviedb.org/3/movie?";
    public static final String SORT_POPULARITY = "popularity.desc";
    public static final String SORT_RATING = "vote_average.desc";

    public static final String API__KEY_THEMOVIEDB = "8db5fae4b2741eb16ec711f91dd555ec";

    public static final String CONTENT_AUTHORITY = "com.example.benjaminlize.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String favouritesUriSelection = "favourites";

    public static final String PATH_MOVIES = "movies";
    public static String updateFlagUriMark = "updateFlag";

    public static final class MoviesEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_ISADULT            = "adult";
        public static final String COLUMN_IMAGE_PATH         = "image_path";
        public static final String COLUMN_MOVIE_ID_FROM_API  = "movie_id_from_API";
        public static final String COLUMN_ORIGINAL_LANGUAGE  = "original_Language";
        public static final String COLUMN_ORIGINAL_TITLE     = "original_Title";
        public static final String COLUMN_OVERVIEW           = "overview";
        public static final String COLUMN_RELEASE_DATE       = "release_date";
        public static final String COLUMN_POSTER_PATH        = "poster_path";
        public static final String COLUMN_POPULARITY         = "popularity";
        public static final String COLUMN_TITLE              = "title";
        public static final String COLUMN_VIDEO              = "video";
        public static final String COLUMN_VOTE_AVERAGE       = "vote_average";
        public static final String COLUMN_VOTE_COUNT         = "vote_count";
        public static final String COLUMN_IS_FAVOURITE       = "is_favourite";
        public static final String COLUMN_WAS_UPDATED        = "was_updated";


        public static Uri buildMoviesUriWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

        public static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }


    }

    public static final class MoviesDetail implements BaseColumns{

        public static final String COLUMN__ID    = "id"  ;
        public static final String COLUMN__KEY   = "key" ;
        public static final String COLUMN__NAME  = "name";
        public static final String COLUMN__SITE  = "site";
        public static final String COLUMN__TYPE  = "type";


    }

}
