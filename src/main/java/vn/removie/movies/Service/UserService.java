package vn.removie.movies.Service;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import vn.removie.movies.Entity.User;
import vn.removie.movies.Repository.UserRepository;


import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    public Optional<User> anUser(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> allUsers() {
        return userRepository.findAll(Sort.by(Sort.Direction.DESC, "createAt"));
    }
    public List<User> allUsersByUserNameAsc() {
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, "username"));
    }
    public List<User> allUsersByUsernameDesc() {
        return userRepository.findAll(Sort.by(Sort.Direction.DESC, "username"));
    }



    public Page<User> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<User> list;
        if (allUsers().size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, allUsers().size());
            list = allUsers().subList(startItem, toIndex);
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createAt");

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, sort);

        Page<User> userPage = new PageImpl<User>(list, pageRequest, allUsers().size());
        return userPage;
    }
    public Page<User> findPaginatedByUsernameAsc(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<User> list;
        if (allUsersByUserNameAsc().size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, allUsersByUserNameAsc().size());
            list = allUsersByUserNameAsc().subList(startItem, toIndex);
        }

        Sort sort = Sort.by(Sort.Direction.ASC, "username");

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, sort);

        Page<User> userPage = new PageImpl<User>(list, pageRequest, allUsersByUserNameAsc().size());

        return userPage;
    }

    public Page<User> findPaginatedByUsernameDesc(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<User> list;
        if (allUsersByUsernameDesc().size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, allUsersByUsernameDesc().size());
            list = allUsersByUsernameDesc().subList(startItem, toIndex);
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "username");

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, sort);

        Page<User> userPage = new PageImpl<User>(list, pageRequest, allUsersByUsernameDesc().size());

        return userPage;
    }


    public void saveUser(User user) {
        Date timeStamp = Calendar.getInstance().getTime();
        user.setCreateAt(timeStamp);
        userRepository.save(user);
    }


    public void deleteUser(User user) {
        userRepository.delete(user);
    }


    public Page<User> findPaginatedByUsername(String username, Pageable pageable) {
        return userRepository.findByUsernameContainingIgnoreCase(username, pageable);
    }

    public boolean isExistUsername(String username){
        return userRepository.existsByUsername(username);
    }
    public boolean isExistEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public User getUserById(ObjectId id) {
        return userRepository.findById(id).get();
    }
}
