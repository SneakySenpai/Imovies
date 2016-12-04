package com.example.amgad.imovies;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by Amgad on 09-Oct-16.
 */

public class MainFragment extends Fragment {

    RecyclerView recyclerView;
    MainFragment mContext;
    movieObject movieObject;
    public static final String EXTRA_MOVIE_ID = "Movie_ID";
    private Realm realm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mContext = this;
        setHasOptionsMenu(true);
        FetchMovieData fetchMovieData = new FetchMovieData();
        fetchMovieData.execute("popular");
        recyclerView = (RecyclerView) rootView.findViewById(R.id.gridView);
        Realm.init(getActivity());
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();

        // Clear the realm from last time
        // Realm.deleteRealm(realmConfiguration);
        // Create a new empty instance of Realm
        realm = Realm.getInstance(realmConfiguration);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close(); // Remember to close Realm when done.
    }

    public class FetchMovieData extends AsyncTask {
        private final String LOG_TAG = FetchMovieData.class.getSimpleName();
        ArrayList<movieObject> movieObjectArrayList;

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(movieObjectArrayList);
            realm.commitTransaction();
            // Pull all the cities from the realm
            RealmResults<movieObject> movies;
            if (null != o) {
                if (o == "top_rated") {
                    movies = realm.where(movieObject.class).equalTo("type", 0).findAll();
                } else {
                    movies = realm.where(movieObject.class).equalTo("type", 1).findAll();
                }
                movieAdapter movieAdapter = new movieAdapter(mContext, movies);
                RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(movieAdapter);
            }
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieJsonStr = null;

            try {
                URL url;
                if (objects[0].equals("top_rated")) {
                    url = new URL("http://api.themoviedb.org/3/movie/top_rated?api_key=19ad67fedf195474947ef34e79e3b14c");
                } else {
                    url = new URL("http://api.themoviedb.org/3/movie/popular?api_key=19ad67fedf195474947ef34e79e3b14c");
                }
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                movieJsonStr = buffer.toString();
                Log.e("forecastJsonStr", movieJsonStr);
                if (objects[0].equals("top_rated")) {
                    movieObjectArrayList = MovieData.getMovies(movieJsonStr, 0);
                } else {
                    movieObjectArrayList = MovieData.getMovies(movieJsonStr, 1);
                }
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            return objects[0];
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int id = item.getItemId();
        switch (id) {
            case R.id.topRated:
                FetchMovieData movieTask = new FetchMovieData();
                movieTask.execute("top_rated");
                break;
            case R.id.mostPopular:
                movieTask = new FetchMovieData();
                movieTask.execute("popular");
                break;
            case R.id.favourite:
                // Pull all the cities from the realm
                RealmResults<movieObject> movies;
                movies = realm.where(movieObject.class).equalTo("isFav", true).findAll();
                movieAdapter movieAdapter = new movieAdapter(mContext, movies);
                RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(movieAdapter);
                break;
        }
        return super.
                onOptionsItemSelected(item);
    }
}