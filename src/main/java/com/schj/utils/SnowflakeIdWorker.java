package com.schj.utils;

public class SnowflakeIdWorker {

    // 起始的时间戳
    private final static long START_STMP = 1672531200000L; // 2023-01-01 00:00:00

    // 每一部分占用的位数
    private final static long SEQUENCE_BIT = 12; // 序列号占用的位数
    private final static long MACHINE_BIT = 10; // 机器标识占用的位数
    private final static long TIMESTAMP_BIT = 41; // 时间戳占用的位数

    // 每一部分的最大值
    private final static long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);
    private final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);

    // 每一部分向左的位移
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long TIMESTAMP_LEFT = SEQUENCE_BIT + MACHINE_BIT;

    private long machineId; // 机器标识
    private long sequence = 0L; // 序列号
    private long lastStmp = -1L; // 上一次时间戳

    public SnowflakeIdWorker(long machineId) {
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.machineId = machineId;
    }

    // 产生下一个 ID
    public synchronized long nextId() {
        long currStmp = getNewstmp();
        if (currStmp < lastStmp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate id");
        }

        if (currStmp == lastStmp) {
            // 相同毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            // 同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currStmp = getNextMill();
            }
        } else {
            // 不同毫秒内，序列号置为 0
            sequence = 0L;
        }

        lastStmp = currStmp;

        return (currStmp - START_STMP) << TIMESTAMP_LEFT // 时间戳部分
                | machineId << MACHINE_LEFT // 机器标识部分
                | sequence; // 序列号部分
    }

    // 获取当前时间戳
    private long getNewstmp() {
        return System.currentTimeMillis();
    }

    // 阻塞到下一个毫秒，直到获得新的时间戳
    private long getNextMill() {
        long mill = getNewstmp();
        while (mill <= lastStmp) {
            mill = getNewstmp();
        }
        return mill;
    }
}
