package by.ras.entity.particular;

import by.ras.entity.BaseEntity;
import by.ras.entity.ProductType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

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
    /*
        1- It must start and end with a digit or character
        2- It must be exactly 4 to 22 character long
        3- Allowed Special Characters are _.-
     */

    @Column
    @Pattern(regexp = "^[a-zA-Z0-9][a-zA-Z0-9-_.]{0,20}[a-zA-Z0-9]$", message = "1- It must start and end with a digit or character 2- It must be exactly 4 to 10 character long 3- Allowed Special Characters are _.-")
    private String company;
    @Column(name = "product_name")
    @Pattern(regexp = "^[a-zA-Z0-9][a-zA-Z0-9-_.]{0,20}[a-zA-Z0-9]$", message = "1- It must start and end with a digit or character 2- It must be exactly 4 to 10 character long 3- Allowed Special Characters are _.-")
    private String productName;
    @Column(unique = true)
    @Pattern(regexp = "^[a-zA-Z0-9][a-zA-Z0-9-_.]{0,20}[a-zA-Z0-9]$", message = "1- It must start and end with a digit or character 2- It must be exactly 4 to 10 character long 3- Allowed Special Characters are _.-")
    private String model;
    @Column(name = "product_type")
    @Pattern(regexp = "^[a-zA-Z0-9][a-zA-Z0-9-_.]{0,20}[a-zA-Z0-9]$", message = "1- It must start and end with a digit or character 2- It must be exactly 4 to 10 character long 3- Allowed Special Characters are _.-")
    private String productType;
    @Column
    @Pattern(regexp = "^[0-9]{1,7}$", message="Contains only of numbers with no spaces. 7 numbers in total")
    private String price;
    @Column(length = 2000)
    private String description;
//    @Column
//    private Integer quantity;

}
