package com.cpunisher.qrcodebeautifier.db.entity;

import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

import java.util.List;

public class Collection {
    @Embedded public Style style;
    @Relation(
            entity = Param.class,
            parentColumn = "id",
            entityColumn = "styleId"
    )
    public List<ParamWithOptions> params;

    public Collection() {}

    @Ignore
    public Collection(Style style, List<ParamWithOptions> params) {
        this.style = style;
        this.params = params;
    }
}
