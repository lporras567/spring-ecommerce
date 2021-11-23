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
import com.pormarweb.ecommerce.model.Usuario;
import com.pormarweb.ecommerce.service.IProductoService;
import com.pormarweb.ecommerce.service.IUsuarioService;

@Controller
@RequestMapping("/")
public class HomeController {

	private final Logger log = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private IProductoService productoService;
	
	@Autowired
	private IUsuarioService usuarioService;

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

	// Agregar un producto al carrito
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
		detalleOrden.setTotal(cantidad * producto.getPrecio());
		detalleOrden.setProducto(producto);
		
		// Validar que el producto no se añada más de una vez
		Integer idProducto=producto.getId();				
		boolean ingresado=detalles.stream().anyMatch(p -> p.getProducto().getId() == idProducto);
		if (!ingresado)
			detalles.add(detalleOrden);

		// Función lambda con función anonima para sumar todos los totales que tenga esa
		// lista
		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();

		orden.setTotal(sumaTotal);

		// Pasamos a model los valores a pasar a la vista
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);

		return "usuario/carrito";
	}

	// Quitar un producto del carrito
	@GetMapping("/delete/cart/{id}")
	public String delCart(@PathVariable Integer id, Model model) {

		// Declara la Lista nueva de productos en el carrito
		List<DetalleOrden> ordenesNueva = new ArrayList<DetalleOrden>();

		for (DetalleOrden detalleOrden : detalles)
			if (detalleOrden.getProducto().getId() != id)
				ordenesNueva.add(detalleOrden);

		// Asignamos a detalles las nuevas ordenes que no incluyen el id eliminado
		detalles = ordenesNueva;

		double sumaTotal = 0;

		// Función lambda con función anonima para sumar todos los totales que tenga esa
		// lista
		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();

		orden.setTotal(sumaTotal);

		// Pasamos a model los  valores a pasar a la vista
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);

		return "usuario/carrito";
	}
	
	@GetMapping("/getCart")
	public String getCart(Model model) {
		
		// Pasamos a model los  valores a pasar a la vista
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		
		return "/usuario/carrito";
	}
	
	@GetMapping("/order")
	public String order(Model model) {

		Usuario usuario = usuarioService.findById(1).get();
		
		// Pasamos a model los  valores a pasar a la vista
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		model.addAttribute("usuario", usuario);
		
		
		return "usuario/resumenorden";
	}

}
