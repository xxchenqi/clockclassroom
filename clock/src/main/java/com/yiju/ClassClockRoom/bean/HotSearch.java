package com.yiju.ClassClockRoom.bean;

/**
 * 作者： 葛立平
 * 2016/3/9 10:39
 */
public class HotSearch {
    /**
     * id : 1
     * word : 徐汇红梅
     * create_time : 0
     * sort : 0
     */
    private String id;
    private String word;
    private String create_time;
    private String sort;

    public void setId(String id) {
        this.id = id;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public String getCreate_time() {
        return create_time;
    }

    public String getSort() {
        return sort;
    }
}
