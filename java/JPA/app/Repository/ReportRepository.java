package JPA.app.Repository;

import JPA.app.Entity.Project;
import JPA.app.Entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report,Long>
{
    List<Report> findAll();
    Report findById(long id);
    Report findFirstByProjectid(long id);
}
