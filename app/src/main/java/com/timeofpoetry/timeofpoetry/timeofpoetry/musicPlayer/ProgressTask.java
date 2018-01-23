package com.timeofpoetry.timeofpoetry.timeofpoetry.musicPlayer;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by sangroklee on 2018. 1. 3..
 */

public class ProgressTask {

    private Player player;
    private Task task;

    public ProgressTask(Player player) {
        this.player = player;
    }

    void startProgress(){
        if(task == null || task.isCancelled()){
            task = new Task();
            task.execute(player);
        }
        else if(task.getStatus().equals(AsyncTask.Status.PENDING)){
            task.execute(player);
        }
    }

    void stopProgress(){
        if(task != null && !task.isCancelled()){
            task.cancel(true);
        }
    }

    static private class Task extends AsyncTask<Player, Player, Integer> {

        @Override
        protected Integer doInBackground(Player... players) {
            while(!isCancelled()) {
                publishProgress(players);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Player... values) {
            if (values[0].getIsPlaying()) {
                values[0].setProgress();
            }
        }
    }
}