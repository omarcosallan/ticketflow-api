package dev.marcos.ticketflow_api.controller;

import dev.marcos.ticketflow_api.dto.organization.CreateOrganizationRequest;
import dev.marcos.ticketflow_api.dto.organization.OrganizationDetailResponse;
import dev.marcos.ticketflow_api.dto.organization.OrganizationSummaryResponse;
import dev.marcos.ticketflow_api.dto.organization.UpdateOrganizationRequest;
import dev.marcos.ticketflow_api.entity.User;
import dev.marcos.ticketflow_api.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @PostMapping
    public ResponseEntity<OrganizationDetailResponse> create(@Valid @RequestBody CreateOrganizationRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(organizationService.save(dto));
    }

    @GetMapping
    public ResponseEntity<List<OrganizationSummaryResponse>> listMine(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(organizationService.listMyOrganizations(user));
    }

    @GetMapping("/{orgId}")
    @PreAuthorize("@orgGuard.hasPermission(authentication, #orgId, 'STAFF')")
    public ResponseEntity<OrganizationDetailResponse> findById(@PathVariable UUID orgId) {
        return ResponseEntity.ok(organizationService.findById(orgId));
    }

    @PutMapping("/{orgId}")
    @PreAuthorize("@orgGuard.hasPermission(authentication, #orgId, 'OWNER')")
    public ResponseEntity<OrganizationDetailResponse> update(@Valid @RequestBody UpdateOrganizationRequest dto, @PathVariable UUID orgId) {
        return ResponseEntity.ok(organizationService.update(orgId, dto));
    }

    @DeleteMapping("/{orgId}")
    @PreAuthorize("@orgGuard.hasPermission(authentication, #orgId, 'OWNER')")
    public ResponseEntity<OrganizationDetailResponse> delete(@PathVariable UUID orgId) {
        organizationService.delete(orgId);
        return ResponseEntity.noContent().build();
    }
}
