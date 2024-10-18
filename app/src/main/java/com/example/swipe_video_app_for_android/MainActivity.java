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

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * MainActivity class that sets up the ViewPager2 to display videos to swipe.
 * Supports manually inputted URLs and handles lifecycle events to release VideoView resources.
 *
 * @author Daniel Tongu
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
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Apply window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize ViewPager with video list
        ViewPager2 viewPager = findViewById(R.id.VideosViewPager);
        List<Video> videoList = Arrays.asList(
                new Video("ID: DNLFV01", "Samson", "A PBS video I borrowed to demostrate the functionality of this app.",
                        "https://firebasestorage.googleapis.com/v0/b/swipe-video-app-33f88.appspot.com/o/videos%2Fsampson.mp4?alt=media&token=7934d17d-8ed4-4d3c-aecd-2baf720d7961"),

                new Video("ID: DNLFV02", "12 Man", "Seahawks flag up on the mountain. See that little blue peeking from the woods?",
                        "https://firebasestorage.googleapis.com/v0/b/swipe-video-app-33f88.appspot.com/o/videos%2F12man.MP4?alt=media&token=e45bd1d8-5b62-47fc-9cb7-875c0998fe1e"),

                new Video("ID: DNLFV03", "Hobbs Grove", "This is another borrowed video. Enjoy",
                        "https://firebasestorage.googleapis.com/v0/b/swipe-video-app-33f88.appspot.com/o/videos%2Fhobbsgrove.mp4?alt=media&token=cbd7dd9f-db1a-46be-9223-c027cb51d189"),

                new Video("ID: DNLFV04", "Lights", "Disco lights at a friends party.",
                        "https://firebasestorage.googleapis.com/v0/b/swipe-video-app-33f88.appspot.com/o/videos%2Fdiscolights.MOV?alt=media&token=df505942-46f6-4d00-b91c-0396f56e254e"),

                new Video("ID: DNLFV05", "Snow car", "Aftermath of a snow storm in CWU.",
                        "https://firebasestorage.googleapis.com/v0/b/swipe-video-app-33f88.appspot.com/o/videos%2Fsnowcar.mov?alt=media&token=f9ba2091-0e26-4276-a883-05bc24c16e17")

                );

        viewPager.setAdapter(new VideoAdapter(videoList));
        Toast.makeText(this, "Videos loaded", Toast.LENGTH_SHORT).show();
    }
}

/**
 * Video class that represents a video with its ID, title, description, and URL.
 * Implements Serializable to allow passing Video objects in Bundles.
 *
 * @author Daniel Tongu
 */
class Video implements Serializable {
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
    public Video(String id, String title, String description, String videoUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.videoUrl = videoUrl;
    }
}

/**
 * Adapter class to manage videos in the ViewPager2.
 *
 * @author Daniel Tongu
 */
class VideoAdapter extends RecyclerView.Adapter<VideoViewHolder> {
    private final List<Video> videoList;

    /**
     * Constructor for VideoAdapter.
     *
     * @param videoList The list of videos to display
     */
    public VideoAdapter(List<Video> videoList) {
        this.videoList = videoList;
    }

    /**
     * Called when RecyclerView needs a new {@link VideoViewHolder} of the given type to represent an item.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new VideoViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the item.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.setVideoData(videoList.get(position));
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of videos.
     */
    @Override
    public int getItemCount() {
        return videoList.size();
    }
}

/**
 * ViewHolder class that manages the view and data binding for each video item.
 *
 * @author Daniel Tongu
 */
class VideoViewHolder extends RecyclerView.ViewHolder {
    private final TextView TEXT_VIDEO_TITLE;
    private final TextView TEXT_VIDEO_ID;
    private final TextView TEXT_VIDEO_DESCRIPTION;
    private final VideoView VIDEO_VIEW;
    private final ProgressBar PROGRESSBAR;

    /**
     * Constructor for VideoViewHolder.
     *
     * @param itemView The view that represents the video.
     */
    public VideoViewHolder(@NonNull View itemView) {
        super(itemView);
        VIDEO_VIEW = itemView.findViewById(R.id.videoView);
        TEXT_VIDEO_TITLE = itemView.findViewById(R.id.textVideoTitle);
        TEXT_VIDEO_ID = itemView.findViewById(R.id.textVideoID);
        TEXT_VIDEO_DESCRIPTION = itemView.findViewById(R.id.textVideoDescription);
        PROGRESSBAR = itemView.findViewById(R.id.videoProgressBar);
    }

    /**
     * Sets the video data (title, description, video URL) for the ViewHolder.
     *
     * @param video The video containing the data to display.
     */
    public void setVideoData(Video video) {
        TEXT_VIDEO_TITLE.setText(video.title);
        TEXT_VIDEO_ID.setText(video.id);
        TEXT_VIDEO_DESCRIPTION.setText(video.description);
        VIDEO_VIEW.setVideoPath(video.videoUrl);

        VIDEO_VIEW.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                onVideoPrepared(mp);
            }
        });

        VIDEO_VIEW.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                onVideoCompletion(mp);
            }
        });
    }

    /**
     * Called when the video is prepared and ready to be played.
     * Adjusts the scaling and starts video playback.
     *
     * @param mp The MediaPlayer instance representing the prepared video.
     */
    public void onVideoPrepared(MediaPlayer mp) {
        PROGRESSBAR.setVisibility(View.GONE);
        mp.start();

        // Adjust video scaling to fit the screen
        float videoRatio = mp.getVideoWidth() / (float) mp.getVideoHeight();
        float screenRatio = VIDEO_VIEW.getWidth() / (float) VIDEO_VIEW.getHeight();
        VIDEO_VIEW.setScaleX(videoRatio / screenRatio >= 1f ? videoRatio / screenRatio : 1f / (videoRatio / screenRatio));
    }

    /**
     * Called when the video playback is completed.
     * Restarts the video to create a looping effect.
     *
     * @param mp The MediaPlayer instance representing the completed video.
     */
    public void onVideoCompletion(MediaPlayer mp) {
        mp.start(); // Loop the video playback
    }
}