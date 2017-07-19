package com.example.android.newsapp;

        import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by martin on 19.7.17.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private static final String LOG_TAG = NewsLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructor
     */
    public NewsLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    // On Background Thread
    @Override
    public List<News> loadInBackground() {

        if (mUrl == null){
            return null;
        }

        /**
         * Network request, parsing, extraction
         */

        List<News> news = QueryUtils.fetchData(mUrl);
        return news;
    }

}
