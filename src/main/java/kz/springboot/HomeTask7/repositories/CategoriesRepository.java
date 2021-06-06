package kz.springboot.HomeTask7.repositories;


import kz.springboot.HomeTask7.entities.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CategoriesRepository extends JpaRepository<Categories,Long> {

}
