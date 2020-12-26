package com.example.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

public class MusicService extends Service {
    private MediaPlayer player;
    private Timer timer;

    public MusicService() {}

    @Override
    public IBinder onBind(Intent intent) {
/*        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");*/
        return new MusicControl();//创建音乐播放器对象
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player == null)
            return;
        if (player.isPlaying())
            player.stop();//停止音乐播放
        player.release();
        player = null;
    }

    public void addTimer(){
        if (timer == null){
            timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                   if(player == null)
                        return;
                    int duration = player.getDuration();
                    int currentPostition = player.getCurrentPosition();
                    Message msg = MainActivity.handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putInt("duration",duration);
                    bundle.putInt("currentPostition",currentPostition);
                    msg.setData(bundle);
                    MainActivity.handler.sendMessage(msg);
                }
            };
            timer.schedule(task,5,500);
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    class MusicControl extends Binder {
        MusicService getService() {
            return MusicService.this;
        }

    }

    public void play(){



        try{
            player.reset();
            player = MediaPlayer.create(getApplicationContext(),R.raw.music);
            player.start();

            addTimer();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void pausePlay(){
        player.pause();
    }
    public void continuePlay(){
        player.start();
    }
    public void seekTo(int progress){
        player.seekTo(progress);
    }
}
