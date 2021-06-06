package kz.springboot.HomeTask7.services.impl;

import kz.springboot.HomeTask7.entities.*;
import kz.springboot.HomeTask7.repositories.*;
import kz.springboot.HomeTask7.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ShopItemRepository shopItemRepository;

    @Autowired
    private CountriesRepository countriesRepository;

    @Autowired
    private BrandsRepository brandsRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private PicturesRepository picturesRepository;

    @Autowired
    private CommentsRepository commentsRepository;

    @Override
    public ShopItems addItem(ShopItems item) {
        return shopItemRepository.save(item);
    }

    @Override
    public ShopItems saveItem(ShopItems item) {
        return shopItemRepository.save(item);
    }

    @Override
    public ShopItems getItem(Long id) {
        return shopItemRepository.getOne(id);
    }


    public List<ShopItems> getAllItemsTop() {
        return shopItemRepository.findAllByInTopPageTrue();
    }

    public List<ShopItems> getAllItems() {
        return shopItemRepository.findAll();
    }

    public List<ShopItems> getItemsByName(String name) {

        return shopItemRepository.findAllByNameLikeOrderByPriceAsc(name);
    }

    public List<ShopItems> getItemsByNameDesc(String name) {
        return shopItemRepository.findAllByNameLikeOrderByPriceDesc(name);
    }

    @Override
    public List<ShopItems> getItemsBeetwenPriceDesc(String name, Double price_from, Double price_to) {

        return shopItemRepository.findAllByNameLikeAndPriceBetweenOrderByPriceDesc(name, price_from, price_to);
    }

    @Override
    public Countries addCountry(Countries country) {
        return countriesRepository.save(country);
    }

    @Override
    public Countries saveCountry(Countries country) {
        return countriesRepository.save(country);
    }

    @Override
    public Countries getCountry(Long id) {
        return countriesRepository.getOne(id);
    }

    @Override
    public List<Countries> getAllCountries() {
        return countriesRepository.findAll();
    }

    @Override
    public Brands addBrand(Brands brand) {
        return brandsRepository.save(brand);
    }

    @Override
    public Brands saveBrand(Brands brand) {
        return brandsRepository.save(brand);
    }

    @Override
    public Brands getBrand(Long id) {
        return brandsRepository.getOne(id);
    }

    @Override
    public List<Brands> getAllBrands() {
        return brandsRepository.findAll();
    }

    @Override
    public Categories addCategories(Categories categories) {
        return categoriesRepository.save(categories);
    }

    @Override
    public Categories saveCategories(Categories categories) {
        return categoriesRepository.save(categories);
    }

    @Override
    public Categories getCategories(Long id) {
        return categoriesRepository.getOne(id);
    }

    @Override
    public List<Categories> getAllCategories() {
        return categoriesRepository.findAll();
    }

    @Override
    public Pictures addPicture(Pictures picture) {
        return picturesRepository.save(picture);
    }

    @Override
    public Pictures savePicture(Pictures picture) {
        return picturesRepository.save(picture);
    }

    @Override
    public Pictures getPicture(Long id) {
        return picturesRepository.getOne(id);
    }

    @Override
    public List<Pictures> getAllPictures() {
        return picturesRepository.findAll();
    }

    @Override
    public void deletePicture(Pictures picture) {
        picturesRepository.delete(picture);
    }

    @Override
    public Comments addComment(Comments comment) {
        return commentsRepository.save(comment);
    }

    @Override
    public Comments saveComments(Comments comment) {
        return commentsRepository.save(comment);
    }

    @Override
    public Comments getComment(Long id) {
        return commentsRepository.getOne(id);
    }

    @Override
    public List<Comments> getAllComments() {
        return commentsRepository.findAll();
    }

    @Override
    public void deleteComment(Comments comment) {
        commentsRepository.delete(comment);
    }

    @Override
    public List<ShopItems> getItemsBeetwenPrice(String name, Double price_from, Double price_to) {
        return shopItemRepository.findAllByNameLikeAndPriceBetweenOrderByPriceAsc(name, price_from, price_to);
    }

    @Override
    public List<ShopItems> getItemsByBrandAsc(Brands brand) {
        return shopItemRepository.findAllByBrandsEqualsOrderByPriceAsc(brand);
    }

    @Override
    public List<ShopItems> getItemsByBrandDesc(Brands brand) {
        return shopItemRepository.findAllByBrandsEqualsOrderByPriceDesc(brand);
    }

    @Override
    public List<ShopItems> getItemsByNameAndBrandAsc(String name, Brands brand) {
        return shopItemRepository.findAllByNameLikeAndBrandsEqualsOrderByPriceAsc(name,brand);
    }

    @Override
    public List<ShopItems> getItemsByNameAndBrandDesc(String s, Brands brand) {
        return shopItemRepository.findAllByNameLikeAndBrandsEqualsOrderByPriceDesc(s,brand);
    }

    @Override
    public void deleteItem(ShopItems item) {
        shopItemRepository.delete(item);
    }




}
