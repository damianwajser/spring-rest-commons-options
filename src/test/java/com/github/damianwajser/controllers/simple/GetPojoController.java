package com.github.damianwajser.controllers.simple;

import java.util.Collection;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.damianwajser.model.Pojo;

@RequestMapping("/test123")
public class GetPojoController {
	@GetMapping
	public Collection<Pojo> getAll() {
		return null;
	}

	@GetMapping("/{id}")
	public Pojo getById(@PathVariable Integer id) {
		return null;
	}

	@GetMapping
	public Iterable<Pojo> getByParamenters(@RequestParam Integer cod, @RequestParam String str) {
		return null;
	}

}
