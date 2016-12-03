package com.example.amgad.imovies;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Amgad on 09-Oct-16.
 */

public class movieObject extends RealmObject {

    private String title;
    private String image;
    private String year;
    @PrimaryKey
    private int id;
    private int type;
    private double voteRange;
    private String overview;
    private boolean isFav;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getVoteRange() {
        return voteRange;
    }

    public void setVoteRange(double voteRange) {
        this.voteRange = voteRange;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}
