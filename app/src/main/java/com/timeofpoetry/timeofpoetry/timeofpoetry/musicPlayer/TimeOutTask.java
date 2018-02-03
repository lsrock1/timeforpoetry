package com.timeofpoetry.timeofpoetry.timeofpoetry.musicPlayer;

import android.os.AsyncTask;

import com.timeofpoetry.timeofpoetry.timeofpoetry.NetworkTasks;

/**
 * Created by sangroklee on 2018. 1. 4..
 */

class TimeOutTask {

    private Player player;
    private Task task;
    private boolean isCancelled = false;

    TimeOutTask(Player player) {
        this.player = player;
    }

    void startTimeout(){
        isCancelled = false;
        if(task == null || task.isCancelled() || task.getStatus().equals(AsyncTask.Status.FINISHED)){
            task = new Task();
            task.execute(player);
        }
    }

    void cancel(){
        task.cancel(true);
        isCancelled = true;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    static public class Task extends AsyncTask<Player, Integer, Integer> {

        @Override
        protected Integer doInBackground(Player... sv) {
            try {
                Thread.sleep(7000);
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
