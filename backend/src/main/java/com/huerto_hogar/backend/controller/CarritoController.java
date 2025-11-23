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
@CrossOrigin(origins = "*") // Permite que React (Web) y Android consuman la API sin bloqueos
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    // 1. Ver el carrito de un usuario
    // GET /api/carrito/{uid}
    @GetMapping("/{uid}")
    public ResponseEntity<List<CarritoItem>> verCarrito(@PathVariable String uid) {
        List<CarritoItem> items = carritoService.obtenerCarrito(uid);
        return ResponseEntity.ok(items);
    }

    // 2. Agregar producto al carrito
    // POST /api/carrito/agregar
    // Recibe JSON: { "uid": "A1B2...", "productoId": 10, "cantidad": 2 }
    @PostMapping("/agregar")
    public ResponseEntity<?> agregar(@RequestBody SolicitudAgregar solicitud) {
        try {
            CarritoItem item = carritoService.agregarProducto(
                    solicitud.getUid(),
                    solicitud.getProductoId(),
                    solicitud.getCantidad());
            return ResponseEntity.ok(item);
        } catch (RuntimeException e) {
            // Si el producto no existe, devolvemos error 404 o 400
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 3. Eliminar un item específico (ej: Sacar solo las Bananas)
    // DELETE /api/carrito/item/{id}
    @DeleteMapping("/item/{id}")
    public ResponseEntity<Void> eliminarItem(@PathVariable Long id) {
        carritoService.eliminarItem(id);
        return ResponseEntity.noContent().build();
    }

    // 4. Vaciar todo el carrito (ej: Botón "Limpiar Carro")
    // DELETE /api/carrito/{uid}
    @DeleteMapping("/{uid}")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable String uid) {
        carritoService.vaciarCarrito(uid);
        return ResponseEntity.noContent().build();
    }

    // --- DTO AUXILIAR ---
    // Esta clase sirve solo para "atrapar" el JSON que envía el celular/web
    @Data
    static class SolicitudAgregar {
        private String uid; // Quién agrega
        private Long productoId; // Qué agrega
        private int cantidad; // Cuántos agrega
    }
}
