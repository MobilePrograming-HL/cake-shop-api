package com.cakeshop.api_main.utils;

public class RedisUtils {
    public static String getRedisKeyForConfirmEmail(String email) {
        return "auth:email:otp:" + email;
    }

    public static String getRedisKeyForResendOtp(String email) {
        return "auth:email:otp:resend:" + email;
    }
}
