package dev.marcos.ticketflow_api.controller;

import dev.marcos.ticketflow_api.dto.member.AddMemberRequest;
import dev.marcos.ticketflow_api.dto.member.MemberResponse;
import dev.marcos.ticketflow_api.service.MemberService;
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
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    @PreAuthorize("@orgGuard.hasPermission(authentication, #orgId, 'ADMIN')")
    public ResponseEntity<List<MemberResponse>> findAllByOrg(@PathVariable UUID orgId) {
        return ResponseEntity.ok(memberService.findAllByOrg(orgId));
    }

    @PostMapping
    @PreAuthorize("@orgGuard.hasPermission(authentication, #orgId, 'ADMIN')")
    public ResponseEntity<MemberResponse> addMember(@PathVariable UUID orgId, @Valid @RequestBody AddMemberRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.addMember(orgId, dto));
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("@orgGuard.hasPermission(authentication, #orgId, 'OWNER')")
    public ResponseEntity<Void> removeMember(@PathVariable UUID orgId, @PathVariable UUID userId) {
        memberService.removeMember(orgId, userId);
        return ResponseEntity.noContent().build();
    }
}
