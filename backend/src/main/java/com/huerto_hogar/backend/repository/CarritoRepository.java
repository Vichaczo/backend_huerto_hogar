package com.huerto_hogar.backend.repository;

import com.huerto_hogar.backend.model.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CarritoRepository extends JpaRepository<CarritoItem, Long> {

    // Para mostrar el carrito en la App/Web
    List<CarritoItem> findByUsuarioUid(String usuarioUid);

    // Para saber si ya agrege ese producto antes (y sumar cantidad en vez de
    // duplicar fila)
    Optional<CarritoItem> findByUsuarioUidAndProductoId(String usuarioUid, Long productoId);

    // Para vaciar el carrito después de comprar
    void deleteByUsuarioUid(String usuarioUid);
}
