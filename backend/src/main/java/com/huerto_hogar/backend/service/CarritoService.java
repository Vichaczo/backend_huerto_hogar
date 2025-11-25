package com.huerto_hogar.backend.service;

import com.huerto_hogar.backend.model.CarritoItem;
import com.huerto_hogar.backend.model.Producto;
import com.huerto_hogar.backend.repository.CarritoRepository;
import com.huerto_hogar.backend.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        // 1. Buscamos el producto en la base de datos (Inventario)
        Producto producto = productoRepo.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productoId));

        // 2. Buscamos si el usuario ya tiene este item en su carrito
        Optional<CarritoItem> itemExistente = carritoRepo.findByUsuarioUidAndProductoId(uid, productoId);

        if (itemExistente.isPresent()) {
            // --- ESCENARIO A: EL PRODUCTO YA ESTABA EN EL CARRITO ---
            CarritoItem item = itemExistente.get();

            // Calculamos cuánto quedaría (Lo que tenía + lo que mandaste)
            // Ej: Tenía 5, mando -1 = 4.
            // Ej: Tenía 1, mando -1 = 0.
            int nuevaCantidadTotal = item.getCantidad() + cantidad;

            // 3. REGLA DE BORRADO: Si llega a 0 o menos, lo sacamos del carrito
            if (nuevaCantidadTotal <= 0) {
                carritoRepo.delete(item);
                return null; // Retornamos null para avisar al controller
            }

            // 4. REGLA DE STOCK: Solo validamos stock si estamos SUMANDO cosas
            if (cantidad > 0) {
                if (nuevaCantidadTotal > producto.getStock()) {
                    throw new RuntimeException("Stock insuficiente. Tienes " + item.getCantidad() +
                            " en el carro y solo quedan " + producto.getStock() + " en la tienda.");
                }
            }

            // Si todo bien, actualizamos
            item.setCantidad(nuevaCantidadTotal);
            return carritoRepo.save(item);

        } else {
            // --- ESCENARIO B: ES UN ITEM NUEVO ---

            // No puedes agregar negativos si no existe
            if (cantidad <= 0) {
                throw new RuntimeException("La cantidad inicial debe ser mayor a 0");
            }

            // Verificamos si hay stock suficiente en la tienda
            if (cantidad > producto.getStock()) {
                throw new RuntimeException("Stock insuficiente. Solicitaste " + cantidad +
                        " pero solo quedan " + producto.getStock() + " unidades.");
            }

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