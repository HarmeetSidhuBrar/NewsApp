package com.sidhutechpvtltd.www.newsapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<NewsStory> {

    public NewsAdapter (Activity context, ArrayList<NewsStory> newsStories){
        super(context,0,newsStories);
    }

    private static final String TIME_SEPARATOR = "T";

    @Override
    public View getView(int position , View convertView , ViewGroup parent){
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;

        if (listItemView==null){
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.list_view,parent,false);
        }

        // Find the news story at the given position in the list of news Stories.
        NewsStory currentNewsStory = getItem(position);

        // Find the TextView with the Id headline_text
        TextView headlineTextView = listItemView.findViewById(R.id.headline_text);
        headlineTextView.setText(currentNewsStory.getmTitle());

        // Find the TextView with the Id news_body_text .
        TextView newsBodyTextView = listItemView.findViewById(R.id.news_body_text);
        newsBodyTextView.setText(currentNewsStory.getmDescription());

        // Find the textView with the Id writer_text_view .
        TextView writerTextView = listItemView.findViewById(R.id.writer_name_text);
        writerTextView.setText(currentNewsStory.getmAuthor());


        // Get the original time from the News Story object,
        String originalTime = currentNewsStory.getmPublishedAt();

        //Variable for time
        String timeText = null;

        //variable for date
        String dateText = null;

        //Check whether the originalTime string contains the "T" text
        if (originalTime.contains(TIME_SEPARATOR)){
            // Split the string into different parts (as an array of Strings)
            // based on the " T " text. We expect an array of 2 Strings, where
            // the first String will be Date and the second String will be Time .
            String[] parts = originalTime.split(TIME_SEPARATOR);
            // Location offset should be "5km N " + " of " --> "5km N of"
            dateText = parts[0] ;
            // Primary location should be "Cairo, Egypt"
            timeText = parts[1];
        }

        // Find the textView with the Id date_details_text.
        TextView dateTextView = listItemView.findViewById(R.id.date_details_text);
        dateTextView.setText(dateText);

        // Find the textView with the Id time-details_text.
        TextView timeTextView = listItemView.findViewById(R.id.time_details_text);
        timeTextView.setText(timeText);

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }
}
