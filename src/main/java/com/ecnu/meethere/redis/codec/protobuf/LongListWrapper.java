package com.ecnu.meethere.redis.codec.protobuf;

import java.util.List;

public class LongListWrapper extends ListWrapper<Long> {
    public LongListWrapper() {
    }

    public LongListWrapper(List<Long> list) {
        super(list);
    }
}
