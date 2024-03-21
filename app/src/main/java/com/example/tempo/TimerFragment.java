package com.example.tempo;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class TimerFragment extends Fragment {

    private EditText editTextCountdown;
    private Button buttonStartPause;
    private Button buttonReset;

    private CountDownTimer countDownTimer;
    private boolean timerRunning;
    private long startTimeInMillis;
    private long timeLeftInMillis;
    private long endTime;

    private MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer, container, false);

        editTextCountdown = view.findViewById(R.id.edit_text_countdown);
        buttonStartPause = view.findViewById(R.id.button_start_pause);
        buttonReset = view.findViewById(R.id.button_reset);

        editTextCountdown.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No implementation needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No implementation needed
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No implementation needed
            }
        });

        buttonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        return view;
    }

    private void startTimer() {
        String input = editTextCountdown.getText().toString().trim();

        // Split the input into hours, minutes, and seconds
        String[] timeComponents = input.split(":");
        if (timeComponents.length != 3) {
            // Handle invalid input
            Toast.makeText(getActivity(), "Please enter time in HH:MM:SS format", Toast.LENGTH_SHORT).show();
            return;
        }

        int hours = Integer.parseInt(timeComponents[0]);
        int minutes = Integer.parseInt(timeComponents[1]);
        int seconds = Integer.parseInt(timeComponents[2]);

        // Calculate total milliseconds
        long totalTimeInMillis = ((hours * 60 + minutes) * 60 + seconds) * 1000;

        if (totalTimeInMillis <= 0) {
            Toast.makeText(getActivity(), "Please enter a valid time", Toast.LENGTH_SHORT).show();
            return;
        }

        startTimeInMillis = totalTimeInMillis;
        resetTimer();

        countDownTimer = new CountDownTimer(startTimeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountdownText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                updateButtons();
                playAlarm();
            }
        }.start();

        timerRunning = true;
        updateButtons();
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        timerRunning = false;
        updateButtons();
    }

    private void resetTimer() {
        timeLeftInMillis = startTimeInMillis;
        updateCountdownText();
        updateButtons();

        // Stop the alarm sound if it's playing
        stopAlarm();
    }

    private void stopAlarm() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void updateCountdownText() {
        int hours = (int) (timeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((timeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        editTextCountdown.setText(timeLeftFormatted);
    }

    private void updateButtons() {
        if (timerRunning) {
            buttonStartPause.setText("Pause");
            buttonReset.setVisibility(View.INVISIBLE);
        } else {
            buttonStartPause.setText("Start");
            if (timeLeftInMillis < 1000) {
                buttonStartPause.setVisibility(View.INVISIBLE);
            } else {
                buttonStartPause.setVisibility(View.VISIBLE);
            }

            if (timeLeftInMillis < startTimeInMillis) {
                buttonReset.setVisibility(View.VISIBLE);
            } else {
                buttonReset.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void playAlarm() {
        // Play your alarm sound here
        // For example:
        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.digital_timer);
        mediaPlayer.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
