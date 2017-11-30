package com.github.damianwajser.controllers.generics;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.damianwajser.model.generic.OtherParameter;

@RestController
@RequestMapping("/other/parameter")
public class OtherParameterController extends AbstractController<OtherParameter> {

	
	@GetMapping("/response")
	public OtherParameter otherEndPoint (){
		return null;
	}
}
