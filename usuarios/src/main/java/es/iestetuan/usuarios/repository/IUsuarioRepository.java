package es.iestetuan.usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.iestetuan.usuarios.entity.UsuarioEntity;

public interface IUsuarioRepository extends JpaRepository<UsuarioEntity, String>{

}
