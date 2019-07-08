package com.sidhutechpvtltd.www.newsapp;

public class NewsStory {

    private String mTitle ;

    private String mDescription ;

    private String mAuthor ;

    private String mPublishedAt ;

    private String mUrl ;


    public NewsStory (String title , String description , String author , String publishedAt , String url ){

        mTitle = title;
        mDescription = description;
        mAuthor = author;
        mPublishedAt = publishedAt;
        mUrl = url;

    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmPublishedAt() {
        return mPublishedAt;
    }

    public String getmUrl() {
        return mUrl;
    }

}
