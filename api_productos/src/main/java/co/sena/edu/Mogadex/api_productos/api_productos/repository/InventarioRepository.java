package co.sena.edu.Mogadex.api_productos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import co.sena.edu.Mogadex.api_productos.model.Inventario;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {
}