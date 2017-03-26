package com.yiju.ClassClockRoom.bean.result;

import com.yiju.ClassClockRoom.bean.base.BaseEntity;

/*
 * Created by wh on 2016/3/18.
 */
public class CommonResultBean extends BaseEntity {
    /**
     * data : http://api.51shizhong.com
     * newData : {"baseUrl":"http://api.51shizhong.com","javaUrl":"http://api2.51shizhong.com","basePicUrl":"http://i.upload.file.dc.cric.com/","ejuPayUrl":"http://ejupay.17shihui.com/gateway-outrpc/acquirer/interact"}
     */

    private String data;
    /**
     * baseUrl : http://api.51shizhong.com
     * javaUrl : http://api2.51shizhong.com
     * basePicUrl : http://i.upload.file.dc.cric.com/
     * ejuPayUrl : http://ejupay.17shihui.com/gateway-outrpc/acquirer/interact
     */

    private NewDataEntity newData;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public NewDataEntity getNewData() {
        return newData;
    }

    public void setNewData(NewDataEntity newData) {
        this.newData = newData;
    }

    public static class NewDataEntity {
        private String baseUrl;
        private String javaUrl;
        private String basePicUrl;
        private String ejuPayUrl;
        private String h5BaseUrl;

        public String getH5BaseUrl() {
            return h5BaseUrl;
        }

        public void setH5BaseUrl(String h5BaseUrl) {
            this.h5BaseUrl = h5BaseUrl;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public String getJavaUrl() {
            return javaUrl;
        }

        public void setJavaUrl(String javaUrl) {
            this.javaUrl = javaUrl;
        }

        public String getBasePicUrl() {
            return basePicUrl;
        }

        public void setBasePicUrl(String basePicUrl) {
            this.basePicUrl = basePicUrl;
        }

        public String getEjuPayUrl() {
            return ejuPayUrl;
        }

        public void setEjuPayUrl(String ejuPayUrl) {
            this.ejuPayUrl = ejuPayUrl;
        }
    }
    /**
     * baseUrl : http://api.51shizhong.com
     * javaUrl : http://api2.51shizhong.com
     * basePicUrl : http://i.upload.file.dc.cric.com/
     * ejuPayUrl : http://ejupay.17shihui.com/gateway-outrpc/acquirer/interact
     */


   /* private DataEntity data;

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        private String baseUrl;
        private String javaUrl;
        private String basePicUrl;
        private String ejuPayUrl;

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public String getJavaUrl() {
            return javaUrl;
        }

        public void setJavaUrl(String javaUrl) {
            this.javaUrl = javaUrl;
        }

        public String getBasePicUrl() {
            return basePicUrl;
        }

        public void setBasePicUrl(String basePicUrl) {
            this.basePicUrl = basePicUrl;
        }

        public String getEjuPayUrl() {
            return ejuPayUrl;
        }

        public void setEjuPayUrl(String ejuPayUrl) {
            this.ejuPayUrl = ejuPayUrl;
        }
    }*/


  /*  private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }*/
}
