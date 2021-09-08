package com.shoppinger.common.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "countries")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length =45, nullable = false)
    private String name;
    @Column(length =5, nullable = false)
    private String code;
    @OneToMany(mappedBy = "country")
    private Set<State> states;


    public Country() {
    }

    public Country(Integer id) {
        this.id = id;
    }

    public Country(String name) {
        this.name = name;
    }

    public Country(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
