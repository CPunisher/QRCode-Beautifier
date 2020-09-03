package com.cpunisher.qrcodebeautifier.db;

import android.content.Context;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.cpunisher.qrcodebeautifier.db.dao.CollectionDao;
import com.cpunisher.qrcodebeautifier.db.entity.Option;
import com.cpunisher.qrcodebeautifier.db.entity.Param;
import com.cpunisher.qrcodebeautifier.db.entity.Style;

@Database(entities = {Style.class, Param.class, Option.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    @VisibleForTesting
    public static final String DATABASE_NAME = "collection";

    public abstract CollectionDao collectionDao();

    public static synchronized AppDatabase getInstance(final Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).build();
        }
        return instance;
    }
}
