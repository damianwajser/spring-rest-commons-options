package com.github.damianwajser.controllers.simple;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.damianwajser.model.Pojo;

@RequestMapping("/test123")
public class ModifyPojoController {

	@PutMapping("/{id}")
	public Pojo put(Pojo pojo, @PathVariable Integer id) {
		return null;
	}
	
	@PostMapping
	public Pojo post(Pojo pojo, @PathVariable Integer id) {
		return null;
	}
}
