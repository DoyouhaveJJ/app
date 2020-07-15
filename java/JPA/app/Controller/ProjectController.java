package JPA.app.Controller;

import JPA.app.Entity.Project;
import JPA.app.Repository.ProjectRepository;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class ProjectController
{
    @Autowired
    private ProjectRepository projectRepository;

    @RequestMapping(value = "/project")
    public String index(){
        return "main/project/projectIndex";
    }

    @RequestMapping(value = "/getprojlst")
    public JSONArray getlst(){
        List<Project> projLst=projectRepository.findAll();
        JSONArray jsonArray=JSONArray.parseArray(JSON.toJSONString(projLst));
        return jsonArray;
    }
}
