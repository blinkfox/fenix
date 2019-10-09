package com.blinkfox.fenix.provider;

import com.blinkfox.fenix.bean.SqlInfo;
import com.blinkfox.fenix.core.Fenix;
import com.blinkfox.fenix.helper.StringHelper;
import com.blinkfox.fenix.vo.UserBlogInfo;

import org.springframework.data.repository.query.Param;

/**
 * 使用 Java 代码来拼接用户动态 SQL 的 Provider 类.
 *
 * @author blinkfox on 2019-10-09.
 * @since v1.1.0
 */
public final class BlogSqlInfoProvider {

    /**
     * 使用 Java 拼接 SQL 的方式来拼接查询用户博客信息的 SQL 信息.
     *
     * @param userId 用户 ID
     * @param title 标题
     * @return SQL 信息
     */
    public SqlInfo queryUserBlogsWithFenixJava(@Param("userId") String userId, @Param("title") String title) {
        return Fenix.start()
                .select("u.id AS userId, u.name AS name, b.id AS blogId, b.title AS title, b.author AS author, "
                        + "b.content AS content")
                .from("Blog as b, User as u")
                .where("u.id = b.userId")
                .andEqual("b.userId", userId)
                .andLike("b.title", title, StringHelper.isNotBlank(title))
                .end()
                .setResultTypeClass(UserBlogInfo.class);
    }

}
