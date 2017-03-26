package com.yiju.ClassClockRoom.bean;

import java.util.ArrayList;

/**
 * ----------------------------------------
 * 注释: 押金BEAN
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: on 2016/1/25 15:13
 * ----------------------------------------
 */
public class PledgeBean {
    private String code;
    private String msg;
    private ArrayList<Data> data;

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public class Data {
        private String sid;
        private String name;
        private ArrayList<Data2> data;

        public String getSid() {
            return sid;
        }

        public String getName() {
            return name;
        }

        public ArrayList<Data2> getData() {
            return data;
        }

        public class Data2 {
            private String id;
            private String desc;
            private String deposit;

            public String getId() {
                return id;
            }

            public String getDesc() {
                return desc;
            }

            public String getDeposit() {
                return deposit;
            }
        }

    }

}
