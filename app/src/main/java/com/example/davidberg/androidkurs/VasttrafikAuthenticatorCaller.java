package com.example.davidberg.androidkurs;

/**
 * Created by davidberg on 07/02/16.
 *
 * Implement this interface in order to transfer Authorisation info from the
 * AsyncTask VasttrafikAuthenticator.
 */
public interface VasttrafikAuthenticatorCaller {
    public void onAuthReceived(VasttrafikAuthenticatorInfo vAuth);
    //public VasttrafikAuthenticatorInfo getAuth();
    //public void setAuth(VasttrafikAuthenticatorInfo auth);
}
