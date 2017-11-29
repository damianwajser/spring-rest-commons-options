package com.github.damianwajser.controllers.simple;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.damianwajser.model.Pojo;

@RestController
@RequestMapping("/test123")
public class GetPojoController {
	

	@GetMapping("/{id}")
	public Pojo getById(@PathVariable Integer id, @RequestHeader("Authorization") String apikey) {
		return null;
	}

	@GetMapping
	public Iterable<Pojo> getByParamenters(@RequestParam Integer cod, @RequestParam String str) {
		return null;
	}

}
