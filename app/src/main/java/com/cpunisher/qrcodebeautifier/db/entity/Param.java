package com.cpunisher.qrcodebeautifier.db.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Param {

    @PrimaryKey(autoGenerate = true)
    public long id;
    public String paramId;
    public String name;
    public String type;
    public String value;

    public long styleId;

    public Param() {}

    @Ignore
    public Param(String paramId, String name, String type, String value, long styleId) {
        this.paramId = paramId;
        this.name = name;
        this.type = type;
        this.value = value;
        this.styleId = styleId;
    }
}
