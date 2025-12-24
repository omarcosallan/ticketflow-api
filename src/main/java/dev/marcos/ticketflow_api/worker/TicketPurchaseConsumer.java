package dev.marcos.ticketflow_api.worker;

import dev.marcos.ticketflow_api.config.RabbitMQConfig;
import dev.marcos.ticketflow_api.dto.ticket.TicketPurchaseMessage;
import dev.marcos.ticketflow_api.entity.Event;
import dev.marcos.ticketflow_api.entity.Ticket;
import dev.marcos.ticketflow_api.entity.TicketType;
import dev.marcos.ticketflow_api.entity.User;
import dev.marcos.ticketflow_api.exception.NotFoundException;
import dev.marcos.ticketflow_api.repository.TicketRepository;
import dev.marcos.ticketflow_api.repository.TicketTypeRepository;
import dev.marcos.ticketflow_api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class TicketPurchaseConsumer {

    private final TicketRepository ticketRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final UserService userService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_TICKET_PURCHASE)
    @Transactional
    public void processTicketPurchase(TicketPurchaseMessage message) {

        UUID userId = message.userId();
        UUID ticketTypeId = message.ticketTypeId();
        int quantityTickets = message.quantity();

        log.info("Processando compra: User {} - TicketType {}", userId, ticketTypeId);

        int updatedRows = ticketTypeRepository.incrementSoldQuantity(ticketTypeId, quantityTickets);

        if (updatedRows == 0) {
            log.error("Ticket esgotado para o tipo: {}", ticketTypeId);
            return;
        }

        try {
            User customer = userService.findById(userId);
            TicketType type = ticketTypeRepository.findById(ticketTypeId)
                    .orElseThrow(() -> new NotFoundException("TicketType não encontrado"));
            Event event = type.getEvent();

            List<Ticket> ticketsToSave = new ArrayList<>();

            IntStream.range(0, quantityTickets).forEach(i -> {
                Ticket ticket = new Ticket();
                ticket.setCode(UUID.randomUUID().toString());
                ticket.setCustomer(customer);
                ticket.setTicketType(type);
                ticket.setEvent(event);

                ticketsToSave.add(ticket);
            });

            ticketRepository.saveAll(ticketsToSave);

            log.info("Gerados {} ingressos para User {}", quantityTickets, userId);
        } catch (Exception e) {
            log.error("Erro ao gerar ingresso, fazendo rollback manual...", e);
            throw e;
        }
    }
}
