package com.example.swipe_video_app_for_android;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.FirebaseApp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * MainActivity class that sets up the ViewPager2 to display swipeable videos.
 * Supports manually inputted URLs and handles lifecycle events to release VideoView resources.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Called when the activity is first created.
     * Initializes the ViewPager2 and loads video URLs into the adapter.
     *
     * @param savedInstanceState Saved instance state from the previous state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // IDE generated code
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        //ViewPager for the video list
        ViewPager2 viewPager = findViewById(R.id.VideosViewPager);

        // List to store video information
        List<VideoItem> videoItemList = new ArrayList<>();
        videoItemList.add(new VideoItem("DNLFV1", "Brooks Library", "I was just recording a random piece of art.", "https://firebasestorage.googleapis.com/v0/b/swipe-video-app-33f88.appspot.com/o/videos%2Fsampson.mp4?alt=media&token=5f02185d-1a03-4e74-8d4d-da6f0daed7d1"));
        videoItemList.add(new VideoItem("DNLFV2", "Video 2", "This video was borrowed for demo purpose from my web programming class.", "https://firebasestorage.googleapis.com/v0/b/swipe-video-app-33f88.appspot.com/o/videos%2Fhobbsgrove.mp4?alt=media&token=11215306-d377-4ece-a3ec-14ba6c019e3b"));
        videoItemList.add(new VideoItem("DNLFV3", "Video 3", "Another borrowed video for demo purpose.", "https://firebasestorage.googleapis.com/v0/b/swipe-video-app-33f88.appspot.com/o/videos%2Fsampson.mp4?alt=media&token=5f02185d-1a03-4e74-8d4d-da6f0daed7d1"));

        viewPager.setAdapter(new VideoAdapter(videoItemList));
        Toast.makeText(this, "Videos loaded", Toast.LENGTH_SHORT).show();
    }
}

/**
 * VideoItem class that represents each video with its ID, title, description, and URL.
 * Implements Serializable to allow passing Video objects in Bundles.
 */
class VideoItem implements Serializable {
    /**Unique ID of the video*/
    public final String id;
    /**Title of the video*/
    public final String title;
    /**Description of the video*/
    public final String description;
    /**URL of the video to be played*/
    public final String videoUrl;

    /**
     * Constructor for the Video class.
     *
     * @param id          The unique ID of the video
     * @param title       The title of the video
     * @param description The description of the video
     * @param videoUrl    The URL of the video to be played
     */
    public VideoItem(String id, String title, String description, String videoUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.videoUrl = videoUrl;
    }
}


class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private final List<VideoItem> videoItems;

    public VideoAdapter(List<VideoItem> videoItems) {
        this.videoItems = videoItems;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.setVideoData(videoItems.get(position));
    }

    @Override
    public int getItemCount() {
        return videoItems.size();
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView textVideoTitle, textVideoId, textVideoDescription;
        VideoView videoView;
        ProgressBar progressBar;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
            textVideoTitle = itemView.findViewById(R.id.textVideoTitle);
            textVideoId = itemView.findViewById(R.id.textVideoID);
            textVideoDescription = itemView.findViewById(R.id.textVideoDescription);
            progressBar = itemView.findViewById(R.id.videoProgressBar);
        }

        void setVideoData(VideoItem videoItem) {
            textVideoTitle.setText(videoItem.title);
            textVideoId.setText(videoItem.id);
            textVideoDescription.setText(videoItem.description);
            videoView.setVideoPath(videoItem.videoUrl);

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    progressBar.setVisibility(View.GONE);
                    mp.start();

                    float videoRatio = mp.getVideoWidth() / (float) mp.getVideoHeight();
                    float screenRatio = videoView.getWidth() / (float) videoView.getHeight();
                    float scale = videoRatio / screenRatio;

                    videoView.setScaleX(scale >= 1f ? scale : 1f / scale);
                }
            });

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
        }
    }
}