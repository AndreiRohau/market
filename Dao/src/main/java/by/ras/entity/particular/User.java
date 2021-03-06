package by.ras.entity.particular;

import by.ras.entity.*;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.sql.Date;
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
    @Pattern(regexp="^[A-Z][a-z]{2,10}$", message="Name must be alpha with no spaces and first capital. 3-10 letters")
    private String name;
    @Column
    @Pattern(regexp="^[A-Z][a-z]{2,10}$", message="Surname must be alpha with no spaces and first capital. 3-10 letters")
    private String surname;
    @Column(unique = true)
    @Pattern(regexp="^[A-Za-z0-9]{2,10}$", message="Login must be alpha with no spaces and first capital. 3-10 letters")
    private String login;
    @Column
    // /* <!-- Minimum 3 characters, at least one letter and one number: --> */
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{3,10}$", message = "Login must be alphanumeric: " +
            "at least 1 capital, 1 lower case, 1 num. 3-10 letters")
    private String password;
    @Column
    private String sex;
    @Column
    private String occupation;
    @Column
    private String role;
    @Column(name = "creation_date")
    private Date date;

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
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






    public User(String name, String surname, String login, String password, String role, Date date) {
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
        this.role = role;
        this.date = date;
    }

    public User(String login) {
        this.login = login;
    }


}
