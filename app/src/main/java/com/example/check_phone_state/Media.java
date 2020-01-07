package com.example.check_phone_state;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class Media extends AppCompatActivity implements View.OnClickListener {
     ImageButton  pause,forward,back;
     SeekBar seekBar;
     static MediaPlayer mediaPlayer;
     Runnable runnable;
     Handler handler;
     Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_media);
         pause =findViewById(R.id.pause);
        seekBar =findViewById(R.id.seekabar);
        handler =new Handler();
        pause.setOnClickListener(this);
        toolbar =findViewById(R.id.mtoolbar);
        setSupportActionBar(toolbar);
      /*  forward.setOnClickListener(this);
        back.setOnClickListener(this);
        */
        if(mediaPlayer!= null)
        {
            try {
                mediaPlayer.stop();
            }
            catch (IllegalStateException e)
            {

            }
            mediaPlayer.release();
        }
        Bundle bundle = getIntent().getExtras();
        ArrayList<File> songs  = (ArrayList) bundle.getParcelableArrayList("songs");
        int position = bundle.getInt("position");
        Uri uri =Uri.parse(songs.get(position).toString());



         mediaPlayer =  MediaPlayer.create(Media.this, uri);


             mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                 @Override
                 public void onPrepared(MediaPlayer mp) {
                     seekBar.setMax(mp.getDuration());
                     mp.start();
                    changeseekbar();

                 }
             });

             seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                 @Override
                 public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                     if (fromUser) {
                         mediaPlayer.seekTo(progress);

                     }
                 }

                 @Override
                 public void onStartTrackingTouch(SeekBar seekBar) {

                 }

                 @Override
                 public void onStopTrackingTouch(SeekBar seekBar) {

                 }
             });
         }



    private void changeseekbar() {

        try {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            if(mediaPlayer.isPlaying())
            {
                runnable =new Runnable() {
                    @Override
                    public void run() {

                        changeseekbar();
                    }
                };}

            handler.postDelayed(runnable,1000);

        }
        catch (IllegalStateException e)
        {
          //  Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }




        }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.pause:

                if(mediaPlayer.isPlaying())
                {
                    mediaPlayer.pause();
                   // pause.setText(">");
                    pause.setBackgroundResource(R.drawable.ic_play_circle_filled_black_24dp);

                }
                else {

                    mediaPlayer.start();

                    pause.setBackgroundResource(R.drawable.ic_pause_circle_filled_black_24dp);
                  //  pause.setText("!!");

                    changeseekbar();

                }

           /*     break;
            case R.id.backward:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-5000);
                break;

            case  R.id.forward:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+5000);


                break;
                */
        }
        }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mediaPlayer.isPlaying())
        {
           // mediaPlayer.pause();
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }


    }


