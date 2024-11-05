package com.cajucard.controller;

import com.cajucard.adapters.dtos.CreateTransferRequest;
import com.cajucard.adapters.dtos.CreateTransferResponse;
import com.cajucard.adapters.dtos.TransferResponse;
import com.cajucard.controllers.TransferController;
import com.cajucard.usecases.TransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TransferControllerTest {

    @Mock
    private TransferService transferService;

    @InjectMocks
    private TransferController transferController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenCallCreateTransferWithCorrectPayload_shouldReturnSuccessCreatedTransferResponse() {
        CreateTransferRequest request = new CreateTransferRequest("123", new BigDecimal("100.00"), "", "");
        CreateTransferResponse expectedResponse = new CreateTransferResponse("00");

        when(transferService.createTransfer(request)).thenReturn(expectedResponse);

        ResponseEntity<CreateTransferResponse> response = transferController.createTransfer(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(URI.create("/api/v1/transfer/1"), response.getHeaders().getLocation());
        assertEquals(expectedResponse, response.getBody());
        verify(transferService, times(1)).createTransfer(request);
    }

    @Test
    void whenCallGetAllTransfer_shouldReturnListOfTransfers() {
        CreateTransferRequest request = new CreateTransferRequest("123", new BigDecimal("100.00"), "", "");
        TransferResponse transfer = new TransferResponse(1L, "100.00", "", "", "");
        List<TransferResponse> expectedTransfers = Collections.singletonList(transfer);

        when(transferService.getTransfer()).thenReturn(expectedTransfers);

        ResponseEntity<List<TransferResponse>> response = transferController.getAllTransfer();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedTransfers, response.getBody());
        verify(transferService, times(1)).getTransfer();
    }

    @Test
    void whenCallGetTransferById_shouldReturnTransferResponse() {
        Long transferId = 1L;
        TransferResponse transferResponse = new TransferResponse(transferId, "100.00", "", "", "");

        when(transferService.getTransfer(transferId)).thenReturn(transferResponse);

        ResponseEntity<TransferResponse> response = transferController.getTransferById(transferId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transferResponse, response.getBody());
        verify(transferService, times(1)).getTransfer(transferId);
    }

    @Test
    void whenCallGetCallTransferByIdWithWrongValues_shouldReturnNotFound() {
        Long transferId = 1L;
        when(transferService.getTransfer(transferId)).thenReturn(null);

        ResponseEntity<TransferResponse> response = transferController.getTransferById(transferId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(transferService, times(1)).getTransfer(transferId);
    }
}
