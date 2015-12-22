package com.example.benjaminlize.popularmovies.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

/**
 * Created by benjamin.lize on 04/11/2015.
 */
public class TestDb extends AndroidTestCase{

    public void testSetDbWasUpdatedToZero() {

        MoviesDbHelper dbHelper = new MoviesDbHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("UPDATE movies SET was_updated = 0;");

    }

    public void testMoviesTable() {

        // First step: Get reference to writable database
        MoviesDbHelper dbHelper;
        SQLiteDatabase db;
        Cursor dbCursor;

        int INDEX_ISADULT                  ;
        int INDEX_IMAGE_PATH               ;
        int INDEX_MOVIE_ID_FROM_API        ;
        int INDEX_ORIGINAL_LANGUAGE        ;
        int INDEX_ORIGINAL_TITLE           ;
        int INDEX_OVERVIEW                 ;
        int INDEX_RELEASE_DATE             ;
        int INDEX_POSTER_PATH              ;
        int INDEX_POPULARITY               ;
        int INDEX_TITLE                    ;
        int INDEX_VIDEO                    ;
        int INDEX_VOTE_AVERAGE             ;
        int INDEX_VOTE_COUNT               ;
        int INDEX_IS_FAVOURITE             ;
        int INDEX_WAS_UPDATED              ;

        String  RETURNED_ISADULT           ;
        String  RETURNED_IMAGE_PATH        ;
        int     RETURNED_MOVIE_ID_FROM_API ;
        String  RETURNED_ORIGINAL_LANGUAGE ;
        String  RETURNED_ORIGINAL_TITLE    ;
        String  RETURNED_OVERVIEW          ;
        String  RETURNED_RELEASE_DATE      ;
        String  RETURNED_POSTER_PATH       ;
        float   RETURNED_POPULARITY        ;
        String  RETURNED_TITLE             ;
        String  RETURNED_VIDEO             ;
        float   RETURNED_VOTE_AVERAGE      ;
        int     RETURNED_VOTE_COUNT        ;
        int     RETURNED_IS_FAVOURITE      ;
        int     RETURNED_WAS_UPDATED       ;

        long locationRowId;

        ContentValues testValues;

        dbHelper = new MoviesDbHelper(getContext());
        db = dbHelper.getWritableDatabase();


        // Create ContentValues of what you want to insert
        // (you can use the createNorthPoleLocationValues if you wish)
        testValues = TestUtilities.createMovieValues();

        // Insert ContentValues into database and get a row ID back
        locationRowId = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null , testValues);

        assertTrue("Error: Failure to insert Values", locationRowId != -1);

        // Query the database and receive a Cursor back

        dbCursor = db.query(MoviesContract.MoviesEntry.TABLE_NAME, null, null, null, null, null, null);

        // Move the cursor to a valid database row
        dbCursor.moveToFirst();

        // Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)

        INDEX_ISADULT              = dbCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_ISADULT          );
        INDEX_IMAGE_PATH           = dbCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_IMAGE_PATH       );
        INDEX_MOVIE_ID_FROM_API    = dbCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID_FROM_API);
        INDEX_ORIGINAL_LANGUAGE    = dbCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_LANGUAGE);
        INDEX_ORIGINAL_TITLE       = dbCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE   );
        INDEX_OVERVIEW             = dbCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_OVERVIEW         );
        INDEX_RELEASE_DATE         = dbCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE     );
        INDEX_POSTER_PATH          = dbCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH      );
        INDEX_POPULARITY           = dbCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_POPULARITY       );
        INDEX_TITLE                = dbCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_TITLE            );
        INDEX_VIDEO                = dbCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_VIDEO            );
        INDEX_VOTE_AVERAGE         = dbCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE     );
        INDEX_VOTE_COUNT           = dbCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_VOTE_COUNT       );
        INDEX_IS_FAVOURITE         = dbCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_IS_FAVOURITE     );
        INDEX_WAS_UPDATED          = dbCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_IS_FAVOURITE     );

        RETURNED_ISADULT           = dbCursor.getString(INDEX_ISADULT);
        RETURNED_IMAGE_PATH        = dbCursor.getString(INDEX_IMAGE_PATH);
        RETURNED_MOVIE_ID_FROM_API = dbCursor.getInt(INDEX_MOVIE_ID_FROM_API);
        RETURNED_ORIGINAL_LANGUAGE = dbCursor.getString(INDEX_ORIGINAL_LANGUAGE);
        RETURNED_ORIGINAL_TITLE    = dbCursor.getString(INDEX_ORIGINAL_TITLE);
        RETURNED_OVERVIEW          = dbCursor.getString(INDEX_OVERVIEW);
        RETURNED_RELEASE_DATE      = dbCursor.getString(INDEX_RELEASE_DATE);
        RETURNED_POSTER_PATH       = dbCursor.getString(INDEX_POSTER_PATH);
        RETURNED_POPULARITY        = dbCursor.getFloat(INDEX_POPULARITY);
        RETURNED_TITLE             = dbCursor.getString(INDEX_TITLE);
        RETURNED_VIDEO             = dbCursor.getString(INDEX_VIDEO);
        RETURNED_VOTE_AVERAGE      = dbCursor.getFloat(INDEX_VOTE_AVERAGE);
        RETURNED_VOTE_COUNT        = dbCursor.getInt(INDEX_VOTE_COUNT);
        RETURNED_IS_FAVOURITE      = dbCursor.getInt(INDEX_IS_FAVOURITE);

        assertTrue("ISADULT           DONT MATCH", RETURNED_ISADULT           .equals(TestUtilities.VALUE_ISADULT          ));
        assertTrue("IMAGE_PATH        DONT MATCH", RETURNED_IMAGE_PATH        .equals(TestUtilities.VALUE_IMAGE_PATH));
        assertTrue("MOVIE_ID_FROM_API DONT MATCH", RETURNED_MOVIE_ID_FROM_API == TestUtilities.     VALUE_MOVIE_ID_FROM_API);
        assertTrue("ORIGINAL_LANGUAGE DONT MATCH", RETURNED_ORIGINAL_LANGUAGE .equals(TestUtilities.VALUE_ORIGINAL_LANGUAGE));
        assertTrue("ORIGINAL_TITLE    DONT MATCH", RETURNED_ORIGINAL_TITLE    .equals(TestUtilities.VALUE_ORIGINAL_TITLE));
        assertTrue("OVERVIEW          DONT MATCH", RETURNED_OVERVIEW          .equals(TestUtilities.VALUE_OVERVIEW));
        assertTrue("RELEASE_DATE      DONT MATCH", RETURNED_RELEASE_DATE      .equals(TestUtilities.VALUE_RELEASE_DATE));
        assertTrue("POSTER_PATH       DONT MATCH", RETURNED_POSTER_PATH       .equals(TestUtilities.VALUE_POSTER_PATH));
        assertTrue("POPULARITY        DONT MATCH", RETURNED_POPULARITY        == (TestUtilities.    VALUE_POPULARITY       ));
        assertTrue("TITLE             DONT MATCH", RETURNED_TITLE             .equals(TestUtilities.VALUE_TITLE            ));
        assertTrue("VIDEO             DONT MATCH", RETURNED_VIDEO             .equals(TestUtilities.VALUE_VIDEO            ));
        assertTrue("VOTE_AVERAGE      DONT MATCH", RETURNED_VOTE_AVERAGE      == (TestUtilities.    VALUE_VOTE_AVERAGE     ));
        assertTrue("VOTE_COUNT        DONT MATCH", RETURNED_VOTE_COUNT        == (TestUtilities.    VALUE_VOTE_COUNT       ));
        assertTrue("IS_FAVOURITE      DONT MATCH", RETURNED_IS_FAVOURITE      == (TestUtilities.    VALUE_IS_FAVOURITE       ));

        // Finally, close the cursor and database
        dbCursor.close();
        db.close();
    }



}
