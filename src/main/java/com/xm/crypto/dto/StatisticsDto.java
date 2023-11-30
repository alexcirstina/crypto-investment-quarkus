package com.xm.crypto.dto;

public record StatisticsDto(String symbol, AmountAndTimeDto oldest, AmountAndTimeDto newest, AmountAndTimeDto min, AmountAndTimeDto max) {
}
