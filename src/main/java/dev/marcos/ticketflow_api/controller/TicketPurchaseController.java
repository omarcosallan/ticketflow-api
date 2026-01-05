package dev.marcos.ticketflow_api.controller;

import dev.marcos.ticketflow_api.dto.ticket.PurchaseRequest;
import dev.marcos.ticketflow_api.dto.ticket.TicketGroupResponse;
import dev.marcos.ticketflow_api.entity.User;
import dev.marcos.ticketflow_api.service.TicketProducerService;
import dev.marcos.ticketflow_api.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Tag(name = "Tickets purchase", description = "Endpoints for managing tickets purchases")
public class TicketPurchaseController {

    private final TicketProducerService ticketProducerService;
    private final TicketService ticketService;

    @PostMapping("/purchase")
    @Operation(summary = "Purchase Ticket", description = "Purchase ticket by ticket type id")
    public ResponseEntity<String> purchase(
            @Valid @RequestBody PurchaseRequest dto,
            @AuthenticationPrincipal User user
    ) {

        ticketProducerService.sendPurchaseRequest(user, dto.ticketTypeId(), dto.quantity());

        return ResponseEntity.accepted().body("Seu pedido de compra foi recebido e está sendo processado");
    }

    @GetMapping
    @Operation(
            summary = "List Tickets",
            description = "Retrieve a paginated list of tickets with optional filtering.")
    public ResponseEntity<List<TicketGroupResponse>> findAll(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) UUID eventId
    ) {
        return ResponseEntity.ok(ticketService.findAll(user, page, size, eventId));
    }
}
