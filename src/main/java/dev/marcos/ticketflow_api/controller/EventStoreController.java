package dev.marcos.ticketflow_api.controller;

import dev.marcos.ticketflow_api.dto.event.EventDetailResponse;
import dev.marcos.ticketflow_api.dto.event.EventSummaryResponse;
import dev.marcos.ticketflow_api.entity.User;
import dev.marcos.ticketflow_api.entity.enums.EventStatus;
import dev.marcos.ticketflow_api.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Tag(name = "Events Store", description = "Endpoints for Events Store.")
public class EventStoreController {

    private final EventService eventService;

    @GetMapping
    @Operation(
            summary = "List Events",
            description = "Retrieve a paginated list of events with optional filtering.")
    public ResponseEntity<List<EventSummaryResponse>> findAll(@RequestParam(required = false, defaultValue = "0") int page,
                                                              @RequestParam(required = false, defaultValue = "10") int size,
                                                              @RequestParam(required = false) String title,
                                                              @RequestParam(required = false, defaultValue = "PUBLISHED") EventStatus status,
                                                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(eventService.findAll(page, size, title, status, startDate, endDate));
    }

    @GetMapping("/{eventId}")
    @Operation(summary = "Get Event by ID", description = "Retrieve a event by its ID")
    public ResponseEntity<EventDetailResponse> findById(@PathVariable UUID eventId) {
        return ResponseEntity.ok(eventService.findById(eventId));
    }

    @GetMapping("/purchased")
    @Operation(
            summary = "List Events purchases",
            description = "Retrieve a list of all purchased events.")
    public ResponseEntity<List<EventSummaryResponse>> findAllPurchased(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(eventService.findAllPurchased(user));
    }
}
