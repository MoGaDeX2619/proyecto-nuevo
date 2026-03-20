package co.sena.edu.mogadex.api_productos.api_productos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import co.sena.edu.mogadex.api_productos.api_productos.model.Inventario;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {
}