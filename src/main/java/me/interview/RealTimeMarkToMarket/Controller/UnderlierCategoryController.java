package me.interview.RealTimeMarkToMarket.Controller;

import me.interview.RealTimeMarkToMarket.DAO.UnderlierCategory;
import me.interview.RealTimeMarkToMarket.Repository.UnderlierCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UnderlierCategoryController {
    @Autowired
    private UnderlierCategoryRepository underlierCategoryRepository;

    @GetMapping("/underliercategories")
    public List<UnderlierCategory> getUnderlierCategories() {
        return this.underlierCategoryRepository.findAll();
    }
}
