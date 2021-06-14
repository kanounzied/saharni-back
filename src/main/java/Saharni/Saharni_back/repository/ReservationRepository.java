package Saharni.Saharni_back.repository;

import Saharni.Saharni_back.entity.Reservation;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface ReservationRepository extends CrudRepository<Reservation, Long>{
}
