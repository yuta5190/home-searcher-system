package com.example.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * toppage表示のコントローラー.
 * 
 *  @author yoshida_yuuta
 */
@RequestMapping("/")
@Controller
public class TopPageController {
	@GetMapping("/")
	public String showTopPage() {
		return "top-page.html";
}

}
