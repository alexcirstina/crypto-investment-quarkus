package com.xm.crypto.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "crypto_data", indexes = {
        @Index(columnList = "symbol", name="crypto_data_symbol_index")
})
public class CryptoData{

    @Id
    @SequenceGenerator(name = "crypto_data_seq", sequenceName = "crypto_data_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "crypto_data_seq")
    private Long cryptoDataId;
    private LocalDateTime timestamp;
    private String symbol;
    private BigDecimal price;

}
