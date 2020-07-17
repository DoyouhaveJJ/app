package JPA.app.Controller;

import JPA.app.Entity.Project;
import JPA.app.Entity.Team;
import JPA.app.Entity.User;
import JPA.app.Repository.ProjectRepository;
import JPA.app.Repository.TeamRepository;
import JPA.app.Repository.UserRepository;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.xdevapi.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class TeamController
{
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @RequestMapping(value = "/team")
    public String team(@RequestParam long id, HttpSession httpSession){
        System.out.println("teamid"+id);
        httpSession.setAttribute("projectid",id);
        return "main/project/team";
    }
    @RequestMapping(value = "/getmemberlst")
    @ResponseBody
    public JSONArray getmemberlst(HttpSession httpSession){
        List<User> lst=userRepository.findAll();
        JSONArray arr=new JSONArray();
        long projectid=(long)httpSession.getAttribute("projectid");

        for(User u: lst){
            if(u.getRole()==0){
                JSONObject json=new JSONObject();
                json.put("id",u.getId());
                json.put("skill",u.getSkill());
                json.put("name",u.getName());
                json.put("experience",u.getExperience());
                List<Team> teamlst=teamRepository.findByMemberId(u.getId());
                json.put("status",false);
                for(Team t:teamlst){
                    if(t.getProjectId()==projectid){
                        json.put("status",true);
                        break;
                    }
                }
                arr.add(json);
            }
        }
        return arr;
    }
    @RequestMapping(value = "/invite")
    @ResponseBody
    public String invite(@RequestBody User invited,HttpSession httpSession){

        User user=(User)httpSession.getAttribute("user");
        if(user!=null &&user.getRole()!=0){
            long projectid=(long)httpSession.getAttribute("projectid");
            Project project=projectRepository.findById(projectid);
            Team team=new Team();
            team.setCreaterId(project.getCreaterId());
            team.setMemberId(invited.getId());
            team.setProjectId(projectid);
            teamRepository.save(team);
            return "success";
        }
        return "failure";


    }
    @RequestMapping(value = "/joinin")
    @ResponseBody
    public String joinin(@RequestBody Project project,HttpSession httpSession){
        User user=(User)httpSession.getAttribute("user");
        if(user!=null){
            long projid=project.getId();
            project=projectRepository.findById(projid);
            long createrid=project.getCreaterId();
            Team team=new Team();
            team.setProjectId(projid);
            team.setMemberId(user.getId());
            team.setCreaterId(createrid);
            teamRepository.save(team);
            return "success";
        }
        return "failure";
    }
    @RequestMapping(value = "/remove")
    @ResponseBody
    public String remove(@RequestBody User target,HttpSession httpSession){

        User user=(User)httpSession.getAttribute("user");

        if(user!=null &&user.getRole()!=0){
            long projectid=(long)httpSession.getAttribute("projectid");
            Project project=projectRepository.findById(projectid);
            Team team=teamRepository.findByMemberIdAndProjectId(target.getId(),projectid);
            teamRepository.delete(team);
            return "success";
        }
        return "failure";

    }
    @RequestMapping(value = "/getexp")
    @ResponseBody
    public JSONArray getexp(@RequestBody User mem){
        long memid=mem.getId();
        List<Team> teamlst=teamRepository.findByMemberId(memid);
        JSONArray arr=new JSONArray();
        for(Team t:teamlst){
            JSONObject json=new JSONObject();
            long projid=t.getProjectId();
            Project proj=projectRepository.findById(projid);
            json.put("name",proj.getName());
            arr.add(json);
        }
        return arr;

    }
    @RequestMapping(value = "/getmembers")
    @ResponseBody
    public JSONArray getmembers(@RequestBody Project proj,HttpSession httpSession){
        httpSession.setAttribute("projectid",proj.getId());
        long projid=proj.getId();
        List<Team> teamlst=teamRepository.findByProjectId(projid);
        JSONArray arr=new JSONArray();
        for(Team t :teamlst){
            long memid=t.getMemberId();
            User mem=userRepository.findById(memid);
            JSONObject json=new JSONObject();
            json.put("id",memid);
            json.put("name",mem.getName());
            arr.add(json);
        }
        return arr;
    }
    @RequestMapping(value = "/checkmem")
    @ResponseBody
    public String checkmem(@RequestBody Team team, HttpSession httpSession){
        long projid=team.getProjectId();
        User user=(User)httpSession.getAttribute("user");
        List<Team> teams=teamRepository.findByProjectId(projid);
        for(Team t:teams){
            if(user.getId()==t.getMemberId()){
                return "yes";
            }
        }
        return "no";
    }
}
