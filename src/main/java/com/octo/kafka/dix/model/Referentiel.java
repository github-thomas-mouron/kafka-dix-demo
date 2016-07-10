package com.octo.kafka.dix.model;

/**
 * Created by thm on 08/07/2016.
 */
public class Referentiel {

    private Long id;
    private String name;


    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
