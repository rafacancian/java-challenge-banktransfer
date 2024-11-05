package com.cajucard.adapters.dtos;

public record TransferResponse(Long id, String account, String totalAmount, String mcc, String merchantAccount) {
}
