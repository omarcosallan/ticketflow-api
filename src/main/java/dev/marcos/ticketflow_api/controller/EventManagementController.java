package dev.marcos.ticketflow_api.controller;

import dev.marcos.ticketflow_api.dto.event.*;
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
    public ResponseEntity<EventDetailResponse> save(@PathVariable UUID orgId, @Valid @RequestBody CreateEventRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.save(orgId, dto));
    }

    @GetMapping
    @PreAuthorize("@orgGuard.hasPermission(authentication, #orgId, 'STAFF')")
    public ResponseEntity<List<EventSummaryResponse>> listAll(@PathVariable UUID orgId) {
        return ResponseEntity.ok(eventService.listByOrganization(orgId));
    }

    @PutMapping("/{eventId}")
    @PreAuthorize("@orgGuard.hasPermission(authentication, #orgId, 'ADMIN')")
    public ResponseEntity<EventDetailResponse> update(@PathVariable UUID orgId, @PathVariable UUID eventId, @Valid @RequestBody UpdateEventRequest dto) {
        return ResponseEntity.ok(eventService.update(orgId, eventId, dto));
    }

    @PatchMapping("/{eventId}/status")
    @PreAuthorize("@orgGuard.hasPermission(authentication, #orgId, 'ADMIN')")
    public ResponseEntity<EventDetailResponse> updateStatus(@PathVariable UUID orgId, @PathVariable UUID eventId, @Valid @RequestBody UpdateEventStatusRequest dto) {
        return ResponseEntity.ok(eventService.updateStatus(orgId, eventId, dto));
    }

    @GetMapping("/{eventId}/dashboard")
    @PreAuthorize("@orgGuard.hasPermission(authentication, #orgId, 'ADMIN')")
    public ResponseEntity<EventDashboardResponse> dashboard(@PathVariable UUID orgId, @PathVariable UUID eventId) {
        return ResponseEntity.ok(eventService.dashboard(orgId, eventId));
    }
}
