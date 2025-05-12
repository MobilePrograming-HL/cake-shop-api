package com.cakeshop.api_main.service;

import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OrderCodeService {
    public static final int TWO_HOUR = 2 * 60 * 60 * 1000;
    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private final SecureRandom secureRandom;
    private List<Integer> numberRand = new ArrayList<>();

    private Map<String, Long> storeOrderSttForCheck = new ConcurrentHashMap<>();

    public OrderCodeService() throws NoSuchAlgorithmException {
        secureRandom = SecureRandom.getInstance("SHA1PRNG");
        for(int i =0; i<10;i++){
            numberRand.add(i);
        }
    }

    public synchronized String generate(int maxLength) {
        final StringBuilder otp = new StringBuilder(maxLength);
        for (int i = 0; i < maxLength; i++) {
            otp.append(secureRandom.nextInt(9));
        }
        return otp.toString();
    }

    public synchronized String orderCode(Long keyNumber) {
        // Xóa các key đã quá 2 giờ
        Iterator<Map.Entry<String, Long>> iterator = storeOrderSttForCheck.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Long> entry = iterator.next();
            if ((System.currentTimeMillis() - entry.getValue()) >= TWO_HOUR) {
                iterator.remove();
            }
        }

        secureRandom.setSeed(keyNumber);

        String code;
        do {
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < 4; i++) {
                int index = secureRandom.nextInt(LETTERS.length());
                builder.append(LETTERS.charAt(index));
            }

            for (int i = 0; i < 6; i++) {
                int index = secureRandom.nextInt(DIGITS.length());
                builder.append(DIGITS.charAt(index));
            }

            code = builder.toString();
        } while (storeOrderSttForCheck.containsKey(code));

        storeOrderSttForCheck.put(code, System.currentTimeMillis());
        return code;
    }
}
