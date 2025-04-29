package com.example.wizquiz;

public class OTPData {
    private String otpCode;
    private long expirationTime;

//    fusha per vete kodin OTP dhe ruan kohen (millisecond) kur kodi skadon.
    public OTPData(String otpCode, long expirationTime) {
        this.otpCode = otpCode;
        this.expirationTime = expirationTime;
    }
// metodat getter per mi lexu vlerat
    public String getOtpCode() {
        return otpCode;
    }

    public long getExpirationTime() {
        return expirationTime;
    }
}
