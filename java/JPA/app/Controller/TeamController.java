package JPA.app.Controller;

import JPA.app.Entity.Project;
import JPA.app.Entity.User;
import JPA.app.Repository.TeamRepository;
import JPA.app.Repository.UserRepository;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(value = "/team")
    public String team(@RequestParam long id, HttpSession httpSession){
        System.out.println("teamid"+id);
        httpSession.setAttribute("projectid",id);
        return "main/project/team";
    }
    @RequestMapping(value = "/getmemberlst")
    @ResponseBody
    public JSONArray getmemberlst(){
        List<User> lst=userRepository.findAll();
        JSONArray arr=new JSONArray();
        for(User u: lst){
            if(u.getRole()==0){
                JSONObject json=new JSONObject();
                json.put("id",u.getId());
                json.put("skill",u.getSkill());
                json.put("name",u.getName());
                json.put("experience",u.getExperience());
                arr.add(json);
            }
        }
        return arr;
    }
}
