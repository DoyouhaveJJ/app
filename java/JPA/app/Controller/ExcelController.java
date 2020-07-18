package JPA.app.Controller;

import JPA.app.Entity.Project;
import JPA.app.Entity.Report;
import JPA.app.Entity.Risk;
import JPA.app.Entity.User;
import JPA.app.Repository.ProjectRepository;
import JPA.app.Repository.ReportRepository;
import JPA.app.Repository.RiskRepository;
import JPA.app.Repository.UserRepository;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ExcelController
{

    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RiskRepository riskRepository;


    @RequestMapping(value = "/excel")
    public String excel(){return"main/Excel";}

    @RequestMapping(value = "/getExcel")
    @ResponseBody
    public String getExcel(@RequestBody JSONObject json, HttpSession httpSession)throws IOException
    {

        String staticPath = ClassUtils.getDefaultClassLoader().getResource("static").getPath();
        String fileName = "报告.xlsx";  //获取文件名
        String path=staticPath+ "/"+fileName;
        List<String> lst=(ArrayList<String>)json.get("value");
        List<Map<String,Object>> values=new ArrayList<>();
        List<Report> reports=reportRepository.findAll();
        for(Report repo :reports){
            Map<String ,Object> value=new HashMap<>();
            for(String str :lst){
                switch (str){
                    case "ID":value.put(str,repo.getId());break;
                    case "描述":value.put(str,repo.getDescription());break;
                    case "人数":value.put(str,repo.getNumber());break;
                    case "项目名":{
                        long projid=repo.getProjectid();
                        String projname=projectRepository.findById(projid).getName();
                        value.put(str,projname);break;
                    }
                    case "进度":value.put(str,repo.getProgress()+"%");break;
                    case "质量":value.put(str,repo.getQuality());break;
                    case "更新时间":value.put(str,repo.getUpdatetime());break;
                    case "主题":value.put(str,repo.getTitle());break;
                    case "负责人":{
                        long createrid=repo.getCreaterid();
                        String creater=userRepository.findById(createrid).getName();
                        value.put(str,creater);break;
                    }
                }

            }
            values.add(value);
        }

        ExcelUtils.writeExcel(path,"text",lst,values);
        httpSession.setAttribute("path",path);
        return "success";
    }

    @RequestMapping(value = "/download")
    @ResponseBody
    public String download(HttpServletResponse res,HttpSession httpSession){
        String path=(String)httpSession.getAttribute("path");
        File excelFile = new File(path);
        res.setCharacterEncoding("UTF-8");
        res.setHeader("content-type", "application/octet-stream;charset=UTF-8");
        res.setContentType("application/octet-stream;charset=UTF-8");
        //加上设置大小下载下来的.xlsx文件打开时才不会报“Excel 已完成文件级验证和修复。此工作簿的某些部分可能已被修复或丢弃”
        res.addHeader("Content-Length", String.valueOf(excelFile.length()));
        try {
            res.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode("报告.xlsx".trim(), "UTF-8"));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = res.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(new File(path)));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return "success";
    }

    //获取统计
    @RequestMapping(value = "/getProjStatic")
    @ResponseBody
    public JSONObject getProjStatic(HttpSession httpSession){
        User u = (User)httpSession.getAttribute("user");
        if(u==null){
            return null;
        }
        List<Project> projects = projectRepository.findAll();
        JSONArray jsonArray1 = new JSONArray();
        JSONArray jsonArray2 = new JSONArray();
        int unstart=0,staring=0,finished=0;
        int sm=0,md=0,lg=0;
        for(Project p : projects){
            if(p.getStatus()==0){
                unstart++;
            }else if(p.getStatus()==1){
                staring++;
            }else if(p.getStatus()==2){
                finished++;
            }

            if(p.getType().equals("sm")){
                sm++;
            }else if(p.getType().equals("md")){
                md++;
            }else if(p.getType().equals("lg")){
                lg++;
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("y","未启动");
        jsonObject.put("a",unstart);
        jsonArray1.add(jsonObject);
        jsonObject = new JSONObject();
        jsonObject.put("y","进行中");
        jsonObject.put("a",staring);
        jsonArray1.add(jsonObject);
        jsonObject = new JSONObject();
        jsonObject.put("y","已结束");
        jsonObject.put("a",finished);
        jsonArray1.add(jsonObject);
        jsonObject = new JSONObject();
        jsonObject.put("y","小型");
        jsonObject.put("a",sm);
        jsonArray2.add(jsonObject);
        jsonObject = new JSONObject();
        jsonObject.put("y","中型");
        jsonObject.put("a",md);
        jsonArray2.add(jsonObject);
        jsonObject = new JSONObject();
        jsonObject.put("y","大型");
        jsonObject.put("a",lg);
        jsonArray2.add(jsonObject);

        JSONObject res = new JSONObject();
        res.put("1",jsonArray1);
        res.put("2",jsonArray2);
        return res;
    }

    @RequestMapping(value = "/getRiskStatic")
    @ResponseBody
    public JSONObject getRiskStatic(HttpSession httpSession){
        User u = (User)httpSession.getAttribute("user");
        if(u==null){
            return null;
        }
        List<Risk> risks = riskRepository.findAll();
        int t0=0,t1=0,t2=0,t3=0,t4=0,t5=0,t6=0;
        for(Risk r : risks){
            switch (r.getType()){
                case 0:{t0++;break;}
                case 1:{t1++;break;}
                case 2:{t2++;break;}
                case 3:{t3++;break;}
                case 4:{t4++;break;}
                case 5:{t5++;break;}
                case 6:{t6++;break;}
            }
        }
        JSONArray jsonArray1 = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        //0=已处理，1=人员，2=开发过程，3=环境，4=后勤，5=需求，6=进度
        jsonObject.put("label","已处理风险");
        jsonObject.put("value",t0);
        jsonArray1.add(jsonObject);
        jsonObject = new JSONObject();
        jsonObject.put("label","人员风险");
        jsonObject.put("value",t1);
        jsonArray1.add(jsonObject);
        jsonObject = new JSONObject();
        jsonObject.put("label","开发过程风险");
        jsonObject.put("value",t2);
        jsonArray1.add(jsonObject);
        jsonObject = new JSONObject();
        jsonObject.put("label","环境风险");
        jsonObject.put("value",t3);
        jsonArray1.add(jsonObject);
        jsonObject = new JSONObject();
        jsonObject.put("label","后勤风险");
        jsonObject.put("value",t4);
        jsonArray1.add(jsonObject);
        jsonObject = new JSONObject();
        jsonObject.put("label","需求风险");
        jsonObject.put("value",t5);
        jsonArray1.add(jsonObject);
        jsonObject = new JSONObject();
        jsonObject.put("label","进度风险");
        jsonObject.put("value",t6);
        jsonArray1.add(jsonObject);

        JSONObject res = new JSONObject();
        res.put("1",jsonArray1);
        return res;

    }

    @RequestMapping(value = "/getReportStatic")
    @ResponseBody
    public JSONArray getReportStatic(HttpSession httpSession){
        User u = (User)httpSession.getAttribute("user");
        if(u==null){
            return null;
        }
        List<Report> reportList = reportRepository.findAll();
        int[] counter=new int[11];
        for(Report r : reportList){
            counter[(r.getProgress()/10)] ++ ;
        }
        JSONArray res = new JSONArray();
        for(int i = 0 ; i < 11 ; ++i){
            JSONObject jsonObject =new JSONObject();
            jsonObject.put("period","完成进度"+i*10+"%");
            jsonObject.put("dl",counter[i]);
            res.add(jsonObject);
        }
        return res;
    }

}
