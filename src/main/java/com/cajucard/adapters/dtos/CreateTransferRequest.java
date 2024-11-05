package com.cajucard.adapters.dtos;

import java.math.BigDecimal;

public record CreateTransferRequest(String account, BigDecimal totalAmount, String mcc, String merchant) {

}
