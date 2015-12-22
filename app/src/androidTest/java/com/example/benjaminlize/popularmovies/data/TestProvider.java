package com.example.benjaminlize.popularmovies.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

/**
 * Created by benjamin.lize on 09/11/2015.
 */
public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    Cursor dbCursor;

    public void testBasicMovieQuery(){

        long locationRowId = TestUtilities.insertNorthPoleLocationValues(mContext);

            Cursor cursor = mContext.getContentResolver().query(
                    MoviesContract.MoviesEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );

        cursor.moveToPosition((int)locationRowId);

        int INDEX_COLUMN_ISADULT           = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_ISADULT           );
        int INDEX_COLUMN_IMAGE_PATH        = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_IMAGE_PATH        );
        int INDEX_COLUMN_MOVIE_ID_FROM_API = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID_FROM_API );
        int INDEX_COLUMN_ORIGINAL_LANGUAGE = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_LANGUAGE );
        int INDEX_COLUMN_ORIGINAL_TITLE    = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE    );
        int INDEX_COLUMN_OVERVIEW          = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_OVERVIEW          );
        int INDEX_COLUMN_RELEASE_DATE      = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE      );
        int INDEX_COLUMN_POSTER_PATH       = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH       );
        int INDEX_COLUMN_POPULARITY        = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_POPULARITY        );
        int INDEX_COLUMN_TITLE             = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_TITLE             );
        int INDEX_COLUMN_VIDEO             = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_VIDEO             );
        int INDEX_COLUMN_VOTE_AVERAGE      = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE      );
        int INDEX_COLUMN_VOTE_COUNT        = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_VOTE_COUNT        );


        String  RETURNED_ISADULT           = cursor.getString(INDEX_COLUMN_ISADULT);
        String  RETURNED_IMAGE_PATH        = cursor.getString(INDEX_COLUMN_IMAGE_PATH);
        int     RETURNED_MOVIE_ID_FROM_API = cursor.getInt(INDEX_COLUMN_MOVIE_ID_FROM_API);
        String  RETURNED_ORIGINAL_LANGUAGE = cursor.getString(INDEX_COLUMN_ORIGINAL_LANGUAGE);
        String  RETURNED_ORIGINAL_TITLE    = cursor.getString(INDEX_COLUMN_ORIGINAL_TITLE    );
        String  RETURNED_OVERVIEW          = cursor.getString(INDEX_COLUMN_OVERVIEW          );
        String  RETURNED_RELEASE_DATE      = cursor.getString(INDEX_COLUMN_RELEASE_DATE      );
        String  RETURNED_POSTER_PATH       = cursor.getString(INDEX_COLUMN_POSTER_PATH       );
        float   RETURNED_POPULARITY        = cursor.getFloat(INDEX_COLUMN_POPULARITY);
        String  RETURNED_TITLE             = cursor.getString(INDEX_COLUMN_TITLE);
        String  RETURNED_VIDEO             = cursor.getString(INDEX_COLUMN_VIDEO             );
        float   RETURNED_VOTE_AVERAGE      = cursor.getFloat(INDEX_COLUMN_VOTE_AVERAGE);
        int     RETURNED_VOTE_COUNT        = cursor.getInt(INDEX_COLUMN_VOTE_COUNT);

    }




}
