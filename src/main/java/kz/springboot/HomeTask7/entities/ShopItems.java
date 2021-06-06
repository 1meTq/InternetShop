package kz.springboot.HomeTask7.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "items")
public class ShopItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "stars")
    private int stars;

    @Column(name = "small_pic_url")
    private String smallPicUrl;

    @Column(name = "large_pic_url")
    private String largePicUrl;

    @Column(name = "added_date")
    private String addedDate;

    @Column(name = "in_top_page")
    private Boolean inTopPage;

    @ManyToOne(fetch = FetchType.EAGER)
    private Brands brands;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Categories> categories;



}
