package kz.springboot.HomeTask7.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "t_comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @Column(name = "comment")
    private String comment;

    @Column(name = "added_date")
    private String addedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private ShopItems items;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users author;

}


