package com.pormarweb.ecommerce.controller;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.pormarweb.ecommerce.model.Producto;
import com.pormarweb.ecommerce.model.Usuario;
import com.pormarweb.ecommerce.service.IProductoService;
import com.pormarweb.ecommerce.service.UploadFileService;

@Controller
@RequestMapping("/productos")
public class ProductoController {

	private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);
	
	@Autowired
	private IProductoService productoService;
	
	@Autowired
	private UploadFileService upload;
 
	@GetMapping("")
	public String show(Model model) {
		model.addAttribute("productos", productoService.findAll());
		return "productos/show";
	}

	@GetMapping("/create")
	public String create() {
		return "productos/create";
	}
	
	@PostMapping("/save")
	public String save(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
		LOGGER.info("Este es el contenido del objeto producto en save {}", producto);
		Usuario u = new Usuario(1, "", "", "", "", "", "", "");
		producto.setUsuario(u);
		
		if (producto.getId()==null) { // Cuando se crea un producto
			
			String nombreImagen = upload.saveImagen(file);
			producto.setImagen(nombreImagen);
			
		} else { 

			
		
		}
		
		LOGGER.info("Este es el contenido del objeto producto u {}", producto);
		productoService.save(producto);
		
		return "redirect:/productos";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Integer id, Model model) {
		
		
		Producto producto = new Producto();
		Optional<Producto> optionalProducto = productoService.get(id);
		producto=optionalProducto.get();
		
		LOGGER.info("Producto en edit {}", producto);
		
		model.addAttribute("producto", producto);
		
		return "productos/edit";
	}
	
	@PostMapping("/update")
	public String update(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
		
		Producto p = new Producto();
		p = productoService.get(producto.getId()).get();
		
		if (file.isEmpty()) {  // Cuando se edita el producto y la imagen no cambia
			
			producto.setImagen(p.getImagen());
			
		} else { // Cuando se edita el producto y la imagen cambia
			

			
			// Eliminar cuando no sea la imagen por defecto
			if (!(p.getImagen().equals("default.jpg")))
				upload.deleteImagen(p.getImagen());
			
			String nombreImagen = upload.saveImagen(file);
			producto.setImagen(nombreImagen);
			
		}
		
		//Usuario u = new Usuario(1, "", "", "", "", "", "", "");
		producto.setUsuario(p.getUsuario());
		
		productoService.update(producto);
		return "redirect:/productos";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id) {
		
		Producto p = new Producto();
		
		p = productoService.get(id).get();
		
		// Eliminar cuando no sea la imagen por defecto
		if (!(p.getImagen().equals("default.jpg")))
			upload.deleteImagen(p.getImagen());
		
		productoService.delete(id);
		return "redirect:/productos";
	
	}
	
}
