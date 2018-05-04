package by.ras.entity.particular;

import by.ras.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "orders")
public class Order extends BaseEntity{

    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    //list of product ids in order
    @ManyToMany
    @JoinTable(
            name = "ordered_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> orderedProducts = new LinkedList<>();

    //set products from Order with reserved products
    public void setOrderProducts(LinkedList<Product> reservedProducts){
        orderedProducts.addAll(reservedProducts);
    }

    //counted sum to be payed for this order
    private Integer totalSum;
}