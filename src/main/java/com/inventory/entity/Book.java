package com.inventory.entity;

/**
 * 图书实体类
 * <p>
 * 封装图书基本信息，并提供与持久化层配套的序列化 / 反序列化方法，
 * 持久化文本行以 {@link #DELIMITER} 作为字段分隔符。
 */
public class Book {

    /** 持久化文件中的字段分隔符 */
    public static final String DELIMITER = "|";

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

    /**
     * 序列化为持久化文本行：isbn|title|author|stock|price
     */
    public String toLine() {
        return isbn + DELIMITER + title + DELIMITER + author + DELIMITER + stock + DELIMITER + price;
    }

    /**
     * 从持久化文本行反序列化为 Book 对象
     *
     * @param line 以 | 分隔的一行文本
     * @throws IllegalArgumentException 字段数量不足或数值格式非法时抛出
     */
    public static Book fromLine(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length != 5) {
            throw new IllegalArgumentException("数据行字段数量异常：" + line);
        }
        try {
            return new Book(parts[0], parts[1], parts[2],
                    Integer.parseInt(parts[3]), Double.parseDouble(parts[4]));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("数据行数值格式异常：" + line, e);
        }
    }

    @Override
    public String toString() {
        return "Book{isbn='" + isbn + "', title='" + title + "', author='" + author
                + "', stock=" + stock + ", price=" + price + '}';
    }
}
