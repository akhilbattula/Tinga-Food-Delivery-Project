package com.sytiqhub.tinga.DBManager;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class RoomViewModel extends AndroidViewModel {

    private RoomRepository mRepository;

    private LiveData<List<OrderFood>> mAllFoodItems;

    public RoomViewModel(Application application) {
        super(application);
        mRepository = new RoomRepository(application);
        mAllFoodItems = ((RoomRepository) mRepository).getAllFoodItems();
    }

    LiveData<List<OrderFood>> getAllWords() { return mAllFoodItems; }

    public void insert(OrderFood foodItem) { mRepository.insert(foodItem); }
}