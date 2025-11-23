package com.huerto_hogar.backend.controller;

import com.huerto_hogar.backend.model.Producto;
import com.huerto_hogar.backend.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    // 1. Listar todos
    // GET /api/productos
    @GetMapping
    public ResponseEntity<List<Producto>> listarTodos() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    // 2. Obtener por ID
    // GET /api/productos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        Producto producto = productoService.obtenerPorId(id);
        if (producto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(producto);
    }

    // 3. Filtrar por Categoría
    // GET /api/productos/categoria/{categoria}
    // Ejemplo: /api/productos/categoria/Frutas
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Producto>> listarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(productoService.listarPorCategoria(categoria));
    }

    // 4. Buscar por nombre (NUEVO)
    // GET /api/productos/buscar?nombre=banana
    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(productoService.buscarPorNombre(nombre));
    }

    // 5. Crear o Actualizar Producto Completo (Admin)
    // POST /api/productos
    @PostMapping
    public ResponseEntity<Producto> guardar(@RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.guardar(producto));
    }

    // 6. Actualizar solo el Stock
    // PATCH /api/productos/{id}/stock?cantidad=50
    @PatchMapping("/{id}/stock")
    public ResponseEntity<Void> actualizarStock(@PathVariable Long id, @RequestParam int cantidad) {
        // Verificamos si existe antes de intentar actualizar
        if (productoService.obtenerPorId(id) == null) {
            return ResponseEntity.notFound().build();
        }
        productoService.actualizarStock(id, cantidad);
        return ResponseEntity.ok().build();
    }

    // 7. Eliminar Producto (Admin)
    // DELETE /api/productos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}