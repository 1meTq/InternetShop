package kz.springboot.HomeTask7.services;

import kz.springboot.HomeTask7.entities.*;

import java.util.List;

public interface ItemService {

    //ShopItems
    ShopItems addItem(ShopItems item);

    ShopItems saveItem(ShopItems item);

    ShopItems getItem(Long id);

    List<ShopItems> getAllItems();


    //Countries
    Countries addCountry(Countries country);

    Countries saveCountry(Countries country);

    Countries getCountry(Long id);

    List<Countries> getAllCountries();


    //Brands
    Brands addBrand(Brands brand);

    Brands saveBrand(Brands brand);

    Brands getBrand(Long id);

    List<Brands> getAllBrands();


    //Categories
    Categories addCategories(Categories categories);

    Categories saveCategories(Categories categories);

    Categories getCategories(Long id);

    List<Categories> getAllCategories();

    //Pictures
    Pictures addPicture(Pictures picture);

    Pictures savePicture(Pictures picture);

    Pictures getPicture(Long id);

    List<Pictures> getAllPictures();

    void deletePicture(Pictures picture);

    //Comments
    Comments addComment(Comments comment);

    Comments saveComments(Comments comment);

    Comments getComment(Long id);

    List<Comments> getAllComments();

    void deleteComment(Comments comment);


    //Search
    void deleteItem(ShopItems item);

    List<ShopItems> getAllItemsTop();

    List<ShopItems> getItemsByName(String name);

    List<ShopItems> getItemsByNameDesc(String name);

    List<ShopItems> getItemsBeetwenPriceDesc(String s, Double price_from, Double price_to);

    List<ShopItems> getItemsBeetwenPrice(String name, Double price_from, Double price_to);

    List<ShopItems> getItemsByBrandAsc(Brands brands);

    List<ShopItems> getItemsByBrandDesc(Brands brand);

    List<ShopItems> getItemsByNameAndBrandAsc(String name, Brands brand);

    List<ShopItems> getItemsByNameAndBrandDesc(String s, Brands brand);
}
