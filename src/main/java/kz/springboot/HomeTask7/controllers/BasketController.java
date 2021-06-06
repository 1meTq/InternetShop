package kz.springboot.HomeTask7.controllers;

import kz.springboot.HomeTask7.entities.Brands;
import kz.springboot.HomeTask7.entities.Categories;
import kz.springboot.HomeTask7.entities.ShopItems;
import kz.springboot.HomeTask7.entities.Users;
import kz.springboot.HomeTask7.services.ItemService;
import kz.springboot.HomeTask7.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class BasketController {


    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/addBasket", method = RequestMethod.GET)
    public ModelAndView addItemToBasket(@RequestParam(name = "item_id") Long id,
                                        HttpServletRequest request,
                                        HttpServletResponse response) {

        ShopItems item = itemService.getItem(id);
        if (item != null) {
            Cookie[] cookies = request.getCookies();
            Cookie cookieItemId = null;
            for (Cookie c : cookies) {
                if (c.getName().equals("items")) {
                    cookieItemId = c;
                    break;
                }
            }

            if (cookieItemId == null) {
                cookieItemId = new Cookie("items", "" + id);
            } else {
                String items = cookieItemId.getValue();
                items += "i" + id;
                cookieItemId.setValue(items);
            }
            cookieItemId.setMaxAge(3600 * 24 * 30);

            response.addCookie(cookieItemId);
        }

        if (request.getHeader("Referer").equals("http://localhost:8000/myBasket"))
            return new ModelAndView("redirect:/myBasket");


        return new ModelAndView("redirect:/details/" + id);
    }


    @RequestMapping(value = "/deleteItemFromBasket", method = RequestMethod.GET)
    public ModelAndView deleteItemFromBasket(@RequestParam(name = "item_id") Long id,
                                             HttpServletRequest request,
                                             HttpServletResponse response) {

        ShopItems item = itemService.getItem(id);
        if (item != null) {
            Cookie[] cookies = request.getCookies();
            Cookie cookieItemId = null;
            for (Cookie c : cookies) {
                if (c.getName().equals("items")) {
                    cookieItemId = c;
                    break;
                }
            }


            String[] itemsId = cookieItemId.getValue().split("i");
            for (int i = itemsId.length - 1; i >= 0; i--) {
                if (itemsId[i].equals(id + "")) {
                    itemsId[i] = "a";
                    break;
                }
            }
            String newItemsId = "";
            for (String s : itemsId) {
                if (!s.equals("a")) {
                    newItemsId += s + "i";
                }
            }

            newItemsId = newItemsId.substring(0, newItemsId.length() - 1);

            cookieItemId.setValue(newItemsId);
            cookieItemId.setMaxAge(3600 * 24 * 30);

            response.addCookie(cookieItemId);
        }

        return new ModelAndView("redirect:/myBasket");
    }

    @RequestMapping(value = "/clearBasket", method = RequestMethod.GET)
    public ModelAndView clearBasket(HttpServletRequest request,
                                    HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        Cookie cookieItemId = null;
        for (Cookie c : cookies) {
            if (c.getName().equals("items")) {
                cookieItemId = c;
                break;
            }
        }

        cookieItemId.setMaxAge(0);

        response.addCookie(cookieItemId);

        return new ModelAndView("redirect:/myBasket");
    }

    @RequestMapping(value = "/myBasket", method = RequestMethod.GET)
    public ModelAndView myBasket(HttpServletRequest request,
                                 Model model) {
        Cookie[] cookies = request.getCookies();
        List<ShopItems> itemsList = new ArrayList<>();
        Map<Long, Integer> itemsCount = new HashMap<>();
        List<Brands> brands = itemService.getAllBrands();
        List<Categories> categories = itemService.getAllCategories();

        int basketItemsCount = 0;
        double itemsTotalPrice = 0;
        Cookie itemsCookie = null;

        for (Cookie c : cookies) {
            if (c.getName().equals("items")) {
                itemsCookie = c;
                break;
            }
        }

        if (itemsCookie != null) {

            String[] itemsId = itemsCookie.getValue().split("i");

            // Count of Items in Basket
            basketItemsCount = itemsId.length;

            for (String id : itemsId) {
                itemsTotalPrice += itemService.getItem(Long.parseLong(id)).getPrice();
                if (itemsList.contains(itemService.getItem(Long.parseLong(id)))) {
                    itemsCount.put(Long.parseLong(id), itemsCount.get(Long.parseLong(id)) + 1);
                } else {
                    itemsList.add(itemService.getItem(Long.parseLong(id)));
                    itemsCount.put(Long.parseLong(id), 1);
                }
            }

        }


        model.addAttribute("items", itemsList);
        model.addAttribute("itemsCount", itemsCount);
        model.addAttribute("basketCount", basketItemsCount);
        model.addAttribute("brands", brands);
        model.addAttribute("categories", categories);
        model.addAttribute("itemsTotalPrice", itemsTotalPrice);
        model.addAttribute("currentUser", getUserData());

        return new ModelAndView("basket");
    }

    private Users getUserData() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {

            User secUser = (User) authentication.getPrincipal();
            Users myUser = userService.getUsersByEmail(secUser.getUsername());
            return myUser;
        }

        return null;
    }

}
