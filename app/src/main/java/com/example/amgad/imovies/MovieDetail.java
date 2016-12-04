package com.example.amgad.imovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

import static com.example.amgad.imovies.MainFragment.EXTRA_MOVIE_ID;

public class MovieDetail extends AppCompatActivity {

    private static final String LOG_TAG = MovieDetail.class.getSimpleName();
    TextView Movie_Name;
    TextView Movie_Year;
    TextView Movie_OverView;
    ImageView Movie_Image;
    TextView Movie_Rate;
    ListView List_View;
    ListView List_View2;
    int Movie_ID;
    private ShareActionProvider mShareActionProvider;
    ImageButton favButton;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Movie_Name = (TextView) findViewById(R.id.movie_name);
        Movie_Year = (TextView) findViewById(R.id.movie_year);
        Movie_OverView = (TextView) findViewById(R.id.movie_detail);
        Movie_Image = (ImageView) findViewById(R.id.movie_image);
        Movie_Rate = (TextView) findViewById(R.id.rate);
        favButton = (ImageButton) findViewById(R.id.Fav_button);
        List_View = (ListView) findViewById(R.id.trailer_list);
        List_View2 = (ListView) findViewById(R.id.review_list);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        realm = Realm.getInstance(realmConfiguration);

        Intent intent = getIntent();
        Movie_ID = intent.getIntExtra(EXTRA_MOVIE_ID, 0);
        final movieObject movie = realm.where(movieObject.class).equalTo("id", Movie_ID).findFirst();
        Movie_Name.setText(movie.getTitle());
        Movie_Year.setText(movie.getYear());
        Movie_OverView.setText(movie.getOverview());
        Picasso.with(getBaseContext()).load("http://image.tmdb.org/t/p/w185/" + movie.getImage()).into(Movie_Image);
        Movie_Rate.setText(String.valueOf(movie.getVoteRange()));

        if (movie.isFav()) {
            favButton.setImageResource(R.drawable.like_48px);
        } else {
            favButton.setImageResource(R.drawable.unlike_50px);
        }

        new trailerLoading().execute("trailer");
        new reviewLoading().execute("review");

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realm.beginTransaction();
                movie.setFav((!movie.isFav()));
                realm.copyToRealmOrUpdate(movie);
                realm.commitTransaction();
                if (movie.isFav()) {
                    favButton.setImageResource(R.drawable.like_48px);
                } else {
                    favButton.setImageResource(R.drawable.unlike_50px);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.detail_fragment, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.action_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(setShareIntent());
        } else {
            Log.d(LOG_TAG, "Share Action Provider is null?");
        }
        // Return true to display menu
        return true;
    }

    private Intent setShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                "https://www.themoviedb.org/movie/" + Movie_ID);
        return shareIntent;
    }

    private class trailerLoading extends AsyncTask {
        ArrayList<trailerObject> trailerObjectArrayList;

        @Override
        protected void onPostExecute(Object o) {
            if (null != o) {
                if (o == "trailer") {
                    trailerAdapter trailerAdapter = new trailerAdapter(getBaseContext(), R.layout.trailer_item, R.id.trailer_title, trailerObjectArrayList);
                    List_View.setAdapter(trailerAdapter);
                    setListViewHeightBasedOnChildren(List_View);

                } else {
                    trailerAdapter trailerAdapter = new trailerAdapter(getBaseContext(), R.layout.trailer_item, R.id.trailer_title, trailerObjectArrayList);
                    List_View.setAdapter(trailerAdapter);
                    setListViewHeightBasedOnChildren(List_View);

                }
            }
            super.onPostExecute(o);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonStr = null;

            try {
                URL url;
                if (objects[0].equals("trailer")) {
                    url = new URL("http://api.themoviedb.org/3/movie/" + Movie_ID + "/videos?api_key=19ad67fedf195474947ef34e79e3b14c");
                } else {
                    url = new URL("http://api.themoviedb.org/3/movie/" + Movie_ID + "reviews?api_key=19ad67fedf195474947ef34e79e3b14c");
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

                if (objects[0].equals("trailer")) {
                    trailerObjectArrayList = MovieData.getTrailers(movieJsonStr, 0);
                } else {
                    trailerObjectArrayList = MovieData.getTrailers(movieJsonStr, 1);
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


    private class reviewLoading extends AsyncTask {
        ArrayList<reviewObject> reviewObjectArrayList;

        @Override
        protected void onPostExecute(Object o) {
            if (null != o) {
                if (o == "review") {
                    reviewAdapter reviewAdapter = new reviewAdapter(getBaseContext(), R.layout.review_item, R.id.review_author, reviewObjectArrayList);
                    reviewAdapter reviewAdapter1 = new reviewAdapter(getBaseContext(), R.layout.review_item, R.id.review_content, reviewObjectArrayList);
                    List_View2.setAdapter(reviewAdapter);
                    List_View2.setAdapter(reviewAdapter1);
                    setListViewHeightBasedOnChildren(List_View2);
                } else {
                    reviewAdapter reviewAdapter = new reviewAdapter(getBaseContext(), R.layout.review_item, R.id.review_author, reviewObjectArrayList);
                    reviewAdapter reviewAdapter1 = new reviewAdapter(getBaseContext(), R.layout.review_item, R.id.review_content, reviewObjectArrayList);
                    List_View2.setAdapter(reviewAdapter1);
                    List_View2.setAdapter(reviewAdapter);
                    setListViewHeightBasedOnChildren(List_View2);

                }
            }
            super.onPostExecute(o);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonStr = null;

            try {
                URL url;
                if (objects[0].equals("review")) {
                    url = new URL("http://api.themoviedb.org/3/movie/" + Movie_ID + "/reviews?api_key=19ad67fedf195474947ef34e79e3b14c");
                } else {
                    url = new URL("http://api.themoviedb.org/3/movie/" + Movie_ID + "reviews?api_key=19ad67fedf195474947ef34e79e3b14c");
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

                if (objects[0].equals("review")) {
                    reviewObjectArrayList = MovieData.getReview(movieJsonStr, 0);
                } else {
                    reviewObjectArrayList = MovieData.getReview(movieJsonStr, 1);
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

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
