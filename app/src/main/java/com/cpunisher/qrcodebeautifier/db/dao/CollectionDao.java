package com.cpunisher.qrcodebeautifier.db.dao;

import androidx.room.*;
import com.cpunisher.qrcodebeautifier.db.entity.*;

import java.util.List;

@Dao
public abstract class CollectionDao {

    @Transaction
    @Query("SELECT * FROM Style")
    public abstract List<Collection> getAll();

    @Transaction
    @Query("SELECT * FROM Style WHERE styleId IN (:styleIds)")
    public abstract List<Collection> loadAllByIds(String[] styleIds);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long[] insertOptions(List<Option> options);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insertParam(Param param);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insertStyle(Style style);

    @Delete
    public abstract void delete(Style style);

    public void insertCollection(Collection collection) {
        long styleId = insertStyle(collection.style);
        List<ParamWithOptions> paramWithOptionsList = collection.params;
        for (ParamWithOptions paramWithOptions : paramWithOptionsList) {
            paramWithOptions.param.styleId = styleId;
            long paramId = insertParam(paramWithOptions.param);

            List<Option> optionList = paramWithOptions.options;
            optionList.stream().forEach((o) -> o.paramId = paramId);
            insertOptions(optionList);
        }
    }
}
