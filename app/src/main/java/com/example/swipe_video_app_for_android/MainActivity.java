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
        List<VideoItem> videoItemList = Arrays.asList(
                new VideoItem("DNLFV1", "Brooks Library", "A random piece of art.",
                        "https://firebasestorage.googleapis.com/v0/b/swipe-video-app-33f88.appspot.com/o/videos%2Fsampson.mp4?alt=media&token=5f02185d-1a03-4e74-8d4d-da6f0daed7d1"),
                new VideoItem("DNLFV2", "Video 2", "Borrowed for demo purposes.",
                        "https://firebasestorage.googleapis.com/v0/b/swipe-video-app-33f88.appspot.com/o/videos%2Fhobbsgrove.mp4?alt=media&token=11215306-d377-4ece-a3ec-14ba6c019e3b"),
                new VideoItem("DNLFV3", "Video 3", "Another demo video.",
                        "https://firebasestorage.googleapis.com/v0/b/swipe-video-app-33f88.appspot.com/o/videos%2Fsampson.mp4?alt=media&token=5f02185d-1a03-4e74-8d4d-da6f0daed7d1")
        );
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
     * Constructor for the VideoItem class.
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

/**
 * Adapter class to manage video items in the ViewPager2.
 */
class VideoAdapter extends RecyclerView.Adapter<VideoViewHolder> {
    private final List<VideoItem> videoItems;

    /**
     * Constructor for VideoAdapter.
     *
     * @param videoItems The list of video items to display
     */
    public VideoAdapter(List<VideoItem> videoItems) {
        this.videoItems = videoItems;
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
        holder.setVideoData(videoItems.get(position));
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of video items.
     */
    @Override
    public int getItemCount() {
        return videoItems.size();
    }
}

/**
 * ViewHolder class that manages the view and data binding for each video item.
 */
class VideoViewHolder extends RecyclerView.ViewHolder {
    TextView textVideoTitle, textVideoId, textVideoDescription;
    VideoView videoView;
    ProgressBar progressBar;

    /**
     * Constructor for VideoViewHolder.
     *
     * @param itemView The view that represents the video item.
     */
    public VideoViewHolder(@NonNull View itemView) {
        super(itemView);
        videoView = itemView.findViewById(R.id.videoView);
        textVideoTitle = itemView.findViewById(R.id.textVideoTitle);
        textVideoId = itemView.findViewById(R.id.textVideoID);
        textVideoDescription = itemView.findViewById(R.id.textVideoDescription);
        progressBar = itemView.findViewById(R.id.videoProgressBar);
    }

    /**
     * Sets the video data (title, description, video URL) for the ViewHolder.
     *
     * @param videoItem The video item containing the data to display.
     */
    public void setVideoData(VideoItem videoItem) {
        textVideoTitle.setText(videoItem.title);
        textVideoId.setText(videoItem.id);
        textVideoDescription.setText(videoItem.description);
        videoView.setVideoPath(videoItem.videoUrl);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                onVideoPrepared(mp);
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
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
        progressBar.setVisibility(View.GONE);
        mp.start();

        // Adjust video scaling to fit the screen
        float videoRatio = mp.getVideoWidth() / (float) mp.getVideoHeight();
        float screenRatio = videoView.getWidth() / (float) videoView.getHeight();
        videoView.setScaleX(videoRatio / screenRatio >= 1f ? videoRatio / screenRatio : 1f / (videoRatio / screenRatio));
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