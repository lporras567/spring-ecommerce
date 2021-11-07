package com.pormarweb.ecommerce.controller;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pormarweb.ecommerce.model.Producto;
import com.pormarweb.ecommerce.model.Usuario;
import com.pormarweb.ecommerce.service.ProductoService;

@Controller
@RequestMapping("/productos")
public class ProductoController {

	private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);
	
	@Autowired
	private ProductoService productoService;

	@GetMapping("")
	public String show() {
		return "productos/show";
	}

	@GetMapping("/create")
	public String create() {
		return "productos/create";
	}
	
	@PostMapping("/save")
	public String save(Producto producto) {
		LOGGER.info("Este es el contenido del objeto producto {}", producto);
		Usuario u = new Usuario(1, "", "", "", "", "", "", "");
		producto.setUsuario(u);
		LOGGER.info("Este es el contenido del objeto producto u {}", producto);
		productoService.save(producto);
		return "redirect:/productos";
	}
}
