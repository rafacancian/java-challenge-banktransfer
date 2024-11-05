package com.cajucard.controllers;

import com.cajucard.adapters.dtos.CreateTransferRequest;
import com.cajucard.adapters.dtos.CreateTransferResponse;
import com.cajucard.adapters.dtos.TransferResponse;
import com.cajucard.usecases.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transfer")
public class TransferController {

    @Autowired
    private TransferService transferService;

    @PostMapping()
    public ResponseEntity<CreateTransferResponse> createTransfer(@RequestBody CreateTransferRequest createTransferRequest) {
        CreateTransferResponse createTransferResponse = transferService.createTransfer(createTransferRequest);
        URI location = URI.create("/api/v1/transfer/" + 1L); //mocked id
        return ResponseEntity.created(location).body(createTransferResponse);
    }

    @GetMapping()
    public ResponseEntity<List<TransferResponse>> getAllTransfer() {
        List<TransferResponse> transfersResponse = transferService.getTransfer();
        return ResponseEntity.ok(transfersResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferResponse> getTransferById(@PathVariable Long id) {
        TransferResponse transferResponse = transferService.getTransfer(id);
        if (transferResponse != null) {
            return ResponseEntity.ok(transferResponse);
        }
        return ResponseEntity.notFound().build();
    }
}
