package com.pormarweb.ecommerce.service;

import java.util.Optional;

import com.pormarweb.ecommerce.model.Usuario;

public interface IUsuarioService {
	
	// Definir los métodos
	// 1. Un método para obtener un usuario de la base de datos
	Optional<Usuario> findById(Integer id);

}
