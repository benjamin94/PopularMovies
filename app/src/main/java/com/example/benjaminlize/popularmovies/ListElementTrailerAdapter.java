package com.example.benjaminlize.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by benjamin.lize on 01/12/2015.
 */
public class ListElementTrailerAdapter extends BaseAdapter{

    Context mContext;
    LayoutInflater mLayoutInflater;
    Trailer[] mTrailerArray;


    public ListElementTrailerAdapter(Context context, Trailer[] trailerArray) {
        super();

        mContext = context;
        mTrailerArray = trailerArray;
        mLayoutInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        if (mTrailerArray!=null) {
            return mTrailerArray.length;
        }else {
            return 0;
        }
    }


    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = mLayoutInflater.inflate(R.layout.video_trailer_item, null);

        final ViewHolder viewHolder = new ViewHolder(convertView);

        convertView.setTag(viewHolder);

        viewHolder.mTvTitle.setText(mTrailerArray[position].getName());
        viewHolder.mTvDescription.setText(mTrailerArray[position].getSite() + " + click button to open");
        viewHolder.mIbLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri builtUri = Uri.parse("http://youtu.be/").buildUpon()
                        .appendPath(String.valueOf(mTrailerArray[position].getKey()))
                        .build();

                try {
                    URL url = new URL(builtUri.toString());

                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url.toString()));
                    mContext.startActivity(i);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            }
        });


        return convertView;

    }


    public class ViewHolder{

        TextView mTvTitle;
        TextView mTvDescription;
        ImageButton mIbLink;


        public ViewHolder(View view) {

            mTvTitle       = (TextView)   view.findViewById(R.id.videoText);
            mTvDescription = (TextView)   view.findViewById(R.id.videoTrailerText);
            mIbLink        = (ImageButton)view.findViewById(R.id.videoTrailerImageButton);

        }

    }


}

