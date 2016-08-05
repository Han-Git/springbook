package com.millky.blog.presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {

	@RequestMapping("/hello")
	public String index(Model model) {
		System.out.println("test");
		model.addAttribute("name", "SpringBlog from Millky");
		return "hello";
	}
}