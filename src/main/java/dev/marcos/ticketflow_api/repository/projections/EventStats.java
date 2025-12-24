package dev.marcos.ticketflow_api.repository.projections;

import java.math.BigDecimal;

public interface EventStats {

    Long getSold();
    Long getAvailable();
    BigDecimal getRevenue();
}
