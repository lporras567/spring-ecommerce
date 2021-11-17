package com.pormarweb.ecommerce.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pormarweb.ecommerce.model.DetalleOrden;
import com.pormarweb.ecommerce.model.Orden;
import com.pormarweb.ecommerce.model.Producto;
import com.pormarweb.ecommerce.service.ProductoService;

@Controller
@RequestMapping("/")
public class HomeController { 
	
	private final Logger log = LoggerFactory.getLogger(HomeController.class);
		
	@Autowired
	private ProductoService productoService;
	
	// Para almacenar los detalls de la orden
	List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();
	
	// Para almacenar datos de la orden
	Orden orden = new Orden();
	
	@GetMapping("")
	public String home(Model model) {
		
		model.addAttribute("productos", productoService.findAll());
		
		return "usuario/home";
	}
	
	@GetMapping("productohome/{id}")
	public String productoHome(@PathVariable Integer id, Model model) {
		
		log.info("id producto enviado como parametro {}", id);
		
		Producto producto = new Producto();
		Optional<Producto> productoOptional = productoService.get(id);
		producto = productoOptional.get();
		
		model.addAttribute("producto", producto);
		
		return "usuario/productohome";
		
	}
	
	@PostMapping("/cart")
	public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model) {
		
		DetalleOrden detalleOrden = new DetalleOrden();
		Producto producto = new Producto();
		double sumaTotal = 0;
		
    	Optional<Producto> optionalProducto = productoService.get(id);
		log.info("Producto añadido {}", optionalProducto);
		log.info("Cantidad {}", cantidad);
		producto = optionalProducto.get();
		
		detalleOrden.setCantidad(cantidad);
		detalleOrden.setPrecio(producto.getPrecio());
		detalleOrden.setNombre(producto.getNombre());
		detalleOrden.setTotal(cantidad*producto.getPrecio());
		detalleOrden.setProducto(producto);
		
		detalles.add(detalleOrden);
		
		//Función lambda con función anonima para sumar todos los totales que tenga esa lista
		sumaTotal=detalles.stream().mapToDouble(dt->dt.getTotal()).sum();
		
		orden.setTotal(sumaTotal);
		
		// Pasamos a model los valores a pasar a la vista
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		
		
		return "usuario/carrito";
	}

}
