package com.yasobafinibus.nnmtc.demonstration.infra.security.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;


@MappedSuperclass
@EntityListeners(AbstractAuditEntityListener.class)
public class AbstractAuditEntity {

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    Integer id;

    @Basic(optional = false)
    @Column(name = "created_on", nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdOn;

    @Version
    @Column(name = "version", columnDefinition = "integer DEFAULT 0", nullable = false)
    private long version = 0L;


    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public long getVersion() {
        return this.version;
    }

    private void setVersion(long version) {
        this.version = version;
    }
}
