package com.cakeshop.api_main.service;

import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderCodeService {
    public static final int TWO_HOUR = 2 * 60 * 60 * 1000;
    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private final AtomicLong counter = new AtomicLong(System.currentTimeMillis());
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
        long next = counter.getAndIncrement();

        String base36 = Long.toString(next, 36).toUpperCase();

        String padded = String.format("%10s", base36).replace(' ', '0');

        StringBuilder letters = new StringBuilder();
        StringBuilder digits = new StringBuilder();

        for (char c : padded.toCharArray()) {
            if (Character.isLetter(c) && letters.length() < 4) {
                letters.append(c);
            } else if (Character.isDigit(c)) {
                digits.append(c);
            }
        }

        while (letters.length() < 4) {
            letters.append(LETTERS.charAt(secureRandom.nextInt(LETTERS.length())));
        }
        while (digits.length() < 6) {
            digits.append(DIGITS.charAt(secureRandom.nextInt(DIGITS.length())));
        }

        return letters.substring(0, 4) + digits.substring(0, 6);
    }
}
