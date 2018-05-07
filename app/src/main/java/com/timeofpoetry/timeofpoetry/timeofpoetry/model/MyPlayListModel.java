package com.timeofpoetry.timeofpoetry.timeofpoetry.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PlayListDB;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryModelData;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.RepeatMode.RepeatAll;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.RepeatMode.RepeatNone;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.RepeatMode.RepeatOne;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.RepeatMode.Shuffle;
import com.timeofpoetry.timeofpoetry.timeofpoetry.interfaces.RepeatState;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.concerns.SharedPreferenceController;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * 플레이 리스트를 담당하는 모델
 * 플레이리스트에 시를 추가하는 기능을 가진 모든 viewModel에서 싱글톤으로 이 모델 사용
 * MonthlyPoetryViewModel,
 */

@Singleton
public class MyPlayListModel extends PoetryModel{

    public static final int SHUFFLE = 4;

    private SharedPreferenceController sharedPreferenceController;
    private PlayListDB playListDB;

    private MutableLiveData<RepeatState> mode = new MutableLiveData<>();
    private MutableLiveData<PoetryClass.Poem> currentPoem = new MutableLiveData<>();
    private int position;
    private PoetryModelData poetryModelData;
    private MutableLiveData<PoetryModelData> playList = new MutableLiveData<>();

    @Inject
    public MyPlayListModel(SharedPreferenceController sharedPreferenceController, PlayListDB playListDB) {
        this.sharedPreferenceController = sharedPreferenceController;
        this.playListDB = playListDB;
        position = sharedPreferenceController.getLastPosition();
        initMode();
        poetryModelData = playListDB.getPoetryModelData();
        playList.setValue(poetryModelData);
        currentPoem.setValue(PoetryClass.getNullPoem());
        setCurrentPoem(false);
    }

    private void initMode(){
        boolean isShuffle = sharedPreferenceController.getShuffleMode();
        RepeatState state;
        if(isShuffle){
            state = Shuffle.getInstance(this);
        }
        else{
            int mode = sharedPreferenceController.getRepeatMode();
            if(mode == PlaybackStateCompat.REPEAT_MODE_NONE){
                state = RepeatNone.getInstance(this);
            }
            else if(mode == PlaybackStateCompat.REPEAT_MODE_ONE){
                state = RepeatOne.getInstance(this);
            }
            else{
                state = RepeatAll.getInstance(this);
            }
        }
        mode.setValue(state);
    }

    public void addPoetry(PoetryClass.Poem poem){
        poem.setDatabaseId((int) playListDB.addItem(poem));
        position = 0;
        poetryModelData.addOnePoem(poem);
        setCurrentPoem(false);
        playList.setValue(poetryModelData);
    }

    @Override
    public void addPoetry(List<PoetryClass.Poem> items) {
        if (items.size() != 0) {
            LinkedList<PoetryClass.Poem> data = new LinkedList<>(poetryModelData.getPoetry());
            for (PoetryClass.Poem item : items) {
                item.setDatabaseId((int) playListDB.addItem(item));
                data.add(0, item);
            }

            position = 0;
            poetryModelData.setNewArray(data, false);
            setCurrentPoem(false);
            playList.setValue(poetryModelData);
        }
    }

    @Override
    public void removePoetry(){
        LinkedList<PoetryClass.Poem> data = new LinkedList<>(poetryModelData.getPoetry());
        ListIterator<PoetryClass.Poem> itr = data.listIterator();

        while(itr.hasNext()){
            int index = itr.nextIndex();
            PoetryClass.Poem poem = itr.next();
            if(poem.getIsSelected().get()){
                playListDB.removeItem(poem.getDatabaseId());
                itr.remove();
                if(index < position){
                    position--;
                }
                else if(position > data.size() - 1){
                    position = 0;
                }
            }
        }

        poetryModelData.setNewArray(data, false);
        setCurrentPoem(true);
        playList.setValue(poetryModelData);
    }

    private void setCurrentPoem(boolean isRemoved){
        PoetryClass.Poem tmpPoem;
        sharedPreferenceController.setLastPosition(position);
        if(poetryModelData.getPoetry().size() == 0){
            tmpPoem = PoetryClass.getNullPoem();
            BitmapDrawable drawable = (BitmapDrawable) ContextCompat.getDrawable(getApplicationContext(), R.drawable.logo);
            Bitmap resource = drawable.getBitmap();
            tmpPoem.setArtwork(resource);
            currentPoem.setValue(tmpPoem);
        }
        else{
            tmpPoem = poetryModelData.getPoetry().get(position);
            //삭제 후 같은 시일 경우에 currentpoem set이 되면서 재생이되는데 이를 막고자 삭제일 경우 동일 음원은 observer event를 일으키지 않게 함
            if(!isRemoved || currentPoem.getValue().getDatabaseId() != tmpPoem.getDatabaseId()){
                currentPoem.setValue(tmpPoem);
            }
        }
    }

    private void setCurrentPoemByWard(){
        PoetryClass.Poem tmpPoem;
        sharedPreferenceController.setLastPosition(position);
        if(poetryModelData.getPoetry().size() == 0){
            tmpPoem = PoetryClass.getNullPoem();
            BitmapDrawable drawable = (BitmapDrawable) ContextCompat.getDrawable(getApplicationContext(), R.drawable.logo);
            Bitmap resource = drawable.getBitmap();
            tmpPoem.setArtwork(resource);
            currentPoem.setValue(tmpPoem);
        }
        else{
            tmpPoem = poetryModelData.getPoetry().get(position);
            tmpPoem.setWard(true);
            currentPoem.setValue(tmpPoem);

        }
    }

    @Override
    public LiveData<PoetryModelData> getPoetry(){
        return playList;
    }

    public LiveData<PoetryClass.Poem> getCurrentPoem(){
        return currentPoem;
    }

    public MutableLiveData<RepeatState> getMode(){
        return mode;
    }

    public void setMode(RepeatState mode, int preferData){
        if(preferData == MyPlayListModel.SHUFFLE){
            sharedPreferenceController.setShuffleMode(true);
        }
        else if(preferData == PlaybackStateCompat.REPEAT_MODE_ALL){
            sharedPreferenceController.setShuffleMode(false);
            sharedPreferenceController.setRepeatMode(preferData);
        }
        else if(preferData == PlaybackStateCompat.REPEAT_MODE_ONE){
            sharedPreferenceController.setRepeatMode(preferData);
        }
        else if(preferData == PlaybackStateCompat.REPEAT_MODE_NONE){
            sharedPreferenceController.setRepeatMode(preferData);
        }
        this.mode.setValue(mode);
    }

    public void setPosition(int position, boolean byWard){
        if(position >= poetryModelData.getPoetry().size() || position < 0){
            return;
        }
        this.position = position;
        if(byWard){
            setCurrentPoemByWard();
        }
        else{
            setCurrentPoem(false);
        }
    }

    public void forward(){
        mode.getValue().forward(this);
    }

    public void backward(){
        mode.getValue().backward(this);
    }

    public int getPosition(){
        return position;
    }

    public void modeSwitch(){
        mode.getValue().modeSwitch();
    }
}
