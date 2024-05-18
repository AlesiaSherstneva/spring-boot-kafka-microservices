package com.develop.estore.ui;

import com.develop.estore.model.TransferRestModel;
import com.develop.estore.service.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/transfers")
@RequiredArgsConstructor
public class TransfersController {
    private final TransferService transferService;

    @PostMapping()
    public boolean transfer(@RequestBody TransferRestModel transferRestModel) {
        return transferService.transfer(transferRestModel);
    }
}