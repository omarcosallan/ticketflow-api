package dev.marcos.ticketflow_api.controller;

import dev.marcos.ticketflow_api.dto.member.AddMemberRequest;
import dev.marcos.ticketflow_api.dto.member.MemberResponse;
import dev.marcos.ticketflow_api.service.MemberService;
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
@RequestMapping("/api/organizations/{orgId}/members")
@RequiredArgsConstructor
@Tag(name = "Members", description = "Endpoints for managing members of an organization.")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    @PreAuthorize("@orgGuard.hasPermission(authentication, #orgId, 'ADMIN')")
    @Operation(
            summary = "List Members by Organization",
            description = "Retrieve the list of all members of an organization.")
    public ResponseEntity<List<MemberResponse>> findAllByOrg(@PathVariable UUID orgId) {
        return ResponseEntity.ok(memberService.findAllByOrg(orgId));
    }

    @PostMapping
    @PreAuthorize("@orgGuard.hasPermission(authentication, #orgId, 'ADMIN')")
    @Operation(summary = "Add Member", description = "Add a new Member to the organization.")
    public ResponseEntity<MemberResponse> addMember(@PathVariable UUID orgId, @Valid @RequestBody AddMemberRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.addMember(orgId, dto));
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("@orgGuard.hasPermission(authentication, #orgId, 'OWNER')")
    @Operation(summary = "Remove Member", description = "Removing a member from an organization.")
    public ResponseEntity<Void> removeMember(@PathVariable UUID orgId, @PathVariable UUID userId) {
        memberService.removeMember(orgId, userId);
        return ResponseEntity.noContent().build();
    }
}
