package com.timeofpoetry.timeofpoetry.timeofpoetry.data;

import android.arch.lifecycle.LiveData;

import java.util.List;

/**
 * 시 클래스를 공급하는 모델이 상속하는 클래스
 * 추가, 제거, 데이터 가져오기를 가지고 있으나
 * 월간 몇시 같은 경우 사용자가 추가, 제거를 하면 안되기 때문에 추가, 제거 기능이 없어야 한다
 * 하지만 getPoetry는 모든 클래스에서 갖춰야하기 때문에
 * getPoetry만 추상메소드, 나머지는 구현의 자유가 있는 메소드
 */

abstract public class PoetryModel {

    public void addPoetry(List<PoetryClass.Poem> poetry){

    }

    public void removePoetry(){

    }

    public abstract LiveData<PoetryModelData> getPoetry();
}
