package com.jiahe.iot.common.util;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;


public final class UniqueIdUtil {

    private static final String MACHINE_ID;

    static {
        MACHINE_ID = StringUtils.leftPad(IpUtil.getIpAddress().split("\\.")[3], 4, '0');
    }

    private static final AtomicInteger SEQUENCE = new AtomicInteger(1000);

    /**
     * 生成requestId
     *
     * @return RIDyyyyMMddmmss4位序号4位随机码
     * RID2020050910390112341234
     */
    public static String newRequestId() {
        return newUniqueId("RID");
    }

    public static String newTraceId() {
        return newUniqueId("TZX");
    }

    public static String newUserId() {
        return "UID" + UUID.randomUUID().toString().replace("-", "");
    }

    public static String newAreaId() {
        return "AID" + UUID.randomUUID().toString().replace("-", "");
    }

    public static String newProductId() {
        return "PID" + UUID.randomUUID().toString().replace("-", "");
    }

    public static String newDevicetId() {
        return "DID" + UUID.randomUUID().toString().replace("-", "") + newRamdomString(16);
    }

    public static String newUniqueId(String prefix) {
        int id = SEQUENCE.getAndIncrement();
        if (id >= 5000) {
            SEQUENCE.set(1000);
        }
        return prefix + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + id + MACHINE_ID;
    }

    static final String sources = "0123456789abcdefghijklmnopqrstuvyxyz";

    public static String newRamdomString(int length) {
        StringBuffer flag = new StringBuffer();
        Random rand = new Random();
        for (int j = 0; j < length; j++) {
            flag.append(sources.charAt(rand.nextInt(sources.length() - 1)) + "");
        }
        return flag.toString();
    }

    static final String code = "0123456789";

    public static String newCodeString(int length) {
        StringBuffer flag = new StringBuffer();
        Random rand = new Random();
        for (int j = 0; j < length; j++) {
            flag.append(code.charAt(rand.nextInt(sources.length() - 1)) + "");
        }
        return flag.toString();
    }
}
