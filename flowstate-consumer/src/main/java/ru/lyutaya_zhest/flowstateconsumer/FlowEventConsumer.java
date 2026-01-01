package ru.lyutaya_zhest.flowstateconsumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.lyutaya_zhest.flowstateapi.FlowEvent;

import java.util.function.Consumer;

@Configuration
public class FlowEventConsumer {
    @Bean
    public Consumer<FlowEvent> consumeMessage() { // Указываем наш класс вместо String
        return event -> {
            System.out.println("==== НОВОЕ СОБЫТИЕ ====");
            System.out.println("ID: " + event.id());
            System.out.println("Сообщение: " + event.payload());
            System.out.println("Время: " + new java.util.Date(event.createdAt()));
            System.out.println("========================");
        };
    }
}
