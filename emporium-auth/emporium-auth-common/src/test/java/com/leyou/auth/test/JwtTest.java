package com.leyou.auth.test;

import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utlis.JwtUtils;
import com.leyou.common.utlis.RsaUtils;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtTest {

    private static final String pubKeyPath = "C:\\tmp\\rsa\\rsa.pub";

    private static final String priKeyPath = "C:\\tmp\\rsa\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "234");
    }

    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(20L, "jack"), privateKey, 5);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIooiamFjayIsImV4cCI6MTY1MDc4MZAzMH0.MK523Zx-Cmo7FFs1vld7I0Umn0UN1hRuCVQAwIaxDJSdi_sQ_L_c6vpAitdo2LXSmDzNiJ2pHt6hH0zYNO3Qcident5S9Z4Ln_3SeLwUP_ASFtyBels1q6w3BuBtv6nWWNxrWZ4tP860vMK796WPewsjTNDDKUyqFtn-PsM0shU";

        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}