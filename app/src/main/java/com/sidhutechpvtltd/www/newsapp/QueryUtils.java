package com.sidhutechpvtltd.www.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {

    }

    /**
     * Query the NewsApi dataset and return a list of {@link NewsStory} objects.
     */
    public static List<NewsStory> fetchNewsData(String requestUrl){
        Log.i(LOG_TAG,"TEST: fetchNewsData() called");

        //Create Url object
        URL url = createUrl(requestUrl);

        // Perform a HTTP request to URL and receive JSON response back
        String jsonResponse = null;
        try {
            jsonResponse= makeHttpRequest(url);
        } catch (IOException e){
            Log.e(LOG_TAG , "Error while making HTTP request.",e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link News}s
        List<NewsStory> newsStory = extractFeatureFromJson(jsonResponse);

        // Return the list of NewsStory
        return newsStory;

    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl){
        URL url = null;
        try {
            url= new URL(stringUrl);
        } catch (MalformedURLException e){
            Log.e(LOG_TAG,"Problem in building Url ",e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url)throws IOException{
        String jsonResponse = "";

        //if URL is null return early
        if (url==null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode()==200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else {
                Log.e(LOG_TAG,"Error response code: " + urlConnection.getResponseCode());
            }
        }catch (IOException e){
            Log.e(LOG_TAG,"Problem with the retrieving results from json response",e);
        }finally {
            if (urlConnection!=null){
                urlConnection.disconnect();
            }
            if (inputStream!=null){

                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream!=null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line!=null){
                output.append(line);
                line= reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link NewsStory} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<NewsStory> extractFeatureFromJson(String NewsJSON) {
        //If JSON response us null or empty then return early.
        if (TextUtils.isEmpty(NewsJSON)) {
            return null;
        }

        //Create an empty array list so that we can start adding new stories to it.
        List<NewsStory> newsStories = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            JSONObject baseJsonResponse = new JSONObject(NewsJSON);


            // Extract the JSONArray associated with the key called "response",
            // which represents a list of results (or news).
            JSONArray jsonResults = baseJsonResponse.getJSONArray("articles");

            for (int i = 0;i<jsonResults.length();i++){
                JSONObject resultJSON = jsonResults.getJSONObject(i);
                String title = resultJSON.getString("title");
                String url = resultJSON.getString("url");
                String description = resultJSON.getString("description");
                String publishedAt = resultJSON.getString("publishedAt");
                String author = resultJSON.getString("author");

                newsStories.add(new NewsStory(title,description,author,publishedAt,url));
            }
        } catch (JSONException e) {
            Log.e("Log", "Error in JSON response", e);
        }

        return newsStories;

    }
}
