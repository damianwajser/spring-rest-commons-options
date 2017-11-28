package com.github.damianwajser.controllers.simple;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TreeUrlsController {
	@PostMapping("/public/v1/ypf")
	public void post() {
	}

	@PostMapping("/private/v1/ypf")
	public void post1() {
	}

	@RequestMapping(method = RequestMethod.GET, path = "private/v1/account/{accountId}/ypf")
	public void get() {
	}

	@RequestMapping(method = RequestMethod.GET, path = "private/v1/ypf")
	public void get1() {
	}
}
