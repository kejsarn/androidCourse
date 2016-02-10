package com.example.davidberg.androidkurs;

/**
 * Created by davidberg on 07/02/16.
 *
 * Contains important info returned from Vasttrafiks OAuth server.
 *
 */
public class VasttrafikAuthenticatorInfo {
    private String accessToken = "No token";
    private Integer expirationTime = 0;

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

    public boolean isSet() {
        return (!accessToken.equals("No token") && expirationTime!=0);
    }

}
