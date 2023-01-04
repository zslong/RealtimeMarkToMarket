package me.interview.RealTimeMarkToMarket.DAO;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "UNDERLIER_CATEGORY")
@Data
public class UnderlierCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private long id;

    @Column
    private String name;

}
