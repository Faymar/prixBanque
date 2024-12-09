package transaction.prixBanque.transaction.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Source account ID cannot be null")
    @Column(nullable = false)
    private Long fromAccountId; // Compte source pour les virements

    @PositiveOrZero(message = "Target account ID must be positive or null")
    private Long toAccountId; // Compte cible pour les virements, null pour les débits/crédits simples

    @NotNull(message = "Transaction amount cannot be null")
    @DecimalMin(value = "0.01", message = "Transaction amount must be at least 0.01")
    @Column(nullable = false)
    private Double amount;

    @NotNull(message = "Transaction type cannot be null")
    @Pattern(regexp = "CREDIT|DEBIT|EPARGNE", message = "Transaction type must be CREDIT, DEBIT, or EPARGNE")
    @Column(nullable = false)
    private String type; // CREDIT, DEBIT, EPARGNE

    @NotNull(message = "Transaction date cannot be null")
    @PastOrPresent(message = "Transaction date must be in the past or present")
    @Column(nullable = false)
    private LocalDateTime date;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description; // Facultatif, par exemple, "Virement vers X"
}
