package com.xm.crypto.reader;

import com.xm.crypto.entity.Crypto;

import java.util.Map;

public interface CryptoDataReader {

    Map<String, Crypto> findAll();

}
