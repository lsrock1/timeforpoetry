package com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.naviView;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ActivityScope;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.BoardModel;

import java.util.List;

import javax.inject.Inject;

/**
 * view/naviView/BoardActivity view를 위한 뷰모델
 * 공지사항 데이터를 boardModel에서 가져와 view가 사용할 수 있게 wrapping
 */

public class BoardActivityViewModel extends ViewModel {

    private BoardModel mBoardModel;
    private LiveData<List<PoetryClass.BoardIdItem>> mBoardItems;

    BoardActivityViewModel(BoardModel boardModel) {
        mBoardModel = boardModel;
        mBoardItems = mBoardModel.getList();
    }

    public LiveData<List<PoetryClass.BoardIdItem>> getBoardItems(){
        return mBoardItems;
    }

    //layout/board_item onclick시 작동
    //content가 비어있으면 model에 요청해 content를 가져옴
    //비어있지 않으면 내용이 나타나는 text를 열어줌
    public void getBody(View view, int id, PoetryClass.BoardIdItem content){
        ExpandableLinearLayout exView = ((ExpandableLinearLayout) ((ViewGroup)view.getParent().getParent()).getChildAt(1));
        if(content.displayContent.get().equals("")){
            mBoardModel.getContent(id, content.displayContent);
        }
        else{
            exView.toggle();
        }
    }

    @ActivityScope
    public static class BoardActivityViewModelFactory implements ViewModelProvider.Factory{

        private BoardModel mBoardModel;

        @Inject
        public BoardActivityViewModelFactory(BoardModel boardModel) {
            mBoardModel = boardModel;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new BoardActivityViewModel(mBoardModel);
        }
    }
}
