package JPA.app.Controller;

import JPA.app.Entity.*;
import JPA.app.Repository.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
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
public class RiskController {
    @Autowired
    private RiskRepository riskRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamRepository teamRepository;
    @RequestMapping(value = "/getProjWithRisk")
    @ResponseBody
    public JSONArray getList(HttpSession httpSession){
        User u = (User)httpSession.getAttribute("user");
        if(u==null){
            return null;
        }
        List<Project> projLst=projectRepository.findByCreaterId(u.getId());
        JSONArray jsonArray = new JSONArray();
        for(Project p : projLst){
            List<Risk> risk = riskRepository.findByProjectid(p.getId());
            JSONObject object = new JSONObject();
            object.put("id",p.getId());
            object.put("name",p.getName());
            object.put("type",p.getType());
            object.put("description",p.getDescription());
            object.put("status",p.getStatus());
            int finished = countType(risk);
            object.put("finished",finished);
            object.put("unfinished",risk.size()-finished);
            jsonArray.add(object);
        }
        return jsonArray;
    }


    @RequestMapping(value = "/getRiskList")
    @ResponseBody
    public JSONArray getRiskList(HttpSession httpSession, @RequestBody Project project){
        User u = (User)httpSession.getAttribute("user");
        if(u==null){
            return null;
        }
        long id = project.getId();
        System.out.println(id);
        List<Risk> risks = riskRepository.findByProjectid(id);
        JSONArray jsonArray = new JSONArray();
        for(Risk r : risks){
            JSONObject object = new JSONObject();
            if(r.getOwner()==null || r.getOwner()==-1 ){
                object.put("owner",-1);
                object.put("ownername","无负责人");
            }else{
                User user = userRepository.findFirstById(r.getOwner());
                object.put("owner",user.getId());
                object.put("ownername",u.getName());
            }
            object.put("id",r.getId());
            object.put("projectid",r.getProjectid());
            object.put("description",r.getDescription());
            object.put("type",r.getType());
            object.put("prior",r.getPrior());
            object.put("updatetime",r.getUpdatetime());
            jsonArray.add(object);
        }
        return jsonArray;
    }

    @RequestMapping(value = "/getProjTeam")
    @ResponseBody
    public JSONArray getProjTeam(HttpSession httpSession, @RequestBody Project project){
        User u = (User)httpSession.getAttribute("user");
        if(u==null){
            return null;
        }
        long id = project.getId();
        List<Team> teams = teamRepository.findByProjectId(id);
        JSONArray jsonArray = new JSONArray();
        for(Team t : teams){
            JSONObject object = new JSONObject();
            User mu = userRepository.findFirstById(t.getMemberId());
            object.put("id",mu.getId());
            object.put("name",mu.getName());
            object.put("skills",mu.getSkill());
            jsonArray.add(object);
        }
        return jsonArray;
    }

    @RequestMapping(value = "/updateRisk")
    @ResponseBody
    public String updateRisk(HttpSession httpSession, @RequestBody Risk risk){
        User u = (User)httpSession.getAttribute("user");
        if(u==null){
            return null;
        }
        risk.setUpdatetime(LocalDateTime.now().toString());
        try{
            riskRepository.save(risk);
        }catch (Exception e){
            return "failure";
        }
        return "success";
    }

    private int countType(List<Risk> risks){
        int counter = 0;
        for(Risk r : risks){
            if(r.getType()==0){
                counter++;
            }
        }
        return counter;
    }
}
