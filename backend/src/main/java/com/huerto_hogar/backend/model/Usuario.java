package com.huerto_hogar.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    @Builder.Default
    private String rol = "usuario";
}
