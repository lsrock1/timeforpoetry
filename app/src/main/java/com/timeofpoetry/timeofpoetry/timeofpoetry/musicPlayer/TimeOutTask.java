package com.timeofpoetry.timeofpoetry.timeofpoetry.musicPlayer;

import android.os.AsyncTask;

import com.timeofpoetry.timeofpoetry.timeofpoetry.NetworkTasks;

/**
 * Created by sangroklee on 2018. 1. 4..
 */

public class TimeOutTask {

    private Player player;
    private Task task;

    public TimeOutTask(Player player) {
        this.player = player;
    }

    public void startTimeout(){
        if(task == null || task.isCancelled() || task.getStatus().equals(AsyncTask.Status.FINISHED)){
            task = new Task();
            task.execute(player);
        }
    }

    public void cancel(){
        task.cancel(true);
    }

    static public class Task extends AsyncTask<Player, Integer, Integer> {

        @Override
        protected Integer doInBackground(Player... sv) {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(!isCancelled()) {
                sv[0].timeOut();
            }
            return null;
        }
    }
}
