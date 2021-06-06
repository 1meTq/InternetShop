package kz.springboot.HomeTask7.repositories;
import kz.springboot.HomeTask7.entities.Brands;
import kz.springboot.HomeTask7.entities.ShopItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
@Transactional
public interface ShopItemRepository extends JpaRepository<ShopItems, Long> {

    List<ShopItems> findAllByInTopPageTrue();

    List<ShopItems> findAllByNameLikeOrderByPriceAsc(String name);

    List<ShopItems> findAllByNameLikeOrderByPriceDesc(String name);

    List<ShopItems> findAllByNameLikeAndPriceBetweenOrderByPriceAsc(String name, double price1, double price2);

    List<ShopItems> findAllByNameLikeAndPriceBetweenOrderByPriceDesc(String name, double price1, double price2);

    List<ShopItems> findAllByBrandsEqualsOrderByPriceAsc(Brands brands);

    List<ShopItems> findAllByBrandsEqualsOrderByPriceDesc(Brands brands);

    List<ShopItems> findAllByNameLikeAndBrandsEqualsOrderByPriceAsc( String name, Brands brand);

    List<ShopItems> findAllByNameLikeAndBrandsEqualsOrderByPriceDesc( String name, Brands brand);

}
