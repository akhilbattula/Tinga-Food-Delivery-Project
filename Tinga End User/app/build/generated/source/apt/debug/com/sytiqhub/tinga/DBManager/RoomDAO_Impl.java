package com.sytiqhub.tinga.DBManager;

import android.arch.lifecycle.ComputableLiveData;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.InvalidationTracker.Observer;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.arch.persistence.room.SharedSQLiteStatement;
import android.database.Cursor;
import android.support.annotation.NonNull;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unchecked")
public class RoomDAO_Impl implements RoomDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfOrderFood;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public RoomDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfOrderFood = new EntityInsertionAdapter<OrderFood>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `tbl_orders_food`(`foodId`,`foodName`,`quantity`,`total_price`,`food_price`) VALUES (?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, OrderFood value) {
        if (value.getFoodId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getFoodId());
        }
        if (value.getFoodName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getFoodName());
        }
        stmt.bindLong(3, value.getQuantity());
        stmt.bindLong(4, value.getTotal_price());
        stmt.bindLong(5, value.getFood_price());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM tbl_orders_food";
        return _query;
      }
    };
  }

  @Override
  public void insert(OrderFood orderFood) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfOrderFood.insert(orderFood);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll() {
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public LiveData<List<OrderFood>> getAllFoodItems() {
    final String _sql = "SELECT * from tbl_orders_food ORDER BY foodId ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return new ComputableLiveData<List<OrderFood>>() {
      private Observer _observer;

      @Override
      protected List<OrderFood> compute() {
        if (_observer == null) {
          _observer = new Observer("tbl_orders_food") {
            @Override
            public void onInvalidated(@NonNull Set<String> tables) {
              invalidate();
            }
          };
          __db.getInvalidationTracker().addWeakObserver(_observer);
        }
        final Cursor _cursor = __db.query(_statement);
        try {
          final int _cursorIndexOfFoodId = _cursor.getColumnIndexOrThrow("foodId");
          final int _cursorIndexOfFoodName = _cursor.getColumnIndexOrThrow("foodName");
          final int _cursorIndexOfQuantity = _cursor.getColumnIndexOrThrow("quantity");
          final int _cursorIndexOfTotalPrice = _cursor.getColumnIndexOrThrow("total_price");
          final int _cursorIndexOfFoodPrice = _cursor.getColumnIndexOrThrow("food_price");
          final List<OrderFood> _result = new ArrayList<OrderFood>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final OrderFood _item;
            _item = new OrderFood();
            final String _tmpFoodId;
            _tmpFoodId = _cursor.getString(_cursorIndexOfFoodId);
            _item.setFoodId(_tmpFoodId);
            final String _tmpFoodName;
            _tmpFoodName = _cursor.getString(_cursorIndexOfFoodName);
            _item.setFoodName(_tmpFoodName);
            final int _tmpQuantity;
            _tmpQuantity = _cursor.getInt(_cursorIndexOfQuantity);
            _item.setQuantity(_tmpQuantity);
            final int _tmpTotal_price;
            _tmpTotal_price = _cursor.getInt(_cursorIndexOfTotalPrice);
            _item.setTotal_price(_tmpTotal_price);
            final int _tmpFood_price;
            _tmpFood_price = _cursor.getInt(_cursorIndexOfFoodPrice);
            _item.setFood_price(_tmpFood_price);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    }.getLiveData();
  }
}
