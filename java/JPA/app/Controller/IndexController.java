package JPA.app.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * @Description: $
 * @Param: $
 * @return: $
 * @Author: DoyouhaveJJ
 * @Date: $
 */
@Controller
public class IndexController {
    //fucker
    @RequestMapping(value = "/myself")
    public String Myself()
    {
        return "main/myself";
    }
    @RequestMapping(value = "/myreport")
    public String MyReport()
    {
        return "business/myreport";
    }

}
