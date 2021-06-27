package com.ebizprise.winw.project.test.util;

import org.junit.Test;

import com.ebizprise.project.utility.code.Base64Util;
import com.ebizprise.project.utility.code.CryptoUtil;

public class CryptoTest {

    @Test
    public void testBase64Process() {
        String oriWord = "123qweaS";
        CryptoUtil cryptoUtil = new CryptoUtil(new Base64Util());
        String encrypt = cryptoUtil.encrypt(oriWord);
        System.out.println(encrypt);
        System.out.println(cryptoUtil.decode(encrypt));
    }

}