package com.huerto_hogar.backend.service;

import com.huerto_hogar.backend.model.Producto;
import com.huerto_hogar.backend.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepo;

    public List<Producto> listarTodos() {
        return productoRepo.findAll();
    }

    public List<Producto> listarPorCategoria(String categoria) {
        return productoRepo.findByCategoria(categoria);
    }

    public Producto obtenerPorId(Long id) {
        return productoRepo.findById(id).orElse(null);
    }

    public Producto guardar(Producto p) {
        return productoRepo.save(p);
    }

    public void eliminar(Long id) {
        productoRepo.deleteById(id);
    }

    public void actualizarStock(Long id, int nuevoStock) {
        Producto producto = productoRepo.findById(id).orElse(null);
        if (producto != null) {
            producto.setStock(nuevoStock);
            productoRepo.save(producto);
        }
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepo.findByNombreContainingIgnoreCase(nombre);
    }
}
