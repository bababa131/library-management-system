package com.inventory.entity;

/**
 * 图书实体类
 * <p>
 * 封装图书基本信息：ISBN、书名、作者、库存数量、价格。
 */
public class Book {

    /** ISBN，作为图书的唯一标识 */
    private String isbn;

    /** 书名 */
    private String title;

    /** 作者 */
    private String author;

    /** 库存数量 */
    private int stock;

    /** 价格 */
    private double price;

    public Book(String isbn, String title, String author, int stock, double price) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        setStock(stock);
        setPrice(price);
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("库存数量不能为负数：" + stock);
        }
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("价格不能为负数：" + price);
        }
        this.price = price;
    }

    @Override
    public String toString() {
        return "Book{isbn='" + isbn + "', title='" + title + "', author='" + author
                + "', stock=" + stock + ", price=" + price + '}';
    }
}
