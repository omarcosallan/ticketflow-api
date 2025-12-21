package dev.marcos.ticketflow_api.controller;

import dev.marcos.ticketflow_api.dto.organization.OrganizationCreateDTO;
import dev.marcos.ticketflow_api.dto.organization.OrganizationDTO;
import dev.marcos.ticketflow_api.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @PostMapping
    public ResponseEntity<OrganizationDTO> create(@Valid @RequestBody OrganizationCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(organizationService.save(dto));
    }

    @GetMapping
    public ResponseEntity<List<OrganizationDTO>> listMine() {
        return ResponseEntity.ok(organizationService.listMyOrganizations());
    }

    @GetMapping("/{orgId}")
    @PreAuthorize("@orgGuard.hasPermission(authentication, #orgId, 'STAFF')")
    public ResponseEntity<OrganizationDTO> findById(@PathVariable UUID orgId) {
        return ResponseEntity.ok(organizationService.findById(orgId));
    }
}
