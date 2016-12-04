package com.example.amgad.imovies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Amgad on 09-Oct-16.
 *
 */

public class MovieData {

    public static ArrayList getMoviesName(String nameJsonStr) throws JSONException {
        ArrayList movieTitlesArray = new ArrayList();
        JSONObject page = new JSONObject(nameJsonStr);
        JSONArray results = page.getJSONArray("results");
        for (int i = 0; i < results.length(); i++) {
            JSONObject movie = results.getJSONObject(i);
            String title = movie.getString("original_title");
            movieTitlesArray.add(title);
        }
        return movieTitlesArray;
    }
    // @movieType
    // (topRated = 0)
    // (popular = 1)
    public static ArrayList<movieObject> getMovies(String nameJsonStr, int movieType) throws JSONException {
        ArrayList<movieObject> moviesArray = new ArrayList<>();
        JSONObject page = new JSONObject(nameJsonStr);
        JSONArray results = page.getJSONArray("results");
        for (int i = 0; i < results.length(); i++) {
            JSONObject movieJason = results.getJSONObject(i);
            String title = movieJason.getString("original_title");
            int id = movieJason.getInt("id");
            double vote = movieJason.getDouble("vote_average");
            String releaseDate = movieJason.getString("release_date");
            String overView = movieJason.getString("overview");
            String image = movieJason.getString("poster_path");
            movieObject movie = new movieObject();
            movie.setTitle(title);
            movie.setId(id);
            movie.setOverview(overView);
            movie.setImage(image);
            movie.setVoteRange(vote);
            movie.setYear(releaseDate);
            movie.setType(movieType);
            moviesArray.add(movie);
        }
        return moviesArray;
    }

    public static ArrayList<trailerObject> getTrailers(String nameJsonStr, int trailerType) throws JSONException {
        ArrayList<trailerObject> trailersArray = new ArrayList<>();
        JSONObject page = new JSONObject(nameJsonStr);
        JSONArray results = page.getJSONArray("results");
        for (int i = 0; i < results.length(); i++) {
            JSONObject trailerJason = results.getJSONObject(i);
            String title = trailerJason.getString("name");
            String id = trailerJason.getString("id");
            String key =trailerJason.getString("key");
            String type = trailerJason.getString("type");
            trailerObject trailer = new trailerObject();
            trailer.setTitle(title);
            trailer.setId(id);
            trailer.setKey(key);
            trailer.setType(type);
            trailersArray.add(trailer);
        }
        return trailersArray;
    }

    public static ArrayList<reviewObject> getReview(String nameJsonStr, int reviewType) throws JSONException {
        ArrayList<reviewObject> reviewsArray = new ArrayList<>();
        JSONObject page = new JSONObject(nameJsonStr);
        JSONArray results = page.getJSONArray("results");
        for (int i = 0; i < results.length(); i++) {
            JSONObject reviewJason = results.getJSONObject(i);
            String id = reviewJason.getString("id");
            String author = reviewJason.getString("author");
            String content = reviewJason.getString("content");
            reviewObject review = new reviewObject();
            review.setAuthor(author);
            review.setId(id);
            review.setContent(content);
            reviewsArray.add(review);
        }
        return reviewsArray;
    }
}
