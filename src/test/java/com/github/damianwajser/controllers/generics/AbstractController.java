package com.github.damianwajser.controllers.generics;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.damianwajser.model.generic.GenericParameter;

public class AbstractController<E extends GenericParameter> {

	@GetMapping("/{id}")
	public E findById(@PathVariable("id") Integer id) {
		return null;

	}

	@GetMapping("/")
	public Iterable<E> findAll() {
		return null;

	}

	@GetMapping("/active")
	public Iterable<E> findActive() {
		return null;

	}

	@GetMapping("/inactive")
	public Iterable<E> findInActive() {
		return null;

	}

	@GetMapping
	public Iterable<E> getByCode(@RequestParam("code") Optional<String> code) {
		return null;
	}

	@PostMapping
	public E post(@RequestBody @Valid E e) {
		return null;
	}

	@PutMapping("/{id}")
	public E put(@PathVariable("id") Integer id, @RequestBody @Valid E inputMode) {
		return null;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable Integer id) {
	}
}
