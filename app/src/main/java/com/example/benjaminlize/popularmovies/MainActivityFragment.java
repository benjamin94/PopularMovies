package com.example.benjaminlize.popularmovies;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;

import com.example.benjaminlize.popularmovies.data.MoviesContract;

import java.util.concurrent.ExecutionException;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri dateUri);
    }

    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    private static final int MY_LOADER_ID = 100;

    MoviesAdapter mMoviesAdapter;

    GridView mGridView;

    Spinner mSpinner;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        CreateAndBindCursorAdapter(rootView);

        mSpinner = (Spinner)rootView.findViewById(R.id.spinner);

        SharedPreferences preferences = getActivity().getSharedPreferences(String.valueOf(R.string.pref_name), 0);

        String value = preferences.getString(String.valueOf(R.string.pref_spinner_key), String.valueOf(R.string.pref_spinner_default));
        Log.d(LOG_TAG, value);

        linkSpinnerToSharedPreferences(mSpinner);
        return rootView;
    }

    private void linkSpinnerToSharedPreferences(final Spinner spinner) {

        final SharedPreferences preferences = getActivity().getSharedPreferences(String.valueOf(R.string.pref_name), 0);
        String value = preferences.getString(String.valueOf(R.string.pref_spinner_key), String.valueOf(R.string.pref_spinner_default));

        ArrayAdapter<String> myAdap = (ArrayAdapter<String>) spinner.getAdapter();
        int spinnerPosition = myAdap.getPosition(value);

// set the default according to value
        spinner.setSelection(spinnerPosition);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String mSpnValue = parent.getSelectedItem().toString();
                SharedPreferences.Editor prefsEditor = preferences.edit();
                prefsEditor.putString(String.valueOf(R.string.pref_spinner_key), mSpnValue);
                prefsEditor.commit();

                try {
                    fetchMoviesTask();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                reinitLoader();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }

    public void CreateAndBindCursorAdapter(View rootView){
        Cursor cursor = getContext()
                .getContentResolver().query(
                        MoviesContract.MoviesEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null
                );

        mMoviesAdapter = new MoviesAdapter(getActivity(),cursor,0);

        mGridView = (GridView)rootView.findViewById(R.id.gridView);
        mGridView.setAdapter(mMoviesAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cursor cursor = (Cursor) parent.getItemAtPosition(position);

                int index = cursor.getColumnIndex(MoviesContract.MoviesEntry._ID);

                if (cursor != null) {
                    ((Callback) getActivity()).onItemSelected(MoviesContract.MoviesEntry.buildMoviesUriWithId(cursor.getInt(index)));

                }
            }
        });
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(MY_LOADER_ID, null, this);

    }

    @Override
    public void onStart() {
        super.onStart();
        hasOptionsMenu();

        try {
            fetchMoviesTask();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu_fragment_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //Intent intent = new Intent(getContext(),SettingsActivity.class);
            //startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchMoviesTask() throws ExecutionException, InterruptedException {
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(getActivity());
        fetchMoviesTask.execute();

    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {

        final SharedPreferences preferences = getActivity().getSharedPreferences(String.valueOf(R.string.pref_name), 0);
        String value = preferences.getString(String.valueOf(R.string.pref_spinner_key), String.valueOf(R.string.pref_spinner_default));

        if (!value.equals("favorites")){
            return new CursorLoader(getContext(),
                    MoviesContract.MoviesEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );

        } else {

            String selection = MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesContract.MoviesEntry.COLUMN_IS_FAVOURITE + " = ?";

            return new CursorLoader(getContext(),
                    MoviesContract.MoviesEntry.CONTENT_URI,
                    null,
                    selection,
                    new String[]{"1"},
                    null
            );
        }


    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        mMoviesAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        mMoviesAdapter.swapCursor(null);
    }

    public void reinitLoader(){
        getLoaderManager().restartLoader(MY_LOADER_ID, null, this);
    };
}
