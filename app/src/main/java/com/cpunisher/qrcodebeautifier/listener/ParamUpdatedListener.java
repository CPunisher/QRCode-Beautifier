package com.cpunisher.qrcodebeautifier.listener;

import com.cpunisher.qrcodebeautifier.pojo.StyleModel;

public interface ParamUpdatedListener {
    void onParamUpdated(StyleModel styleModel, String[] params);
}