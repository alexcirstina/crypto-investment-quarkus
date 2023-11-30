package com.xm.crypto.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AmountAndTimeDto(LocalDateTime timestamp, BigDecimal value) {
}
