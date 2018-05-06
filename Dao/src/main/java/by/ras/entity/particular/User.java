package by.ras.entity.particular;

import by.ras.entity.*;
import lombok.*;

import javax.persistence.*;
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
public class User extends BaseEntity {

    @Column
    @Pattern(regexp="^[A-Z]+[a-z]+{3,10}$", message="Name must be alphanumeric with no spaces and first capital. 3-10 letters")
    private String name;
    @Column
    @Pattern(regexp="^[A-Z]+[a-z]+{3,10}$", message="Surname must be alphanumeric with no spaces and first capital. 3-10 letters")
    private String surname;
    @Column(unique = true)
    @Pattern(regexp="^[a-z]+[a-z]+{3,10}$", message="Login must be alphanumeric with no spaces and first capital. 3-10 letters")
    private String login;
    @Column
    // /* <!-- Minimum 3 characters, at least one letter and one number: --> */
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{3,10}$", message = "Login must be alphanumeric: " +
            "at least 1 capital, 1 lower case, 1 num. 3-10 letters")
    private String password;
    @Column
    private Sex sex;
    @Column
    private Occupation occupation;
    @Column
    private Role role;
    @Column(name = "account_status")
    private Status status;
    @Column(name = "creation_date")
    private LocalDateTime date;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Contact contact;

    //list of product in reserve
    @ManyToMany(cascade = CascadeType.REMOVE)
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
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
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
