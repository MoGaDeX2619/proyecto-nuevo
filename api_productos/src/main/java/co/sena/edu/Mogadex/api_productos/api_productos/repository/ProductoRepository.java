package co.sena.edu.Mogadex.api_productos.api_productos.repository;

import com.sena.productosapi.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface ProductoRepository
        extends JpaRepository<Producto, Long> {
    // Spring Data genera el SQL por el nombre del método:
// SELECT * FROM productos WHERE nombre LIKE '%?%'
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    // SELECT * FROM productos WHERE precio < ?
    List<Producto> findByPrecioLessThan(Double precio);
// JpaRepository ya incluye sin código adicional:
// save(p) → INSERT o UPDATE
// findAll() → SELECT *
// findById(id) → SELECT WHERE id=?
// deleteById(id) → DELETE WHERE id=?
// count() → SELECT COUNT(*)
}
