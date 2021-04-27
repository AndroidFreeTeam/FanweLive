package com.off.bean;

public class CarsBean {

    /**
     * id : 1
     * name : 小毛驴
     * diamonds : 500
     * icon : http://fangwei.com./public/attachment/test/noavatar_1.JPG
     * sort : 12
     * is_effect : 1
     * data : 20
     * icon_gif : http://fangwei.com./public/attachment/test/noavatar_8.JPG
     * create_time : 1545644645
     */

    private int id;
    private String name;
    private double diamonds;
    private String icon;
    private String sort;
    private String is_effect;
    private String data;
    private String icon_gif;
    private String create_time;

    //我的座驾
    private String user_id;
    private String nick_name;
    private String v_id;
    private String v_name;
    private String add_time;
    private String car_type;
    private int is_def;

    public int getIs_def() {
        return is_def;
    }

    public void setIs_def(int is_def) {
        this.is_def = is_def;
    }

    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getV_id() {
        return v_id;
    }

    public void setV_id(String v_id) {
        this.v_id = v_id;
    }

    public String getV_name() {
        return v_name;
    }

    public void setV_name(String v_name) {
        this.v_name = v_name;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDiamonds() {
        return diamonds;
    }

    public void setDiamonds(double diamonds) {
        this.diamonds = diamonds;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getIs_effect() {
        return is_effect;
    }

    public void setIs_effect(String is_effect) {
        this.is_effect = is_effect;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getIcon_gif() {
        return icon_gif;
    }

    public void setIcon_gif(String icon_gif) {
        this.icon_gif = icon_gif;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
