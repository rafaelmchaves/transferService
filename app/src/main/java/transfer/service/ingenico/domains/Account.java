package transfer.service.ingenico.domains;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Setter
@Getter
@Builder
@Entity
@Table(name="account")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Account implements Serializable {

    private static final long serialVersionUID = -8019229870854666001L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "currentBalance")
    private BigDecimal currentBalance;

}
