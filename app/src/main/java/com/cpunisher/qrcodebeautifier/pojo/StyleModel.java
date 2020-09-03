package com.cpunisher.qrcodebeautifier.pojo;

import java.io.Serializable;

public class StyleModel implements Serializable {

    public String id;
    public String name;
    public String description;
    public String imgUri;
    public ParamModel[] params;

    public long databaseId;
    public byte[] imgBlog;
}
