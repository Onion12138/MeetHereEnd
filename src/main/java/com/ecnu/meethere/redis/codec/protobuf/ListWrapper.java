package com.ecnu.meethere.redis.codec.protobuf;

import java.util.List;

public class ListWrapper<T> {
    private final List<T> list;

    public ListWrapper() {
        this(null);
    }

    public ListWrapper(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }
}
