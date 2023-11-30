package com.xm.crypto.exception.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(value = {"status_code", "details"})
public record CryptoError(@JsonProperty(value = "status_code") int statusCode, @JsonProperty(value = "details")String details) {

}
