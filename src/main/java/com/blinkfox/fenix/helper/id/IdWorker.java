package com.blinkfox.fenix.helper.id;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * {@code Id} 生成器的核心 API 类.
 *
 * @author blinkfox on 2020-12-07.
 * @since v2.4.0
 */
public final class IdWorker {

    private static final int MAX_WORKER_INDEX = 0x0F;

    private static final int WORKER_LENGTH = 16;

    private final AtomicInteger indexCounter = new AtomicInteger(0);

    private IdGenerator[] generators;

    /**
     * 默认的构造方法.
     */
    public IdWorker() {
        this.setWorkerIndexes();
    }

    /**
     * 基于索引参数的构造方法.
     *
     * @param indexes 索引不定参的数组
     */
    public IdWorker(int... indexes) {
        this.setWorkerIndexes(indexes);
    }

    /**
     * 设置 ID 生成器的索引数组.
     *
     * @param workIndexes workIndexes
     */
    private void setWorkerIndexes(int... workIndexes) {
        int[] indexes = workIndexes == null || workIndexes.length == 0 ? newSequence() : newSequence(workIndexes);
        Map<Integer, IdGenerator> generatorMap = new HashMap<>(WORKER_LENGTH);
        this.generators = new IdGenerator[WORKER_LENGTH];
        for (int i = 0; i < WORKER_LENGTH; i++) {
            int index = indexes[i];
            this.generators[i] = generatorMap.computeIfAbsent(index, k -> new IdGenerator(index));
        }
    }

    /**
     * 生成 {@code int} 型数组序列.
     *
     * @param source 源 {@code int} 型数组
     * @return 目标 {@code int} 型数组
     */
    private int[] newSequence(int... source) {
        int[] arr = new int[WORKER_LENGTH];
        int len = source.length;
        for (int i = 0, j = 0; i < WORKER_LENGTH; i++, j++) {
            if (j >= len) {
                j = 0;
            }

            if (source[j] >= WORKER_LENGTH) {
                throw new IllegalArgumentException("ID Worker 索引必须小于" + WORKER_LENGTH + ", 实际值是:" + source[j]);
            }
            arr[i] = source[j];
        }

        return arr;
    }

    private int[] newSequence() {
        int[] arr = new int[WORKER_LENGTH];
        for (int i = 0; i < WORKER_LENGTH; i++) {
            arr[i] = i;
        }
        return arr;
    }

    /**
     * 获取长度为 16 位数的 {@code long} 长整型 {@code ID} 值.
     *
     * @return 16 位数的整型 {@code ID} 值
     */
    public long getId() {
        return this.generators[this.indexCounter.incrementAndGet() & MAX_WORKER_INDEX].nextId();
    }

    /**
     * 获取长度为 16 位数的长整型 {@code ID} 的字符串值.
     *
     * @return 长整型 {@code ID} 的字符串值
     */
    public String getIdString() {
        return Long.toString(this.getId());
    }

    /**
     * 获取 36 进制的 {@code ID} 符串值.
     *
     * @return 36 进制的 {@code ID} 字符串
     */
    public String get36RadixId() {
        return Long.toString(this.getId(), Character.MAX_RADIX);
    }

    /**
     * 根据给定的 16 位数的长整型 {@code ID}，得到其对应的 36 进制的 {@code ID} 符串值.
     *
     * @param id 长整型 {@code ID}
     * @return 36 进制的 {@code ID} 字符串
     */
    public static String get36RadixId(long id) {
        return Long.toString(id, Character.MAX_RADIX);
    }

    /**
     * 获取 62 进制的 {@code ID} 符串值.
     *
     * @return 62 进制的 {@code ID} 字符串
     */
    public String get62RadixId() {
        return get62RadixId(this.getId());
    }

    /**
     * 根据给定的长整型 {@code ID}，得到其对应的 62 进制的 {@code ID} 符串值.
     *
     * @param id 长整型 {@code ID}
     * @return 62 进制的 {@code ID} 字符串
     */
    public static String get62RadixId(long id) {
        return Radix.toString(id, Radix.RADIX_62);
    }

    /**
     * 获取长度为 32 位数的 {@code UUID} 字符串.
     *
     * @return 32 位的 {@code UUID}
     */
    public static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取 {@code 62} 进制的长度为 19 位长度的 {@code UUID} 字符串.
     *
     * @return {@code 62} 进制位的 {@code UUID}
     */
    public static String get62RadixUuid() {
        UUID uuid = UUID.randomUUID();
        return new StringBuilder()
                .append(Radix.digits(uuid.getMostSignificantBits() >> 32, 8))
                .append(Radix.digits(uuid.getMostSignificantBits() >> 16, 4))
                .append(Radix.digits(uuid.getMostSignificantBits(), 4))
                .append(Radix.digits(uuid.getLeastSignificantBits() >> 48, 4))
                .append(Radix.digits(uuid.getLeastSignificantBits(), 12))
                .toString();
    }

    /**
     * ID 生成器静态内部类.
     *
     * @author blinkfox 2019-11-07.
     */
    private static class IdGenerator {

        private final long workerId;

        private static final long EPOCH = 1457258545962L;

        private static final long MAX_WORKER_ID = 15L;

        private static final long WORKER_ID_SHIFT = 10L;

        private static final long TIMESTAMP_LEFT_SHIFT = 14L;

        private static final long SEQUENCE_MASK = 1023L;

        private long sequence = 0L;

        private int vibrance = -1;

        private long lastTimestamp = -1L;

        /**
         * 基于 workerId 的构造方法.
         *
         * @param workerId workerId
         */
        IdGenerator(final long workerId) {
            if (workerId < 0 || workerId > MAX_WORKER_ID) {
                throw new IllegalArgumentException(String.format("worker Id 不能小于0或者大于 %d", MAX_WORKER_ID));
            }
            this.workerId = workerId;
        }

        /**
         * 生成下一个 {@code long} 型的 ID.
         *
         * @return ID
         */
        synchronized long nextId() {
            long timestamp = System.currentTimeMillis();
            if (timestamp == this.lastTimestamp) {
                this.sequence = (this.sequence + 1) & SEQUENCE_MASK;
                if (this.sequence == 0) {
                    timestamp = this.tillNextMillis(this.lastTimestamp);
                }
            } else {
                this.sequence = (vibrance = ~vibrance & 1);
            }

            this.lastTimestamp = timestamp;
            return (timestamp - EPOCH << TIMESTAMP_LEFT_SHIFT) | (this.workerId << WORKER_ID_SHIFT) | (this.sequence);
        }

        private long tillNextMillis(final long lastTimestamp) {
            long timestamp = System.currentTimeMillis();
            while (timestamp <= lastTimestamp) {
                timestamp = System.currentTimeMillis();
            }
            return timestamp;
        }
    }

}
