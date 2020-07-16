package JPA.app.Controller;

import JPA.app.Entity.User;
import JPA.app.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
public class Login_Register
{
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/login")
    public String login() {return "main/login";}
    @RequestMapping(value = "/register")
    public String register(){return "main/register";}
    @RequestMapping(value = "/doRegister")
    @ResponseBody
    public String addNewUser(@RequestBody User user, HttpSession httpSession){
        User temp=userRepository.findByAccount(user.getAccount());
        if(temp==null){
            user.setPrivielge("");
            userRepository.save(user);
            httpSession.setAttribute("user",user);
            return "success";
        }
        return "failure";
    }
    @RequestMapping(value = "/doLogin")
    @ResponseBody
    public String doLogin(@RequestBody User user, HttpSession httpSession){
        User temp=userRepository.findByAccount(user.getAccount());
        if(temp.getPassword().equals(user.getPassword())){
            httpSession.setAttribute("user",temp);
            return "success";
        }
        return "failure";
    }

    @RequestMapping(value = "/doLogout",method = RequestMethod.POST)
    @ResponseBody
    public String doLogout(HttpSession httpSession){
        if(httpSession.getAttribute("user")!=null){
            httpSession.setAttribute("user",null);
            return "success";

        }
        return "failure";
    }


}
