package com.example.benjaminlize.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle args = new Bundle();
        args.putParcelable(DetailActivityFragment.DETAIL_URI,getIntent().getData());

        DetailActivityFragment daf = new DetailActivityFragment();
        daf.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(
                R.id.movies_detail_container,daf).commit();
    }

}
