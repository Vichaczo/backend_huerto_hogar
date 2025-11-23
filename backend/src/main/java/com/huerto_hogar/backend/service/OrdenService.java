package com.huerto_hogar.backend.service;

import com.huerto_hogar.backend.model.CarritoItem;
import com.huerto_hogar.backend.model.Orden;
import com.huerto_hogar.backend.repository.OrdenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrdenService {

    private final OrdenRepository ordenRepo;
    private final CarritoService carritoService; // Necesario para obtener los precios y vaciar

    // Importante: Si falla el guardado, no se vacía el carrito (seguridad)
    public Orden generarOrden(String uid) {

        // 1. Obtener los items actuales del carrito
        List<CarritoItem> items = carritoService.obtenerCarrito(uid);

        if (items.isEmpty()) {
            throw new RuntimeException("El carrito está vacío, no se puede realizar la compra.");
        }

        // 2. Calcular el Total Matemático
        double totalCalculado = 0;

        for (CarritoItem item : items) {
            // Precio del producto * Cantidad que lleva
            double subtotal = item.getProducto().getPrecio() * item.getCantidad();
            totalCalculado += subtotal;
        }

        // 3. Crear el objeto Orden solo con los datos básicos
        Orden nuevaOrden = Orden.builder()
                .usuarioUid(uid)
                .fecha(LocalDateTime.now()) // Fecha actual del servidor
                .total(totalCalculado)
                .build();

        // 4. Guardar la Orden en Base de Datos (MySQL)
        Orden ordenGuardada = ordenRepo.save(nuevaOrden);

        // 5. Vaciar el carrito (porque ya se compró)
        carritoService.vaciarCarrito(uid);

        return ordenGuardada;
    }

    // Para ver el historial ("Mis Compras")
    public List<Orden> listarMisCompras(String uid) {
        return ordenRepo.findByUsuarioUidOrderByFechaDesc(uid);
    }
}