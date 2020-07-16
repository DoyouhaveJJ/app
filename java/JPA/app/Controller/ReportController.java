package JPA.app.Controller;

import JPA.app.Entity.Project;
import JPA.app.Entity.Report;
import JPA.app.Repository.ProjectRepository;
import JPA.app.Repository.ReportRepository;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Description: $
 * @Param: $
 * @return: $
 * @Author: DoyouhaveJJ
 * @Date: $
 */
@Controller
public class ReportController {
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @RequestMapping(value = "/getProjWithReport")
    @ResponseBody
    public JSONArray getlst(){
        List<Project> projLst=projectRepository.findAll();
        JSONArray jsonArray = new JSONArray();
        for(Project p : projLst){
            Report report = reportRepository.findFirstByProjectid(p.getId());
            JSONObject object = new JSONObject();
            object.put("name",p.getName());
            object.put("type",p.getType());
            object.put("description",p.getDescription());
            object.put("status",p.getStatus());
            if(report==null){
                object.put("reportid",-1);
            }else{
                object.put("reportid",report.getId());
            }
            jsonArray.add(object);
        }
        return jsonArray;
    }
}
