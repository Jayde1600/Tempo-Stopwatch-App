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

public class StopwatchFragment extends Fragment {
    private TextView timerTextView;
    private FloatingActionButton startButton, stopButton, resetButton;
    private long startTime = 0L;
    private Handler handler = new Handler();
    private long millisecondTime, timeSwapBuff, updateTime = 0L;
    private boolean isTimerRunning = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stopwatch, container, false);
        timerTextView = view.findViewById(R.id.TimeKeep);

        startButton = view.findViewById(R.id.StartWatch);
        stopButton = view.findViewById(R.id.StoppedWatch);
        resetButton = view.findViewById(R.id.RestartWatch);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStartButtonClick();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStopButtonClick();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResetButtonClick();
            }
        });

        return view;
    }

    private void onStartButtonClick() {
        if (!isTimerRunning) {
            startTime = System.currentTimeMillis();
            handler.postDelayed(runnable, 0);
            isTimerRunning = true;
        }
    }

    private void onStopButtonClick() {
        if (isTimerRunning) {
            timeSwapBuff += millisecondTime;
            handler.removeCallbacks(runnable);
            isTimerRunning = false;
        }
    }

    private void onResetButtonClick() {
        handler.removeCallbacks(runnable);
        millisecondTime = 0L;
        timeSwapBuff = 0L;
        startTime = 0L;
        updateTime = 0L;
        timerTextView.setText("00:00:00");
        isTimerRunning = false;
    }

    private Runnable runnable = new Runnable() {
        public void run() {
            millisecondTime = System.currentTimeMillis() - startTime;
            updateTime = timeSwapBuff + millisecondTime;
            int seconds = (int) (updateTime / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            int milliseconds = (int) (updateTime % 1000);
            timerTextView.setText(String.format("%02d:%02d:%03d", minutes, seconds, milliseconds));
            handler.postDelayed(this, 0);
        }
    };
}
