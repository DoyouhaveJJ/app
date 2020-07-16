package JPA.app.Repository;

import JPA.app.Entity.Project;
import JPA.app.Entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team,Long>
{
    List<Team> findByProjectId(long id);
    List<Team> findByMemberId(long id);
}
