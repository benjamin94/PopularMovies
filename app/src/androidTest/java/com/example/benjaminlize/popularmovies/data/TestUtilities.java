package com.example.benjaminlize.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

/**
 * Created by benjamin.lize on 04/11/2015.
 */
public class TestUtilities extends AndroidTestCase{

    static final String  VALUE_ISADULT           = "false";
    static final String  VALUE_IMAGE_PATH        = "/dkMD5qlogeRMiEixC4YNPUvax2T.jpg";
    static final int     VALUE_MOVIE_ID_FROM_API = 1353977;
    static final String  VALUE_ORIGINAL_LANGUAGE = "en";
    static final String  VALUE_ORIGINAL_TITLE    = "Jurassic World";
    static final String  VALUE_OVERVIEW          = "Twenty-two years after the events of Jurassic Park, Isla Nublar now feature";
    static final String  VALUE_RELEASE_DATE      = "2015-06-12";
    static final String  VALUE_POSTER_PATH       = "/jjBgi2r5cRt36xF6iNUEhzscEcb.jpg";
    static final float   VALUE_POPULARITY        = (float) 50.019738;
    static final String  VALUE_TITLE             = "Jurassic World";
    static final String  VALUE_VIDEO             = "false";
    static final float   VALUE_VOTE_AVERAGE      = (float) 6.9;
    static final int     VALUE_VOTE_COUNT        = 2847;
    static final int     VALUE_IS_FAVOURITE      = 1;



    static ContentValues createMovieValues(){

        ContentValues movieValues = new ContentValues();

        movieValues.put(MoviesContract.MoviesEntry.COLUMN_ISADULT, VALUE_ISADULT);
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_IMAGE_PATH, VALUE_IMAGE_PATH);
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID_FROM_API, VALUE_MOVIE_ID_FROM_API);
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_LANGUAGE, VALUE_ORIGINAL_LANGUAGE);
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE, VALUE_ORIGINAL_TITLE);
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_OVERVIEW, VALUE_OVERVIEW);
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE, VALUE_RELEASE_DATE);
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH, VALUE_POSTER_PATH);
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_POPULARITY, VALUE_POPULARITY);
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_TITLE, VALUE_TITLE);
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_VIDEO, VALUE_VIDEO);
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE, VALUE_VOTE_AVERAGE);
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_VOTE_COUNT, VALUE_VOTE_COUNT);
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_IS_FAVOURITE, VALUE_IS_FAVOURITE);

        return movieValues;
    }

    static long insertNorthPoleLocationValues(Context context) {
        // insert our test records into the database
        MoviesDbHelper dbHelper = new MoviesDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createMovieValues();

        long locationRowId;
        locationRowId = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert North Pole Location Values", locationRowId != -1);

        return locationRowId;
    }

}
