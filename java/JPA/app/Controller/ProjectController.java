package JPA.app.Controller;

import JPA.app.Entity.Project;
import JPA.app.Entity.User;
import JPA.app.Repository.ProjectRepository;
import JPA.app.Repository.ReportRepository;
import JPA.app.Repository.UserRepository;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.xdevapi.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProjectController
{
    //aaaaaaaaa
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;


    @RequestMapping(value = "/project")
    public String index(){
        return "main/project/projectIndex";
    }

    @RequestMapping(value = "/getprojlst")
    @ResponseBody
    public JSONArray getlst(){
        List<Project> projLst=projectRepository.findAll();
        JSONArray jsonArray=JSONArray.parseArray(JSON.toJSONString(projLst));
        return jsonArray;
    }
    @RequestMapping(value = "/getCreater")
    @ResponseBody
    public JSONObject getCreater(){
        List<User> lst1=userRepository.findByRole(1);
        List<User> lst2=userRepository.findByRole(2);
        lst1.addAll(lst2);
        JSONObject json=new JSONObject();
        for(User u:lst1){
            json.put(u.getId().toString(),u.getName());
        }


        return json;
    }
    @RequestMapping(value = "/addNewProj")
    @ResponseBody
    public String addNewProj(@RequestBody Project proj, HttpSession httpSession){
        Project temp=projectRepository.findByName(proj.getName());
        User user=(User)httpSession.getAttribute("user");
        if(temp==null && user.getRole()!=0){
            proj.setCreaterId(user.getId());
            proj.setStatus(0);
            System.out.println(proj.getName());
            projectRepository.save(proj);
            return "success";
        }
        return "failure";
    }
    @RequestMapping(value = "/getproj")
    @ResponseBody
    public JSONObject getproj(@RequestBody Project proj)
    {
        long id=proj.getId();
        System.out.println(proj.getId());
        Project temp = projectRepository.findById(id);
        JSONObject json=new JSONObject();
        SimpleDateFormat sdf =new SimpleDateFormat( " yyyy-MM-dd" );
        json.put("name",temp.getName());
        json.put("createrId",temp.getCreaterId());
        if(temp.getStart()!=null){
            json.put("start",sdf.format(temp.getStart()));
        }else json.put("start","未开始");
        if(temp.getStart()!=null){
            json.put("end",sdf.format(temp.getEnd()));
        }else json.put("end","未结束");
        json.put("tech",temp.getTech());
        json.put("type",temp.getType());
        json.put("description",temp.getDescription());
        return json;

    }



}
