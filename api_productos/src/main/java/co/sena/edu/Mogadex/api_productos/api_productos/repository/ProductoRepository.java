package co.sena.edu.Mogadex.api_productos.repository;

import co.sena.edu.Mogadex.api_productos.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    Optional<Producto> findByCodigo(String codigo);
    
    List<Producto> findByActivoTrue();
    
    List<Producto> findByCategoria(String categoria);
    
    boolean existsByCodigo(String codigo);
}
