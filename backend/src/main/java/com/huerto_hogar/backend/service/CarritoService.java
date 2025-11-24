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

        if (cantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a 0");
        }
        // Se busca el producto en la base de datos para ver su stock
        Producto producto = productoRepo.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productoId));

        // Se busca si el usuario ya tiene este item en su carrito
        Optional<CarritoItem> itemExistente = carritoRepo.findByUsuarioUidAndProductoId(uid, productoId);

        if (itemExistente.isPresent()) {
            // --- CASO A: EL PRODUCTO YA ESTABA EN EL CARRITO ---
            CarritoItem item = itemExistente.get();

            // Calculamos cuánto se quiere tener en total (lo que tenía + lo nuevo)
            int nuevaCantidadTotal = item.getCantidad() + cantidad;

            // Verificamos el stock de la tienda
            if (nuevaCantidadTotal > producto.getStock()) {
                throw new RuntimeException("No quedan existencias suficientes. Tienes " + item.getCantidad() +
                        ", pero el stock total es " + producto.getStock());
            }

            // Si hay stock, se actualizan la cantidad
            item.setCantidad(nuevaCantidadTotal);
            return carritoRepo.save(item);

        } else {
            // --- CASO B: ES UN ITEM NUEVO PARA ESTE USUARIO ---

            // Verificamos si hay stock inicial suficiente
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