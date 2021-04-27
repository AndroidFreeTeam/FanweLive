package com.off.bean;

import java.util.List;

/**
 * Created by Yan on 2018/12/30 0030
 */
public class CarsModel{
    private List<CarsBean> result;

    public List<CarsBean> getList() {
        return result;
    }

    public void setList(List<CarsBean> list) {
        this.result = list;
    }
}
