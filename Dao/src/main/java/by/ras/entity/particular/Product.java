package by.ras.entity.particular;

import by.ras.entity.BaseEntity;
import by.ras.entity.ProductType;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "products")
public class Product extends BaseEntity {

    @Column
    private String company;
    @Column
    private String productName;
    @Column
    private String model;
    @Column
    private String productType;
    @Column
    private Integer price;
    @Column
    private String description;
//    @Column
//    private Integer quantity;

}
