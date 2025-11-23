package com.huerto_hogar.backend.controller;

import com.huerto_hogar.backend.model.Orden;
import com.huerto_hogar.backend.service.OrdenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordenes")
@CrossOrigin(origins = "*") // Vital para que React y Android puedan conectarse
@RequiredArgsConstructor
public class OrdenController {

    private final OrdenService ordenService;

    // 1. Finalizar Compra (Generar Orden y Vaciar Carrito)
    // POST /api/ordenes/comprar/{uid}
    @PostMapping("/comprar/{uid}")
    public ResponseEntity<?> comprar(@PathVariable String uid) {
        try {
            // Intentamos generar la orden
            Orden orden = ordenService.generarOrden(uid);
            return ResponseEntity.ok(orden);
        } catch (RuntimeException e) {
            // Si el servicio falla (ej: "Carrito vacío"), devolvemos un error 400 Bad
            // Request
            // con el mensaje del error para mostrarlo en la App (Toast/Snackbar)
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2. Ver Historial de Compras ("Mis Pedidos")
    // GET /api/ordenes/{uid}
    @GetMapping("/{uid}")
    public ResponseEntity<List<Orden>> historial(@PathVariable String uid) {
        List<Orden> compras = ordenService.listarMisCompras(uid);
        return ResponseEntity.ok(compras);
    }
}