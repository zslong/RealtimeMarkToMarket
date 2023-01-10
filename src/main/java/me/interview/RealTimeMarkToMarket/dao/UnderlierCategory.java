package me.interview.RealTimeMarkToMarket.dao;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "UNDERLIER_CATEGORY")
@Data
public class UnderlierCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

}
