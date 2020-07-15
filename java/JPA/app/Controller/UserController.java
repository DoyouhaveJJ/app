package JPA.app.Controller;

import JPA.app.Entity.User;
import JPA.app.Repository.UserRepository;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: $
 * @Param: $
 * @return: $
 * @Author: DoyouhaveJJ
 * @Date: $
 */
@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/getInfo",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject doLogout(HttpSession httpSession){
        if(httpSession.getAttribute("user")!=null){
            User u = (User)httpSession.getAttribute("user");
            String skills = u.getSkill();
            String[] s = skills.split(";");
            Map<String, Object> resmp = new HashMap<>();
            resmp.put("C/C++".toLowerCase(),"0");
            resmp.put("Java".toLowerCase(),"0");
            resmp.put(".Net".toLowerCase(),"0");
            resmp.put("Python".toLowerCase(),"0");
            for(String i : s){
                resmp.put(i,"1");
            }
            resmp.put("password",u.getPassword());
            resmp.put("experience",""+u.getExperience());
            resmp.put("name",u.getName());
            resmp.put("user",u);
            return JSON.parseObject(JSON.toJSONString(resmp));

        }
        return null;
    }

    @RequestMapping(value = "/doInfoAlert")
    @ResponseBody
    public String addNewUser(@RequestBody User user, HttpSession httpSession){
        user.setPrivielge("");
        userRepository.save(user);
        httpSession.setAttribute("user",user);
        return "success";

    }
}
