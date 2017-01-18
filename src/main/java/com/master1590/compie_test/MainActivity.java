package com.master1590.compie_test;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener {

    public static YouTubePlayerView youTubePlayerView;
    public static YouTubePlayer player;
    private final static String url_get_data = "http://www.razor-tech.co.il/hiring/youtube-api.json";
    private ArrayList<PlayList> playLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initImageLoader();
        initPlayer();
        new GetPlayLists().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(player==null){
            initPlayer();
        }
    }

    @Override
    public void onBackPressed() {
        if (youTubePlayerView!=null && youTubePlayerView.getVisibility()==View.VISIBLE) {
            if(player!=null)
                player.pause();
            youTubePlayerView.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult result) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getResources().getString(R.string.no_youtube_title));
        builder.setMessage(getResources().getString(R.string.no_youtube_message));
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.install_now), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=com.google.android.youtube"));
                try { //Try Google play
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.exit), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finish();
            }
        });
        builder.show();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {
        this.player = player;
    }

    private void initPlayer(){
        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubePlayerView.initialize(Config.DEVELOPER_KEY, this);
        youTubePlayerView.setVisibility(View.GONE);
    }

    private void setPlayListsList() {
        ExpandableListView playListList = (ExpandableListView) findViewById(R.id.playlists);
        ArrayList<String> playListsTitles  = new ArrayList<>();
        HashMap<String,List<Video>> listData = new HashMap<>();
        for(PlayList playList : playLists){
            listData.put(playList.getTitle(),playList.getVideos());
            playListsTitles.add(playList.getTitle());
        }
        PlayListExpandableAdapter playListAdapter = new PlayListExpandableAdapter(this, playListsTitles, listData);
        playListList.setAdapter(playListAdapter);

    }

    private void initImageLoader(){
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .build();
        ImageLoader.getInstance().init(config);
    }

    class GetPlayLists extends AsyncTask<String, String, JSONObject> {


        protected JSONObject doInBackground(String... args) {
            try {
                HashMap<String, String> params = new HashMap<>();


                JSONParser jsonParser = new JSONParser();
                return jsonParser.makeHttpRequest(url_get_data,
                        "POST", params);


            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }


        }

        protected void onPostExecute(JSONObject result) {


            if (result != null) {
                try {
                    playLists.clear();
                    JSONArray ja;
                    ja = result.getJSONArray("Playlists");

                    for (int i = 0; i < ja.length(); i++) {
                        PlayList playList = new PlayList();
                        JSONObject jo = ja.getJSONObject(i);
                        playList.setTitle(jo.getString("ListTitle"));

                        JSONArray jaVideos = jo.getJSONArray("ListItems");

                        for (int j = 0; j < jaVideos.length(); j++) {
                            JSONObject joVideos = jaVideos.getJSONObject(j);
                            Video video = new Video();
                            video.setTitle(joVideos.getString("Title"));
                            video.setLink(joVideos.getString("link"));
                            video.setThumb(joVideos.getString("thumb"));
                            playList.addVideo(video);
                        }

                        playLists.add(playList);
                    }

                    setPlayListsList();

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("GetAllPLayLists", "Couldn't get any data from the url");
            }


        }
    }
}
