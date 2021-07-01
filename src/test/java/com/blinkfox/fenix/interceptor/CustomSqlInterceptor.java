package com.blinkfox.fenix.interceptor;

import com.blinkfox.fenix.helper.AnnotationHelper;
import com.blinkfox.fenix.jpa.interceptor.SqlInterceptor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class CustomSqlInterceptor implements SqlInterceptor {

    /**
     * @param method
     * @param sql
     * @return
     */
    @Override
    public String onPrepareStatement(Method method, String sql) {
        /**
         * 这里只是进行模拟自动增加别名, 如果用户引用了 durid连接池 则可以使用它的sqlparse模块自动增加上别名, 或者引入其它的sql解析模块动态给sql增加别名, 在这里还可以实现转驼峰
         */

        System.out.println("拦截到sql: " + sql);
        String name = method.getName();
        if("interceptorQuery".equals(name)){
            String newSql = "SELECT " +
                    " u.id as id, u.name as name, u.age as age, u.sex as sex, u.password as password, " +
                    " u.birthday as birthday, u.createTime as createTime, u.updateTime as updateTime, " +
                    " u.status as status, u.email as email " +
                    " FROM User AS u";

            Query query = method.getAnnotation(Query.class);
            System.out.println("修改前: " + query.value());
            // 修改 @Query 注解的值
            AnnotationHelper.updateAnnotationProperty(query, "value", newSql);
            System.out.println("修改后: " + query.value());
            System.out.println("修改拦截到的sql为: " + newSql);
        } else if("interceptorQueryFenix".equals(name)){
            String newSql = "SELECT\n" +
                    "            u.id as id,\n" +
                    "            u.name as name,\n" +
                    "            u.age as age,\n" +
                    "            u.sex as sex,\n" +
                    "            u.password as password,\n" +
                    "            u.birthday as birthday,\n" +
                    "            u.createTime as createTime,\n" +
                    "            u.updateTime as updateTime,\n" +
                    "            u.status as status,\n" +
                    "            u.email as email\n" +
                    "        FROM User AS u\n" +
                    "        WHERE\n" +
                    "        1 = 1\n";

            System.out.println("修改拦截到的sql为: " + newSql);
            return newSql;
        }

        return sql;
    }

}
