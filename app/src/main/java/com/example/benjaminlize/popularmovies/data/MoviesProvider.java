package com.example.benjaminlize.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.Nullable;

import static com.example.benjaminlize.popularmovies.data.MoviesContract.*;

/**
 * Created by benjamin.lize on 05/11/2015.
 */
public class MoviesProvider extends ContentProvider {

    // 1 - Determine Uri
    // 2 - Update Contract
    // 3 - Uri Matcher
    // 4 - Querry, Delete, Insert and Type functions

    private static UriMatcher sUriMatcher = buildUriMatcher();

    private MoviesDbHelper mOpenHelper;

    static final int movies = 101;
    static final int moviesWithId = 102;
    static final int favouriteMovies = 103;
    static final int updateFlag = 104;

    private static final String sMovieIdSelection =
            MoviesEntry.TABLE_NAME + "." + MoviesEntry._ID + " = ?";

    private static final String sMovieIsFavourite =
            MoviesEntry.TABLE_NAME + "." + MoviesEntry.COLUMN_IS_FAVOURITE + " = ?";

    static UriMatcher buildUriMatcher(){

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(CONTENT_AUTHORITY,"movies",movies);
        uriMatcher.addURI(CONTENT_AUTHORITY,"movies/#",moviesWithId);
        uriMatcher.addURI(CONTENT_AUTHORITY,"movies/" + MoviesContract.favouritesUriSelection
                ,favouriteMovies);
        uriMatcher.addURI(CONTENT_AUTHORITY,"movies/" + MoviesContract.updateFlagUriMark,updateFlag);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MoviesDbHelper(getContext());
        return false;
    }


    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        //TO DO: still need to add other query methods.

        Cursor mCursor;
        switch (sUriMatcher.match(uri)){

            case movies: {

                mCursor = mOpenHelper.getReadableDatabase()
                        .query(MoviesEntry.TABLE_NAME,
                                projection,
                                selection,
                                selectionArgs,
                                null,
                                null,
                                sortOrder
                        );
            } break;

            case moviesWithId: {

                String movieId = MoviesEntry.getIdFromUri(uri);
                selection = sMovieIdSelection;

                mCursor = mOpenHelper.getReadableDatabase()
                        .query(MoviesEntry.TABLE_NAME,
                                projection,
                                selection,
                                new String[]{movieId},
                                null,
                                null,
                                sortOrder
                                );
            } break;


            case favouriteMovies: {

                String movieId = MoviesEntry.getIdFromUri(uri);
                selection = sMovieIsFavourite;

                mCursor = mOpenHelper.getReadableDatabase()
                        .query(MoviesEntry.TABLE_NAME,
                                projection,
                                selection,
                                new String[]{"1"},
                                null,
                                null,
                                sortOrder
                        );
            } break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        mCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return mCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match){
            case movies:{
                return MoviesEntry.CONTENT_DIR_TYPE;
            }
            case moviesWithId:{
                return MoviesEntry.CONTENT_ITEM_TYPE;
            }
            case favouriteMovies:{
                return MoviesEntry.CONTENT_DIR_TYPE;
            }
            default: throw new UnsupportedOperationException("Unknown Uri " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnedUri;

        final int matcher = sUriMatcher.match(uri);

        switch (matcher){

            case movies:
                long _id = db.insert(MoviesEntry.TABLE_NAME, null, values);
                if (_id > 0){
                    returnedUri = MoviesEntry.buildMoviesUriWithId(_id);
                } else {
                    throw new SQLiteException("Failed to insert row " + _id);
                } break;

            default: throw new UnsupportedOperationException("Unknown Uri " + uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return returnedUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int returnedId = 0;

        final int matcher = sUriMatcher.match(uri);

        if (selection == null) selection = "1";

        switch (matcher){

            case movies:
                long _id = db.delete(MoviesEntry.TABLE_NAME, selection, selectionArgs);
                if (_id > 0){
                    returnedId = (int) _id;
                } break;

            default: throw new UnsupportedOperationException("Unknown Uri " + uri);
        }

        if (returnedId != 0) getContext().getContentResolver().notifyChange(uri,null);

        return returnedId;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int returnedId = 0;

        int match = sUriMatcher.match(uri);

        switch (match){
            case movies: {
                int _id = db.update(MoviesEntry.TABLE_NAME, values, selection, selectionArgs);
                if(_id > 0){
                    returnedId = _id;
                }

            } break;

            case moviesWithId: {
                selection = sMovieIdSelection;
                selectionArgs = new String[]{MoviesEntry.getIdFromUri(uri)};
                int _id = db.update(MoviesEntry.TABLE_NAME, values, selection, selectionArgs);
                if (_id > 0) {
                    returnedId = _id;
                }
                break;
            }
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        return returnedId;
    }



    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount = 0;

        switch (match){
            case movies: {
                db.beginTransaction();

                try {

                    for (int i = 0; i < values.length; i++) {
                        long _id = db.insert(MoviesEntry.TABLE_NAME, null, values[i]);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
            }
                getContext().getContentResolver().notifyChange(uri,null);
                return returnCount;

            default: return super.bulkInsert(uri, values);

        }



    }

}
