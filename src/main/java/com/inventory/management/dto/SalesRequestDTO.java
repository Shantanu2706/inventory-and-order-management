package com.inventory.management.dto;

import java.time.LocalDate;
import java.util.Date;

public class SalesRequestDTO {
    private LocalDate startDate;
    private LocalDate endDate;

    public SalesRequestDTO() {}

    public SalesRequestDTO(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
