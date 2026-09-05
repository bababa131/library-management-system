package com.inventory.dao;

import com.inventory.entity.Book;

import java.util.Collection;
import java.util.List;

/**
 * 数据持久层接口
 * <p>
 * 屏蔽具体存储细节，业务层只依赖本接口，便于后续替换存储实现
 * （如从本地文件切换为数据库）。
 */
public interface BookDao {

    /**
     * 从本地存储加载全部图书
     *
     * @return 图书集合；存储文件不存在时返回空集合
     */
    List<Book> loadAll();

    /**
     * 将全部图书写入本地存储（全量覆盖写）
     *
     * @param books 待持久化的图书集合
     */
    void saveAll(Collection<Book> books);
}
