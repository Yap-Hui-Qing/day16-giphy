package vttp.batch5.ssf.day16.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vttp.batch5.ssf.day16.services.HttpbinService;

@Controller
@RequestMapping
public class AppController {
    
    @Autowired
    private HttpbinService httpBinSvc;

    @GetMapping("/search")
    public String getSearch(@RequestParam MultiValueMap<String, String> form, Model model){

        List<String> urlList = httpBinSvc.getWithQueryParams(form);
        model.addAttribute("urlList", urlList);
        return "gif";
    }
}
