package transfer.service.ingenico.domains;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

@Getter
@Setter
@Entity
@Table(name = "transfer")
@Builder
public class Transfer implements Serializable {

    private static final long serialVersionUID = 632380474910793338L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Account senderAccountId;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Account recipientAccountId;

    @Column(name = "value")
    private BigDecimal value;

    @Column(name = "date")
    private Calendar date;

}
