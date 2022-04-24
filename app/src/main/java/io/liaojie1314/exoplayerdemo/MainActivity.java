package io.liaojie1314.exoplayerdemo;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

public class MainActivity extends AppCompatActivity {

    private PlayerView mPlayerView;
    private ProgressBar mProgressBar;
    private ImageView mBtFullScreen;
    SimpleExoPlayer mSimpleExoPlayer;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        mPlayerView = findViewById(R.id.player_view);
        mProgressBar = findViewById(R.id.progress_bar);
        mBtFullScreen = mPlayerView.findViewById(R.id.bt_fullscreen);

        Uri videoUrl = Uri.parse("https://vt1.doubanio.com/202202092347/9ef9a7a6f6014b1e97b43ec77a8718e3/view/movie/M/402860140.mp4");

        mSimpleExoPlayer = new SimpleExoPlayer.Builder(this).build();
        mPlayerView.setPlayer(mSimpleExoPlayer);
        mPlayerView.setKeepScreenOn(true);
        // Build the media item.
        MediaItem mediaItem = MediaItem.fromUri(videoUrl);
        // Set the media item to be played.
        mSimpleExoPlayer.setMediaItem(mediaItem);
        //设置播放器是否当装备好就播放， 如果看源码可以看出，ExoPlayer的play()方法也是调用的这个方法
        mSimpleExoPlayer.setPlayWhenReady(true);
        //资源准备，如果设置 setPlayWhenReady(true) 则资源准备好就立马播放。
        mSimpleExoPlayer.prepare();
        mSimpleExoPlayer.play();
        mSimpleExoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == Player.STATE_BUFFERING) {
                    mProgressBar.setVisibility(View.VISIBLE);
                } else if (playbackState == Player.STATE_READY) {
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });
        int orientation = this.getResources().getConfiguration().orientation;
        mBtFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    mBtFullScreen.setImageDrawable(getResources()
                            .getDrawable(R.drawable.ic_fullscreen));
                    flag = false;
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    mBtFullScreen.setImageDrawable(getResources()
                            .getDrawable(R.drawable.ic_fullscreen_exit));
                    flag = true;
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSimpleExoPlayer.setPlayWhenReady(false);
        mSimpleExoPlayer.getPlaybackState();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSimpleExoPlayer.setPlayWhenReady(true);
        mSimpleExoPlayer.getPlaybackState();
    }
}