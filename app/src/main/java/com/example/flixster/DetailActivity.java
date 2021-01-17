package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Headers;

import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

public class DetailActivity extends YouTubeBaseActivity {

    private static final String YOUTUBE_API_KEY = "AIzaSyBQRsZODwfjCW-2zMQ8nbig4ugySbxy4NY";
    public static final String VIDEOS_URL = "https://api.themoviedb.org/3/movie/209112/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    TextView textView2;
    TextView textView;
    RatingBar ratingBar;
    YouTubePlayerView youTubePlayerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        textView2 = findViewById(R.id.textView2);
        textView = findViewById(R.id.textView);
        ratingBar = findViewById(R.id.ratingBar);
        youTubePlayerView = findViewById(R.id.player);

        //String title = getIntent().getStringExtra("title");

        Movie movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        //String overview = getIntent().getStringExtra("tv_overview");
        textView.setText(movie.getTitle());
        textView2.setText(movie.getOverview());
        ratingBar.setRating((float) movie.getRating());

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(VIDEOS_URL, movie.getMovieId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                try {
                    JSONArray results = json.jsonObject.getJSONArray("results");
                    if (results.length() == 0) {
                        return;
                    }
                    String youtubekey = results.getJSONObject(0).getString("key");
                    Log.d("DetailActivity", youtubekey);
                    initializeYoutube(youtubekey);
                } catch (JSONException e) {
                    Log.e("DetailActivity", "Failed to parse JSON", e);
                }
                //   e.printStackTrace();
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {

            }
        });
    }


    private void initializeYoutube(final String youtubekey) {
            youTubePlayerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                    Log.d("DetailActivity", "onInitializationSuccess");
                    youTubePlayer.cueVideo(youtubekey);

                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                    Log.d("DetailActivity", "onInitializationFailure");

                }
            });
        }

    }
