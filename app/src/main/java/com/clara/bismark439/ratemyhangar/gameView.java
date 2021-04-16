package com.clara.bismark439.ratemyhangar;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.clara.bismark439.ratemyhangar.MainActivity.muteSound;
import static com.clara.bismark439.ratemyhangar.MainActivity.soundList;

public class gameView extends LinearLayout {
    static int ans=0;
    static int myScore=0;
    TextView answer;
    private MediaPlayer mMediaPlayer;
    TextView score;
    public gameView(Context context) {
        super(context);
        init(context);
    }

    public gameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.res_game_view, this, true);
        answer=(TextView)findViewById(R.id.g_ans);
        score=(TextView)findViewById(R.id.prompt);
    }

    public void promptInit(){
        int number=3*1000;
        CountDownTimer cdt = new CountDownTimer(number, 500) {//1000
            public void onTick(long millisUntilFinished) {
                if(millisUntilFinished<1500){
                 score.setText("Turn on the sound!");
                }
            }
            public void onFinish() {
                answer.setVisibility(VISIBLE);
                updateScore();
                playAudio();
            }
        }.start();
    }
    public void updateScore(){
        score.setText("Score: "+myScore);
    }

    public void playAudio() {
        ans=(int)(Math.random()*5);
        try {
            killMediaPlayer();
            int resID = getResources().getIdentifier(soundList[ans], "raw", getContext().getPackageName());
            mMediaPlayer = MediaPlayer.create(getContext().getApplicationContext(), resID);
           if(!muteSound){ mMediaPlayer.start();}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void killMediaPlayer() {
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
