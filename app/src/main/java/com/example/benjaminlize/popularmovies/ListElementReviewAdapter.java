package com.example.benjaminlize.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by benjamin.lize on 27/11/2015.
 */
public class ListElementReviewAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    Review[] mReviewArray;

    public ListElementReviewAdapter( Context context, Review[] reviewArray) {
        super();

        mContext = context;
        mReviewArray = reviewArray;
        mLayoutInflater = LayoutInflater.from(context);

        }

        @Override
        public int getCount() {
            if (mReviewArray != null){
                return mReviewArray.length;
            } else {
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
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = mLayoutInflater.inflate(R.layout.user_review_item, null);

            ViewHolder viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

            viewHolder.mTvTitle.setText(mReviewArray[position].getTitles());
            viewHolder.mTvDescription.setText(mReviewArray[position].getDescrptions());

            return convertView;
        }

    public class ViewHolder{
        TextView mTvTitle;
        TextView mTvDescription;

        public ViewHolder(View view) {
            mTvTitle       = (TextView) view.findViewById(R.id.userReviewTitle);
            mTvDescription = (TextView) view.findViewById(R.id.userReviewText);
        }
    }

    }

