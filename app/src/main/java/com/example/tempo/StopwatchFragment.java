package com.example.tempo;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class StopwatchFragment extends Fragment implements View.OnClickListener {
    private TextView timerTextView;

    /*private static final int START_BUTTON_ID = 1;
    private static final int STOP_BUTTON_ID = 2;
    private static final int RESET_BUTTON_ID = 3;*/
    private FloatingActionButton startButton, stopButton, resetButton;
    private long startTime = 0L;
    private Handler handler = new Handler();
    private long millisecondTime, timeSwapBuff, updateTime = 0L;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stopwatch, container, false);
        timerTextView = view.findViewById(R.id.TimeKeep);

        startButton = (FloatingActionButton) view.findViewById(R.id.start);
        stopButton = (FloatingActionButton) view.findViewById(R.id.Stop);
        resetButton = (FloatingActionButton) view.findViewById(R.id.Restart);

        startButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.start) {
            startTime = System.currentTimeMillis();
            handler.postDelayed(runnable, 0);
        } else if (v.getId() == R.id.Stop) {
            timeSwapBuff += millisecondTime;
            handler.removeCallbacks(runnable);
        } else if (v.getId() == R.id.Restart) {
            handler.removeCallbacks(runnable);
            millisecondTime = 0L;
            timeSwapBuff = 0L;
            startTime = 0L;
            updateTime = 0L;
            timerTextView.setText("00:00:00");
        }
    }

    public Runnable runnable = new Runnable() {
        public void run() {
            millisecondTime = System.currentTimeMillis() - startTime;
            updateTime = timeSwapBuff + millisecondTime;
            int seconds = (int) (updateTime / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            int milliseconds = (int) (updateTime % 1000);
            timerTextView.setText("" + String.format("%02d", minutes) + ":"
                    + String.format("%02d", seconds) + ":"
                    + String.format("%03d", milliseconds));
            handler.postDelayed(this, 0);
        }
    };
}
