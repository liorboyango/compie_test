package com.master1590.compie_test;

/**
 * Created by master1590 on 18/01/17.
 */

import java.util.HashMap;
        import java.util.List;

        import android.content.Context;
        import android.graphics.Typeface;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PlayListExpandableAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> playListsTitles; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<Video>> videosListByTitle;

    public PlayListExpandableAdapter(Context context, List<String> playListsTitles,
                                 HashMap<String, List<Video>> videosListByTitle) {
        this.context = context;
        this.playListsTitles = playListsTitles;
        this.videosListByTitle = videosListByTitle;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.videosListByTitle.get(this.playListsTitles.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Video childVideo = (Video) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.video_item, null);
        }

        TextView videoTitle = (TextView) convertView
                .findViewById(R.id.row_title);
        videoTitle.setText(childVideo.getTitle());

        ImageView videoThumb = (ImageView) convertView.findViewById(R.id.row_thumb);
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .showImageOnLoading(R.mipmap.ic_launcher)
                .build();
        imageLoader.displayImage(childVideo.getThumb(),videoThumb,options);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.youTubePlayerView!=null&& MainActivity.player!=null) {
                    MainActivity.youTubePlayerView.setVisibility(View.VISIBLE);
                    MainActivity.player.loadVideo(childVideo.getCode());
                }
            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.videosListByTitle.get(this.playListsTitles.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.playListsTitles.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.playListsTitles.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.playlist_item, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.row_title);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}