package me.interview.RealTimeMarkToMarket.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Underlier {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String code;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private UnderlierCategory underlierCategory;

}
