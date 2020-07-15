package JPA.app.Repository;

import JPA.app.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long>
{
    User findByAccount(String account);
}
