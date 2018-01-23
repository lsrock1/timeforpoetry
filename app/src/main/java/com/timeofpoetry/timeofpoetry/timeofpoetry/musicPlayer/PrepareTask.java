package com.timeofpoetry.timeofpoetry.timeofpoetry.musicPlayer;

import android.os.AsyncTask;
import android.util.Log;

import com.timeofpoetry.timeofpoetry.timeofpoetry.NetworkTasks;

/**
 * Created by sangroklee on 2018. 1. 4..
 */

public class PrepareTask {

    private Task task;
    private Player player;

    PrepareTask(Player player) {
        this.player = player;
    }

    void startPrepare(){
        if(task == null){
            task = new Task();
            task.execute(player);
        }
        else if(task.getStatus().equals(AsyncTask.Status.FINISHED)){
            task.cancel(true);
            task = new Task();
            task.execute(player);
        }
        else if(task.getStatus().equals(AsyncTask.Status.RUNNING)){
            if(task.getPrepareCount() != 0) task.renew();
        }
    }

    static public class Task extends AsyncTask<Player, Integer, Player>{

        private int prepareCount = 800;

        void renew(){
            prepareCount = 800;
        }

        int getPrepareCount(){return prepareCount;}

        @Override
        protected Player doInBackground(Player... sv) {
            while(prepareCount > 0 && !isCancelled()){
                try{
                    Thread.sleep(1);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                prepareCount--;
            }
            return sv[0];
        }

        @Override
        protected void onPostExecute(Player player) {
            super.onPostExecute(player);
            player.prepareStartMediaPlayer();
        }
    }
}
