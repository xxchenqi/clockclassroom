package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 课室布置 data
 * Created by wh on 2016/5/13.
 */
public class ClassroomArrangeData implements Serializable{


    /**
     * puse_name : 文化教学
     * use : [{"use_id":"31","use_name":"全科"},{"use_id":"43","use_name":"语文"},{"use_id":"44","use_name":"数学"},{"use_id":"45","use_name":"外语"},{"use_id":"46","use_name":"物理"},{"use_id":"47","use_name":"化学"},{"use_id":"49","use_name":"文化教学-其他"},{"use_id":"31","use_name":"全科"},{"use_id":"43","use_name":"语文"},{"use_id":"44","use_name":"数学"},{"use_id":"45","use_name":"外语"},{"use_id":"46","use_name":"物理"},{"use_id":"47","use_name":"化学"},{"use_id":"49","use_name":"文化教学-其他"},{"use_id":"31","use_name":"全科"},{"use_id":"43","use_name":"语文"},{"use_id":"44","use_name":"数学"},{"use_id":"45","use_name":"外语"},{"use_id":"46","use_name":"物理"},{"use_id":"47","use_name":"化学"},{"use_id":"49","use_name":"文化教学-其他"}]
     * puse_id : 26
     */

    private String puse_name;
    private int puse_id;
    /**
     * use_id : 31
     * use_name : 全科
     */

    private List<UseEntity> use;

    public void setPuse_name(String puse_name) {
        this.puse_name = puse_name;
    }

    public void setPuse_id(int puse_id) {
        this.puse_id = puse_id;
    }

    public void setUse(List<UseEntity> use) {
        this.use = use;
    }

    public String getPuse_name() {
        return puse_name;
    }

    public int getPuse_id() {
        return puse_id;
    }

    public List<UseEntity> getUse() {
        return use;
    }

    public static class UseEntity implements Serializable{
        private String use_id;
        private String use_name;
        private boolean isSelect;

        public boolean isSelect() {
            return isSelect;
        }

        public void setIsSelect(boolean isSelect) {
            this.isSelect = isSelect;
        }

        public void setUse_id(String use_id) {
            this.use_id = use_id;
        }

        public void setUse_name(String use_name) {
            this.use_name = use_name;
        }

        public String getUse_id() {
            return use_id;
        }

        public String getUse_name() {
            return use_name;
        }
    }
}
