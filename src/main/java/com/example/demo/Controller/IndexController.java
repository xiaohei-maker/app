package com.example.demo.Controller;

import com.example.demo.Model.User;
import com.example.demo.Service.QuestionService;
import com.example.demo.Service.UserService;
import com.example.demo.cache.HotTagCache;
import com.example.demo.dto.PaginationDTO;
import com.mysql.cj.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;

@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private HotTagCache hotTagCache;

    @Autowired
    private UserService userService;
    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "10") Integer size,
                        @RequestParam(name = "search", required = false) String search,
                        @RequestParam(name = "tag", required = false) String tag,
                        @RequestParam(name = "sort", required = false) String sort) {
        PaginationDTO pagination = questionService.list(search, tag, sort, page, size);
        List<String> tags = hotTagCache.getHots();
        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);
        model.addAttribute("tag", tag);
        model.addAttribute("tags", tags);
        model.addAttribute("sort", sort);
        return "index";
    }

    //
    @GetMapping("/zhuce")
    public  String zhuce(){
        return "zhuce";
    }
    //注册功能
    @RequestMapping(value = "/zhu",method = RequestMethod.POST)
    public String zhuce(HttpServletRequest request,
                        HttpServletResponse response,
                        Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "10") Integer size,
                        @RequestParam(name = "search", required = false) String search,
                        @RequestParam(name = "tag", required = false) String tag,
                        @RequestParam(name = "sort", required = false) String sort) {
        PaginationDTO pagination = questionService.list(search, tag, sort, page, size);
        List<String> tags = hotTagCache.getHots();
        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);
        model.addAttribute("tag", tag);
        model.addAttribute("tags", tags);
        model.addAttribute("sort", sort);
        User user=new User();
        //生成标识
        String token= UUID.randomUUID().toString();
        String file=request.getParameter("file");
        String username=request.getParameter("username");
        String password=request.getParameter("password1");
        String email=request.getParameter("email");
        String sex=request.getParameter("usex");

        user.setAvatarUrl(file);
        user.setName(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setSex(sex);
        user.setToken(token);
        userService.createUser(user);
        HttpSession session=request.getSession();

        response.addCookie(new Cookie("token",token));
      //  session.setAttribute("user",user);
        return "redirect:/";
    }
}
