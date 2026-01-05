package dev.marcos.ticketflow_api.controller;

import dev.marcos.ticketflow_api.dto.event.*;
import dev.marcos.ticketflow_api.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Events", description = "Endpoints for managing Events.")
public class EventManagementController {

    private final EventService eventService;

    @PostMapping
    @PreAuthorize("@orgGuard.hasPermission(authentication, #orgId, 'ADMIN')")
    @Operation(summary = "Create Event", description = "Crate a new Event.")
    public ResponseEntity<EventDetailResponse> save(@PathVariable UUID orgId, @Valid @RequestBody CreateEventRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.save(orgId, dto));
    }

    @GetMapping
    @PreAuthorize("@orgGuard.hasPermission(authentication, #orgId, 'STAFF')")
    @Operation(
            summary = "List Events by Organization",
            description = "Retrieve a list of all events by organization.")
    public ResponseEntity<List<EventSummaryResponse>> listAll(@PathVariable UUID orgId) {
        return ResponseEntity.ok(eventService.listByOrganization(orgId));
    }

    @PutMapping("/{eventId}")
    @PreAuthorize("@orgGuard.hasPermission(authentication, #orgId, 'ADMIN')")
    @Operation(summary = "Update Event", description = "Update an existing event by ID.")
    public ResponseEntity<EventDetailResponse> update(@PathVariable UUID orgId, @PathVariable UUID eventId, @Valid @RequestBody UpdateEventRequest dto) {
        return ResponseEntity.ok(eventService.update(orgId, eventId, dto));
    }

    @PatchMapping("/{eventId}/status")
    @PreAuthorize("@orgGuard.hasPermission(authentication, #orgId, 'ADMIN')")
    @Operation(summary = "Update Event status", description = "Update the status of existing events by ID.")
    public ResponseEntity<EventDetailResponse> updateStatus(@PathVariable UUID orgId, @PathVariable UUID eventId, @Valid @RequestBody UpdateEventStatusRequest dto) {
        return ResponseEntity.ok(eventService.updateStatus(orgId, eventId, dto));
    }

    @GetMapping("/{eventId}/dashboard")
    @PreAuthorize("@orgGuard.hasPermission(authentication, #orgId, 'ADMIN')")
    @Operation(summary = "Dashboard Event", description = "Retrieve the event dashboard.")
    public ResponseEntity<EventDashboardResponse> dashboard(@PathVariable UUID orgId, @PathVariable UUID eventId) {
        return ResponseEntity.ok(eventService.dashboard(orgId, eventId));
    }
}
