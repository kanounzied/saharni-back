package Saharni.Saharni_back.repository;

import Saharni.Saharni_back.entity.Sahar;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface SaharRepository extends UserBaseRepository<Sahar>  {
}
