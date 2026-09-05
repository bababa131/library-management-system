package com.inventory.service;

import com.inventory.entity.Book;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务逻辑层：图书核心业务处理
 * <p>
 * 内存中以 HashMap&lt;String, Book&gt; 以 ISBN 为唯一 Key 存储图书对象，
 * 实现 O(1) 时间复杂度的精准查询；书名模糊检索则遍历转换为 ArrayList 完成。
 */
public class BookService {

    /** ISBN -> 图书，O(1) 精准定位 */
    private final Map<String, Book> bookMap = new HashMap<>();

    /**
     * 添加图书
     *
     * @throws IllegalArgumentException ISBN 已存在时抛出
     */
    public void addBook(Book book) {
        if (bookMap.containsKey(book.getIsbn())) {
            throw new IllegalArgumentException("ISBN 已存在：" + book.getIsbn());
        }
        bookMap.put(book.getIsbn(), book);
    }

    /**
     * 按 ISBN 删除图书
     *
     * @return 是否删除成功
     */
    public boolean removeBook(String isbn) {
        return bookMap.remove(isbn) != null;
    }

    /**
     * 修改图书信息（书名、作者、库存、价格）
     *
     * @throws IllegalArgumentException 图书不存在时抛出
     */
    public Book updateBook(String isbn, String title, String author, int stock, double price) {
        Book book = requireBook(isbn);
        book.setTitle(title);
        book.setAuthor(author);
        book.setStock(stock);
        book.setPrice(price);
        return book;
    }

    /**
     * 按 ISBN 精准查询，O(1)
     *
     * @return 未找到返回 null
     */
    public Book getByIsbn(String isbn) {
        return bookMap.get(isbn);
    }

    /** 查看全部图书（按 ISBN 排序） */
    public List<Book> listAll() {
        List<Book> books = new ArrayList<>(bookMap.values());
        books.sort(Comparator.comparing(Book::getIsbn));
        return books;
    }

    /**
     * 按书名模糊检索（不区分大小写），配合 ArrayList 完成列表展示
     */
    public List<Book> searchByTitle(String keyword) {
        List<Book> result = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        for (Book book : bookMap.values()) {
            if (book.getTitle().toLowerCase().contains(lowerKeyword)) {
                result.add(book);
            }
        }
        result.sort(Comparator.comparing(Book::getIsbn));
        return result;
    }

    /** 当前馆藏图书总数 */
    public int size() {
        return bookMap.size();
    }

    private Book requireBook(String isbn) {
        Book book = bookMap.get(isbn);
        if (book == null) {
            throw new IllegalArgumentException("图书不存在，ISBN：" + isbn);
        }
        return book;
    }
}
