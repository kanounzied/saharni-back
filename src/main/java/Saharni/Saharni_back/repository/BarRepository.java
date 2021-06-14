package Saharni.Saharni_back.repository;

import Saharni.Saharni_back.entity.Bar;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface BarRepository extends UserBaseRepository<Bar>  {
}