package com.sytiqhub.tinga.DBManager;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;

import java.util.List;

public class RoomRepository {

    private RoomDAO mRoomDao;
    private LiveData<List<OrderFood>> mAllFoodItems;

    RoomRepository(Application application) {
        TingaRoomDatabase db = TingaRoomDatabase.getDatabase(application);
        mRoomDao = db.roomDAO();
        mAllFoodItems = mRoomDao.getAllFoodItems();
    }

    LiveData<List<OrderFood>> getAllFoodItems() {
        return mAllFoodItems;
    }

    public void insert (OrderFood word) {
        new insertAsyncTask(mRoomDao).execute(word);
    }

    private static class insertAsyncTask extends AsyncTask<OrderFood, Void, Void> {

        private RoomDAO mAsyncTaskDao;

        insertAsyncTask(RoomDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final OrderFood... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

}
