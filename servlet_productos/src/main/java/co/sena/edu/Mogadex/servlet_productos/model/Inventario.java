package co.sena.edu.mogadex.servlet_productos.model;

import java.time.LocalDateTime;

public class Inventario {
    private Long id;
    private Long productoId;
    private int cantidad;
    private int stockMinimo;
    private int stockMaximo;
    private LocalDateTime fechaUltimoMovimiento;
    private String tipoMovimiento;
    private String ubicacion;

    public Inventario() {}

    public Inventario(Long id, Long productoId, int cantidad, int stockMinimo, 
                     int stockMaximo, LocalDateTime fechaUltimoMovimiento, 
                     String tipoMovimiento, String ubicacion) {
        this.id = id;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.stockMinimo = stockMinimo;
        this.stockMaximo = stockMaximo;
        this.fechaUltimoMovimiento = fechaUltimoMovimiento;
        this.tipoMovimiento = tipoMovimiento;
        this.ubicacion = ubicacion;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public int getStockMaximo() {
        return stockMaximo;
    }

    public void setStockMaximo(int stockMaximo) {
        this.stockMaximo = stockMaximo;
    }

    public LocalDateTime getFechaUltimoMovimiento() {
        return fechaUltimoMovimiento;
    }

    public void setFechaUltimoMovimiento(LocalDateTime fechaUltimoMovimiento) {
        this.fechaUltimoMovimiento = fechaUltimoMovimiento;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    @Override
    public String toString() {
        return "Inventario{" +
                "id=" + id +
                ", productoId=" + productoId +
                ", cantidad=" + cantidad +
                ", stockMinimo=" + stockMinimo +
                ", stockMaximo=" + stockMaximo +
                ", fechaUltimoMovimiento=" + fechaUltimoMovimiento +
                ", tipoMovimiento='" + tipoMovimiento + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                '}';
    }
}
