package ru.lyutaya_zhest.flowstateproducer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.lyutaya_zhest.flowstateapi.TransferRequestDto;
import ru.lyutaya_zhest.flowstateapi.TransferResponseDto;
import ru.lyutaya_zhest.flowstateproducer.service.TransferService;

@RestController
@RequestMapping("/v1/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<TransferResponseDto> createTransfer(@RequestBody TransferRequestDto requestDto) {
        return transferService.transfer(requestDto)
                .map(txId -> new TransferResponseDto(txId, "PENDING"));
    }
}