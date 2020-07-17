package JPA.app.Repository;

import JPA.app.Entity.Project;
import JPA.app.Entity.Risk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RiskRepository extends JpaRepository<Risk,Long>
{
    List<Risk> findAll();
    Risk findById(long id);
    List<Risk> findByProjectid(long id);
}
