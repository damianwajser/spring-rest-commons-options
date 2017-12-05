package com.github.damianwajser.controllers.simple;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.damianwajser.model.Pojo;

@RestController
@RequestMapping("/pageable")
public class PageableController {

	@GetMapping("/{id}")
	public Pojo getById(@PathVariable Integer id, @RequestHeader("Authorization") String apikey, Pageable p) {
		return null;
	}
}
