package by.ras.entity.particular;

import by.ras.entity.*;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;


@Entity
@Getter
@Setter
@ToString(callSuper = true, exclude = {"reservedProducts", "orders"})
@NoArgsConstructor @AllArgsConstructor @Builder
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
public class User extends BaseEntity{

    @Column
    @Pattern(regexp="^[A-Z]+[a-z]+$", message="Username must be alphanumeric with no spaces and first capital")
    @Max(value = 15, message = "Max name length should be less then 15 characters")
    @Min(value = 1, message = "Min name length should be great then 1 characters")
    private String name;
    @Column
    @Pattern(regexp="^[A-Z]+[a-z]+$", message="Username must be alphanumeric with no spaces and first capital")
    @Max(value = 15, message = "Max surname length should be less then 15 characters")
    @Min(value = 1, message = "Min surname length should be great then 1 characters")
    private String surname;
    @Column
    @Pattern(regexp="^[a-z]+[a-z]+$", message="Login must be alphanumeric with no spaces and first capital")
    @Max(value = 15, message = "Max login length should be less then 15 characters")
    @Min(value = 1, message = "Min login length should be great then 1 characters")
    private String login;
    @Column
    // Minimum eight characters, at least one letter and one number:
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{3,6}$")
    @Max(value = 10, message = "Max password length should be less then 15 characters")
    @Min(value = 3, message = "Min password length should be great then 1 characters")
    private String password;
    @Column
    private Sex sex;
    @Column
    private Occupation occupation;
    @Column
    private Role role;
    @Column
    private Status status;
    @Column
    private LocalDateTime date;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Contact contact;

    //list of product in reserve
    @ManyToMany
    @JoinTable(
            name = "reserved_products",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> reservedProducts = new LinkedList<>();

    //add reservation for certain product
    public void addReserve(Product product){
        reservedProducts.add(product);
    }
    //cancel reservation for certain product
    public void cancelReserve(Product product){
        reservedProducts.remove(product);
    }

    //list of orders
    @OneToMany(mappedBy = "user")
    private List<Order> orders = new LinkedList<>();





    public User(String name, String surname, String login, String password, Role role, LocalDateTime date) {
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
        this.role = role;
        this.date = date;
    }

    public User(String name) {
        this.name = name;
    }
}
