package vn.removie.movies.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.removie.movies.Entity.ERole;
import vn.removie.movies.Entity.Role;
import vn.removie.movies.Repository.RoleRepository;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    public RoleRepository roleRepository;

    public List<Role> showRoleList() {
        return roleRepository.findAll();
    }
    public Role findRoleByName (ERole name){
        return roleRepository.findByName(name).get();
    }
}
