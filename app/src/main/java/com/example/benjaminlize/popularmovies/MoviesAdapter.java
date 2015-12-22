package com.example.benjaminlize.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.example.benjaminlize.popularmovies.data.MoviesContract;
import com.squareup.picasso.Picasso;

/**
 * Created by benjamin.lize on 09/11/2015.
 */
public class MoviesAdapter extends CursorAdapter {

    boolean cursorValuesSet = false;

    int INDEX_COLUMN_IMAGE_PATH       ;


    public MoviesAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.gridview_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        if (!cursorValuesSet){
            populateIndexValues(cursor);
        }

        ViewHolder viewHolder = (ViewHolder)view.getTag();
        String imageString = cursor.getString(INDEX_COLUMN_IMAGE_PATH);
        if (imageString.equals("null")){
            viewHolder.imageView_poster.setImageResource(R.mipmap.ic_launcher);
        }else{
            String toPicasso = "http://image.tmdb.org/t/p/" + "w185" + imageString;
            Picasso.with(context).load(toPicasso).into(viewHolder.imageView_poster);
        }

    }



    public void populateIndexValues(Cursor cursor){

        cursorValuesSet = true;

        INDEX_COLUMN_IMAGE_PATH        = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_IMAGE_PATH        );
    }

    public static class ViewHolder {

        ImageView imageView_poster;

        public ViewHolder(View view){
            imageView_poster = (ImageView) view.findViewById(R.id.imageView_poster);
        }
    }




}
