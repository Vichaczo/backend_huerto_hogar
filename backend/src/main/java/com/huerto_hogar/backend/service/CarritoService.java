package com.huerto_hogar.backend.service;

import com.huerto_hogar.backend.model.CarritoItem;
import com.huerto_hogar.backend.model.Producto;
import com.huerto_hogar.backend.repository.CarritoRepository;
import com.huerto_hogar.backend.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importante para deletes

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CarritoService {

    private final CarritoRepository carritoRepo;
    private final ProductoRepository productoRepo;

    public List<CarritoItem> obtenerCarrito(String uid) {
        return carritoRepo.findByUsuarioUid(uid);
    }

    public CarritoItem agregarProducto(String uid, Long productoId, int cantidad) {
        // 1. Buscamos si ya existe ese producto en el carro de ese usuario
        Optional<CarritoItem> itemExistente = carritoRepo.findByUsuarioUidAndProductoId(uid, productoId);

        if (itemExistente.isPresent()) {
            // SI EXISTE: Actualizamos la cantidad
            CarritoItem item = itemExistente.get();
            item.setCantidad(item.getCantidad() + cantidad);
            return carritoRepo.save(item);
        } else {
            // NO EXISTE: Creamos nuevo item
            Producto producto = productoRepo.findById(productoId)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            CarritoItem nuevo = CarritoItem.builder()
                    .usuarioUid(uid)
                    .producto(producto)
                    .cantidad(cantidad)
                    .build();
            return carritoRepo.save(nuevo);
        }
    }

    public void eliminarItem(Long idCarritoItem) {
        carritoRepo.deleteById(idCarritoItem);
    }

    public void vaciarCarrito(String uid) {
        carritoRepo.deleteByUsuarioUid(uid);
    }
}