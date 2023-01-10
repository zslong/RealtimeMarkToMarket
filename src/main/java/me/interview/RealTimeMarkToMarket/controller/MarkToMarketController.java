package me.interview.RealTimeMarkToMarket.controller;

import lombok.extern.slf4j.Slf4j;
import me.interview.RealTimeMarkToMarket.dto.AggregateQuery;
import me.interview.RealTimeMarkToMarket.dto.Dimension;
import me.interview.RealTimeMarkToMarket.exception.UnsupportedAggregateQueryException;
import me.interview.RealTimeMarkToMarket.service.MarkToMarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/m2m")
@Slf4j
public class MarkToMarketController {
    @Autowired
    private MarkToMarketService markToMarketService;

    @GetMapping("indicators")
    public List<Dimension> getIndicators(
            @RequestParam(value = "aggregate", required = false, defaultValue = "all") String aggregate) {
        try {
            log.info("Aggregate param is: {}", aggregate);
            String[] params = aggregate.split(";");
            AggregateQuery query = new AggregateQuery();
            Arrays.stream(params).forEach(param -> query.initAggregateQuery(query, param));
            log.info("Aggregate query is: {}", query);

            return markToMarketService.getAggregateMarkToMarketIndicators(query);
        } catch (UnsupportedAggregateQueryException exception) {
            log.error("Unsupported aggregate query param: {}. ", exception.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported aggregate query.", exception);
        }
    }
}
