package com.github.damianwajser.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.damianwajser.model.InputMode;

@RestController
public class OtroController {
	
//	@RequestMapping("/auto/patente/color")
//	public void lala(){}
//	@RequestMapping("/auto")
//	public void lala1(){}
//	@RequestMapping("/auto/patente")
//	public void lala2(){}
	@PostMapping("/private/v1/sell/mpos")
	public ResponseEntity<?> lala2(InputMode a){
		return null;
	}
}
