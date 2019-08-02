package com.sytiqhub.tinga.DBManager;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Callback;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Configuration;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.room.RoomOpenHelper.Delegate;
import android.arch.persistence.room.util.TableInfo;
import android.arch.persistence.room.util.TableInfo.Column;
import android.arch.persistence.room.util.TableInfo.ForeignKey;
import android.arch.persistence.room.util.TableInfo.Index;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;

@SuppressWarnings("unchecked")
public class TingaRoomDatabase_Impl extends TingaRoomDatabase {
  private volatile RoomDAO _roomDAO;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `tbl_orders_food` (`foodId` TEXT NOT NULL, `foodName` TEXT, `quantity` INTEGER NOT NULL, `total_price` INTEGER NOT NULL, `food_price` INTEGER NOT NULL, PRIMARY KEY(`foodId`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"c59e9a5e66b4d15f99a9450dcc00cc52\")");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `tbl_orders_food`");
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsTblOrdersFood = new HashMap<String, TableInfo.Column>(5);
        _columnsTblOrdersFood.put("foodId", new TableInfo.Column("foodId", "TEXT", true, 1));
        _columnsTblOrdersFood.put("foodName", new TableInfo.Column("foodName", "TEXT", false, 0));
        _columnsTblOrdersFood.put("quantity", new TableInfo.Column("quantity", "INTEGER", true, 0));
        _columnsTblOrdersFood.put("total_price", new TableInfo.Column("total_price", "INTEGER", true, 0));
        _columnsTblOrdersFood.put("food_price", new TableInfo.Column("food_price", "INTEGER", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTblOrdersFood = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTblOrdersFood = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTblOrdersFood = new TableInfo("tbl_orders_food", _columnsTblOrdersFood, _foreignKeysTblOrdersFood, _indicesTblOrdersFood);
        final TableInfo _existingTblOrdersFood = TableInfo.read(_db, "tbl_orders_food");
        if (! _infoTblOrdersFood.equals(_existingTblOrdersFood)) {
          throw new IllegalStateException("Migration didn't properly handle tbl_orders_food(com.sytiqhub.tinga.DBManager.OrderFood).\n"
                  + " Expected:\n" + _infoTblOrdersFood + "\n"
                  + " Found:\n" + _existingTblOrdersFood);
        }
      }
    }, "c59e9a5e66b4d15f99a9450dcc00cc52", "9857757727b74e4c5351f8c92445087b");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "tbl_orders_food");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `tbl_orders_food`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public RoomDAO roomDAO() {
    if (_roomDAO != null) {
      return _roomDAO;
    } else {
      synchronized(this) {
        if(_roomDAO == null) {
          _roomDAO = new RoomDAO_Impl(this);
        }
        return _roomDAO;
      }
    }
  }
}
