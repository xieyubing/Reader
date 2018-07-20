package com.xb.reader.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by asus on 2017/8/8.
 */

public class Records  extends DataSupport { //历史纪录
    private long id;
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Records{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
