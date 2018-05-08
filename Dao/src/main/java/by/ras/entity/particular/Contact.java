package by.ras.entity.particular;

import by.ras.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;

@Getter @Setter @ToString(callSuper = true, exclude = "user")
@NoArgsConstructor @AllArgsConstructor @Builder
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "contacts")
@Entity
public class Contact extends BaseEntity{

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column
    @Pattern(regexp = "^[A-Z][a-z]{3,10}$", message="Must be alpha with no spaces and first capital. 3-10 letters")
    private String country;
    @Column
    @Pattern(regexp = "^[A-Z][a-z]{3,10}$", message="Must be alpha with no spaces and first capital. 3-10 letters")
    private String city;
    @Column
    @Pattern(regexp = "^[A-Z][a-z]{3,10}$", message="Must be alpha with no spaces and first capital. 3-10 letters")
    private String street;
    @Column(name = "house_number")
    @Pattern(regexp = "^[0-9]{1,4}$", message="Contains only of numbers with no spaces. 1-4 numbers in total")
    private String houseNumber;
    @Column(name = "phone_number")
    //any 12 numbers
    @Pattern(regexp = "^[0-9]{12,12}$", message="Contains only of numbers with no spaces. Any 12 numbers in total")
    private String phoneNumber;


}
