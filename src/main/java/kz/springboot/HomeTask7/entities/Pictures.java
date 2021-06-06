package kz.springboot.HomeTask7.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "t_pictures")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pictures {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "added_date")
    private String addedDate;

    @ManyToOne(fetch = FetchType.EAGER)
    private ShopItems item;




}
