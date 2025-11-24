package com.huerto_hogar.backend.repository;

import com.huerto_hogar.backend.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Para obtener todos los productos
    List<Producto> findAll();

    // Para obtener productos por categoría
    List<Producto> findByCategoria(String categoria);

    // Para buscar productos por nombre
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

}