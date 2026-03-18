package co.sena.edu.Mogadex.api_productos.api_productos.service;

import jakarta.persistence.*;

@Entity
@Table(name = "productos")
public class ProductoService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "Máximo 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;
    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    private Double precio;
    @Column(length = 255)
    private String descripcion;
// IntelliJ: Alt+Insert → Getters and Setters
}
