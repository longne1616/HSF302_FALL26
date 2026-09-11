package fu.de200357.pojo;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true)
    private String email;

    @Column(precision = 10, scale = 2)
    private BigDecimal salary;

    // Luôn dùng STRING, KHÔNG dùng mặc định ORDINAL
    @Enumerated(EnumType.STRING)
    private Gender gender;

    // JPA 2.2+ map LocalDate trực tiếp, không cần @Temporal
    private LocalDate hireDate;

    private boolean active;

    // KHÔNG có cột tương ứng trong DB - tính toán ngay khi gọi getter
    @Transient
    private int yearsOfService;

}

