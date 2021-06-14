package Saharni.Saharni_back.repository;

import Saharni.Saharni_back.entity.User;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserRepository extends UserBaseRepository<User>  {
}
