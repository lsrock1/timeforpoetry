package com.timeofpoetry.timeofpoetry.timeofpoetry.data;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 시가 담겨있는 리스트를 diff계산하기 위해 만들어진 클래스
 */

public class DiffCallback extends DiffUtil.Callback {

    private final List<PoetryClass.Poem> mOldEmployeeList;
    private final List<PoetryClass.Poem> mNewEmployeeList;

    DiffCallback(List<PoetryClass.Poem> mOldEmployeeList, List<PoetryClass.Poem> mNewEmployeeList) {
        this.mOldEmployeeList = mOldEmployeeList;
        this.mNewEmployeeList = mNewEmployeeList;
    }

    @Override
    public int getOldListSize() {
        return mOldEmployeeList == null ? 0 : mOldEmployeeList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewEmployeeList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        PoetryClass.Poem oldOne = mOldEmployeeList.get(oldItemPosition);
        PoetryClass.Poem newOne = mNewEmployeeList.get(newItemPosition);
        if(oldOne.getDatabaseId() != -1){
            return oldOne.getDatabaseId() == newOne.getDatabaseId();
        }
        else{
            return (oldOne.getPoem().equals(newOne.getPoem()))
                    && (oldOne.getVoice().equals(newOne.getVoice()))
                    && (oldOne.getPoet().equals(newOne.getPoet()));
        }
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        PoetryClass.Poem oldOne = mOldEmployeeList.get(oldItemPosition);
        PoetryClass.Poem newOne = mNewEmployeeList.get(newItemPosition);
        if(oldOne.getDatabaseId() != -1){
            return oldOne.getDatabaseId() == newOne.getDatabaseId();
        }
        else{
            return (oldOne.getPoem().equals(newOne.getPoem()))
                    && (oldOne.getVoice().equals(newOne.getVoice()))
                    && (oldOne.getPoet().equals(newOne.getPoet()));
        }
    }
}
