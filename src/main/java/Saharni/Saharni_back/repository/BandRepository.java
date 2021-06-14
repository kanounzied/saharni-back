package Saharni.Saharni_back.repository;

import Saharni.Saharni_back.entity.Band;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface BandRepository extends UserBaseRepository<Band>{
    List<Band> findDistinctByVerified(boolean verified);
}
