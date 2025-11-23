package com.huerto_hogar.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "usuarios")
public class Usuario {

    @Id
    @Column(length = 128)
    private String uid;

    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String direccion;

    private String rol;

    @PrePersist
    public void antesDeGuardar() {
        if (this.rol == null || this.rol.isEmpty()) {
            this.rol = "usuario";
        }
    }
}