package com.ecnu.meethere.order.result;

import com.ecnu.meethere.common.result.Result;

public class OrderResult {
    public static final int WRONG_ORDER_STATUS = 702;

    public static <T> Result<T> wrongOrderStatus() {
        return new Result<>(WRONG_ORDER_STATUS, "不正确的订单状态");
    }
}
