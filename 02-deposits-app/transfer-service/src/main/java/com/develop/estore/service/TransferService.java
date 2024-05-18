package com.develop.estore.service;

import com.develop.estore.model.TransferRestModel;

public interface TransferService {
    boolean transfer(TransferRestModel productPaymentRestModel);
}