package me.interview.RealTimeMarkToMarket.Controller;

import me.interview.RealTimeMarkToMarket.DAO.UnderlierCategory;
import me.interview.RealTimeMarkToMarket.Repository.UnderlierCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UnderlierCategoryController {
    private static Logger logger = LoggerFactory.getLogger(UnderlierCategoryController.class);
    @Autowired
    private UnderlierCategoryRepository underlierCategoryRepository;

    @GetMapping("/underliercategories")
    public List<UnderlierCategory> getUnderlierCategories() {
        logger.info("getting all underlier categories");
        List<UnderlierCategory> result = this.underlierCategoryRepository.findAll();
        logger.info("Categories are:");
        result.forEach(r -> logger.info("{}", r.getName()));
        return result;
    }
}
