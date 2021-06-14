package Saharni.Saharni_back.repository;

import Saharni.Saharni_back.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface UserBaseRepository<T extends User>
        extends CrudRepository<T, Long> {
    T findByEmail(String email);

    Boolean existsByEmail(String email);

    public User findByEmailIgnoreCase(String email);
    T findOneById(Long id);
}