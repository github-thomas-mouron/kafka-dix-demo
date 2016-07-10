package com.octo.kafka.dix.model;

import java.math.BigDecimal;

/**
 * Created by thm on 08/07/2016.
 */
public class ProduitEnrichi {

    private Long id;
    private String name;
    private BigDecimal price;

    public ProduitEnrichi(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

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

    public void setName(String name) {
        this.name = name;
    }
}
