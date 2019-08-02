package com.sytiqhub.tinga.DBManager;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.*;
import android.content.Context;
import android.support.annotation.NonNull;

@Database(entities = {OrderFood.class}, version = 1, exportSchema = false)
public abstract class TingaRoomDatabase extends RoomDatabase {

    private static TingaRoomDatabase INSTANCE;
    public abstract RoomDAO roomDAO();


    public static TingaRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TingaRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TingaRoomDatabase.class, "tinga_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
