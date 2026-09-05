package com.inventory;

import com.inventory.dao.BookDao;
import com.inventory.dao.FileBookDao;
import com.inventory.service.BookService;
import com.inventory.ui.LibraryConsoleUI;

/**
 * 图书管理系统启动入口
 * <p>
 * 三层架构：UI 交互层（ui） -> 业务逻辑层（service） -> 数据持久层（dao）。
 * 程序启动时从本地文件加载历史数据，关闭时通过 JVM 关闭钩子自动落盘，
 * 确保数据在程序重启后不丢失。
 */
public class LibraryManagementSystemApplication {

    /** 本地数据文件路径（相对项目运行目录） */
    private static final String DATA_FILE = "data/books.dat";

    public static void main(String[] args) {
        BookDao bookDao = new FileBookDao(DATA_FILE);
        BookService bookService = new BookService(bookDao);

        // 程序退出（含异常终止）时自动落盘
        Runtime.getRuntime().addShutdownHook(new Thread(bookService::save, "data-save-hook"));

        new LibraryConsoleUI(bookService).start();
    }
}
