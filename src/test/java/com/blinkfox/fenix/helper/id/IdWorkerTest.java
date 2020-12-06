package com.blinkfox.fenix.helper.id;

import org.junit.Assert;
import org.junit.Test;

/**
 * {@link IdWorker} 的单元测试类.
 *
 * @author blinkfox on 2020-12-07.
 * @since v2.4.0
 */
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

}
