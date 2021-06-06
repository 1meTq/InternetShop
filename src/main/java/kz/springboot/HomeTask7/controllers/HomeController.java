package kz.springboot.HomeTask7.controllers;

import kz.springboot.HomeTask7.entities.*;
import kz.springboot.HomeTask7.services.ItemService;
import kz.springboot.HomeTask7.services.UserService;
import org.apache.commons.codec.cli.Digest;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import java.io.InputStream;
import java.net.Authenticator;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;


    @Value("${file.avatar.viewPath}")
    public String viewPath;

    @Value("${file.avatar.uploadPath}")
    public String uploadPath;

    @Value("${file.avatar.defaultPicture}")
    public String defaultPicture;


    @GetMapping(value = "/")
    public String home(Model model) {

        List<ShopItems> items = itemService.getAllItemsTop();
        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);
        model.addAttribute("tovary", items);
        model.addAttribute("currentUser", getUserData());


        System.out.println();

        return "home";
    }


    @GetMapping(value = "/allproducts")
    public String allproducts(Model model) {

        List<ShopItems> items = itemService.getAllItems();
        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);
        model.addAttribute("tovary", items);
        model.addAttribute("currentUser", getUserData());
        return "allproducts";
    }


    @PostMapping(value = "/em")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String addItem(@RequestParam(name = "item_name", defaultValue = "Noname") String name,
                          @RequestParam(name = "item_description", defaultValue = "Nodesc") String description,
                          @RequestParam(name = "item_price", defaultValue = "0") double price,
                          @RequestParam(name = "item_stars", defaultValue = "0") int stars,
                          @RequestParam(name = "item_pictureUrl", defaultValue = "No Picture") String picture,
                          @RequestParam(name = "item_in_top_page", defaultValue = "0") String inTopPage,
                          @RequestParam(name = "brandId", defaultValue = "0") Long brandId) {

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

        Brands brand = itemService.getBrand(brandId);

        if (brand != null) {
            itemService.addItem(new ShopItems(null, name, description, price, stars, small_pic_url, large_pic_url, added_date, inTop, brand, null));
        }

        return "redirect:/all_items";
    }

    @GetMapping(value = "/adminpanel")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String adminpanel(Model model) {
        model.addAttribute("currentUser", getUserData());
        return "adminpanel";
    }

    @GetMapping(value = "/all_items")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String all_items(Model model) {
        model.addAttribute("currentUser", getUserData());
        model.addAttribute("items", itemService.getAllItems());
        model.addAttribute("brands", itemService.getAllBrands());
        return "all_items";
    }

    @GetMapping(value = "/all_users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String all_users(Model model) {

        model.addAttribute("currentUser", getUserData());
        model.addAttribute("users", userService.getAllUsers());

        return "all_users";
    }

    @GetMapping(value = "/all_roles")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String all_roles(Model model) {

        model.addAttribute("currentUser", getUserData());
        model.addAttribute("roles", userService.getAllRoles());

        return "all_roles";
    }

    @GetMapping(value = "/all_countries")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String all_countries(Model model) {

        model.addAttribute("currentUser", getUserData());
        model.addAttribute("countries", itemService.getAllCountries());

        return "all_countries";
    }

    @GetMapping(value = "/all_brands")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String all_brands(Model model) {

        model.addAttribute("currentUser", getUserData());
        model.addAttribute("brands", itemService.getAllBrands());
        model.addAttribute("countries", itemService.getAllCountries());

        return "all_brands";
    }

    @GetMapping(value = "/all_categories")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String all_categories(Model model) {

        model.addAttribute("currentUser", getUserData());
        model.addAttribute("categories", itemService.getAllCategories());

        return "all_categories";
    }


    @PostMapping(value = "/addCountry")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addCountry(@RequestParam(name = "name", defaultValue = "Noname") String name,
                             @RequestParam(name = "code", defaultValue = "Nocode") String code) {

        if (!name.equals("Noname") && !code.equals("Nocode")) {
            itemService.addCountry(new Countries(null, name, code));
        }

        return "redirect:/all_countries";
    }

    @PostMapping(value = "/addCategories")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addCategories(@RequestParam(name = "name", defaultValue = "Noname") String name,
                                @RequestParam(name = "logoUrl", defaultValue = "Nologo") String logo) {

        if (!name.equals("Noname")) {
            itemService.addCategories(new Categories(null, name, logo));

        }

        return "redirect:/all_categories";
    }

    @PostMapping(value = "/addbrand")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addBrand(@RequestParam(name = "name", defaultValue = "Noname") String name,
                           @RequestParam(name = "country_id", defaultValue = "0") Long country_id) {

        if (!name.equals("Noname") && country_id != 0) {
            Countries country = itemService.getCountry(country_id);

            if (country != null) {
                itemService.addBrand(new Brands(null, name, country));
            }
        }

        return "redirect:/all_brands";
    }


    @PostMapping(value = "/addComment")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String addComment(@RequestParam(name = "comment", defaultValue = "") String comment,
                             @RequestParam(name = "item_id") Long item_id,
                             @RequestParam(name = "user_id") Long user_id,
                             Model model) {

        if (comment != null && item_id != null && user_id != null && comment != "") {

            Users author = userService.getUsersById(user_id);
            ShopItems item = itemService.getItem(item_id);

            if (author != null && item != null) {

                LocalDateTime myDateObj = LocalDateTime.now();
                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");


                String date = myDateObj.format(myFormatObj);
                System.out.println("After formatting: " + date);


                Comments com = new Comments();

                com.setAuthor(author);
                com.setComment(comment);
                com.setItems(item);
                com.setAddedDate(date);

                itemService.addComment(com);

                return "redirect:/details/" + item_id;

            }
        }


        return "redirect:/";
    }


    @PostMapping(value = "/editComment")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String editComment(@RequestParam(name = "comment", defaultValue = "") String comment,
                              @RequestParam(name = "com_id", defaultValue = "0") Long com_id,
                              @RequestParam(name = "item_id", defaultValue = "0") Long item_id,
                              Model model) {

        if (item_id != 0 && comment != "" && com_id != 0) {

            ShopItems item = itemService.getItem(item_id);
            Comments com = itemService.getComment(com_id);

            if (item != null && com != null) {

                com.setComment(comment);

                itemService.saveComments(com);

                return "redirect:/details/" + item_id + "#commentsDiv";

            }
        }

        return "redirect:/";
    }

    @PostMapping(value = "/deleteComment")
    @PreAuthorize("isAuthenticated()")
    public String deleteComment(@RequestParam(name = "com_id", defaultValue = "0") Long com_id,
                                @RequestParam(name = "item_id", defaultValue = "0") Long item_id) {

        if (item_id != 0 && com_id != 0) {

            ShopItems item = itemService.getItem(item_id);
            Comments com = itemService.getComment(com_id);

            if (item != null && com != null) {

                itemService.deleteComment(com);

                return "redirect:/details/" + item_id + "#commentsDiv";

            }
        }

        return "redirect:/";
    }

    @PostMapping(value = "/assignCategories")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String assignCategories(@RequestParam(name = "item_id", defaultValue = "0") Long item_id,
                                   @RequestParam(name = "cat_id", defaultValue = "0") Long cat_id) {

        ShopItems item = itemService.getItem(item_id);
        Categories cat = itemService.getCategories(cat_id);

        if (item != null && cat != null) {

            List<Categories> categories = item.getCategories();

            if (categories == null) {
                categories = new ArrayList<>();
            }
            categories.add(cat);

            itemService.saveItem(item);

            return "redirect:/edit_items/" + item_id + "#categoriesDiv";
        }

        return "redirect:/";
    }


    @PostMapping(value = "/assignCategoriesDelete")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String assignCategoriesDelete(@RequestParam(name = "item_id", defaultValue = "0") Long item_id,
                                         @RequestParam(name = "cat_id", defaultValue = "0") Long cat_id) {

        ShopItems item = itemService.getItem(item_id);
        Categories cat = itemService.getCategories(cat_id);

        if (item != null && cat != null) {

            List<Categories> categories = item.getCategories();


            categories.remove(cat);

            itemService.saveItem(item);

            return "redirect:/edit_items/" + item_id + "#categoriesDiv";
        }

        return "redirect:/";
    }


    @PostMapping(value = "/assignRole")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String assignRole(@RequestParam(name = "user_id", defaultValue = "0") Long user_id,
                             @RequestParam(name = "role_id", defaultValue = "0") Long role_id) {

        Users myUser = userService.getUsersById(user_id);
        Roles role = userService.getRolesById(role_id);

        if (myUser != null && role != null) {

            List<Roles> roles = myUser.getRoles();

            if (roles == null) {
                roles = new ArrayList<>();
            }
            roles.add(role);

            userService.saveUser(myUser);

            return "redirect:/edit_user/" + user_id + "#rolesDiv";
        }

        return "redirect:/";
    }

    @PostMapping(value = "/uploadAvatar")
    @PreAuthorize("isAuthenticated()")
    public String uploadAvatar(@RequestParam(name = "user_ava") MultipartFile file) {


        if (file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png")) {
            try {

                Users curUser = getUserData();

                String picName = DigestUtils.sha1Hex("avatar_" + curUser.getId() + "_!Picture");

                byte[] bytes = file.getBytes();
                Path path = Paths.get(uploadPath + picName + ".jpg");
                Files.write(path, bytes);


                curUser.setUserAvatar(picName);
                userService.saveUser(curUser);

                return "redirect:/profile?success";

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return "redirect:/profile?error";
    }


    @PostMapping(value = "/addPictureToItem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String addPictureToItem(@RequestParam(name = "item_picture") MultipartFile file,
                                   @RequestParam(name = "item_id") Long item_id) {


        ShopItems item = itemService.getItem(item_id);


        if (item != null) {
            if (file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png")) {
                try {

                    //Date
                    LocalDateTime myDateObj = LocalDateTime.now();
                    System.out.println("Before formatting: " + myDateObj);
                    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                    DateTimeFormatter picTimeObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");


                    String picTime = picTimeObj.format(myDateObj);
                    System.out.println(picTime);


                    String formattedDate = myDateObj.format(myFormatObj);
                    System.out.println("After formatting: " + formattedDate);

                    //---------------------

                    Pictures picture = new Pictures();

                    String picName = DigestUtils.sha1Hex("Item" + picTime + "_!Picture");

                    byte[] bytes = file.getBytes();
                    Path path = Paths.get(uploadPath + picName + ".jpg");
                    Files.write(path, bytes);


                    picture.setItem(item);
                    picture.setUrl(picName);
                    picture.setAddedDate(formattedDate);
                    itemService.savePicture(picture);

                    return "redirect:/edit_items/" + item_id + "#picturesDiv";

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


        return "redirect:/";
    }


    @PostMapping(value = "/assignRoleDelete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String assignRoleDelete(@RequestParam(name = "user_id", defaultValue = "0") Long user_id,
                                   @RequestParam(name = "role_id", defaultValue = "0") Long role_id) {

        Users myUser = userService.getUsersById(user_id);
        Roles role = userService.getRolesById(role_id);

        if (myUser != null && role != null) {

            List<Roles> roles = myUser.getRoles();

            roles.remove(role);

            userService.saveUser(myUser);

            return "redirect:/edit_user/" + user_id + "#rolesDiv";
        }

        return "redirect:/";
    }


    @PostMapping(value = "/deletePictureFromItem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String deletePictureFromItem(@RequestParam(name = "pic_id", defaultValue = "0") Long pic_id,
                                        @RequestParam(name = "item_id", defaultValue = "0") Long item_id) {

        Pictures picture = itemService.getPicture(pic_id);
        ShopItems item = itemService.getItem(item_id);


        if (picture != null && item != null) {

            itemService.deletePicture(picture);

            return "redirect:/edit_items/" + item_id + "#picturesDiv";
        }

        return "redirect:/";
    }


    @GetMapping(value = "/details/{id}")
    public String details(Model model, @PathVariable(name = "id") Long id,
                          @RequestParam(name = "pic_id", defaultValue = "0") Long pic_id) {
        ShopItems item = itemService.getItem(id);
        List<Brands> brands = itemService.getAllBrands();
        List<Pictures> pictures = itemService.getAllPictures();
        List<Comments> comments = itemService.getAllComments();

        List<Comments> itemsComments = new ArrayList<>();

        for (Comments c : comments) {

            if (c.getItems().equals(item)) {
                itemsComments.add(c);
            }

        }


        model.addAttribute("brands", brands);
        model.addAttribute("currentUser", getUserData());
        model.addAttribute("pictures", pictures);

        if (item != null) {
            model.addAttribute("item", item);
        }

        if (itemsComments != null) {
            model.addAttribute("comments", itemsComments);
        }

        Pictures pic = null;

        if (pic_id != 0) {
            pic = itemService.getPicture(pic_id);

        } else {
            for (int i = 0; i < pictures.size(); i++) {
                if (pictures.get(i).getItem().getId().equals(item.getId())) {
                    pic = pictures.get(i);
                    break;
                }
            }
        }

        model.addAttribute("fullPicture", pic);


        return "details";
    }


    @GetMapping(value = "/edit_items/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String edit_items(Model model, @PathVariable(name = "id") Long id) {

        ShopItems item = itemService.getItem(id);
        List<Brands> brands = itemService.getAllBrands();
        List<Categories> categories = itemService.getAllCategories();
        List<Pictures> pictures = itemService.getAllPictures();

        if (item != null) {
            model.addAttribute("item", item);
            model.addAttribute("brands", brands);
            model.addAttribute("categories", categories);
            model.addAttribute("currentUser", getUserData());
            model.addAttribute("pictures", pictures);
        }
        return "edit_items";
    }

    @GetMapping(value = "/example")
    public String example(Model model) {


        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);
        return "example";
    }


    @PostMapping(value = "/updateProfile")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String updateProfile(@RequestParam(name = "user_email") String email,
                                @RequestParam(name = "user_full_name") String full_name, Model model) {


        if (userService.updateProfile(email, full_name) != null) {
            model.addAttribute("currentUser", getUserData());

            return "redirect:/profile?nameChange=" + "yes";

        }


        return "redirect:/profile?nameChange=" + "no";
    }

    @PostMapping(value = "/updatePassword")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String updatePassword(Model model, @RequestParam(name = "id") Long id,
                                 @RequestParam(name = "old_password") String old_password,
                                 @RequestParam(name = "new_password") String new_password,
                                 @RequestParam(name = "re_new_password") String re_new_password) {

        if (new_password.equals(re_new_password)) {

            if (userService.updatePassword(id, old_password, new_password) != null) {
                model.addAttribute("currentUser", getUserData());
                return "redirect:/profile?passChange=yes";
            }

        }

        return "redirect:/profile?passChange=no";
    }

    @PostMapping(value = "/updatePasswordForAnyUser")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String updatePasswordForAnyUser(Model model, @RequestParam(name = "id") Long id,
                                           @RequestParam(name = "old_password") String old_password,
                                           @RequestParam(name = "new_password") String new_password,
                                           @RequestParam(name = "re_new_password") String re_new_password) {

        String passChange = "no";

        if (new_password.equals(re_new_password)) {

            if (userService.updatePassword(id, old_password, new_password) != null) {
                model.addAttribute("currentUser", getUserData());
                return "redirect:/edit_user/" + id + "?passChange=yes";
            }

        }

        model.addAttribute("passChange", passChange);
        return "redirect:/edit_user/" + id + "?passChange=no";
    }

    @PostMapping(value = "/deleteitem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String deleteitem(@RequestParam(name = "id", defaultValue = "0") Long id) {

        ShopItems item = itemService.getItem(id);

        if (item != null) {
            itemService.deleteItem(item);
        }

        return "redirect:/";
    }

    @PostMapping(value = "/addRole")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addRole(@RequestParam(name = "role") String role,
                          @RequestParam(name = "description") String description) {

        if (userService.addRole(role, description) != null) {
            return "redirect:/all_roles?success";
        }


        return "redirect:/all_roles?error";
    }


    @PostMapping(value = "/saveitem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String saveitem(@RequestParam(name = "id", defaultValue = "0") Long id,
                           @RequestParam(name = "item_name", defaultValue = "No item") String name,
                           @RequestParam(name = "item_description", defaultValue = "No item") String description,
                           @RequestParam(name = "item_price", defaultValue = "0") double price,
                           @RequestParam(name = "item_stars", defaultValue = "0") int stars,
                           @RequestParam(name = "item_pictureUrl", defaultValue = "No Picture") String picture,
                           @RequestParam(name = "item_in_top_page", defaultValue = "Nothing") String inTopPage,
                           @RequestParam(name = "brandId", defaultValue = "0") Long brandId) {

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

            if (inTopPage.equals("Nothing")) {
                inTop = item.getInTopPage();
            }

            item.setInTopPage(inTop);

            Brands brand = itemService.getBrand(brandId);

            if (brand != null) {
                item.setBrands(brand);
                itemService.saveItem(item);
            }

        }


        return "redirect:/";
    }


    @GetMapping(value = "viewphoto/{url}", produces = {MediaType.IMAGE_JPEG_VALUE})
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    byte[] viewProfilePhoto(@PathVariable(name = "url") String url) throws Exception {


        String pictureUrl = viewPath + defaultPicture;

        InputStream in = null;

        if (url != null && !url.equals("null")) {

            pictureUrl = viewPath + url + ".jpg";


            try {

                ClassPathResource resource = new ClassPathResource(pictureUrl);
                in = resource.getInputStream();


            } catch (Exception e) {
                ClassPathResource resource = new ClassPathResource(viewPath + defaultPicture);
                in = resource.getInputStream();


                e.printStackTrace();
            }

        }

        return IOUtils.toByteArray(in);
    }


    @GetMapping(value = "viewItemPicture/{url}", produces = {MediaType.IMAGE_JPEG_VALUE})
    public @ResponseBody
    byte[] viewItemPicture(@PathVariable(name = "url") String url) throws Exception {


        String pictureUrl;

        InputStream in = null;

        if (url != null && !url.equals("null")) {

            pictureUrl = viewPath + url + ".jpg";


            try {

                ClassPathResource resource = new ClassPathResource(pictureUrl);
                in = resource.getInputStream();


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return IOUtils.toByteArray(in);
    }

    @GetMapping(value = "/search")
    public String search(Model model,
                         @RequestParam(name = "name", defaultValue = "Noname") String name,
                         @RequestParam(name = "price_from", defaultValue = "0.0") Double price_from,
                         @RequestParam(name = "price_to", defaultValue = "10000000.0") Double price_to,
                         @RequestParam(name = "order", defaultValue = "Ascending") String order,
                         @RequestParam(name = "brand", defaultValue = "0") Long brandId) {

        System.out.println(order);

        List<ShopItems> items = null;

        Brands brand = null;
        if (price_from == 0.0 || price_to == 10000000.0) {
            if (order.equals("Descending")) {

                if (!name.equals("Noname") && brandId != 0) {
                    brand = itemService.getBrand(brandId);
                    items = itemService.getItemsByNameAndBrandDesc("%" + name + "%", brand);
                } else if (!name.equals("Noname")) {
                    items = itemService.getItemsByNameDesc("%" + name + "%");
                } else if (brandId != 0) {
                    brand = itemService.getBrand(brandId);
                    items = itemService.getItemsByBrandDesc(brand);
                }
            } else {

                if (!name.equals("Noname") && brandId != 0) {
                    brand = itemService.getBrand(brandId);
                    items = itemService.getItemsByNameAndBrandAsc("%" + name + "%", brand);
                } else if (!name.equals("Noname")) {
                    items = itemService.getItemsByName("%" + name + "%");
                } else if (brandId != 0) {
                    brand = itemService.getBrand(brandId);
                    items = itemService.getItemsByBrandAsc(brand);
                }
            }
        } else if (price_from != null) {
            if (order.equals("Descending")) {
                items = itemService.getItemsBeetwenPriceDesc("%" + name + "%", price_from, price_to);
            } else {
                items = itemService.getItemsBeetwenPrice("%" + name + "%", price_from, price_to);
            }
        }


        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);


        if (brand != null) {
            model.addAttribute("brand", brandId);
        }

        if (!name.equals("Noname")) {
            model.addAttribute("name", name);
        }

        if (price_from != 0.0 || price_to != 10000000.0) {
            model.addAttribute("price_from", price_from);
            model.addAttribute("price_to", price_to);
        }
        model.addAttribute("tovary", items);
        model.addAttribute("currentUser", getUserData());

        return "search";
    }


    @GetMapping(value = "/403")
    public String accessDenied(Model model) {
        model.addAttribute("currentUser", getUserData());
        return "403";
    }

    @GetMapping(value = "/login")
    @PreAuthorize("isAnonymous()")
    public String login(@RequestParam(name = "error", defaultValue = "no") String error, Model model) {

        System.out.println(LocaleContextHolder.getLocale());

        model.addAttribute("error", error);
        return "login";
    }

    @GetMapping(value = "/register")
    public String register(Model model, @RequestParam(name = "success", defaultValue = "") String success) {
        model.addAttribute("currentUser", getUserData());
        model.addAttribute("success", success);
        return "register";
    }

    @GetMapping(value = "/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(@RequestParam(name = "nameChange", defaultValue = "") String nameChange,
                          @RequestParam(name = "passChange", defaultValue = "") String passChange, Model model) {


        model.addAttribute("nameChange", nameChange);
        model.addAttribute("passChange", passChange);
        model.addAttribute("currentUser", getUserData());
        return "profile";
    }

    @PostMapping(value = "/register")
    public String toRegister(@RequestParam(name = "user_email") String email,
                             @RequestParam(name = "user_password") String password,
                             @RequestParam(name = "user_re_password") String rePassword,
                             @RequestParam(name = "user_full_name") String full_name,
                             Model model) {


        if (password.equals(rePassword)) {

            Users myUser = new Users();
            myUser.setFullName(full_name);
            myUser.setEmail(email);
            myUser.setPassword(password);

            if (userService.createUser(myUser) != null) {

                return "redirect:/register?success=yes";
            }

        }

        return "redirect:/register?success=no";
    }


    @GetMapping(value = "/edit_user/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String editUser(Model model, @PathVariable(name = "id") Long id,
                           @RequestParam(name = "passChange", defaultValue = "") String passChange) {


        model.addAttribute("currentUser", getUserData());
        model.addAttribute("user", userService.getUsersById(id));
        model.addAttribute("roles", userService.getAllRoles());
        model.addAttribute("passChange", passChange);
        System.out.println(passChange);


        return "edit_user";
    }


    @PostMapping(value = "/addUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addUser(@RequestParam(name = "user_email") String email,
                          @RequestParam(name = "user_password") String password,
                          @RequestParam(name = "user_re_password") String rePassword,
                          @RequestParam(name = "user_full_name") String full_name) {

        if (password.equals(rePassword)) {

            Users myUser = new Users();
            myUser.setFullName(full_name);
            myUser.setEmail(email);
            myUser.setPassword(password);

            if (userService.createUser(myUser) != null) {

                return "redirect:/all_users?success";
            }

        }


        return "redirect:/all_users?error";
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
