package com.blinkfox.fenix.id;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

/**
 * {@link IdWorker} 的单元测试类.
 *
 * @author blinkfox on 2020-12-07.
 * @since v2.4.0
 */
@Slf4j
public class IdWorkerTest {

    private final IdWorker idWorker = new IdWorker();

    /**
     * 测试生成 ID.
     */
    @Test
    public void getId() {
        long id = idWorker.getId();
        Assert.assertEquals(16, Long.toString(id).length());
        Assert.assertTrue(idWorker.getId() > id);
        Assert.assertNotNull(idWorker.getIdString());
    }

    /**
     * 测试通过其他参数生成 ID.
     */
    @Test
    public void getId2() {
        IdWorker myIdWorker = new IdWorker(5);
        long id = myIdWorker.getId();
        Assert.assertEquals(16, Long.toString(id).length());
        Assert.assertTrue(myIdWorker.getId() > id);
    }

    /**
     * 测试生成 36 进制数的 ID.
     */
    @Test
    public void get36RadixId() {
        long id = idWorker.getId();
        Assert.assertNotNull(IdWorker.get36RadixId(id));
        Assert.assertNotNull(idWorker.get36RadixId());
    }

    /**
     * 测试生成 62 进制数的 ID.
     */
    @Test
    public void get62RadixId() {
        long id = idWorker.getId();
        Assert.assertNotNull(IdWorker.get62RadixId(id));
        Assert.assertNotNull(idWorker.get62RadixId());
    }

    /**
     * 测试生成的 UUID.
     */
    @Test
    public void getSnowflakeId() {
        long id = IdWorker.getSnowflakeId();
        log.info("snowflake id: [{}]", id);
        Assert.assertTrue(id > 0);

        String id2 = IdWorker.getSnowflakeIdString();
        log.info("snowflake id string: [{}]", id2);
        Assert.assertNotNull(id2);

        String id3 = IdWorker.getSnowflake36RadixId();
        log.info("36 radix snowflake id: [{}]", id3);
        Assert.assertNotNull(id3);

        String id4 = IdWorker.getSnowflake62RadixId();
        log.info("62 radix snowflake id: [{}]", id4);
        Assert.assertNotNull(id4);
    }

    /**
     * 测试生成的 UUID.
     */
    @Test
    public void getUuid() {
        Assert.assertEquals(32, IdWorker.getUuid().length());
    }

    /**
     * 测试生成的 UUID.
     */
    @Test
    public void get62RadixUuid() {
        Assert.assertEquals(19, IdWorker.get62RadixUuid().length());
    }

    @Test
    public void getNanoId() {
        String nanoId = IdWorker.getNanoId();
        log.info("NanoId 为：【{}】.", nanoId);
        Assert.assertEquals(IdWorker.DEFAULT_SIZE, nanoId.length());

        int size = 15;
        String nanoId2 = IdWorker.getNanoId(size);
        log.info("长度为 15 的 NanoId 为：【{}】.", nanoId2);
        Assert.assertEquals(size, nanoId2.length());
    }

}
