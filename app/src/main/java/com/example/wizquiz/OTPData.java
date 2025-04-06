package com.example.wizquiz;

public class OTPData {
    private String otpCode;
    private long expirationTime;

    public OTPData(String otpCode, long expirationTime) {
        this.otpCode = otpCode;
        this.expirationTime = expirationTime;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public long getExpirationTime() {
        return expirationTime;
    }
}
