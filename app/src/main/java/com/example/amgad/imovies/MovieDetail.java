package com.example.amgad.imovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import static com.example.amgad.imovies.MainFragment.EXTRA_MOVIE_ID;

public class MovieDetail extends AppCompatActivity {

    private static final String LOG_TAG =MovieDetail.class.getSimpleName() ;
    TextView Movie_Name;
    TextView Movie_Year;
    TextView Movie_OverView;
    ImageView Movie_Image;
    TextView Movie_Rate;
    int Movie_ID ;
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
        favButton =(ImageButton) findViewById(R.id.Fav_button);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        realm = Realm.getInstance(realmConfiguration);

        Intent intent = getIntent();
        Movie_ID= intent.getIntExtra(EXTRA_MOVIE_ID,0);
        final movieObject movie = realm.where(movieObject.class).equalTo("id",Movie_ID ).findFirst();
        Movie_Name.setText(movie.getTitle());
        Movie_Year.setText(movie.getYear());
        Movie_OverView.setText(movie.getOverview());
        Picasso.with(getBaseContext()).load("http://image.tmdb.org/t/p/w185/" + movie.getImage()).into(Movie_Image);
        Movie_Rate.setText(String.valueOf(movie.getVoteRange()));

        if (movie.isFav()){
            favButton.setImageResource(R.drawable.like_48px);
        }else{
            favButton.setImageResource(R.drawable.unlike_50px);
        }

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realm.beginTransaction();
                movie.setFav((!movie.isFav()));
                realm.copyToRealmOrUpdate(movie);
                realm.commitTransaction();
                if (movie.isFav()){
                    favButton.setImageResource(R.drawable.like_48px);
                }else{
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
                "https://www.themoviedb.org/movie/" +Movie_ID);
        return shareIntent;
    }
}
