package dev.marcos.ticketflow_api.repository;

import dev.marcos.ticketflow_api.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @EntityGraph(attributePaths = { "memberships" })
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
