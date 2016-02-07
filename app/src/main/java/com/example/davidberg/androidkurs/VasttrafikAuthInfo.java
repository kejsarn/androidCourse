package com.example.davidberg.androidkurs;

/**
 * Created by davidberg on 07/02/16.
 */
public class VasttrafikAuthInfo{
    private String accessToken;
    private Integer expirationTime;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(int expirationTime) {
        this.expirationTime = expirationTime;
    }

}
