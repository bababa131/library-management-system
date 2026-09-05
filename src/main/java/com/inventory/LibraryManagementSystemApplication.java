package com.inventory;

import com.inventory.service.BookService;
import com.inventory.ui.LibraryConsoleUI;

/**
 * 图书管理系统启动入口
 * <p>
 * 当前阶段：UI 交互层（ui） + 业务逻辑层（service），数据仅存于内存。
 */
public class LibraryManagementSystemApplication {

    public static void main(String[] args) {
        new LibraryConsoleUI(new BookService()).start();
    }
}
