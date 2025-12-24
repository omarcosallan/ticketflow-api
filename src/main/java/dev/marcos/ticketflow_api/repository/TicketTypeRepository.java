package dev.marcos.ticketflow_api.repository;

import dev.marcos.ticketflow_api.entity.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface TicketTypeRepository extends JpaRepository<TicketType, UUID> {

    @Modifying
    @Query("""
        UPDATE TicketType t
        SET t.soldQuantity = t.soldQuantity + :quantity
        WHERE t.id = :id
        AND (t.soldQuantity + :quantity) <= t.totalQuantity
    """)
    int incrementSoldQuantity(@Param("id") UUID id, @Param("quantity") int quantity);
}
