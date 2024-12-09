package gestionCompte.prixBanque.gestionCompte.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDTO {

    private Long id;
    private Double balance;
    private String status;
    private String accountType;
}

