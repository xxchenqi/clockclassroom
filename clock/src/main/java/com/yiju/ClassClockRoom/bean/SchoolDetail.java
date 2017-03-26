package com.yiju.ClassClockRoom.bean;

import java.util.List;
import java.util.Map;

/**
 * 学校详情 2015/11/24.
 */
public class SchoolDetail {


    private Integer code;
    private String msg;

    private DataEntity data;

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        private String id;
        private String tags;
        private String name;
        private String address;
        private String detail;
        private String praise;
        private String lng;
        private String lat;
        private String type_desc;
        private String pic_ids;
        private String atype;
        private String adurl;
        private String hotteacher;

        public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getTags() {
			return tags;
		}
		public void setTags(String tags) {
			this.tags = tags;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getDetail() {
			return detail;
		}
		public void setDetail(String detail) {
			this.detail = detail;
		}
		public String getPraise() {
			return praise;
		}
		public void setPraise(String praise) {
			this.praise = praise;
		}
		public String getLng() {
			return lng;
		}
		public void setLng(String lng) {
			this.lng = lng;
		}
		public String getLat() {
			return lat;
		}
		public void setLat(String lat) {
			this.lat = lat;
		}
		public String getType_desc() {
			return type_desc;
		}
		public void setType_desc(String type_desc) {
			this.type_desc = type_desc;
		}
		public String getPic_ids() {
			return pic_ids;
		}
		public void setPic_ids(String pic_ids) {
			this.pic_ids = pic_ids;
		}
		public String getAtype() {
			return atype;
		}
		public void setAtype(String atype) {
			this.atype = atype;
		}
		public String getAdurl() {
			return adurl;
		}
		public void setAdurl(String adurl) {
			this.adurl = adurl;
		}
		public String getHotteacher() {
			return hotteacher;
		}
		public void setHotteacher(String hotteacher) {
			this.hotteacher = hotteacher;
		}
		public List<AroundEntity> getArounddining() {
			return arounddining;
		}
		public void setArounddining(List<AroundEntity> arounddining) {
			this.arounddining = arounddining;
		}
		public List<AroundEntity> getAroundhotel() {
			return aroundhotel;
		}
		public void setAroundhotel(List<AroundEntity> aroundhotel) {
			this.aroundhotel = aroundhotel;
		}
		public List<AroundEntity> getAroundschool() {
			return aroundschool;
		}
		public void setAroundschool(List<AroundEntity> aroundschool) {
			this.aroundschool = aroundschool;
		}
		public List<ClassesEntity> getClasses() {
			return classes;
		}
		public void setClasses(List<ClassesEntity> classes) {
			this.classes = classes;
		}
		public List<ServersEntity> getFreeservers() {
			return freeservers;
		}
		public void setFreeservers(List<ServersEntity> freeservers) {
			this.freeservers = freeservers;
		}
		public List<ServersEntity> getPayservers() {
			return payservers;
		}
		public void setPayservers(List<ServersEntity> payservers) {
			this.payservers = payservers;
		}
		public List<PicSmallEntity> getPic_small() {
			return pic_small;
		}
		public void setPic_small(List<PicSmallEntity> pic_small) {
			this.pic_small = pic_small;
		}
		public Map<String, ContentEntity> getTraffic() {
			return traffic;
		}
		public void setTraffic(Map<String, ContentEntity> traffic) {
			this.traffic = traffic;
		}
		private List<AroundEntity> arounddining;
        private List<AroundEntity> aroundhotel;
        private List<AroundEntity> aroundschool;
        private List<ClassesEntity> classes;
        private List<ServersEntity> freeservers;
        private List<ServersEntity> payservers;
        private List<PicSmallEntity> pic_small;
        private Map<String,ContentEntity> traffic;

        public static class AroundEntity{
        	private String address;
        	private String adurl;
        	private String distance;
        	private String lat;
        	private String lng;
        	private String name;
        	private String price;
        	private String tel;
			public String getAddress() {
				return address;
			}
			public void setAddress(String address) {
				this.address = address;
			}
			public String getAdurl() {
				return adurl;
			}
			public void setAdurl(String adurl) {
				this.adurl = adurl;
			}
			public String getDistance() {
				return distance;
			}
			public void setDistance(String distance) {
				this.distance = distance;
			}
			public String getLat() {
				return lat;
			}
			public void setLat(String lat) {
				this.lat = lat;
			}
			public String getLng() {
				return lng;
			}
			public void setLng(String lng) {
				this.lng = lng;
			}
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			public String getPrice() {
				return price;
			}
			public void setPrice(String price) {
				this.price = price;
			}
			public String getTel() {
				return tel;
			}
			public void setTel(String tel) {
				this.tel = tel;
			}
        	
        }
        public static class ClassesEntity {
        	private String udesc;
            private String uname;
            private String uurl;
			public String getUdesc() {
				return udesc;
			}
			public void setUdesc(String udesc) {
				this.udesc = udesc;
			}
			public String getUname() {
				return uname;
			}
			public void setUname(String uname) {
				this.uname = uname;
			}
			public String getUurl() {
				return uurl;
			}
			public void setUurl(String uurl) {
				this.uurl = uurl;
			}
            
        }
        public static class ServersEntity {
            private String did;
            private String dname;
            private String dfee;
            private String dunit;
			public String getDid() {
				return did;
			}
			public void setDid(String did) {
				this.did = did;
			}
			public String getDname() {
				return dname;
			}
			public void setDname(String dname) {
				this.dname = dname;
			}
			public String getDfee() {
				return dfee;
			}
			public void setDfee(String dfee) {
				this.dfee = dfee;
			}
			public String getDunit() {
				return dunit;
			}
			public void setDunit(String dunit) {
				this.dunit = dunit;
			}
            
        }
        public static class PicSmallEntity {
            private String url;
            private String ids;
			public String getUrl() {
				return url;
			}
			public void setUrl(String url) {
				this.url = url;
			}
			public String getIds() {
				return ids;
			}
			public void setIds(String ids) {
				this.ids = ids;
			}
            
        }
        public static class ContentEntity{
        	private String categroy;

			public String getCategroy() {
				return categroy;
			}

			public void setCategroy(String categroy) {
				this.categroy = categroy;
			}
        	
        } 
        	
        
        

        
    }
}
