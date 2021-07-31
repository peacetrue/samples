package com.github.peacetrue.sample.spring.security;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

/**
 * @author : xiayx
 * @since : 2021-07-31 19:46
 **/
@Controller
public class IndexController {

    @RequestMapping({"/index", "/"})
    public String index(Model model, Principal principal) {
        model.addAttribute("user", principal);
        return "index";
    }

}
