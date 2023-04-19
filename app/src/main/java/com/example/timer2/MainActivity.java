package com.example.timer2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

// Ayan Masood ID: 221228094
    EditText mTimer;
    EditText mRest;
    TextView tTime;
    Button sStart, sStop, sSet;
    ProgressBar bBar;
    boolean mIsResting = false;

    private boolean mTimerRunning;

    private CountDownTimer mCount;
    private CountDownTimer mCount2;
    private boolean rRunning;
    private long mStartTimeMili;
    private long mRestTimeMili;
    long mRestTimeLeftMili;

    private long mTimeLeftMili;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTimer = findViewById(R.id.TIMER);
        mRest = findViewById(R.id.REST);
        sStart = findViewById(R.id.START);
        sStop = findViewById(R.id.STOP);
        sSet = findViewById(R.id.SET);
        bBar = findViewById(R.id.progressBar);
        tTime = findViewById(R.id.textView2);

//get details for timer
        sSet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (mTimerRunning){
                    mCount.cancel();
                }else if (mIsResting){
                    mCount2.cancel();
                    mIsResting=false;
                }
                //getting user input
                String winput = mTimer.getText().toString();
                String rinput = mRest.getText().toString();
                if (winput.length()==0 || rinput.length()==0){
                    Toast.makeText(MainActivity.this, "Cant be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                long workoutLong= Long.parseLong(winput) *60000;
                long restLong= Long.parseLong(rinput) *60000;
                if (workoutLong==0 || restLong==0 ){
                    Toast.makeText(MainActivity.this, "please enter above 0", Toast.LENGTH_SHORT).show();
                    return;
                }
                setTime(workoutLong, restLong);
                mTimer.setText("");
                mRest.setText("");
            }
        });

//start function to button
        sStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

               startTimer();
                //setting text to empty
                mTimer.setText("");
                mRest.setText("");


            }
        });
//pause function to button
        sStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pauseTimer();
            }
        });

    }
    //taking user input and setting time
    private void setTime(long wmilliseconds,long rmilliseconds){
        mStartTimeMili = wmilliseconds;
        mRestTimeMili = rmilliseconds;
        resetTimer();
    }
    //timer operation
    private void startTimer() {

        //timer 1
        mCount = new CountDownTimer(mTimeLeftMili, 1) {
            @Override
            public void onTick(long milliLeft) {
                mTimeLeftMili= milliLeft; //milliseconds left to finish
                updateText();
            }

            @Override
            public void onFinish() {
                Toast.makeText(MainActivity.this, "Workout complete", Toast.LENGTH_SHORT).show();
                mTimerRunning = false;
                //on finish activate boolean to start timer 2
                mIsResting = true;
                mCount2 = new CountDownTimer(mRestTimeLeftMili, 1) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        mRestTimeLeftMili = millisUntilFinished;
                        updateText();
                    }

                    @Override
                    public void onFinish() {
                        //display text and set condition to false
                        mIsResting = false;
                        Toast.makeText(MainActivity.this, "Timer has been completed", Toast.LENGTH_SHORT).show();
                    }
                }.start();

            }

        }.start();

        mTimerRunning = true;

    }



    private void resetTimer(){
        mTimeLeftMili = mStartTimeMili;
        mRestTimeLeftMili=mRestTimeMili;

        updateText();
    }

    //pause on null reference check
private void pauseTimer(){
        if (mIsResting && mCount2 != null){
            mCount2.cancel();
        }else if (mCount !=null){
            mCount.cancel();
        }
        mTimerRunning=false;
}

    private void updateText() {
        //update the text for the appropriate timer
        if (mIsResting) {
            //formatting into minutes hours and seconds
            int rHours = (int) (mRestTimeLeftMili / 1000) / 3600;
            int rMinutes = (int) ((mRestTimeLeftMili / 1000) % 3600) / 60;
            int rSeconds = (int) (mRestTimeLeftMili / 1000) % 60;
            String rFormat;
            if (rHours > 0) {
                rFormat = String.format(Locale.getDefault(), "%d:%02d:%02d", rHours, rMinutes, rSeconds);
            } else {
                rFormat = String.format(Locale.getDefault(), "%02d:%02d", rMinutes, rSeconds);
            }
            tTime.setText("Rest Time: " + rFormat);
            //update progress bar
            int progress = (int) ((mRestTimeMili - mRestTimeLeftMili) * 100 / mRestTimeMili);
            bBar.setProgress(progress);
        } else {
            int wHours = (int) (mTimeLeftMili / 1000) / 3600;
            int wMinutes = (int) ((mTimeLeftMili / 1000) % 3600) / 60;
            int wSeconds = (int) (mTimeLeftMili / 1000) % 60;
            String wFormat;
            if (wHours > 0) {
                wFormat = String.format(Locale.getDefault(), "%d:%02d:%02d", wHours, wMinutes, wSeconds);
            } else {
                wFormat = String.format(Locale.getDefault(), "%02d:%02d", wMinutes, wSeconds);
            }
            tTime.setText("Workout Time: " + wFormat);
            //update progress bar
            int progress = (int) ((mStartTimeMili - mTimeLeftMili) * 100 / mStartTimeMili);
            bBar.setProgress(progress);
        }



    }

}