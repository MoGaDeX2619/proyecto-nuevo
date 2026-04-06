<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sistema de Gestión de Productos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .sidebar {
            min-height: 100vh;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        .card {
            border: none;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }
        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border: none;
        }
        .table-hover tbody tr:hover {
            background-color: #f8f9fa;
        }
        .loading {
            display: none;
            text-align: center;
            padding: 20px;
        }
    </style>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <nav class="col-md-3 col-lg-2 d-md-block sidebar collapse">
                <div class="position-sticky pt-3">
                    <div class="text-center mb-4">
                        <h4 class="text-white">
                            <i class="fas fa-box"></i> Productos
                        </h4>
                    </div>
                    <ul class="nav flex-column">
                        <li class="nav-item">
                            <a class="nav-link text-white active" href="#" onclick="showSection('productos')">
                                <i class="fas fa-box"></i> Productos
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white" href="#" onclick="showSection('inventario')">
                                <i class="fas fa-warehouse"></i> Inventario
                            </a>
                        </li>
                    </ul>
                </div>
            </nav>

            <!-- Main content -->
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                    <h1 class="h2">Sistema de Gestión</h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <button type="button" class="btn btn-sm btn-outline-secondary" onclick="refreshData()">
                            <i class="fas fa-sync-alt"></i> Actualizar
                        </button>
                    </div>
                </div>

                <!-- Sección de Productos -->
                <div id="productos-section" class="content-section">
                    <div class="card mb-4">
                        <div class="card-header d-flex justify-content-between align-items-center">
                            <h5 class="mb-0">
                                <i class="fas fa-box"></i> Gestión de Productos
                            </h5>
                            <button class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#productoModal">
                                <i class="fas fa-plus"></i> Nuevo Producto
                            </button>
                        </div>
                        <div class="card-body">
                            <div class="loading" id="productos-loading">
                                <div class="spinner-border text-primary" role="status">
                                    <span class="visually-hidden">Cargando...</span>
                                </div>
                            </div>
                            <div class="table-responsive">
                                <table class="table table-hover" id="productos-table">
                                    <thead class="table-light">
                                        <tr>
                                            <th>ID</th>
                                            <th>Código</th>
                                            <th>Nombre</th>
                                            <th>Descripción</th>
                                            <th>Precio</th>
                                            <th>Categoría</th>
                                            <th>Stock</th>
                                            <th>Estado</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody id="productos-tbody">
                                        <!-- Los datos se cargarán dinámicamente -->
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Sección de Inventario -->
                <div id="inventario-section" class="content-section" style="display: none;">
                    <div class="card mb-4">
                        <div class="card-header d-flex justify-content-between align-items-center">
                            <h5 class="mb-0">
                                <i class="fas fa-warehouse"></i> Gestión de Inventario
                            </h5>
                            <button class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#inventarioModal">
                                <i class="fas fa-plus"></i> Nuevo Inventario
                            </button>
                        </div>
                        <div class="card-body">
                            <div class="loading" id="inventario-loading">
                                <div class="spinner-border text-primary" role="status">
                                    <span class="visually-hidden">Cargando...</span>
                                </div>
                            </div>
                            <div class="table-responsive">
                                <table class="table table-hover" id="inventario-table">
                                    <thead class="table-light">
                                        <tr>
                                            <th>ID</th>
                                            <th>ID Producto</th>
                                            <th>Cantidad</th>
                                            <th>Stock Mínimo</th>
                                            <th>Stock Máximo</th>
                                            <th>Último Movimiento</th>
                                            <th>Tipo Movimiento</th>
                                            <th>Ubicación</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody id="inventario-tbody">
                                        <!-- Los datos se cargarán dinámicamente -->
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <!-- Modal Producto -->
    <div class="modal fade" id="productoModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Nuevo Producto</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form id="productoForm">
                        <div class="mb-3">
                            <label for="codigo" class="form-label">Código</label>
                            <input type="text" class="form-control" id="codigo" required>
                        </div>
                        <div class="mb-3">
                            <label for="nombre" class="form-label">Nombre</label>
                            <input type="text" class="form-control" id="nombre" required>
                        </div>
                        <div class="mb-3">
                            <label for="descripcion" class="form-label">Descripción</label>
                            <textarea class="form-control" id="descripcion" rows="3"></textarea>
                        </div>
                        <div class="mb-3">
                            <label for="precio" class="form-label">Precio</label>
                            <input type="number" class="form-control" id="precio" step="0.01" required>
                        </div>
                        <div class="mb-3">
                            <label for="categoria" class="form-label">Categoría</label>
                            <input type="text" class="form-control" id="categoria" required>
                        </div>
                        <div class="mb-3">
                            <label for="stock" class="form-label">Stock</label>
                            <input type="number" class="form-control" id="stock" required>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                    <button type="button" class="btn btn-primary" onclick="saveProducto()">Guardar</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal Inventario -->
    <div class="modal fade" id="inventarioModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Nuevo Inventario</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form id="inventarioForm">
                        <div class="mb-3">
                            <label for="productoId" class="form-label">ID Producto</label>
                            <input type="number" class="form-control" id="productoId" required>
                        </div>
                        <div class="mb-3">
                            <label for="cantidad" class="form-label">Cantidad</label>
                            <input type="number" class="form-control" id="cantidad" required>
                        </div>
                        <div class="mb-3">
                            <label for="stockMinimo" class="form-label">Stock Mínimo</label>
                            <input type="number" class="form-control" id="stockMinimo">
                        </div>
                        <div class="mb-3">
                            <label for="stockMaximo" class="form-label">Stock Máximo</label>
                            <input type="number" class="form-control" id="stockMaximo">
                        </div>
                        <div class="mb-3">
                            <label for="ubicacion" class="form-label">Ubicación</label>
                            <input type="text" class="form-control" id="ubicacion">
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                    <button type="button" class="btn btn-primary" onclick="saveInventario()">Guardar</button>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        const API_BASE = '/servlet_productos/api';

        function showSection(section) {
            document.querySelectorAll('.content-section').forEach(s => s.style.display = 'none');
            document.querySelectorAll('.nav-link').forEach(l => l.classList.remove('active'));
            
            document.getElementById(section + '-section').style.display = 'block';
            event.target.classList.add('active');
            
            if (section === 'productos') {
                loadProductos();
            } else if (section === 'inventario') {
                loadInventario();
            }
        }

        function loadProductos() {
            document.getElementById('productos-loading').style.display = 'block';
            
            fetch(`${API_BASE}/productos`)
                .then(response => response.json())
                .then(data => {
                    const tbody = document.getElementById('productos-tbody');
                    tbody.innerHTML = '';
                    
                    data.forEach(producto => {
                        const row = tbody.insertRow();
                        row.innerHTML = `
                            <td>${producto.id}</td>
                            <td>${producto.codigo}</td>
                            <td>${producto.nombre}</td>
                            <td>${producto.descripcion || ''}</td>
                            <td>$${producto.precio}</td>
                            <td>${producto.categoria}</td>
                            <td>${producto.stock}</td>
                            <td>
                                <span class="badge ${producto.activo ? 'bg-success' : 'bg-danger'}">
                                    ${producto.activo ? 'Activo' : 'Inactivo'}
                                </span>
                            </td>
                            <td>
                                <button class="btn btn-sm btn-warning" onclick="editProducto(${producto.id})">
                                    <i class="fas fa-edit"></i>
                                </button>
                                <button class="btn btn-sm btn-danger" onclick="deleteProducto(${producto.id})">
                                    <i class="fas fa-trash"></i>
                                </button>
                            </td>
                        `;
                    });
                    
                    document.getElementById('productos-loading').style.display = 'none';
                })
                .catch(error => {
                    console.error('Error:', error);
                    document.getElementById('productos-loading').style.display = 'none';
                });
        }

        function loadInventario() {
            document.getElementById('inventario-loading').style.display = 'block';
            
            fetch(`${API_BASE}/inventario`)
                .then(response => response.json())
                .then(data => {
                    const tbody = document.getElementById('inventario-tbody');
                    tbody.innerHTML = '';
                    
                    data.forEach(inventario => {
                        const row = tbody.insertRow();
                        row.innerHTML = `
                            <td>${inventario.id}</td>
                            <td>${inventario.productoId}</td>
                            <td>${inventario.cantidad}</td>
                            <td>${inventario.stockMinimo}</td>
                            <td>${inventario.stockMaximo}</td>
                            <td>${new Date(inventario.fechaUltimoMovimiento).toLocaleString()}</td>
                            <td>${inventario.tipoMovimiento}</td>
                            <td>${inventario.ubicacion}</td>
                            <td>
                                <button class="btn btn-sm btn-warning" onclick="editInventario(${inventario.id})">
                                    <i class="fas fa-edit"></i>
                                </button>
                                <button class="btn btn-sm btn-danger" onclick="deleteInventario(${inventario.id})">
                                    <i class="fas fa-trash"></i>
                                </button>
                            </td>
                        `;
                    });
                    
                    document.getElementById('inventario-loading').style.display = 'none';
                })
                .catch(error => {
                    console.error('Error:', error);
                    document.getElementById('inventario-loading').style.display = 'none';
                });
        }

        function saveProducto() {
            const producto = {
                codigo: document.getElementById('codigo').value,
                nombre: document.getElementById('nombre').value,
                descripcion: document.getElementById('descripcion').value,
                precio: parseFloat(document.getElementById('precio').value),
                categoria: document.getElementById('categoria').value,
                stock: parseInt(document.getElementById('stock').value)
            };

            fetch(`${API_BASE}/productos`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(producto)
            })
            .then(response => response.json())
            .then(data => {
                bootstrap.Modal.getInstance(document.getElementById('productoModal')).hide();
                document.getElementById('productoForm').reset();
                loadProductos();
            })
            .catch(error => console.error('Error:', error));
        }

        function saveInventario() {
            const inventario = {
                productoId: parseInt(document.getElementById('productoId').value),
                cantidad: parseInt(document.getElementById('cantidad').value),
                stockMinimo: parseInt(document.getElementById('stockMinimo').value) || 0,
                stockMaximo: parseInt(document.getElementById('stockMaximo').value) || 0,
                ubicacion: document.getElementById('ubicacion').value || 'GENERAL'
            };

            fetch(`${API_BASE}/inventario`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(inventario)
            })
            .then(response => response.json())
            .then(data => {
                bootstrap.Modal.getInstance(document.getElementById('inventarioModal')).hide();
                document.getElementById('inventarioForm').reset();
                loadInventario();
            })
            .catch(error => console.error('Error:', error));
        }

        function deleteProducto(id) {
            if (confirm('¿Está seguro de eliminar este producto?')) {
                fetch(`${API_BASE}/productos/${id}`, {
                    method: 'DELETE'
                })
                .then(() => loadProductos())
                .catch(error => console.error('Error:', error));
            }
        }

        function deleteInventario(id) {
            if (confirm('¿Está seguro de eliminar este inventario?')) {
                fetch(`${API_BASE}/inventario/${id}`, {
                    method: 'DELETE'
                })
                .then(() => loadInventario())
                .catch(error => console.error('Error:', error));
            }
        }

        function refreshData() {
            const activeSection = document.querySelector('.content-section:not([style*="display: none"])');
            if (activeSection && activeSection.id === 'productos-section') {
                loadProductos();
            } else if (activeSection && activeSection.id === 'inventario-section') {
                loadInventario();
            }
        }

        // Cargar datos iniciales
        document.addEventListener('DOMContentLoaded', function() {
            loadProductos();
        });
    </script>
</body>
</html>
