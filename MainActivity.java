package com.example.stopwatch;

import android.os.Bundle;
import android.os.SystemClock;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.ComponentActivity;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends ComponentActivity {

    private TextView timerText;
    private Button startButton, pauseButton, resetButton;
    private Handler updateHandler;

    private boolean isRunning = false;
    private long startTime = 0;
    private long timeBuffer = 0;
    private long timeMillis = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerText = findViewById(R.id.timerText);
        startButton = findViewById(R.id.startButton);
        pauseButton = findViewById(R.id.pauseButton);
        resetButton = findViewById(R.id.resetButton);

        updateHandler = new Handler();

        startButton.setOnClickListener(v -> {
            if (!isRunning) {
                startStopwatch();
            }
        });

        pauseButton.setOnClickListener(v -> {
            if (isRunning) {
                pauseStopwatch();
            }
        });

        resetButton.setOnClickListener(v -> resetStopwatch());
    }

    private void startStopwatch() {
        isRunning = true;
        startButton.setText("Start");
        startTime = SystemClock.uptimeMillis();
        updateHandler.postDelayed(runnable, 0);
    }

    private void pauseStopwatch() {
        isRunning = false;
        timeBuffer += SystemClock.uptimeMillis() - startTime;
        updateHandler.removeCallbacks(runnable);
    }

    private void resetStopwatch() {
        isRunning = false;
        timeBuffer = 0;
        timeMillis = 0;
        timerText.setText("00:00:00");
        startButton.setText("Start");
        updateHandler.removeCallbacks(runnable);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            timeMillis = SystemClock.uptimeMillis() - startTime;
            long totalTime = timeBuffer + timeMillis;

            int seconds = (int) (totalTime / 1000);
            int minutes = seconds / 60;
            int millis = (int) (totalTime % 1000);

            timerText.setText(String.format("%02d:%02d:%03d", minutes, seconds % 60, millis));

            if (isRunning) {
                updateHandler.postDelayed(this, 10);
            }
        }
    };
}
