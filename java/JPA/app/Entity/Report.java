package JPA.app.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @Description: $
 * @Param: $
 * @return: $
 * @Author: DoyouhaveJJ
 * @Date: $
 */
@Entity
public class Report implements Serializable {

    private static final long serialVersionUID=1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String title;
    @Column
    private String updatetime;
    @Column
    private String describe;
    @Column
    private String quality; //项目质量
    @Column
    private int number;//项目人数
    @Column
    private Long projectid;

    public Long getProjectid() {
        return projectid;
    }

    public void setProjectid(Long projectid) {
        this.projectid = projectid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Column
    private int progress;//进度

}
