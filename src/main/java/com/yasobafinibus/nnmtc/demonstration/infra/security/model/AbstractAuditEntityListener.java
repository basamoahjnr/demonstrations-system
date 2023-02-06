package com.yasobafinibus.nnmtc.demonstration.infra.security.model;

import jakarta.persistence.PrePersist;

import java.time.LocalDateTime;
import static java.time.LocalDateTime.now;
import java.util.Objects;
import static java.util.Objects.isNull;

public class AbstractAuditEntityListener {


    @PrePersist
    public void beforeSave(Object o) {
        if (o instanceof LoginToken && isNull(((LoginToken) o).getExpiredDate())) {
            ((LoginToken) o).setExpiredDate(now().plusDays(14L));
        }
    }
}
