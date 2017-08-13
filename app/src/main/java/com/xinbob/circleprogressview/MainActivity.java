package com.xinbob.circleprogressview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.xinbob.circleprogressview.custom.ProgressIndicatorView;

public class MainActivity extends AppCompatActivity {

    private int mPercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ProgressIndicatorView progressView = (ProgressIndicatorView) findViewById(R.id.progressIndicatorView);
        // progressView.setProgressPercent(80);

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
                Toast.makeText(MainActivity.this, "percent = " + mPercent, Toast.LENGTH_SHORT).show();
            }
        });

        Button btnSetup = (Button) findViewById(R.id.btn_setup);
        btnSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressView.resetUI();
                progressView.setProgressPercent(mPercent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_action_start_second){
            startActivity(new Intent(this, SecondActivity.class));
        }
        return true;
    }
}
