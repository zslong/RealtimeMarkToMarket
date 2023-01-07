package me.interview.RealTimeMarkToMarket.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "OTC_OPTION_PORTFOLIO")
public class OtcOptionPortfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "portfolioList")
    private List<OtcOptionContract> contractList;
}
