package JPA.app.Controller;

import JPA.app.Entity.Project;
import JPA.app.Entity.Report;
import JPA.app.Entity.User;
import JPA.app.Repository.ProjectRepository;
import JPA.app.Repository.ReportRepository;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
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
    public JSONArray getList(HttpSession httpSession){
        User u = (User)httpSession.getAttribute("user");
        if(u==null){
            return null;
        }
        List<Project> projLst=projectRepository.findByCreaterId(u.getId());
        JSONArray jsonArray = new JSONArray();
        for(Project p : projLst){
            Report report = reportRepository.findFirstByProjectid(p.getId());
            JSONObject object = new JSONObject();
            object.put("id",p.getId());
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

    @RequestMapping(value = "/getReport",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getReport(@RequestBody Report r){
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAaAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAaAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAaAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa");

        Report report = reportRepository.findFirstById(r.getId());
        return JSON.parseObject(JSON.toJSONString(report));
    }

    @RequestMapping(value = "/updateReport" ,method = RequestMethod.POST)
    @ResponseBody
    public String getReport(HttpSession httpSession,@RequestBody Report r){
        User u = (User)httpSession.getAttribute("user");
        if(u==null){
            return "failure";
        }
        r.setUpdatetime(LocalDateTime.now().toString());
        System.out.println(u.getName()+r.getId());
        try{
            reportRepository.save(r);
        }catch (Exception e){
            return "failure";
        }
        return "success";



    }
}
