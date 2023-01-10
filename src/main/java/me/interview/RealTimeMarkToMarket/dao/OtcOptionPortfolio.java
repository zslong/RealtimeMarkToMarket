package me.interview.RealTimeMarkToMarket.dao;

import lombok.Data;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "OTC_OPTION_PORTFOLIO")
@Proxy(lazy = false)
public class OtcOptionPortfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name="portfolio_contract",
    joinColumns = {@JoinColumn(name = "portfolio_id")},
    inverseJoinColumns = {@JoinColumn(name = "contract_num")})
    private List<OtcOptionContract> contractList;

    public OtcOptionPortfolio(){}

    public OtcOptionPortfolio(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
