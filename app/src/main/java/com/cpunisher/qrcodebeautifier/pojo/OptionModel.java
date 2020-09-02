package com.cpunisher.qrcodebeautifier.pojo;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class OptionModel implements Serializable {

    public String id;
    public String name;

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
