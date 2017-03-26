package com.yiju.ClassClockRoom.bean;

public class PictureWrite {
	private boolean flag;
	private Result result;

	public boolean isFlag() {
		return flag;
	}

	public Result getResult() {
		return result;
	}

	public class Result {
		private int pic_height;
		private String pic_id;
		private int pic_size;
		private int pic_width;

		public int getPic_height() {
			return pic_height;
		}

		public String getPic_id() {
			return pic_id;
		}

		public int getPic_size() {
			return pic_size;
		}

		public int getPic_width() {
			return pic_width;
		}

	}
}
