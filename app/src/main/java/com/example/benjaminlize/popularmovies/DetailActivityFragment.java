package com.example.benjaminlize.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.benjaminlize.popularmovies.data.MoviesContract;
import com.squareup.picasso.Picasso;

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

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int MY_DETAIL_CURSOR_LOADER = 101;
    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    public static final String DETAIL_URI = "sendDataHere";
    android.support.v7.widget.ShareActionProvider mShareActionProvider;
    Intent mTrailerIntent;
    Uri moviesUri;
    boolean mCreatedShareIntent = false;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Bundle args = getArguments();

        if (args != null){
            moviesUri = args.getParcelable(DETAIL_URI);
        }

        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(MY_DETAIL_CURSOR_LOADER,null,this);
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(getContext(),
                moviesUri,
                null,
                null,
                null,
                null
        );

    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, final Cursor cursor) {

        if (cursor.moveToFirst()){

            ViewHolder viewHolder = new ViewHolder(getView(),cursor);

            String toPicasso = "http://image.tmdb.org/t/p/" + "w780" + cursor.getString(viewHolder.INDEX_COLUMN_IMAGE_PATH);
            Picasso.with(getView().getContext()).load(toPicasso).into(viewHolder.mIvPoster);

            viewHolder.mTvYear        .setText(String.valueOf(cursor.getInt(viewHolder.INDEX_COLUMN_RELEASE_DATE)));
            viewHolder.mTvDuration    .setText(String.valueOf((int) cursor.getFloat(viewHolder.INDEX_COLUMN_POPULARITY)) + " Votes");
            viewHolder.mTvRanking     .setText(String.valueOf(cursor.getFloat(viewHolder.INDEX_COLUMN_VOTE_AVERAGE)) + "/10");

            String textDescription = cursor.getString(viewHolder.INDEX_COLUMN_OVERVIEW);
            if (textDescription.equals("null")) textDescription = "The description of the movie is unavailable at the moment";
            viewHolder.mTvDescription .setText(textDescription);

            if (cursor.getInt(viewHolder.INDEX_COLUMN_IS_FAVOURITE) == 0){
                viewHolder.checkBox.setChecked(false);
            } else {
                viewHolder.checkBox.setChecked(true);
            }

                    viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            long update = updateValues(cursor, isChecked);
                        }

                    });

            FetchTrailersAndReviewsTask fetchTrailersAndReviewsTask = new FetchTrailersAndReviewsTask(
                    getActivity(),
                    getView(),
                    cursor.getInt(viewHolder.INDEX_COLUMN_MOVIE_ID_FROM_API));

            fetchTrailersAndReviewsTask.execute();

            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(cursor.getString(viewHolder.INDEX_COLUMN_TITLE));
        }
    }

    public long updateValues(Cursor cursor, boolean isChecked){
        Uri uriToPass = MoviesContract.MoviesEntry.buildMoviesUriWithId((long) cursor.getInt(0));

        ContentValues cv = new ContentValues();

        //Declare all indexes
        int INDEX_ISADULT = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_ISADULT);
        int INDEX_IMAGE_PATH = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_IMAGE_PATH);
        int INDEX_MOVIE_ID_FROM_API = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID_FROM_API);
        int INDEX_ORIGINAL_LANGUAGE = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_LANGUAGE);
        int INDEX_ORIGINAL_TITLE = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE);
        int INDEX_OVERVIEW = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_OVERVIEW);
        int INDEX_RELEASE_DATE = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE);
        int INDEX_POSTER_PATH = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH);
        int INDEX_POPULARITY = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_POPULARITY);
        int INDEX_TITLE = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_TITLE);
        int INDEX_VIDEO = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_VIDEO);
        int INDEX_VOTE_AVERAGE = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE);
        int INDEX_VOTE_COUNT = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_VOTE_COUNT);

        //Get Values from the Cursor
        final String VALUE_ISADULT = cursor.getString(INDEX_ISADULT);
        final String VALUE_IMAGE_PATH = cursor.getString(INDEX_IMAGE_PATH);
        final int VALUE_MOVIE_ID_FROM_API = cursor.getInt(INDEX_MOVIE_ID_FROM_API);
        final String VALUE_ORIGINAL_LANGUAGE = cursor.getString(INDEX_ORIGINAL_LANGUAGE);
        final String VALUE_ORIGINAL_TITLE = cursor.getString(INDEX_ORIGINAL_TITLE);
        final String VALUE_OVERVIEW = cursor.getString(INDEX_OVERVIEW);
        final String VALUE_RELEASE_DATE = cursor.getString(INDEX_RELEASE_DATE);
        final String VALUE_POSTER_PATH = cursor.getString(INDEX_POSTER_PATH);
        final float VALUE_POPULARITY = cursor.getFloat(INDEX_POPULARITY);
        final String VALUE_TITLE = cursor.getString(INDEX_TITLE);
        final String VALUE_VIDEO = cursor.getString(INDEX_VIDEO);
        final float VALUE_VOTE_AVERAGE = cursor.getFloat(INDEX_VOTE_AVERAGE);
        final int VALUE_VOTE_COUNT = cursor.getInt(INDEX_VOTE_COUNT);
        int VALUE_IS_FAVOURITE;

        //swap Flag
        if (isChecked) {
            VALUE_IS_FAVOURITE = 1;
        } else {
            VALUE_IS_FAVOURITE = 0;
        }
        //Store them in new ContentValues with swapped Flag
        cv.put(MoviesContract.MoviesEntry.COLUMN_ISADULT, VALUE_ISADULT);
        cv.put(MoviesContract.MoviesEntry.COLUMN_IMAGE_PATH, VALUE_IMAGE_PATH);
        cv.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID_FROM_API, VALUE_MOVIE_ID_FROM_API);
        cv.put(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_LANGUAGE, VALUE_ORIGINAL_LANGUAGE);
        cv.put(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE, VALUE_ORIGINAL_TITLE);
        cv.put(MoviesContract.MoviesEntry.COLUMN_OVERVIEW, VALUE_OVERVIEW);
        cv.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE, VALUE_RELEASE_DATE);
        cv.put(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH, VALUE_POSTER_PATH);
        cv.put(MoviesContract.MoviesEntry.COLUMN_POPULARITY, VALUE_POPULARITY);
        cv.put(MoviesContract.MoviesEntry.COLUMN_TITLE, VALUE_TITLE);
        cv.put(MoviesContract.MoviesEntry.COLUMN_VIDEO, VALUE_VIDEO);
        cv.put(MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE, VALUE_VOTE_AVERAGE);
        cv.put(MoviesContract.MoviesEntry.COLUMN_VOTE_COUNT, VALUE_VOTE_COUNT);
        cv.put(MoviesContract.MoviesEntry.COLUMN_IS_FAVOURITE, VALUE_IS_FAVOURITE);

        long update = getActivity().getContentResolver().update(uriToPass, cv, null, null);
        return update;
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public static class ViewHolder {

        TextView  mTvYear        ;
        TextView  mTvDuration    ;
        TextView  mTvRanking     ;
        TextView  mTvDescription ;
        ImageView mIvPoster      ;
        CheckBox checkBox;


        int INDEX_COLUMN_ISADULT           ;
        int INDEX_COLUMN_IMAGE_PATH        ;
        int INDEX_COLUMN_MOVIE_ID_FROM_API ;
        int INDEX_COLUMN_ORIGINAL_LANGUAGE ;
        int INDEX_COLUMN_ORIGINAL_TITLE    ;
        int INDEX_COLUMN_OVERVIEW          ;
        int INDEX_COLUMN_RELEASE_DATE      ;
        int INDEX_COLUMN_POSTER_PATH       ;
        int INDEX_COLUMN_POPULARITY        ;
        int INDEX_COLUMN_TITLE             ;
        int INDEX_COLUMN_VIDEO             ;
        int INDEX_COLUMN_VOTE_AVERAGE      ;
        int INDEX_COLUMN_VOTE_COUNT        ;
        int INDEX_COLUMN_IS_FAVOURITE      ;

        public ViewHolder(View view, Cursor cursor){

            mTvYear        = (TextView) view.findViewById(R.id.year) ;
            mTvDuration    = (TextView) view.findViewById(R.id.duration) ;;
            mTvRanking     = (TextView) view.findViewById(R.id.rating) ;
            mTvDescription = (TextView) view.findViewById(R.id.description) ;
            mIvPoster      = (ImageView)view.findViewById(R.id.poster_description_iv) ;
            checkBox       = (CheckBox) view.findViewById(R.id.checkBox_IsFavourite);


            INDEX_COLUMN_ISADULT           = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_ISADULT           );
            INDEX_COLUMN_IMAGE_PATH        = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_IMAGE_PATH        );
            INDEX_COLUMN_MOVIE_ID_FROM_API = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID_FROM_API );
            INDEX_COLUMN_ORIGINAL_LANGUAGE = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_LANGUAGE );
            INDEX_COLUMN_ORIGINAL_TITLE    = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE    );
            INDEX_COLUMN_OVERVIEW          = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_OVERVIEW          );
            INDEX_COLUMN_RELEASE_DATE      = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE      );
            INDEX_COLUMN_POSTER_PATH       = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH       );
            INDEX_COLUMN_POPULARITY        = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_POPULARITY        );
            INDEX_COLUMN_TITLE             = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_TITLE             );
            INDEX_COLUMN_VIDEO             = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_VIDEO             );
            INDEX_COLUMN_VOTE_AVERAGE      = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE      );
            INDEX_COLUMN_VOTE_COUNT        = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_VOTE_COUNT        );
            INDEX_COLUMN_IS_FAVOURITE      = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_IS_FAVOURITE      );
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_detail, menu);

        MenuItem item = menu.findItem(R.id.action_share);

        mShareActionProvider=(android.support.v7.widget.ShareActionProvider)MenuItemCompat.getActionProvider(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(LOG_TAG," you clicked an option menu ");
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            Log.d(LOG_TAG," you clicked the action share option menu ");

            doShare();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Share Intent
    public void doShare() {
        // When you want to share set the share intent.
        if (mShareActionProvider != null ) {
            mShareActionProvider.setShareIntent(mTrailerIntent);
        } else {
            Log.d(LOG_TAG, "Share Action Provider is null?");
        }
    }
    private Intent createShareMoviesIntent(Uri url) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                url.toString());
        mCreatedShareIntent = true;
        return shareIntent;
    }


    public class FetchTrailersAndReviewsTask extends AsyncTask<Void, Void, ReviewAndTrailerArray> {

        Context mContex;
        int mApiId;
        View mView;
        private final String LOG_TAG = FetchTrailersAndReviewsTask.class.getSimpleName();

        public FetchTrailersAndReviewsTask(Context context, View view, int apiId) {
            mContex = context;
            mView = view;
            mApiId = apiId;
        }

        @Override
        protected void onPostExecute(ReviewAndTrailerArray reviewAndTrailerArray) {
            super.onPostExecute(reviewAndTrailerArray);

            ListView lvReviews = (ListView) mView.findViewById(R.id.listViewReviews);
            lvReviews.setAdapter(new ListElementReviewAdapter(mContex, reviewAndTrailerArray.mReview));

            ListView lvTrailers = (ListView) mView.findViewById(R.id.listViewTrailers);
            lvTrailers.setAdapter(new ListElementTrailerAdapter(mContex, reviewAndTrailerArray.mTrailer));

            if (reviewAndTrailerArray.mTrailer != null){
                Uri builtUri = Uri.parse("http://youtu.be/").buildUpon()
                        .appendPath(String.valueOf(reviewAndTrailerArray.mTrailer[0].getKey()))
                        .build();

                mTrailerIntent = createShareMoviesIntent(builtUri);
            }


        }

        protected ReviewAndTrailerArray doInBackground(Void... params) {

            Review[]  reviewArray  = fetchReviewsData();
            Trailer[] trailerArray = fetchTrailersData();

            ReviewAndTrailerArray reviewAndTrailerArray = new ReviewAndTrailerArray(reviewArray,trailerArray);

            return reviewAndTrailerArray;
        }

        private Trailer[] fetchTrailersData() {
            Trailer[] trailerArray = null;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;

            final String APPID_PARAM = "api_key";

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast

                Uri builtUri = Uri.parse(MoviesContract.BASE_PASS_URI_DESCRIPTION).buildUpon()
                        .appendPath(String.valueOf(mApiId))
                        .appendPath("videos")
                        .appendQueryParameter(APPID_PARAM, MoviesContract.API__KEY_THEMOVIEDB)
                        .build();

                URL url = new URL(builtUri.toString());

                //http://api.themoviedb.org/3/movie/177677/videos?api_key=8db5fae4b2741eb16ec711f91dd555ec
                //URL url = new URL(builtUri);

                // Create the request to MoviesDb, and open the connection
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
                moviesJsonStr = buffer.toString();
                Log.d(LOG_TAG, "detail string: " + moviesJsonStr);
                trailerArray = getTrailerDataFromJson(moviesJsonStr);

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
            return trailerArray;
        }

        public Trailer[] getTrailerDataFromJson(String moviesJsonString){

            Trailer[] trailerArray = null;

            String VALUE_ID;
            String VALUE_KEY;
            String VALUE_NAME;
            String VALUE_SITE;
            String VALUE_TYPE;

            try {
                JSONObject moviesJson = new JSONObject(moviesJsonString);
                JSONArray moviesArray = moviesJson.getJSONArray("results");

                if (moviesArray.length() > 0) {

                    trailerArray = new Trailer[moviesArray.length()];

                    for (int i = 0; i < moviesArray.length(); i++) {

                        JSONObject thisMovie = moviesArray.getJSONObject(i);

                        VALUE_ID   = thisMovie.getString("id");
                        VALUE_KEY  = thisMovie.getString("key");
                        VALUE_NAME = thisMovie.getString("name");
                        VALUE_SITE = thisMovie.getString("site");
                        VALUE_TYPE = thisMovie.getString("type");

                        trailerArray[i] = new Trailer(VALUE_ID, VALUE_KEY, VALUE_NAME, VALUE_SITE, VALUE_TYPE);

                    }
                }
            }
            catch(JSONException e){
                e.printStackTrace();
                trailerArray = new Trailer[0];
            }
            return trailerArray;
        }

        private Review[] fetchReviewsData() {
            Review[] reviewArray = null;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;

            final String APPID_PARAM = "api_key";

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast

                Uri builtUri = Uri.parse(MoviesContract.BASE_PASS_URI_DESCRIPTION).buildUpon()
                        .appendPath(String.valueOf(mApiId))
                        .appendPath("reviews")
                        .appendQueryParameter(APPID_PARAM, MoviesContract.API__KEY_THEMOVIEDB)
                        .build();

                URL url = new URL(builtUri.toString());

                //http://api.themoviedb.org/3/movie/177677/videos?api_key=8db5fae4b2741eb16ec711f91dd555ec
                //URL url = new URL(builtUri);

                // Create the request to MoviesDb, and open the connection
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
                moviesJsonStr = buffer.toString();
                Log.d(LOG_TAG,"detail string: " + moviesJsonStr);
                reviewArray = getReviewsDataFromJson(moviesJsonStr);

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
            return reviewArray;
        }

        private Review[] getReviewsDataFromJson(String moviesJsonStr) {

            String mAuthor;
            String mDescription;
            Review[] reviewArray = null;

            try {
                JSONObject moviesJson = new JSONObject(moviesJsonStr);
                JSONArray moviesArray = moviesJson.getJSONArray("results");

                if (moviesArray.length() > 0) {

                    reviewArray = new Review[moviesArray.length()];

                    for (int i = 0; i < moviesArray.length(); i++) {

                        JSONObject thisMovie = moviesArray.getJSONObject(i);

                        mAuthor = thisMovie.getString("author");
                        mDescription = thisMovie.getString("content");

                        reviewArray[i] = new Review(mAuthor,mDescription);
                    }
                }
            }
            catch(JSONException e){
                e.printStackTrace();
            }
            return reviewArray;
        }



    }


}
