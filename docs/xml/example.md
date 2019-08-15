对于很长的、复杂的动态或统计性的 SQL 采用注解 或者 Java 书写或者不仅冗长，且不易于调试和维护。因此，我更推荐你通过 `XML` 文件来书写 SQL，使得 SQL 和 Java 代码解耦，更易于维护和阅读。

## Blog 实体类

以下将以博客（`Blog`）作为实体来演示在 Fenix 中 XML 动态 SQL 的使用。`Blog` 实体类代码如下：

```java
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 博客信息实体类.
 *
 * @author blinkfox on 2019-08-13.
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "t_blog")
public class Blog {

    /**
     * ID.
     */
    @Id
    @Column(name = "c_id")
    private String id;

    /**
     * 发表博客的用户 ID.
     */
    @Column(name = "c_user_id")
    private String userId;

    /**
     * 博客标题.
     */
    @Column(name = "c_title")
    private String title;

    /**
     * 博客作者.
     */
    @Column(name = "c_author")
    private String author;

    /**
     * 博客内容.
     */
    @Column(name = "c_content")
    private String content;

    /**
     * 创建时间.
     */
    @Column(name = "dt_create_time")
    private Date createTime;

    /**
     * 更新时间.
     */
    @Column(name = "dt_update_time")
    private Date updateTime;

}
```

## 