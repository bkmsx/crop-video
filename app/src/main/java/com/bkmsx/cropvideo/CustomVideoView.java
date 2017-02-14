package com.bkmsx.cropvideo;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Environment;
import android.view.Surface;

/**
 * Created by bkmsx on 2/14/2017.
 */

public class CustomVideoView extends GLSurfaceView implements CustomRenderer.OnSurfaceTextureListener {
    CustomRenderer customRenderer;
    MediaPlayer mediaPlayer;
    SurfaceTexture surfaceTexture;
    Context context;
    public CustomVideoView(Context context) {
        super(context);
        this.context = context;
        setEGLContextClientVersion(2);
        customRenderer = new CustomRenderer(context, this);
        setRenderer(customRenderer);
    }

    public CustomRenderer getCustomRenderer() {
        return customRenderer;
    }

    @Override
    public void onSurfaceTextureCreated(SurfaceTexture surfaceTexture) {
        String videoPath = Environment.getExternalStorageDirectory() + "/p4.mp4";
        this.surfaceTexture = surfaceTexture;
        setVideoPath(videoPath);
    }

    public void changeFilter(final int type) {
        queueEvent(new Runnable() {
            @Override
            public void run() {
                customRenderer.setupProgram(type);
            }
        });
    }

    public void setVideoSize(float left, float right, float bottom, float top) {
        customRenderer.setupVideoSize(left, right, bottom, top);
    }

    public void setVideoPath(String videoPath) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(context, Uri.parse(videoPath));
        mediaPlayer.setSurface(new Surface(surfaceTexture));
        mediaPlayer.setLooping(true);
    }

    public void setVolume(float volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume, volume);
        }
    }

    public void seekTo(int value) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(value);
        }
    }

    public void play() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
