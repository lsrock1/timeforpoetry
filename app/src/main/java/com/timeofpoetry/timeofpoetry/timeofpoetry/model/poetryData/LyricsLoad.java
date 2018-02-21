package com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData;

import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timeofpoetry.timeofpoetry.timeofpoetry.di.FragScope;
import com.timeofpoetry.timeofpoetry.timeofpoetry.musicPlayer.ProgressTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;

/**
 * Created by sangroklee on 2017. 12. 25..
 */
@FragScope
public class LyricsLoad {

    private Task task;

    @Inject
    public LyricsLoad() {
    }

    public void startLyricsLoad(String url, MutableLiveData<String> data){
        if(task == null || task.isCancelled() || task.getStatus().equals(AsyncTask.Status.FINISHED)){
            task = new Task(data);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
        }
        else if(task.getStatus().equals(AsyncTask.Status.PENDING)){
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
        }
        else if(task.getStatus().equals(AsyncTask.Status.RUNNING)){
            task.cancel(true);
            startLyricsLoad(url, data);
        }
    }

    static private class Task extends AsyncTask<String, Integer, String> {

        private MutableLiveData<String> data;

        Task(MutableLiveData<String> data) {
            this.data = data;
        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder total = new StringBuilder();
            try {
                URL url = new URL(strings[0]);
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

                String str = "";
                while ((str = in.readLine()) != null) {
                    if(isCancelled()) break;
                    total.append(str + "\n");
                }
                in.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return total.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(!isCancelled()) {
                data.setValue(s);
                data = null;
            }
        }
    }
}
