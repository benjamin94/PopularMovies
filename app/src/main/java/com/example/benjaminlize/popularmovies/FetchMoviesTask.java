package com.example.benjaminlize.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.benjaminlize.popularmovies.data.MoviesContract;
import com.example.benjaminlize.popularmovies.data.MoviesDbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Vector;

/**
 * Created by benjamin.lize on 02/11/2015.
 */
public class FetchMoviesTask extends AsyncTask<Void,Void,String> {

    /*Building querry by hand:
    http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=8db5fae4b2741eb16ec711f91dd555ec
    */

    String  VALUE_ISADULT           ;
    String  VALUE_IMAGE_PATH        ;
    int     VALUE_MOVIE_ID_FROM_API ;
    String  VALUE_ORIGINAL_LANGUAGE ;
    String  VALUE_ORIGINAL_TITLE    ;
    String  VALUE_OVERVIEW          ;
    String  VALUE_RELEASE_DATE      ;
    String  VALUE_POSTER_PATH       ;
    float   VALUE_POPULARITY        ;
    String  VALUE_TITLE             ;
    String  VALUE_VIDEO             ;
    float   VALUE_VOTE_AVERAGE      ;
    int     VALUE_VOTE_COUNT        ;
    int     VALUE_IS_FAVOURITE      ;

    Context mContex;

    public FetchMoviesTask(Context context){
        mContex = context;
    }

    private static final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

    @Override
    protected String doInBackground(Void... params) {

        // --- DATA FETCHING ---

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String moviesJsonStr = null;

        final String SORT = "sort_by";
        final String APPID_PARAM = "api_key";
        String sortOrder;

        final SharedPreferences preferences = mContex.getSharedPreferences(String.valueOf(R.string.pref_name), 0);
        String value = preferences.getString(String.valueOf(R.string.pref_spinner_key), String.valueOf(R.string.pref_spinner_default));

        if      (value.equals("most popular"))     sortOrder = MoviesContract.SORT_POPULARITY;
        else if (value.equals("highest rated"))    sortOrder = MoviesContract.SORT_RATING; //vote_average.desc
        else if (value.equals("favorites")){
            sortOrder = MoviesContract.SORT_POPULARITY;
        }
        else sortOrder = MoviesContract.SORT_POPULARITY;


        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast


            Uri builtUri = Uri.parse(MoviesContract.BASE_PASS_URI).buildUpon()
                    .appendQueryParameter(SORT, sortOrder)
                    .appendQueryParameter(APPID_PARAM, MoviesContract.API__KEY_THEMOVIEDB)
                    .build();

                    URL url = new URL(builtUri.toString());


            //String builtUri = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=8db5fae4b2741eb16ec711f91dd555ec";
            //URL url = new URL(builtUri);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }

            //put debug point here to see the raw String collected from API
            moviesJsonStr = buffer.toString();


            // --- DATA PROCESSING ---
            setWasUpdatedToZero();
            getMoviesDataFromJson(moviesJsonStr);
            deleteNonUpdatedColumnsAndCloseDb();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        // This will only happen if there was an error getting or parsing the forecast.
        return moviesJsonStr;
    }

    //Data fetching methods

    //Data processing methods
    public void getMoviesDataFromJson(String moviesJsonString){

        try {
            JSONObject moviesJson = new JSONObject(moviesJsonString);
            JSONArray moviesArray = moviesJson.getJSONArray("results");

            Vector<ContentValues> contentValuesVector = new Vector<ContentValues>(moviesArray.length());

            int moviesAdded = 0;
            for (int i = 0; i<moviesArray.length(); i++){

                JSONObject thisMovie = moviesArray.getJSONObject(i);
                boolean wasAdded = addMovie(thisMovie);
                if (wasAdded) moviesAdded++;
            }
            Log.d(LOG_TAG,"added movies = " + moviesAdded);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    //Database methods
    private void setWasUpdatedToZero(){
        MoviesDbHelper dbHelper = new MoviesDbHelper(mContex);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("UPDATE movies SET was_updated = 0;");
    }
    private void deleteNonUpdatedColumnsAndCloseDb(){
        MoviesDbHelper dbHelper = new MoviesDbHelper(mContex);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE from movies WHERE was_updated = 0 AND is_favourite = 0;");
        dbHelper.close();
        db.close();
    }

    public boolean addMovie(JSONObject thisMovie) throws JSONException {
        // Students: First, check if the location with this city name exists in the db
        long locationId;
        boolean movieAdded;

        VALUE_ISADULT           = thisMovie.getString("adult")            ;
        VALUE_IMAGE_PATH        = thisMovie.getString("backdrop_path")    ;
        VALUE_MOVIE_ID_FROM_API = thisMovie.getInt("id")               ;
        VALUE_ORIGINAL_LANGUAGE = thisMovie.getString("original_language");
        VALUE_ORIGINAL_TITLE    = thisMovie.getString("original_title")   ;
        VALUE_OVERVIEW          = thisMovie.getString("overview")         ;
        VALUE_RELEASE_DATE      = thisMovie.getString("release_date")     ;
        VALUE_POSTER_PATH       = thisMovie.getString("poster_path")      ;
        VALUE_POPULARITY        = thisMovie.getInt("popularity")       ;
        VALUE_TITLE             = thisMovie.getString("title")            ;
        VALUE_VIDEO             = thisMovie.getString("video")            ;
        VALUE_VOTE_AVERAGE      = thisMovie.getInt   ("vote_average")     ;
        VALUE_VOTE_COUNT        = thisMovie.getInt   ("vote_count")       ;

        Cursor dbCursor = mContex.getContentResolver().query(
                MoviesContract.MoviesEntry.CONTENT_URI,
                new String[]{MoviesContract.MoviesEntry._ID, MoviesContract.MoviesEntry.COLUMN_IS_FAVOURITE},
                MoviesContract.MoviesEntry.COLUMN_MOVIE_ID_FROM_API + " = ?",
                new String[]{String.valueOf(VALUE_MOVIE_ID_FROM_API)},
                null
        );

        ContentValues movieValues = new ContentValues();

        movieValues.put(MoviesContract.MoviesEntry.COLUMN_ISADULT          ,VALUE_ISADULT           );
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_IMAGE_PATH       ,VALUE_IMAGE_PATH        );
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID_FROM_API,VALUE_MOVIE_ID_FROM_API );
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_LANGUAGE,VALUE_ORIGINAL_LANGUAGE );
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE   ,VALUE_ORIGINAL_TITLE    );
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_OVERVIEW         ,VALUE_OVERVIEW          );
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE     ,VALUE_RELEASE_DATE      );
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH      ,VALUE_POSTER_PATH       );
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_POPULARITY       ,VALUE_POPULARITY        );
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_TITLE            ,VALUE_TITLE             );
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_VIDEO            ,VALUE_VIDEO             );
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE     ,VALUE_VOTE_AVERAGE      );
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_VOTE_COUNT       ,VALUE_VOTE_COUNT        );
        movieValues.put(MoviesContract.MoviesEntry.COLUMN_WAS_UPDATED      ,1                       );


        if (dbCursor.moveToFirst()) {
            dbCursor.moveToFirst();
            int indexFavourite = dbCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_IS_FAVOURITE);
            int isFav = dbCursor.getInt(indexFavourite);

            movieValues.put(MoviesContract.MoviesEntry.COLUMN_IS_FAVOURITE,isFav);

            //Database element exists and is not refreshed
            int updatedRow = mContex.getContentResolver().update(
                    MoviesContract.MoviesEntry.CONTENT_URI,
                    movieValues,
                    MoviesContract.MoviesEntry.COLUMN_MOVIE_ID_FROM_API + " = ?",
                    new String[]{String.valueOf(VALUE_MOVIE_ID_FROM_API)});

            movieAdded = false;

        } else {

            movieValues.put(MoviesContract.MoviesEntry.COLUMN_IS_FAVOURITE,0);

            Uri insertUri = mContex.getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI, movieValues);

            movieAdded = true;
        }

        // If it exists, return the current ID
        // Otherwise, insert it using the content resolver and the base URI
        dbCursor.close();
        return movieAdded;

    }


}