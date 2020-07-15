package JPA.app.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class User implements Serializable
{
    private static final long serialVersionUID=1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String account;
    @Column
    private String password;
    @Column
    private String name;
    @Column
    private String privielge;
    @Column
    private int role;//0-普通用户；1-项目经理；2-管理员
    @Column
    private String skill;
    @Column
    private int experience;
    @Column
    private String photo;

    public void setSkill(String skill)
    {
        this.skill = skill;
    }

    public String getPhoto()
    {
        return photo;
    }

    public void setPhoto(String photo)
    {
        this.photo = photo;
    }

    public int getExperience()
    {
        return experience;
    }

    public void setExperience(int experience)
    {
        this.experience = experience;
    }

    public String getSkill()
    {
        return skill;
    }


    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }


    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getAccount()
    {
        return account;
    }

    public void setAccount(String account)
    {
        this.account = account;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public int getRole()
    {
        return role;
    }

    public void setRole(int role)
    {
        this.role = role;
    }

    public String getPrivielge()
    {
        return privielge;
    }

    public void setPrivielge(String privielge)
    {
        this.privielge = privielge;
    }
}
