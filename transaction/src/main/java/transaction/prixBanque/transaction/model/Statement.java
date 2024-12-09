package transaction.prixBanque.transaction.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Statement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Account ID cannot be null")
    @Column(nullable = false)
    private Long accountId; // ID du compte pour lequel le relevé est généré

    @NotNull(message = "Start date cannot be null")
    @PastOrPresent(message = "Start date must be in the past or present")
    @Column(nullable = false)
    private LocalDateTime startDate;

    @NotNull(message = "End date cannot be null")
    @FutureOrPresent(message = "End date must be in the present or future")
    @Column(nullable = false)
    private LocalDateTime endDate;

    @NotNull(message = "Total credit cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total credit must be greater than or equal to 0")
    @Column(nullable = false)
    private Double totalCredit;

    @NotNull(message = "Total debit cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total debit must be greater than or equal to 0")
    @Column(nullable = false)
    private Double totalDebit;

    @NotNull(message = "Balance cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Balance must be greater than or equal to 0")
    @Column(nullable = true)
    private Double balance;
}
