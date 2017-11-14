package com.github.damianwajser.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.damianwajser.model.InputMode;

@RestController
@RequestMapping("/input_mode")
public class InputModeController extends AbstractController<InputMode> {

	@GetMapping("/test1")
	public InputMode lala(@RequestParam("code") String c, @RequestParam("cod") int a) {
		return null;
	}

}
