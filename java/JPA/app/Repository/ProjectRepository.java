package JPA.app.Repository;

import JPA.app.Entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project,Long>
{
    List<Project> findAll();
    Project findByName(String name);
    Project findById(long id);
    List<Project> findByCreaterId(long id);
}
