package me.interview.RealTimeMarkToMarket.dto;

import lombok.Data;
import me.interview.RealTimeMarkToMarket.exception.UnsupportedAggregateQueryException;

@Data
public class AggregateQuery {
    public enum AggregateKey {
        PORTFOLIO, CATEGORY, UNDERLIER, PRODUCT
    }
    private boolean qPortfolio;
    private boolean qCategory;
    private boolean qUnderlier;
    private boolean qProduct;

    /**
     *
     * @return the last layer of the aggregate keys
     */
    public AggregateKey getFinalAggregateKey() {
        if (qProduct) return AggregateKey.PRODUCT;
        if (qUnderlier) return AggregateKey.UNDERLIER;
        if (qCategory) return AggregateKey.CATEGORY;
        return AggregateKey.PORTFOLIO;
    }

    public AggregateQuery initAggregateQuery(AggregateQuery query, String param) {
        param = param.toLowerCase();
        switch (param) {
            case "all":
                query.setQPortfolio(true);
                query.setQCategory(true);
                query.setQUnderlier(true);
                query.setQProduct(true);
                return query;
            case "pf":
                query.setQPortfolio(true);
                return query;
            case "cg":
                query.setQCategory(true);
                return query;
            case "ul":
                query.setQUnderlier(true);
                return query;
            case "pd":
                query.setQProduct(true);
                return query;
            default:
                throw new UnsupportedAggregateQueryException("Invalid aggregate param: " + param);

        }
    }
}
