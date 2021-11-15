package com.mindtree.config.server.configserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/configserver")
public class HelloController {

	@GetMapping
	public String sayHello() {
		return "Hi GOod Morning!!";
	}
}
