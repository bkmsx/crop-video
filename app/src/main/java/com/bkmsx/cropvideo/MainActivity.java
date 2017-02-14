package com.bkmsx.cropvideo;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    RelativeLayout mainLayout;
    RelativeLayout layoutVideoView;
    CustomVideoView customVideoView;
    CustomRenderer customRenderer;

    boolean play = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        findViewById(R.id.btn_next).setOnClickListener(this);
        findViewById(R.id.btn_pause).setOnClickListener(this);
        Point[] points = new Point[4];
        points[0] = new Point(100, 100);
        points[1] = new Point(800, 1000);
        points[2] = new Point(300, 300);
        points[3] = new Point(600, 700);
        CropFrame cropFrame = new CropFrame(this, points);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        layoutVideoView = (RelativeLayout) findViewById(R.id.layout_videoview);

        customVideoView = new CustomVideoView(this);
        layoutVideoView.addView(customVideoView, params);
        customRenderer = customVideoView.getCustomRenderer();
        layoutVideoView.addView(cropFrame, params);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                customVideoView.changeFilter(1);
                break;
            case R.id.btn_pause:
                if (play) {
                    customVideoView.pause();
                    play = false;
                } else {
                    customVideoView.play();
                    play = true;
                }
        }
    }
}
