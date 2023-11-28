package com.xm.crypto.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;


public record CryptoData(Timestamp timestamp, String symbol, BigDecimal price) {}
