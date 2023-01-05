package me.interview.RealTimeMarkToMarket.controller;

import me.interview.RealTimeMarkToMarket.model.Underlier;
import me.interview.RealTimeMarkToMarket.model.UnderlierCategory;
import me.interview.RealTimeMarkToMarket.repository.UnderlierCategoryRepository;
import me.interview.RealTimeMarkToMarket.repository.UnderlierRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UnderlierController {
    private static Logger logger = LoggerFactory.getLogger(UnderlierController.class);
    @Autowired
    private UnderlierCategoryRepository underlierCategoryRepository;

    @Autowired
    private UnderlierRepository underlierRepository;

    @GetMapping("/underliercategories")
    public List<UnderlierCategory> getUnderlierCategories() {
        logger.info("getting all underlier categories");
        List<UnderlierCategory> result = this.underlierCategoryRepository.findAll();
        logger.info("Categories are:");
        result.forEach(r -> logger.info("{}", r.getName()));
        return result;
    }

    @GetMapping("/underliers")
    public List<Underlier> getUnderliers() {
        return underlierRepository.findAll();
    }
}
