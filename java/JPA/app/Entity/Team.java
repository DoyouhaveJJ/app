package JPA.app.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Team implements Serializable{
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Long projectId;
    @Column
    private Long createrId;
    @Column
    private Long memberId;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getCreaterId()
    {
        return createrId;
    }

    public Long getMemberId()
    {
        return memberId;
    }

    public Long getProjectId()
    {
        return projectId;
    }

    public void setCreaterId(Long createrId)
    {
        this.createrId = createrId;
    }

    public void setMemberId(Long memberId)
    {
        this.memberId = memberId;
    }

    public void setProjectId(Long projectId)
    {
        this.projectId = projectId;
    }
}
