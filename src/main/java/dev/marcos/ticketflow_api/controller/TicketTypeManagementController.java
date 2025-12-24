package dev.marcos.ticketflow_api.controller;

import dev.marcos.ticketflow_api.dto.ticketType.CreateTicketTypeRequest;
import dev.marcos.ticketflow_api.dto.ticketType.TicketTypeAdminResponse;
import dev.marcos.ticketflow_api.service.TicketTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/organizations/{orgId}/events/{eventId}/tickets")
@RequiredArgsConstructor
public class TicketTypeManagementController {

    private final TicketTypeService ticketTypeService;

    @PostMapping
    @PreAuthorize("@orgGuard.hasPermission(authentication, #orgId, 'ADMIN')")
    public ResponseEntity<TicketTypeAdminResponse> create(
            @PathVariable UUID orgId,
            @PathVariable UUID eventId,
            @RequestBody @Valid CreateTicketTypeRequest dto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(ticketTypeService.create(orgId, eventId, dto));
    }
}
