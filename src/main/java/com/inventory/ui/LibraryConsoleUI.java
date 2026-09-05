package com.inventory.ui;

import com.inventory.entity.Book;
import com.inventory.service.BookService;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * UI 交互层：控制台菜单驱动
 * <p>
 * 负责菜单展示、用户输入校验（类型不匹配 / 空输入 / 非法数值均循环提示重新输入），
 * 并将合法请求转发给业务逻辑层，自身不保存任何业务数据。
 */
public class LibraryConsoleUI {

    private final BookService bookService;
    private final Scanner scanner = new Scanner(System.in);

    public LibraryConsoleUI(BookService bookService) {
        this.bookService = bookService;
    }

    /** 启动菜单主循环 */
    public void start() {
        System.out.println("==================================================");
        System.out.println("           欢迎使用图书管理系统 v1.0");
        System.out.println("==================================================");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("请选择操作编号：");
            try {
                switch (choice) {
                    case 1 -> addBook();
                    case 2 -> listAllBooks();
                    case 3 -> queryByIsbn();
                    case 4 -> searchByTitle();
                    case 5 -> updateBook();
                    case 6 -> removeBook();
                    case 0 -> running = false;
                    default -> System.out.println("无效的操作编号，请重新选择！");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("操作失败：" + e.getMessage());
            }
        }
        System.out.println("感谢使用，再见！");
    }

    // ==================== 菜单功能 ====================

    private void addBook() {
        System.out.println("---------- 添加图书 ----------");
        String isbn;
        while (true) {
            isbn = readRequired("请输入 ISBN：");
            if (bookService.getByIsbn(isbn) == null) {
                break;
            }
            System.out.println("该 ISBN 已存在，请更换后重新输入！");
        }
        String title = readRequired("请输入书名：");
        String author = readRequired("请输入作者：");
        int stock = readNonNegativeInt("请输入库存数量：");
        double price = readNonNegativeDouble("请输入价格：");

        bookService.addBook(new Book(isbn, title, author, stock, price));
        System.out.println("图书添加成功！");
    }

    private void listAllBooks() {
        System.out.println("---------- 全部图书 ----------");
        printBooks(bookService.listAll());
    }

    private void queryByIsbn() {
        System.out.println("---------- ISBN 精确查询 ----------");
        String isbn = readRequired("请输入要查询的 ISBN：");
        Book book = bookService.getByIsbn(isbn);
        if (book == null) {
            System.out.println("未找到 ISBN 为 " + isbn + " 的图书。");
        } else {
            printBooks(List.of(book));
        }
    }

    private void searchByTitle() {
        System.out.println("---------- 书名模糊查询 ----------");
        String keyword = readRequired("请输入书名关键字：");
        printBooks(bookService.searchByTitle(keyword));
    }

    private void updateBook() {
        System.out.println("---------- 修改图书信息 ----------");
        String isbn = readRequired("请输入要修改的图书 ISBN：");
        Book book = bookService.getByIsbn(isbn);
        if (book == null) {
            System.out.println("未找到 ISBN 为 " + isbn + " 的图书，无法修改。");
            return;
        }
        System.out.println("当前信息：" + book);
        String title = readRequired("请输入新书名：");
        String author = readRequired("请输入新作者：");
        int stock = readNonNegativeInt("请输入新库存数量：");
        double price = readNonNegativeDouble("请输入新价格：");

        bookService.updateBook(isbn, title, author, stock, price);
        System.out.println("图书信息修改成功！");
    }

    private void removeBook() {
        System.out.println("---------- 删除图书 ----------");
        String isbn = readRequired("请输入要删除的图书 ISBN：");
        if (bookService.removeBook(isbn)) {
            System.out.println("图书删除成功！");
        } else {
            System.out.println("未找到 ISBN 为 " + isbn + " 的图书，删除失败。");
        }
    }

    // ==================== 展示与输入校验 ====================

    private void printMenu() {
        System.out.println();
        System.out.println("================= 图书管理系统 =================");
        System.out.println("  1. 添加图书");
        System.out.println("  2. 查看所有图书");
        System.out.println("  3. 按 ISBN 精确查询");
        System.out.println("  4. 按书名模糊查询");
        System.out.println("  5. 修改图书信息");
        System.out.println("  6. 删除图书");
        System.out.println("  0. 退出");
        System.out.println("================================================");
    }

    private void printBooks(List<Book> books) {
        if (books.isEmpty()) {
            System.out.println("暂无符合条件的图书记录。");
            return;
        }
        System.out.printf("%-16s %-24s %-16s %-6s %-10s%n", "ISBN", "书名", "作者", "库存", "价格");
        for (Book book : books) {
            System.out.printf("%-16s %-24s %-16s %-6d %-10.2f%n",
                    book.getIsbn(), book.getTitle(), book.getAuthor(),
                    book.getStock(), book.getPrice());
        }
        System.out.println("共 " + books.size() + " 条记录。");
    }

    /** 读取非空字符串 */
    private String readRequired(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                System.out.println("输入不能为空，请重新输入！");
            } else {
                return line;
            }
        }
    }

    /** 读取整数，类型不匹配（InputMismatchException）时循环提示重新输入 */
    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = scanner.nextInt();
                scanner.nextLine(); // 消费行尾换行符
                return value;
            } catch (InputMismatchException e) {
                scanner.nextLine(); // 丢弃非法输入
                System.out.println("输入类型不匹配，请输入一个整数！");
            }
        }
    }

    /** 读取小数，类型不匹配（InputMismatchException）时循环提示重新输入 */
    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = scanner.nextDouble();
                scanner.nextLine();
                return value;
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("输入类型不匹配，请输入一个数字！");
            }
        }
    }

    /** 读取非负整数（用于库存数量） */
    private int readNonNegativeInt(String prompt) {
        while (true) {
            int value = readInt(prompt);
            if (value >= 0) {
                return value;
            }
            System.out.println("数量不能为负数，请重新输入！");
        }
    }

    /** 读取非负小数（用于价格） */
    private double readNonNegativeDouble(String prompt) {
        while (true) {
            double value = readDouble(prompt);
            if (value >= 0) {
                return value;
            }
            System.out.println("数值不能为负数，请重新输入！");
        }
    }
}
