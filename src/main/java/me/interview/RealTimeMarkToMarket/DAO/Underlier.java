package me.interview.RealTimeMarkToMarket.DAO;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Underlier {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String code;
    private long categoryId;
    private String categoryName;

}
