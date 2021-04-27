package com.off.util;

import android.content.res.AssetManager;

import java.io.IOException;

/**
 * Created by Yan on 2019/1/8 0008
 */
public class CarsUtils {

    /**
     * 获得座驾地址
     * @param type 类型
     * @return 地址
     */
    public static String getCarPath(String type){
        String animPath = "";
        if ("mini".equals(type)) {
            animPath = "mini_zxg.svga";
        } else if ("jeep".equals(type)){
            animPath = "jeep_zxg.svga";
        } else if ("kemailuo".equals(type)){
            animPath = "kemailuo_zxg.svga";
        } else if ("luhu".equals(type)){
            animPath = "luhu_zxg.svga";
        } else if ("jiebao".equals(type)){
            animPath = "jiebao_zxg.svga";
        } else if ("baoshijie".equals(type)){
            animPath = "baoshijie_zxg.svga";
        } else if ("danche".equals(type)){
            animPath = "ladanche.svga";
        } else if ("maikailun".equals(type)){
            animPath = "sy_mkl.svga";
        }
        return animPath;
    }

    public static boolean isFileExistsAsAsset(AssetManager assetManager, String fileName){
        if(null == assetManager){
            return false;
        }
        try {
            String[] names = assetManager.list("");
            if(null == names){
                return false;
            }
            for (String name : names) {
                if (name.equals(fileName.trim())) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
