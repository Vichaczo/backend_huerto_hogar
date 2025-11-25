package com.huerto_hogar.backend.controller;

import com.huerto_hogar.backend.model.CarritoItem;
import com.huerto_hogar.backend.service.CarritoService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carrito")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    // Ver el carrito de un usuario
    // GET /api/carrito/{uid}
    @GetMapping("/{uid}")
    public ResponseEntity<List<CarritoItem>> verCarrito(@PathVariable String uid) {
        List<CarritoItem> items = carritoService.obtenerCarrito(uid);
        return ResponseEntity.ok(items);
    }

    // Agregar o Restar producto
    // POST /api/carrito/agregar
    // Body: { "uid": "...", "productoId": 1, "cantidad": 1 } -> Suma 1
    @PostMapping("/agregar")
    public ResponseEntity<?> agregar(@RequestBody SolicitudAgregar solicitud) {
        try {
            CarritoItem item = carritoService.agregarProducto(
                    solicitud.getUid(),
                    solicitud.getProductoId(),
                    solicitud.getCantidad());

            // Si devuelve null significa que el item se borró (llegó a 0)
            if (item == null) {
                return ResponseEntity.ok().body("Item eliminado del carrito");
            }
            return ResponseEntity.ok(item);
        } catch (RuntimeException e) {
            // Si hay error de stock o validación, devolvemos 400 Bad Request
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Eliminar un item específico
    @DeleteMapping("/item/{id}")
    public ResponseEntity<Void> eliminarItem(@PathVariable Long id) {
        carritoService.eliminarItem(id);
        return ResponseEntity.noContent().build();
    }

    // Vaciar carrito completo (Al comprar)
    @DeleteMapping("/{uid}")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable String uid) {
        carritoService.vaciarCarrito(uid);
        return ResponseEntity.noContent().build();
    }

    // DTO Auxiliar
    @Data
    static class SolicitudAgregar {
        private String uid;
        private Long productoId;
        private int cantidad;
    }
}