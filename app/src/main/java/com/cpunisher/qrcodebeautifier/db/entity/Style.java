package com.cpunisher.qrcodebeautifier.db.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Style {

    @PrimaryKey(autoGenerate = true)
    public long id;
    public String styleId;
    public String name;
    public String description;
    public byte[] img;

    public Style() { }

    @Ignore
    public Style(String styleId, String name, String description, byte[] img) {
        this.styleId = styleId;
        this.name = name;
        this.description = description;
        this.img = img;
    }
}
