package kz.springboot.HomeTask7.controllers;

import kz.springboot.HomeTask7.DB.DBManager;
import kz.springboot.HomeTask7.DB.Item;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
public class HomeController {

    @GetMapping(value = "/")
    public String home(Model model) {
        ArrayList<Item> items = DBManager.getItems();
        model.addAttribute("items", items);
        return "home";
    }


    @GetMapping(value = "/add")
    public String add() {

        return "add";
    }

    @PostMapping(value = "/addItem")
    public String addItem(@RequestParam(name = "item_name", defaultValue = "No item") String name,
                          @RequestParam(name = "item_description", defaultValue = "No item") String description,
                          @RequestParam(name = "item_price", defaultValue = "0") int price,
                          @RequestParam(name = "item_amount", defaultValue = "0") int amount,
                          @RequestParam(name = "item_stars", defaultValue = "0") int stars,
                          @RequestParam(name = "item_pictureUrl", defaultValue = "0") String pictureUrl) {

        DBManager.addItem(new Item(null, name, description, price, amount, stars, pictureUrl));

        return "redirect:/";
    }


}
