package kz.springboot.HomeTask7.repositories;

import kz.springboot.HomeTask7.entities.Brands;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
@Transactional
public interface BrandsRepository extends JpaRepository<Brands,Long> {


}
