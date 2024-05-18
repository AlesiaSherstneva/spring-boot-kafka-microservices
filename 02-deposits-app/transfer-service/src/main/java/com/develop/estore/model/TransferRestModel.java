package com.develop.estore.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class TransferRestModel {
    private String senderId;
    private String recipientId;
    private BigDecimal amount;
}