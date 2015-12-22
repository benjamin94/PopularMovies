package com.example.benjaminlize.popularmovies;

/**
 * Created by benjamin.lize on 27/11/2015.
 */
public class Review {

    String mTitles;
    String mDescrptions;



    public Review(String titles, String descrptions) {
        mTitles = titles;
        mDescrptions = descrptions;
    }

    public void setTitles(String titles) {
        mTitles = titles;
    }

    public void setDescrptions(String descrptions) {
        mDescrptions = descrptions;
    }

    public String getTitles() {
        return mTitles;
    }

    public String getDescrptions() {
        return mDescrptions;
    }

}
