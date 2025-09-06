package org.unical.enterprise.shared.dto;

import lombok.Builder;

@Builder
public record TicketCheck(
        String eMail,
        Long eventID
) {
}
