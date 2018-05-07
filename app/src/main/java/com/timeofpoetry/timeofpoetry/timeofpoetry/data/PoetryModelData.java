package com.timeofpoetry.timeofpoetry.timeofpoetry.data;

import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 모델에서 받아온 시 데이터를
 * 옵져버블 변수에 싣어 관리할 때 사용하는 클래스
 * 내부에 diffcallback이 있어서 리사이클러 뷰의 계산 속도를 올려줌
 * 하지만 갯수가 늘어날 때를 대비해 스레드에서 실행할 필요가 있음
 */

public class PoetryModelData {

    private List<PoetryClass.Poem> mPoetry;
    private DiffCallback mCallback;
    private int mChange;
    private boolean mAlert = false;

    public PoetryModelData(List<PoetryClass.Poem> newList) {
        this.mPoetry = newList;
        this.mCallback = new DiffCallback(null, newList);
        this.mChange = 0;
    }

    public boolean isAlert() {
        return mAlert;
    }

    public void setAlert(boolean alert) {
        this.mAlert = alert;
    }

    public List<PoetryClass.Poem> getPoetry() {
        return mPoetry;
    }

    public DiffCallback getCallback() {
        return mCallback;
    }

    public void setNewArray(List<PoetryClass.Poem> newArray, boolean isInit){
        mCallback = new DiffCallback(mPoetry, newArray);
        mChange = isInit ? 0 : newArray.size() - mPoetry.size();
        mPoetry = newArray;
        mAlert = false;
    }

    //시 하나만 넣을 때를 위한 간편 메소드
    //연산 수는 줄어들지 않음
    public void addOnePoem(PoetryClass.Poem poem){
        LinkedList<PoetryClass.Poem> tmp = new LinkedList<>(mPoetry);
        tmp.add(0, poem);
        setNewArray(tmp, false);
    }

    public int getChange() {
        return mChange;
    }
}
