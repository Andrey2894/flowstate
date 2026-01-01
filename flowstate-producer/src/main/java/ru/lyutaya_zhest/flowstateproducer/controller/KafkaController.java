package ru.lyutaya_zhest.flowstateproducer.controller;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.lyutaya_zhest.flowstateapi.FlowEvent;

@RestController
public class KafkaController {

    private final StreamBridge streamBridge;

    public KafkaController(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @GetMapping("/send")
    public String sendMessage(@RequestParam String message) {
        FlowEvent event = new FlowEvent(
                java.util.UUID.randomUUID().toString(),
                message,
                System.currentTimeMillis()
        );
        // "produceMessage-out-0" — это тот самый binding из пропертис
        streamBridge.send("produceMessage-out-0", event);
        return "Sent to Kafka: " + message;
    }
}