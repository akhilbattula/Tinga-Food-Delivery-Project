package com.sytiqhub.tinga.DBManager;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface RoomDAO {

    @Insert
    void insert(OrderFood orderFood);

    @Query("DELETE FROM tbl_orders_food")
    void deleteAll();

    @Query("SELECT * from tbl_orders_food ORDER BY foodId ASC")
    LiveData<List<OrderFood>> getAllFoodItems();
}
