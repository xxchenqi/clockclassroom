package com.yiju.ClassClockRoom.bean;

public class ShareEntityBean {
	private Integer code;
	private String msg;
	private ShareBean data;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public ShareBean getData() {
		return data;
	}

	public void setData(ShareBean data) {
		this.data = data;
	}

	/*public static class ShareBean implements Parcelable {
		private String title;
		private String content;
		private String url;
		private String picurl;
		private Bitmap picicon;

		public String getTitle() {
			return StringUtils.isNullString(title) ? "" : title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getContent() {
			return StringUtils.isNullString(content) ? "" : content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getUrl() {
			return StringUtils.isNullString(url) ? "" : url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getPicurl() {
			return StringUtils.isNullString(picurl) ? "" : picurl;
		}

		public void setPicurl(String picurl) {
			this.picurl = picurl;
		}

		public Bitmap getPicicon() {
			return picicon;
		}

		public void setPicicon(Bitmap picicon) {
			this.picicon = picicon;
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(title);
			dest.writeString(content);
			dest.writeString(url);
			dest.writeString(picurl);

		}

		public static final Parcelable.Creator<ShareBean> CREATOR = new Creator<ShareBean>() {

			@Override
			public ShareBean[] newArray(int size) {
				return new ShareBean[size];
			}

			@Override
			public ShareBean createFromParcel(Parcel source) {
				ShareBean bean = new ShareBean();
				bean.title = source.readString();
				bean.content = source.readString();
				bean.url = source.readString();
				bean.picurl = source.readString();
				return bean;
			}
		};
	}*/
}
