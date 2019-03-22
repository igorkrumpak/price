package si.iitech.price.repository;

import org.springframework.stereotype.Repository;

import si.iitech.lib.repository.UserRepository;
import si.iitech.price.entity.impl.EtPriceUser;

@Repository
public interface PriceUserRepository extends UserRepository<EtPriceUser> {

	
}
