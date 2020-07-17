package JPA.app.Controller;

import JPA.app.Entity.Project;
import JPA.app.Entity.Report;
import JPA.app.Repository.ProjectRepository;
import JPA.app.Repository.ReportRepository;
import JPA.app.Repository.UserRepository;
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
}
