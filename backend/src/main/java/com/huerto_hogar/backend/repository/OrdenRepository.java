package com.huerto_hogar.backend.repository;

import com.huerto_hogar.backend.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrdenRepository extends JpaRepository<Orden, Long> {

    // Historial de un usuario ordenado por fecha (más reciente primero)
    List<Orden> findByUsuarioUidOrderByFechaDesc(String usuarioUid);
}