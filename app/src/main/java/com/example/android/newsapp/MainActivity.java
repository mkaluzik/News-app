package com.example.android.newsapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderCallbacks<List<News>>{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    // let's check what guardian shows about udacity
    private static final String GUARDIAN_URL = "http://content.guardianapis.com/search?q=udacity&api-key=test";

    // set loader ID
    private static final int NEWS_LOADER_ID = 1;

    private NewsAdapter mAdapter;

    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ListView newsListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);

        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        newsListView.setAdapter(mAdapter);

    // Open in browser
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){

                News currentNews = mAdapter.getItem(position);

                Uri newsUri = Uri.parse(currentNews.getNewsUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                startActivity(websiteIntent);

            }
        });

        /**
         * Check connectivity
         */
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo networkInfo = conMgr.getActiveNetworkInfo();

        // If connectivity is OK get the data from Guardian via API
        if (networkInfo != null && networkInfo.isConnected()) {
            // instantiate loader to have a reference
            LoaderManager loaderManager = getLoaderManager();

            //Loader initialization
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);

        }

        // Reset loader and refetch data on click (tap)
        mEmptyStateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLoaderManager().restartLoader(NEWS_LOADER_ID, null, MainActivity.this);
            }
        });

    }

    // Loader On create activity
    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        //Loader in background
        return new NewsLoader(this, GUARDIAN_URL);

    }

    // Loader on Finished activity
    @Override
    public void onLoadFinished(android.content.Loader<List<News>> loader, List<News> news) {
        // Hide loading indicator because the data has been loaded
        final View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state (No news)
        mEmptyStateTextView.setText(R.string.no_news);

        // Clear the adapter to get rid of previously stored data
        mAdapter.clear();

        // If there is a valid list of {@link New}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (news != null && !news.isEmpty()) {
            mAdapter.addAll(news);
        }


        /**
         * Connectivity loss check during runtime
         */
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = conMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && !networkInfo.isConnected()) {

        } else {
            // hide loading indicator
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
            // Refresh if connection lost

        }
    }

    /**
     * onLoaderReset
     */
    @Override
    public void onLoaderReset(android.content.Loader<List<News>> loader) {
        mAdapter.clear();

    }

}
