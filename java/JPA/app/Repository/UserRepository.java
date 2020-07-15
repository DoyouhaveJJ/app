package JPA.app.Repository;

import JPA.app.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long>
{
    User findByAccount(String account);
    List<User> findByRole(int role);
}
