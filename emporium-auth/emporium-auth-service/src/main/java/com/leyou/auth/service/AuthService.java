package com.leyou.auth.service;

/**
 * @author Si6x
 */
public interface AuthService {

    /**
     *  登录授权
     * @param username
     * @param password
     * @return
     */
    String accredit(String username, String password);
}
