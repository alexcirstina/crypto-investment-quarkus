package com.xm.crypto.entity;

import lombok.Builder;

import java.util.List;

@Builder
public record Crypto (String symbol, List<CryptoData> cryptoData){}
