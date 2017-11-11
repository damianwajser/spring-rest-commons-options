package com.github.damianwajser.controllers;

import java.util.Collection;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.damianwajser.builders.OptionBuilder;
import com.github.damianwajser.model.Endpoint;
import com.github.damianwajser.model.EndpointsEdp;
import com.github.damianwajser.model.abstracts.GenericParameter;

public class AbstractController<E extends GenericParameter> {


//	@GetMapping("/{id}")
//	public E findById(@PathVariable("id") Integer id) {
//		return null;
//
//	}
//
//	@GetMapping("/active")
//	public Iterable<E> findActive() {
//		return null;
//
//	}
//
//	@GetMapping("/inactive")
//	public Iterable<E> findInActive() {
//		return null;
//
//	}
//
//	@GetMapping
//	public Iterable<E> getByCode(@RequestParam(value="lala",required=false) Optional<String> code) {
//		return null;
//	}
//
//	@PostMapping
//	public E post(@RequestBody E e) {
//		return e;
//	}

	@PutMapping("/{id}")
	public E put(@PathVariable("id") Integer id, @RequestBody E inputMode, EndpointsEdp x) {
		return inputMode;
	}

//	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
//	@CacheEvict(allEntries = true)
//	public void delete(@PathVariable Integer id) {
//	}

}
