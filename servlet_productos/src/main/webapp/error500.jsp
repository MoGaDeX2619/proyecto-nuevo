<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error 500 - Error Interno del Servidor</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .error-template {
            padding: 40px 15px;
            text-align: center;
        }
        .error-actions {
            margin-top: 15px;
            margin-bottom: 15px;
        }
        .error-details {
            margin-top: 20px;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <div class="error-template">
                    <h1 class="display-1">500</h1>
                    <h2>Error Interno del Servidor</h2>
                    <div class="error-details">
                        <p>Ha ocurrido un error inesperado en el servidor.</p>
                        <p>Por favor, intenta nuevamente más tarde o contacta al administrador del sistema.</p>
                    </div>
                    <div class="error-actions">
                        <a href="<%=request.getContextPath()%>/index.jsp" class="btn btn-primary">
                            <i class="fas fa-home"></i> Ir al Inicio
                        </a>
                        <a href="javascript:history.back()" class="btn btn-secondary">
                            <i class="fas fa-arrow-left"></i> Volver
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/js/all.min.js"></script>
</body>
</html>
