package me.interview.RealTimeMarkToMarket.dao;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Underlier {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String code;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private UnderlierCategory underlierCategory;

}
