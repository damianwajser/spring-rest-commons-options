package com.github.damianwajser.controllers.generics;

import org.springframework.web.bind.annotation.RequestMapping;

import com.github.damianwajser.model.generic.OtherParameter;

@RequestMapping("/other/parameter")
public class OtherParameterController extends AbstractController<OtherParameter> {

	
//	@GetMapping("/response")
//	public OtherParameter otherEndPoint (){
//		return null;
//	}
}
