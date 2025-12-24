package dev.marcos.ticketflow_api.service;

import dev.marcos.ticketflow_api.config.RabbitMQConfig;
import dev.marcos.ticketflow_api.dto.ticket.TicketPurchaseMessage;
import dev.marcos.ticketflow_api.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketProducerService {

    private final RabbitTemplate rabbitTemplate;

    public void sendPurchaseRequest(User user, UUID ticketTypeId, Integer quantity) {

        TicketPurchaseMessage message = new TicketPurchaseMessage(
                user.getId(),
                ticketTypeId,
                quantity
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_TICKET,
                RabbitMQConfig.ROUTING_KEY_PURCHASE,
                message
        );
    }
}
