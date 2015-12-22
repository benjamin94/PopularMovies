package com.example.benjaminlize.popularmovies;

/**
 * Created by benjamin.lize on 27/11/2015.
 */
public class Trailer {

    String mId;
    String mKey;
    String mName;
    String mSite;
    String mType;

    public Trailer(String id, String key, String name, String site, String type) {
        mId = id;
        mKey  = key;
        mName = name;
        mSite = site;
        mType = type;
    }

    public String getId() {
        return mId;
    }

    public String getKey() {
        if (mKey != null){
            return mKey;
        } return "";

    }

    public String getName() {
        return mName;
    }

    public String getSite() {
        return mSite;
    }

    public String getType() {
        return mType;
    }
}
