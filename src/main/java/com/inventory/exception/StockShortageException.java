package com.inventory.exception;

/**
 * 库存不足异常
 * <p>
 * 借阅图书时，若借阅数量超过当前库存则由业务层抛出。
 */
public class StockShortageException extends Exception {

    /** 用户请求借阅的数量 */
    private final int requested;

    /** 当前实际库存 */
    private final int available;

    public StockShortageException(int requested, int available) {
        super("库存不足：请求借阅 " + requested + " 本，当前库存仅 " + available + " 本");
        this.requested = requested;
        this.available = available;
    }

    public int getRequested() {
        return requested;
    }

    public int getAvailable() {
        return available;
    }
}
