package com.octo.kafka.dix.model;

import java.math.BigDecimal;

public class ProduitBrut {

    private Long id;
    private BigDecimal price;

    public BigDecimal getPrice() {
        return price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
