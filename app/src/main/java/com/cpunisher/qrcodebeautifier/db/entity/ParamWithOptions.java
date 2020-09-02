package com.cpunisher.qrcodebeautifier.db.entity;

import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

import java.util.List;

public class ParamWithOptions {
    @Embedded public Param param;
    @Relation(
            parentColumn = "id",
            entityColumn = "paramId"
    )
    public List<Option> options;

    public ParamWithOptions() {}

    @Ignore
    public ParamWithOptions(Param param, List<Option> options) {
        this.param = param;
        this.options = options;
    }
}
