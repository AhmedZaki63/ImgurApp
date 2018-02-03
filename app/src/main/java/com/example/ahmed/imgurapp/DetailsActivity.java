package com.example.ahmed.imgurapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class DetailsActivity extends AppCompatActivity {

    DetailsFragment detailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            detailsFragment = new DetailsFragment();
            detailsFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, detailsFragment, "details_fragment").commit();
        } else {
            detailsFragment = (DetailsFragment) getSupportFragmentManager()
                    .findFragmentByTag("details_fragment");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
