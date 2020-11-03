package kz.springboot.HomeTask7.controllers;

import kz.springboot.HomeTask7.entities.ShopItems;
import kz.springboot.HomeTask7.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ItemService itemService;

    @GetMapping(value = "/")
    public String home(Model model) {

        List<ShopItems> items = itemService.getAllItemsTop();

        model.addAttribute("tovary", items);
        return "home";
    }


    @GetMapping(value = "/allproducts")
    public String allproducts(Model model) {

        List<ShopItems> items = itemService.getAllItems();

        model.addAttribute("tovary", items);
        return "allproducts";
    }

    @GetMapping(value = "/add")
    public String add() {

        return "add";
    }

    @PostMapping(value = "/addItem")
    public String addItem(@RequestParam(name = "item_name", defaultValue = "No item") String name,
                          @RequestParam(name = "item_description", defaultValue = "No item") String description,
                          @RequestParam(name = "item_price", defaultValue = "0") double price,
                          @RequestParam(name = "item_stars", defaultValue = "0") int stars,
                          @RequestParam(name = "item_pictureUrl", defaultValue = "No Picture") String picture,
                          @RequestParam(name = "item_in_top_page", defaultValue = "0") String inTopPage) {

        //Pictures
        String small_pic_url = picture;
        String large_pic_url = picture;

        //date
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDateTime now = LocalDateTime.now();
        String added_date = dtf.format(now);

        Boolean inTop = false;

        if (inTopPage.equals("yes")) {
            inTop = true;
        }


        //DBManager.addItem(new Item(null, name, description, price, amount, stars, pictureUrl));

        itemService.addItem(new ShopItems(null, name, description, price, stars, small_pic_url, large_pic_url, added_date, inTop));

        return "redirect:/";
    }

    @GetMapping(value = "/details/{id}")
    public String details(Model model, @PathVariable(name = "id") Long id) {
        ShopItems item = itemService.getItem(id);

        if (item != null) {
            model.addAttribute("item", item);
        }
        return "details";
    }

    @GetMapping(value = "/edit/{id}")
    public String edit(Model model, @PathVariable(name = "id") Long id) {
        ShopItems item = itemService.getItem(id);

        if (item != null) {
            model.addAttribute("item", item);
        }
        return "edit";
    }

    @PostMapping(value = "/deleteitem")
    public String deleteitem(@RequestParam(name = "id", defaultValue = "0") Long id) {

        ShopItems item = itemService.getItem(id);

        if (item != null) {
            itemService.deleteItem(item);
        }

        return "redirect:/";
    }


    @PostMapping(value = "/saveitem")
    public String saveitem(@RequestParam(name = "id", defaultValue = "0") Long id,
                           @RequestParam(name = "item_name", defaultValue = "No item") String name,
                           @RequestParam(name = "item_description", defaultValue = "No item") String description,
                           @RequestParam(name = "item_price", defaultValue = "0") double price,
                           @RequestParam(name = "item_stars", defaultValue = "0") int stars,
                           @RequestParam(name = "item_pictureUrl", defaultValue = "No Picture") String picture,
                           @RequestParam(name = "item_in_top_page", defaultValue = "0") String inTopPage) {

        //date
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDateTime now = LocalDateTime.now();
        String added_date = dtf.format(now);


        Boolean inTop = false;

        if (inTopPage.equals("yes")) {
            inTop = true;
        }

        ShopItems item = itemService.getItem(id);

        if (item != null) {
            item.setName(name);
            item.setDescription(description);
            item.setPrice(price);
            item.setStars(stars);
            item.setLargePicUrl(picture);
            item.setSmallPicUrl(picture);
            item.setAddedDate(added_date);
            item.setInTopPage(inTop);
            itemService.saveItem(item);
        }


        return "redirect:/";
    }

    @GetMapping(value = "/search")
    public String search(Model model,
                         @RequestParam(name = "name") String name,
                         @RequestParam(name = "price_from", defaultValue = "0.0") Double price_from,
                         @RequestParam(name = "price_to",defaultValue = "10000000.0") Double price_to,
                         @RequestParam(name = "order", defaultValue = "Ascending") String order) {

        System.out.println(order);

        List<ShopItems> items = null;
        if (price_from == null || price_to == null) {
            if (order.equals("Descending")) {
                items = itemService.getItemsByNameDesc("%" + name + "%");
            } else {
                items = itemService.getItemsByName("%" + name + "%");
            }

            /*if(order.equals("Ascending")){
                items = itemService.getItemsByName("%" + name + "%");
            }else if(order.equals("Descending")){
                items = itemService.getItemsByNameDesc("%" + name + "%");
            }else{
                items = itemService.getItemsByName("%" + name + "%");
            }*/

        } else if (price_from != null) {
            if (order.equals("Descending")) {
                items = itemService.getItemsBeetwenPriceDesc("%" + name + "%", price_from, price_to);
            } else {
                items = itemService.getItemsBeetwenPrice("%" + name + "%", price_from, price_to);
            }

            /*if(order.equals("Ascending")){
                items = itemService.getItemsBeetwenPrice("%" + name + "%", price_from, price_to);
            }else if(order.equals("Descending")){
                items = itemService.getItemsBeetwenPriceDesc("%" + name + "%", price_from, price_to);
            }else{
                items = itemService.getItemsBeetwenPrice("%" + name + "%", price_from, price_to);
            }*/
        }


        model.addAttribute("name", name);
        if(price_from != 0.0 || price_to != 10000000.0) {
            model.addAttribute("price_from", price_from);
            model.addAttribute("price_to", price_to);
        }
        model.addAttribute("tovary", items);

        return "search";
    }


}
