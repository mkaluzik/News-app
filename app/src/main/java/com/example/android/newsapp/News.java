package com.example.android.newsapp;

/**
 * Created by martin on 19.7.17.
 */
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;

public class News {

    //Define var
    private String mTitle;
    private String mDate;
    private String mNewsUrl;
    private String mSection;

    //Constructor
    public News(String title, String section, String date, String newsUrl){
        mTitle = title;
        mSection = section;
        mDate = date;
        mNewsUrl = newsUrl;
    }

    //Get
    public String getTitle(){ return mTitle;}
    public String getSection(){ return mSection;}
    public String getDate(){
        String dtStart = mDate;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        try {
            Date date = format.parse(dtStart);
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd.MM. yyyy HH:mm");
            mDate=String.valueOf(fmtOut.format(date));
            return mDate;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mDate;}
    public String getNewsUrl(){ return mNewsUrl;}
}
