package com.xinbob.circleprogressview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.xinbob.circleprogressview.custom.RingProgressView;

/**
 * 测试未闭合的环装进度指示器
 */
public class SecondActivity extends AppCompatActivity {

    private int mPercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        final RingProgressView ringProgressView = (RingProgressView) findViewById(R.id.ring_progress_view);

        AppCompatSeekBar seekBar = (AppCompatSeekBar) findViewById(R.id.seekbar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mPercent = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(SecondActivity.this, "percent = " + mPercent, Toast.LENGTH_SHORT).show();
            }
        });

        Button btnSetup = (Button) findViewById(R.id.btn_setup);
        btnSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ringProgressView.resetUI();
                ringProgressView.setProgress(mPercent);
            }
        });
    }
}
