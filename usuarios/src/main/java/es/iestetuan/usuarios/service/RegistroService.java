package es.iestetuan.usuarios.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.iestetuan.usuarios.entity.UsuarioEntity;
import es.iestetuan.usuarios.model.Usuario;
import es.iestetuan.usuarios.repository.IUsuarioRepository;

@RestController
@RequestMapping("usuario")
public class RegistroService {
	
	@Autowired
	IUsuarioRepository repository;
	
	@PostMapping
	public void recogerDatosBasicos(@RequestBody Usuario usuario) {
		
		UsuarioEntity usuarioEntity = new UsuarioEntity();
		
		//Datos básicos
		usuarioEntity.setCorreoElectronico(usuario.getCorreoElectronico());
		usuarioEntity.setPass(usuario.getPass());
		
		//Datos personales
		usuarioEntity.setNombre(usuario.getNombre());
		usuarioEntity.setPrimerApellido(usuario.getPrimerApellido());
		usuarioEntity.setSegundoApellido(usuario.getSegundoApellido());
		
		repository.save(usuarioEntity);
	}
	
	@PutMapping
	public ResponseEntity<?> actualizarDatos(@RequestBody Usuario usuario) {
		
		ResponseEntity<?> response = null;
		
		if(repository.existsById(usuario.getCorreoElectronico())) {
			
			UsuarioEntity usuarioEntity = repository.findById(usuario.getCorreoElectronico()).orElse(null);
			BeanUtils.copyProperties(usuario, usuarioEntity);
			
			repository.save(usuarioEntity);
			response = new ResponseEntity<Void>(HttpStatus.OK);
		}
		else {
			response = new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		return response;
	}
	
	@DeleteMapping
	public ResponseEntity<?> borrarDatos(@RequestBody Usuario usuario){
		
		ResponseEntity<?> response = null;
		
		if(repository.existsById(usuario.getCorreoElectronico())) {
			repository.deleteById(usuario.getCorreoElectronico());
			response = new ResponseEntity<Void>(HttpStatus.OK);
		}
		else {
			response = new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		return response;
	}
	
	@GetMapping
	public ResponseEntity<?> getAll() {
		
		List<UsuarioEntity> listaUsuariosEntity = repository.findAll();
		
		ResponseEntity<?> response = null;
		
		if(listaUsuariosEntity.size()>0) {
			
			List<Usuario> usuarios = listaUsuariosEntity.stream().
										map(RegistroService::mapToUsuario).
										collect(Collectors.toList());
			
			response = new ResponseEntity<List<Usuario>>(usuarios, HttpStatus.OK);
		}
		else {
			response = new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		return response;
	}
	
	private static Usuario mapToUsuario(UsuarioEntity entity) {
		Usuario usuario = new Usuario();
		BeanUtils.copyProperties(entity, usuario);
		return usuario;
	}

}
