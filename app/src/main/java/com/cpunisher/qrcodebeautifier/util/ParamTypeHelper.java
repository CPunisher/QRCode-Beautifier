package com.cpunisher.qrcodebeautifier.util;

import android.view.View;
import com.cpunisher.qrcodebeautifier.R;
import com.cpunisher.qrcodebeautifier.adapter.ParamAdapter;
import com.cpunisher.qrcodebeautifier.listener.ParamChangeListener;
import com.cpunisher.qrcodebeautifier.pojo.StyleModel;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ParamTypeHelper {
    private static ParamTypeHelper instance;

    private Map<String, Integer> typeNameToId = new HashMap<>();
    private Map<Integer, Integer> idToLayout = new HashMap<>();
    private Map<Integer, Class<? extends ParamAdapter.ParamViewHolder>> idToClass = new HashMap<>();
    private int id = 0;

    static {
        getInstance().registerParamType("string", R.layout.item_param_input, ParamAdapter.ParamEditViewHolder.class);
        getInstance().registerParamType("number", R.layout.item_param_input, ParamAdapter.ParamEditViewHolder.class);
        getInstance().registerParamType("option", R.layout.item_param_option, ParamAdapter.ParamOptionViewHolder.class);
        getInstance().registerParamType("color", R.layout.item_param_color, ParamAdapter.ParamColorViewHolder.class);
    }

    private ParamTypeHelper() {
    }

    public static synchronized ParamTypeHelper getInstance() {
        if (instance == null) {
            instance = new ParamTypeHelper();
        }
        return instance;
    }

    public void registerParamType(String type, final int layoutId, Class<? extends ParamAdapter.ParamViewHolder> clazz) {
        typeNameToId.put(type, id);
        idToLayout.put(id, layoutId);
        idToClass.put(id, clazz);
        id++;
    }

    public ParamAdapter.ParamViewHolder instanceViewHolder(int id, View itemView, StyleModel styleModel, ParamChangeListener listener) {
        ParamAdapter.ParamViewHolder viewHolder = null;
        try {
            viewHolder = idToClass.get(id).getConstructor(StyleModel.class, View.class, ParamChangeListener.class)
                    .newInstance(styleModel, itemView, listener);
        } catch (InvocationTargetException e){
            e.getTargetException().printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return viewHolder;
    }

    public int getLayout(int id) {
        return idToLayout.get(id);
    }

    public int getId(String type) {
        return typeNameToId.getOrDefault(type, 0);
    }
}
