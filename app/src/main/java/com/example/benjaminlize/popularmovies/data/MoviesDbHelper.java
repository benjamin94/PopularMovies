package com.example.benjaminlize.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.benjaminlize.popularmovies.data.MoviesContract.*;

/**
 * Created by benjamin.lize on 03/11/2015.
 */
public class MoviesDbHelper extends SQLiteOpenHelper{

    private static final String LOG_TAG = MoviesDbHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 3;

    static final String DATABASE_NAME = "movies.db";

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MoviesEntry.TABLE_NAME + " (" +

                MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                MoviesEntry.COLUMN_ISADULT            + " TEXT NOT NULL," +
                MoviesEntry.COLUMN_VIDEO              + " TEXT NOT NULL," +
                MoviesEntry.COLUMN_IMAGE_PATH         + " TEXT NOT NULL," +
                MoviesEntry.COLUMN_ORIGINAL_LANGUAGE  + " TEXT NOT NULL," +
                MoviesEntry.COLUMN_OVERVIEW           + " TEXT NOT NULL," +
                MoviesEntry.COLUMN_POSTER_PATH        + " TEXT NOT NULL," +

                MoviesEntry.COLUMN_VOTE_COUNT         + " INTEGER NOT NULL," +
                MoviesEntry.COLUMN_MOVIE_ID_FROM_API  + " INTEGER NOT NULL," +
                MoviesEntry.COLUMN_RELEASE_DATE       + " INTEGER NOT NULL," +
                MoviesEntry.COLUMN_IS_FAVOURITE       + " INTEGER NOT NULL," +
                MoviesEntry.COLUMN_WAS_UPDATED        + " INTEGER NOT NULL," +

                MoviesEntry.COLUMN_ORIGINAL_TITLE     + " TEXT NOT NULL," +
                MoviesEntry.COLUMN_TITLE              + " TEXT NOT NULL," +
                MoviesEntry.COLUMN_POPULARITY         + " REAL NOT NULL," +
                MoviesEntry.COLUMN_VOTE_AVERAGE       + " REAL NOT NULL," +

                " UNIQUE (" + MoviesEntry.COLUMN_MOVIE_ID_FROM_API + ") ON CONFLICT IGNORE);"
                ;

        db.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MoviesEntry.TABLE_NAME);
        onCreate(db);
    }
}
