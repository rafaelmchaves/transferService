package transfer.service.ingenico.gateway;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import transfer.service.ingenico.domains.Transfer;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long>{
}
