package com.inventory.service;

import com.inventory.dao.FileBookDao;
import com.inventory.entity.Book;
import com.inventory.exception.StockShortageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * BookService + FileBookDao 集成测试（基于临时目录的文件持久化）
 */
class BookServiceTest {

    @TempDir
    Path tempDir;

    private String dataFilePath;
    private BookService bookService;

    @BeforeEach
    void setUp() {
        dataFilePath = tempDir.resolve("books.dat").toString();
        bookService = new BookService(new FileBookDao(dataFilePath));
    }

    @Test
    void addBookSuccess() {
        bookService.addBook(new Book("9787111", "Java核心技术", "凯 S. 霍斯特曼", 5, 99.0));
        assertNotNull(bookService.getByIsbn("9787111"));
        assertEquals(1, bookService.size());
    }

    @Test
    void addDuplicateIsbnThrows() {
        bookService.addBook(new Book("9787111", "Java核心技术", "凯 S. 霍斯特曼", 5, 99.0));
        assertThrows(IllegalArgumentException.class,
                () -> bookService.addBook(new Book("9787111", "Effective Java", "布洛赫", 3, 79.0)));
        // 原记录未被覆盖
        assertEquals("Java核心技术", bookService.getByIsbn("9787111").getTitle());
    }

    @Test
    void negativeStockThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new Book("9787111", "Java核心技术", "凯 S. 霍斯特曼", -1, 99.0));
    }

    @Test
    void borrowShortageThrows() {
        bookService.addBook(new Book("9787111", "Java核心技术", "凯 S. 霍斯特曼", 1, 99.0));
        assertThrows(StockShortageException.class, () -> bookService.borrow("9787111", 2));
        // 库存未被错误扣减
        assertEquals(1, bookService.getByIsbn("9787111").getStock());
    }

    @Test
    void borrowAndReturnAdjustStock() throws StockShortageException {
        bookService.addBook(new Book("9787111", "Java核心技术", "凯 S. 霍斯特曼", 3, 99.0));
        bookService.borrow("9787111", 2);
        assertEquals(1, bookService.getByIsbn("9787111").getStock());
        bookService.returnBook("9787111", 2);
        assertEquals(3, bookService.getByIsbn("9787111").getStock());
    }

    @Test
    void borrowNonExistentBookThrows() {
        assertThrows(IllegalArgumentException.class, () -> bookService.borrow("no-such-isbn", 1));
    }

    @Test
    void fuzzySearchByTitle() {
        bookService.addBook(new Book("1", "Java编程思想", "埃克尔", 2, 108.0));
        bookService.addBook(new Book("2", "深入理解Java虚拟机", "周志明", 2, 129.0));
        bookService.addBook(new Book("3", "C++ Primer", "李普曼", 2, 128.0));
        assertEquals(2, bookService.searchByTitle("Java").size());
        assertEquals(2, bookService.searchByTitle("java").size());
        assertTrue(bookService.searchByTitle("Python").isEmpty());
    }

    @Test
    void removeBookWorks() {
        bookService.addBook(new Book("9787111", "Java核心技术", "凯 S. 霍斯特曼", 5, 99.0));
        assertTrue(bookService.removeBook("9787111"));
        assertNull(bookService.getByIsbn("9787111"));
    }

    @Test
    void persistenceRoundTrip() {
        bookService.addBook(new Book("9787111", "Java核心技术", "凯 S. 霍斯特曼", 5, 99.0));
        bookService.save();

        // 模拟程序重启：从同一文件重新加载
        BookService reloaded = new BookService(new FileBookDao(dataFilePath));
        Book book = reloaded.getByIsbn("9787111");
        assertNotNull(book);
        assertEquals("Java核心技术", book.getTitle());
        assertEquals("凯 S. 霍斯特曼", book.getAuthor());
        assertEquals(5, book.getStock());
        assertEquals(99.0, book.getPrice());
    }
}
