package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;
import java.util.List;

/**
 * ----------------------------------------
 * 注释:
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: on 2016/3/14 14:19
 * ----------------------------------------
 */
public class MineOrganizationBean implements Serializable {

    /**
     * code : 1
     * msg : ok
     * data : {"id":"2","name":"n1","short_name":"sn1","contact_name":"111","contact_mobile":"13333333333","contact_address":"222","contact_no":"","contact_pic":"","register_no":"123","register_pic":"bc","corporation":"faren","corporation_no":"123","corporation_pic":"pic","corporation_picback":"picback","create_time":"1457317731","is_auth":"1","auth_time":"0","is_available":"1","is_delete":"0","auth_remark":"","logo":"http://s.cimg.163.com/catchpic/A/A2/A286F279104F4A50B20DEE4FFA6A04A1.jpg.234x166.jpg","info":"info","tags":"2,3","mien":[{"id":"3","pic":"http://s.cimg.163.com/catchpic/A/A2/A286F279104F4A50B20DEE4FFA6A04A1.jpg.234x166.jpg","type":"1","create_time":"1458009377","relation_id":"2","is_delete":"0"},{"id":"4","pic":"http://s.cimg.163.com/catchpic/A/A2/A286F279104F4A50B20DEE4FFA6A04A1.jpg.234x166.jpg","type":"1","create_time":"1458009377","relation_id":"2","is_delete":"0"}]}
     * tags : [{"id":"12","name":"1","type":"1","sort":"1","create_time":"1458293384","org_id":"2"},{"id":"13","name":"2","type":"1","sort":"2","create_time":"1458293384","org_id":"2"},{"id":"14","name":"3","type":"1","sort":"3","create_time":"1458293396","org_id":"2"},{"id":"15","name":"4","type":"1","sort":"4","create_time":"1458526658","org_id":"2"},{"id":"16","name":"5","type":"1","sort":"5","create_time":"1458526688","org_id":"2"},{"id":"7","name":"师资力量","type":"1","sort":"1","create_time":"0","org_id":"0"},{"id":"8","name":"雄厚","type":"1","sort":"1","create_time":"0","org_id":"0"},{"id":"9","name":"普通","type":"1","sort":"2","create_time":"0","org_id":"0"}]
     */
    private String org_auth;
    private String code;
    private String msg;
    /**
     * id : 2
     * name : n1
     * short_name : sn1
     * contact_name : 111
     * contact_mobile : 13333333333
     * contact_address : 222
     * contact_no :
     * contact_pic :
     * register_no : 123
     * register_pic : bc
     * corporation : faren
     * corporation_no : 123
     * corporation_pic : pic
     * corporation_picback : picback
     * create_time : 1457317731
     * is_auth : 1
     * auth_time : 0
     * is_available : 1
     * is_delete : 0
     * auth_remark :
     * logo : http://s.cimg.163.com/catchpic/A/A2/A286F279104F4A50B20DEE4FFA6A04A1.jpg.234x166.jpg
     * info : info
     * tags : 2,3
     * mien : [{"id":"3","pic":"http://s.cimg.163.com/catchpic/A/A2/A286F279104F4A50B20DEE4FFA6A04A1.jpg.234x166.jpg","type":"1","create_time":"1458009377","relation_id":"2","is_delete":"0"},{"id":"4","pic":"http://s.cimg.163.com/catchpic/A/A2/A286F279104F4A50B20DEE4FFA6A04A1.jpg.234x166.jpg","type":"1","create_time":"1458009377","relation_id":"2","is_delete":"0"}]
     */

    private DataEntity data;
    /**
     * id : 12
     * name : 1
     * type : 1
     * sort : 1
     * create_time : 1458293384
     * org_id : 2
     */

    private List<TagsEntity> tags;

    public String getOrg_auth() {
        return org_auth;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public void setTags(List<TagsEntity> tags) {
        this.tags = tags;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public DataEntity getData() {
        return data;
    }

    public List<TagsEntity> getTags() {
        return tags;
    }

    public class DataEntity  implements Serializable{
        private String id;
        private String name;
        private String short_name;
        private String contact_name;
        private String contact_mobile;
        private String contact_address;
        private String contact_no;
        private String contact_pic;
        private String register_no;
        private String register_pic;
        private String corporation;
        private String corporation_no;
        private String corporation_pic;
        private String corporation_picback;
        private String create_time;
        private String is_auth;
        private String auth_time;
        private String is_available;
        private String is_delete;
        private String auth_remark;
        private String logo;
        private String info;
        private String tags;
        /**
         * id : 3
         * pic : http://s.cimg.163.com/catchpic/A/A2/A286F279104F4A50B20DEE4FFA6A04A1.jpg.234x166.jpg
         * type : 1
         * create_time : 1458009377
         * relation_id : 2
         * is_delete : 0
         */

        private List<MienEntity> mien;

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setShort_name(String short_name) {
            this.short_name = short_name;
        }

        public void setContact_name(String contact_name) {
            this.contact_name = contact_name;
        }

        public void setContact_mobile(String contact_mobile) {
            this.contact_mobile = contact_mobile;
        }

        public void setContact_address(String contact_address) {
            this.contact_address = contact_address;
        }

        public void setContact_no(String contact_no) {
            this.contact_no = contact_no;
        }

        public void setContact_pic(String contact_pic) {
            this.contact_pic = contact_pic;
        }

        public void setRegister_no(String register_no) {
            this.register_no = register_no;
        }

        public void setRegister_pic(String register_pic) {
            this.register_pic = register_pic;
        }

        public void setCorporation(String corporation) {
            this.corporation = corporation;
        }

        public void setCorporation_no(String corporation_no) {
            this.corporation_no = corporation_no;
        }

        public void setCorporation_pic(String corporation_pic) {
            this.corporation_pic = corporation_pic;
        }

        public void setCorporation_picback(String corporation_picback) {
            this.corporation_picback = corporation_picback;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public void setIs_auth(String is_auth) {
            this.is_auth = is_auth;
        }

        public void setAuth_time(String auth_time) {
            this.auth_time = auth_time;
        }

        public void setIs_available(String is_available) {
            this.is_available = is_available;
        }

        public void setIs_delete(String is_delete) {
            this.is_delete = is_delete;
        }

        public void setAuth_remark(String auth_remark) {
            this.auth_remark = auth_remark;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public void setMien(List<MienEntity> mien) {
            this.mien = mien;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getShort_name() {
            return short_name;
        }

        public String getContact_name() {
            return contact_name;
        }

        public String getContact_mobile() {
            return contact_mobile;
        }

        public String getContact_address() {
            return contact_address;
        }

        public String getContact_no() {
            return contact_no;
        }

        public String getContact_pic() {
            return contact_pic;
        }

        public String getRegister_no() {
            return register_no;
        }

        public String getRegister_pic() {
            return register_pic;
        }

        public String getCorporation() {
            return corporation;
        }

        public String getCorporation_no() {
            return corporation_no;
        }

        public String getCorporation_pic() {
            return corporation_pic;
        }

        public String getCorporation_picback() {
            return corporation_picback;
        }

        public String getCreate_time() {
            return create_time;
        }

        public String getIs_auth() {
            return is_auth;
        }

        public String getAuth_time() {
            return auth_time;
        }

        public String getIs_available() {
            return is_available;
        }

        public String getIs_delete() {
            return is_delete;
        }

        public String getAuth_remark() {
            return auth_remark;
        }

        public String getLogo() {
            return logo;
        }

        public String getInfo() {
            return info;
        }

        public String getTags() {
            return tags;
        }

        public List<MienEntity> getMien() {
            return mien;
        }

        public class MienEntity  implements Serializable{
            private String id;
            private String pic;
            private String type;
            private String create_time;
            private String relation_id;
            private String is_delete;

            public void setId(String id) {
                this.id = id;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }

            public void setType(String type) {
                this.type = type;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public void setRelation_id(String relation_id) {
                this.relation_id = relation_id;
            }

            public void setIs_delete(String is_delete) {
                this.is_delete = is_delete;
            }

            public String getId() {
                return id;
            }

            public String getPic() {
                return pic;
            }

            public String getType() {
                return type;
            }

            public String getCreate_time() {
                return create_time;
            }

            public String getRelation_id() {
                return relation_id;
            }

            public String getIs_delete() {
                return is_delete;
            }
        }
    }

    public class TagsEntity  implements Serializable{
        private String id;
        private String name;
        private String type;
        private String sort;
        private String create_time;
        private String org_id;

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public void setOrg_id(String org_id) {
            this.org_id = org_id;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public String getSort() {
            return sort;
        }

        public String getCreate_time() {
            return create_time;
        }

        public String getOrg_id() {
            return org_id;
        }
    }
}
