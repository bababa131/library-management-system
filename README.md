图书管理系统


基于 Java SE 开发的 C/S 架构控制台图书管理程序。

实现图书信息管理、借阅与归还、本地数据持久化，不依赖任何数据库与第三方框架。


项目简介


本项目用于夯实面向对象设计与 I/O 编程基础。

采用三层架构：UI 交互层、业务逻辑层、数据持久层。

程序启动时从本地文件加载历史数据，关闭时自动落盘，数据在重启后不丢失。


核心功能


1. 添加图书，ISBN 唯一性校验，重复时循环提示重新输入

2. 查看全部图书，按 ISBN 排序展示

3. 按 ISBN 精确查询，O(1) 时间复杂度

4. 按书名模糊查询，不区分大小写

5. 修改图书信息

6. 删除图书

7. 借阅图书，库存不足时提示并循环重新输入数量

8. 归还图书，自动回补库存

0. 保存并退出


技术要点


数据结构：使用 HashMap 以 ISBN 为唯一 Key 存储图书对象，实现 O(1) 精准查询；配合 ArrayList 完成书名模糊检索与列表展示。

数据持久化：使用 FileWriter 与 BufferedReader 读写 .dat 文本文件，字段之间用竖线分隔。文件不存在时通过 File.exists() 预判并自动初始化空文件，格式异常的数据行自动跳过，不阻断启动。

异常处理：ISBN 重复抛出 IllegalArgumentException；库存与价格强制非负校验；借阅库存不足抛出自定义 StockShortageException；控制台输入类型不匹配捕获 InputMismatchException。全部场景均循环提示重新输入，程序不会因非法操作崩溃。

单元测试：使用 JUnit 5 编写 9 个测试用例，覆盖增删改查、借还流程、库存异常与持久化往返场景。


环境要求


Java 17

Maven 3.6 及以上，或直接使用项目自带的 mvnw 包装器


如何运行


方式一：在 IDEA 中直接运行 LibraryManagementSystemApplication 的 main 方法。

方式二：命令行打包后运行，依次执行下面两条命令。

mvnw.cmd package

java -jar target/library-management-system-1.0.0.jar

程序启动后按菜单编号选择操作，图书数据保存在运行目录下的 data/books.dat 文件中。


如何测试


执行下面这条命令运行全部单元测试。

mvnw.cmd test


分支说明


master：主干分支，包含全部功能合并后的最终代码。

feat-book-add：Book 实体与图书增删改查功能。

feat-borrow-return：图书借阅与归还功能，含自定义库存不足异常。

feat-persistence：本地文件持久化与 JUnit 单元测试。
