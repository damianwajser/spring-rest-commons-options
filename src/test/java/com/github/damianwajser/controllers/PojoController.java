package com.github.damianwajser.controllers;

import java.util.Collection;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.damianwajser.model.Pojo;

@RequestMapping("/test123")
public class PojoController {
	@GetMapping("/")
	public Collection<Pojo> getAll() {
		return null;
	}

	@GetMapping("/{id}")
	public Pojo getById(@PathVariable Integer id) {
		return null;
	}
}
