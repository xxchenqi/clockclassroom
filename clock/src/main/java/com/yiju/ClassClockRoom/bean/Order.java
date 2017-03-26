package com.yiju.ClassClockRoom.bean;

class Order {

    private int order_icon;
    private int arrow_icon;
    private String order;
    private String allorder;

    public Order(int order_icon, int arrow_icon, String order, String allorder) {
        super();
        this.order_icon = order_icon;
        this.arrow_icon = arrow_icon;
        this.order = order;
        this.allorder = allorder;
    }

    public int getOrder_icon() {
        return order_icon;
    }

    public void setOrder_icon(int order_icon) {
        this.order_icon = order_icon;
    }

    public int getArrow_icon() {
        return arrow_icon;
    }

    public void setArrow_icon(int arrow_icon) {
        this.arrow_icon = arrow_icon;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getAllorder() {
        return allorder;
    }

    public void setAllorder(String allorder) {
        this.allorder = allorder;
    }

}
