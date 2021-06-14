package Saharni.Saharni_back.repository;

import Saharni.Saharni_back.entity.ERole;
import Saharni.Saharni_back.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    public Role findByName(ERole name);
}
