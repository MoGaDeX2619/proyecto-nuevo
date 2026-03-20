package co.sena.edu.mogadex.api_productos.api_productos.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Min(value = 0, message = "La cantidad actual no puede ser negativa")
    @Column(nullable = false)
    private int cantidadActual;

    //@NotNull(message = "La cantidad mínima es obligatoria")
    //@Min(value = 0, message = "La cantidad mínima no puede ser negativa")
    @Column(nullable = false)
    private int cantidadMinima;

    @Column(nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime fechaActualizacion;

    @Column(nullable = false, length = 20)
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    // Getters y Setters
    public Long getId() { return id; }

    public int getCantidadActual() { return cantidadActual; }
    public void setCantidadActual(int cantidadActual) { this.cantidadActual = cantidadActual; }

    public int getCantidadMinima() { return cantidadMinima; }
    public void setCantidadMinima(int cantidadMinima) { this.cantidadMinima = cantidadMinima; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
}