package com.timeofpoetry.timeofpoetry.timeofpoetry.data;

import android.databinding.ObservableBoolean;

import com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData.MyPoetryModel;

/**
 * 시 리스트를 보여주는 뷰 중에서도 시를 재생목록에 넣는(supply) 기능을 하는 뷰를 위한 클래스
 * poetryViewModel을 상속하기 때문에 리스트뷰를 위한 메소드는 다 갖추고 있으며
 * 추가로 재생목록을 관장하는 MyPlayListModel에 연결해 선택한 시를 추가하는 메소드를 구현함
 */

public class SupplierPoetryViewModel extends PoetryViewModel {

    private MyPlayListModel mModel;

    public SupplierPoetryViewModel(PoetryModel poetryModel, MyPlayListModel model) {
        super(poetryModel);
        this.mModel = model;
    }

    public void addSelectedPoetryToPlayList(){
        mModel.addPoetry(getSelectedPoetry());
    }

    public void addPoemToPlayList(PoetryClass.Poem poem){
        mModel.addPoetry(poem.clone());
    }
}
