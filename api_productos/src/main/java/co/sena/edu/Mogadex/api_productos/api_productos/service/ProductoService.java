package co.sena.edu.mogadex.api_productos.api_productos.service;

import co.sena.edu.mogadex.api_productos.api_productos.model.Producto;
import co.sena.edu.mogadex.api_productos.api_productos.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> getAllProductos() {
        return productoRepository.findByActivoTrue();
    }

    public Optional<Producto> getProductoById(Long id) {
        return productoRepository.findById(id);
    }

    public Optional<Producto> getProductoByCodigo(String codigo) {
        return productoRepository.findByCodigo(codigo);
    }

    public Producto createProducto(Producto producto) {
        if (productoRepository.existsByCodigo(producto.getCodigo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Ya existe un producto con el código: " + producto.getCodigo());
        }
        return productoRepository.save(producto);
    }

    public Producto updateProducto(Long id, Producto productoDetails) {
        return productoRepository.findById(id)
            .map(producto -> {
                // Validar que el código no exista si se está cambiando
                if (!producto.getCodigo().equals(productoDetails.getCodigo()) && 
                    productoRepository.existsByCodigo(productoDetails.getCodigo())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                        "Ya existe un producto con el código: " + productoDetails.getCodigo());
                }
                
                producto.setCodigo(productoDetails.getCodigo());
                producto.setNombre(productoDetails.getNombre());
                producto.setDescripcion(productoDetails.getDescripcion());
                producto.setPrecio(productoDetails.getPrecio());
                producto.setCategoria(productoDetails.getCategoria());
                producto.setActivo(productoDetails.isActivo());
                return productoRepository.save(producto);
            })
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                "Producto no encontrado con ID: " + id));
    }

    public void deleteProducto(Long id) {
        productoRepository.findById(id)
            .map(producto -> {
                producto.setActivo(false);
                productoRepository.save(producto);
                return true;
            })
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                "Producto no encontrado con ID: " + id));
    }

    public List<Producto> getProductosByCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria);
    }
}
