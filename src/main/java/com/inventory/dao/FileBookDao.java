package com.inventory.dao;

import com.inventory.entity.Book;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 基于本地文本文件的图书持久化实现
 * <p>
 * 不依赖任何数据库，使用 FileWriter / BufferedReader 配合自定义分隔符 "|"
 * 读写 .dat 文本文件：每行存储一本图书，字段顺序为
 * isbn|title|author|stock|price。
 */
public class FileBookDao implements BookDao {

    private final File dataFile;

    public FileBookDao(String filePath) {
        this.dataFile = new File(filePath);
    }

    @Override
    public List<Book> loadAll() {
        List<Book> books = new ArrayList<>();

        // 文件不存在时通过 File.exists() 预判，自动初始化空文件
        if (!dataFile.exists()) {
            initEmptyFile();
            return books;
        }

        try (BufferedReader reader = new BufferedReader(
                new FileReader(dataFile, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                try {
                    books.add(Book.fromLine(line));
                } catch (IllegalArgumentException e) {
                    // 格式异常的数据行跳过，避免脏数据导致启动失败
                    System.err.println("数据文件格式异常，已跳过该行：" + line);
                }
            }
        } catch (IOException e) {
            System.err.println("读取数据文件失败：" + e.getMessage());
        }
        return books;
    }

    @Override
    public void saveAll(Collection<Book> books) {
        ensureParentDir();
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(dataFile, StandardCharsets.UTF_8))) {
            for (Book book : books) {
                writer.write(book.toLine());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("保存数据文件失败：" + e.getMessage());
        }
    }

    /** 数据文件不存在时自动创建空文件（含父目录） */
    private void initEmptyFile() {
        ensureParentDir();
        try {
            if (!dataFile.createNewFile()) {
                System.err.println("初始化数据文件失败：" + dataFile.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("初始化数据文件失败：" + e.getMessage());
        }
    }

    /** 确保数据文件所在目录存在 */
    private void ensureParentDir() {
        File parent = dataFile.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            System.err.println("创建数据目录失败：" + parent.getAbsolutePath());
        }
    }
}
