package com.blinkfox.fenix.jpa;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javax.persistence.EntityManager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 继承了 {@link SimpleJpaRepository} 类，实现了 {@link FenixJpaRepository} 接口的 Fenix JPA Repository 基础实现类.
 *
 * @author blinkfox on 2020-12-04.
 * @since v2.4.0
 */
@Repository
@Transactional(readOnly = true)
public class FenixSimpleJpaRepository<T, ID> extends SimpleJpaRepository<T, ID> implements FenixJpaRepository<T, ID> {

    private final JpaEntityInformation<T, ?> entityInformation;

    private final EntityManager em;

    /**
     * 构造方法.
     *
     * @param entityInformation JPA 实体信息类，不能为 {@literal null}.
     * @param entityManager 实体管理器类，不能为 {@literal null}.
     */
    public FenixSimpleJpaRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        this.em = entityManager;
    }

    /**
     * 构造方法.
     *
     * @param domainClass JPA 实体类的 class，不能为 {@literal null}.
     * @param em 实体管理器类，不能为 {@literal null}.
     */
    public FenixSimpleJpaRepository(Class<T> domainClass, EntityManager em) {
        this(JpaEntityInformationSupport.getEntityInformation(domainClass, em), em);
    }

    /**
     * 保存或更新实体类中非 null 字段的值.
     *
     * <ul>
     *     <li>如果实体的主键 ID 为空，说明是新增的情况，就插入一条新的数据；</li>
     *     <li>如果实体的主键 ID 不为空，说明是更新的情况，会仅更新实体类属性中不为 null 值的数据到数据库中；</li>
     * </ul>
     *
     * @param entity 实体类
     * @return 保存后的实体类
     */
    @Override
    public <S extends T> S saveOrUpdateNotNullFields(S entity) {
        //获取ID
        ID entityId = (ID) entityInformation.getId(entity);
        Optional<T> optionalT;
        if (StringUtils.isEmpty(entityId)) {
            //String uuid = UUID.randomUUID().toString();
            //防止UUID重复
            //if (findById((ID) uuid).isPresent()) {
            //    uuid = UUID.randomUUID().toString();
            //}
            //若ID为空 则设置为UUID
            //new BeanWrapperImpl(entity).setPropertyValue(entityInformation.getIdAttribute().getName(), uuid);
            //标记为新增数据
            optionalT = Optional.empty();
        } else {
            //若ID非空 则查询最新数据
            optionalT = findById(entityId);
        }
        //获取空属性并处理成null
        String[] nullProperties = getNullProperties(entity);
        //若根据ID查询结果为空
        if (!optionalT.isPresent()) {
            //新增
            em.persist(entity);
            return entity;
        } else {
            //1.获取最新对象
            T target = optionalT.get();
            //2.将非空属性覆盖到最新对象
            BeanUtils.copyProperties(entity, target, nullProperties);
            //3.更新非空属性
            em.merge(target);
            return entity;
        }

    }

    /**
     * 获取对象的空属性
     */
    private static String[] getNullProperties(Object src) {
        //1.获取Bean
        BeanWrapper srcBean = new BeanWrapperImpl(src);
        //2.获取Bean的属性描述
        PropertyDescriptor[] pds = srcBean.getPropertyDescriptors();
        //3.获取Bean的空属性
        Set<String> properties = new HashSet<>();
        for (PropertyDescriptor propertyDescriptor : pds) {
            String propertyName = propertyDescriptor.getName();
            Object propertyValue = srcBean.getPropertyValue(propertyName);
            if (StringUtils.isEmpty(propertyValue)) {
                srcBean.setPropertyValue(propertyName, null);
                properties.add(propertyName);
            }
        }
        return properties.toArray(new String[0]);
    }

}
