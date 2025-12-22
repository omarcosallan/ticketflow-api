package dev.marcos.ticketflow_api.controller;

import dev.marcos.ticketflow_api.dto.event.EventCreateDTO;
import dev.marcos.ticketflow_api.dto.event.EventDTO;
import dev.marcos.ticketflow_api.dto.event.EventDashboardDTO;
import dev.marcos.ticketflow_api.dto.event.EventStatusRequestDTO;
import dev.marcos.ticketflow_api.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/organizations/{orgId}/events")
@RequiredArgsConstructor
public class EventManagementController {

    private final EventService eventService;

    @PostMapping
    @PreAuthorize("@orgGuard.hasPermission(authentication, #orgId, 'ADMIN')")
    public ResponseEntity<EventDTO> save(@PathVariable UUID orgId, @Valid @RequestBody EventCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.save(orgId, dto));
    }

    @GetMapping
    @PreAuthorize("@orgGuard.hasPermission(authentication, #orgId, 'STAFF')")
    public ResponseEntity<List<EventDTO>> listAll(@PathVariable UUID orgId) {
        return ResponseEntity.ok(eventService.listByOrganization(orgId));
    }

    @PutMapping("/{eventId}")
    @PreAuthorize("@orgGuard.hasPermission(authentication, #orgId, 'ADMIN')")
    public ResponseEntity<EventDTO> update(@PathVariable UUID orgId, @PathVariable UUID eventId, @Valid @RequestBody EventCreateDTO dto) {
        return ResponseEntity.ok(eventService.update(orgId, eventId, dto));
    }

    @PatchMapping("/{eventId}/status")
    @PreAuthorize("@orgGuard.hasPermission(authentication, #orgId, 'ADMIN')")
    public ResponseEntity<EventDTO> updateStatus(@PathVariable UUID orgId, @PathVariable UUID eventId, @Valid @RequestBody EventStatusRequestDTO dto) {
        return ResponseEntity.ok(eventService.updateStatus(orgId, eventId, dto));
    }

    @GetMapping("/{eventId}/dashboard")
    @PreAuthorize("@orgGuard.hasPermission(authentication, #orgId, 'ADMIN')")
    public ResponseEntity<EventDashboardDTO> dashboard(@PathVariable UUID orgId, @PathVariable UUID eventId) {
        return ResponseEntity.ok(eventService.dashboard(orgId, eventId));
    }
}
