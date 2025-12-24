package dev.marcos.ticketflow_api.service;

import dev.marcos.ticketflow_api.dto.ticket.TicketResponse;
import dev.marcos.ticketflow_api.entity.User;
import dev.marcos.ticketflow_api.mapper.TicketMapper;
import dev.marcos.ticketflow_api.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper mapper;

    public List<TicketResponse> findAll() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ticketRepository.getTicketsByCustomerId(user.getId()).stream()
                .map(mapper::toDTO)
                .toList();
    }
}
