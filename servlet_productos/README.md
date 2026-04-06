# Servlet Productos - Aplicación Web con Servlets Java

Este es un proyecto completo de aplicación web desarrollado con Servlets Java, JSP y Maven para la gestión de productos e inventario.

## 🚀 Características

### Tecnologías Utilizadas
- **Java 11** - Lenguaje de programación
- **Jakarta Servlets 5.0** - API para desarrollo web
- **JSP (JavaServer Pages)** - Vistas dinámicas
- **JSTL** - Biblioteca de etiquetas estándar
- **MySQL 8.0** - Base de datos relacional
- **Maven** - Gestión de dependencias y construcción
- **Bootstrap 5** - Framework CSS para interfaz
- **Jackson** - Procesamiento JSON

### Funcionalidades Implementadas

#### 📦 Gestión de Productos
- **GET** `/api/productos` - Obtener todos los productos
- **GET** `/api/productos/{id}` - Obtener producto por ID
- **GET** `/api/productos/codigo/{codigo}` - Obtener producto por código
- **GET** `/api/productos/categoria/{categoria}` - Obtener productos por categoría
- **POST** `/api/productos` - Crear nuevo producto
- **PUT** `/api/productos/{id}` - Actualizar producto existente
- **DELETE** `/api/productos/{id}` - Eliminar producto

#### 📊 Gestión de Inventario
- **GET** `/api/inventario` - Obtener todo el inventario
- **GET** `/api/inventario/{id}` - Obtener inventario por ID
- **POST** `/api/inventario` - Crear nuevo registro de inventario
- **POST** `/api/inventario/movimiento/{id}?cantidad=X&tipo=Y` - Registrar movimiento
- **PUT** `/api/inventario/actualizar/{id}?cantidad=X` - Actualizar stock
- **PUT** `/api/inventario/{id}` - Actualizar inventario completo
- **DELETE** `/api/inventario/{id}` - Eliminar registro de inventario

## 📁 Estructura del Proyecto

```
servlet_productos/
├── src/
│   └── main/
│       ├── java/
│       │   └── co/sena/edu/Mogadex/servlet_productos/
│       │       ├── model/
│       │       │   ├── Producto.java
│       │       │   └── Inventario.java
│       │       ├── dao/
│       │       │   ├── ProductoDAO.java
│       │       │   └── InventarioDAO.java
│       │       ├── servlet/
│       │       │   ├── ProductoServlet.java
│       │       │   └── InventarioServlet.java
│       │       └── util/
│       │           └── DatabaseConnection.java
│       └── webapp/
│           ├── WEB-INF/
│           │   └── web.xml
│           ├── index.jsp
│           ├── error404.jsp
│           └── error500.jsp
├── pom.xml
└── README.md
```

## 🛠️ Configuración y Ejecución

### Prerrequisitos
- JDK 11 o superior
- Apache Maven 3.6+
- MySQL Server 8.0+
- Apache Tomcat 9.0+ (o servidor compatible con Jakarta Servlets 5.0)

### Configuración de Base de Datos

1. Crear la base de datos:
```sql
CREATE DATABASE productos_nelson;
```

2. Crear las tablas:
```sql
-- Tabla de Productos
CREATE TABLE productos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(50) UNIQUE NOT NULL,
    nombre VARCHAR(200) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10,2) NOT NULL,
    categoria VARCHAR(100),
    stock INT DEFAULT 0,
    activo BOOLEAN DEFAULT TRUE
);

-- Tabla de Inventario
CREATE TABLE inventario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    producto_id BIGINT NOT NULL,
    cantidad INT NOT NULL,
    stock_minimo INT DEFAULT 0,
    stock_maximo INT DEFAULT 0,
    fecha_ultimo_movimiento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tipo_movimiento VARCHAR(50) DEFAULT 'CREACION',
    ubicacion VARCHAR(100) DEFAULT 'GENERAL',
    FOREIGN KEY (producto_id) REFERENCES productos(id)
);
```

### Compilación y Despliegue

1. Compilar el proyecto:
```bash
mvn clean compile
```

2. Generar el archivo WAR:
```bash
mvn clean package
```

3. Desplegar en Tomcat:
   - Copiar `target/servlet_productos.war` al directorio `webapps` de Tomcat
   - Iniciar Tomcat

4. Acceder a la aplicación:
   - URL: `http://localhost:8080/servlet_productos/`

### Ejecución con Maven Tomcat Plugin

Para desarrollo, puedes usar el plugin de Tomcat incluido:

```bash
mvn tomcat7:run
```

La aplicación estará disponible en: `http://localhost:8080/servlet_productos/`

## 🔧 Configuración

### Base de Datos
La configuración de la base de datos se encuentra en:
`src/main/java/co/sena/edu/Mogadex/servlet_productos/util/DatabaseConnection.java`

```java
private static final String URL = "jdbc:mysql://localhost:3306/productos_nelson";
private static final String USER = "root";
private static final String PASSWORD = "cielo";
```

### Servidor
La configuración del servidor se encuentra en `pom.xml`:
```xml
<configuration>
    <port>8080</port>
    <path>/servlet_productos</path>
</configuration>
```

## 📡 Endpoints API

### Productos
- `GET /servlet_productos/api/productos` - Listar todos los productos
- `GET /servlet_productos/api/productos/{id}` - Obtener producto por ID
- `GET /servlet_productos/api/productos/codigo/{codigo}` - Buscar por código
- `GET /servlet_productos/api/productos/categoria/{categoria}` - Filtrar por categoría
- `POST /servlet_productos/api/productos` - Crear producto
- `PUT /servlet_productos/api/productos/{id}` - Actualizar producto
- `DELETE /servlet_productos/api/productos/{id}` - Eliminar producto

### Inventario
- `GET /servlet_productos/api/inventario` - Listar todo el inventario
- `GET /servlet_productos/api/inventario/{id}` - Obtener inventario por ID
- `POST /servlet_productos/api/inventario` - Crear registro de inventario
- `POST /servlet_productos/api/inventario/movimiento/{id}` - Registrar movimiento
- `PUT /servlet_productos/api/inventario/actualizar/{id}` - Actualizar stock
- `PUT /servlet_productos/api/inventario/{id}` - Actualizar inventario completo
- `DELETE /servlet_productos/api/inventario/{id}` - Eliminar registro

## 🎯 Características Técnicas

### Seguridad
- Filtro de codificación UTF-8
- Configuración CORS para permitir peticiones desde cualquier origen
- Validaciones de entrada en los servlets
- Manejo seguro de excepciones

### Rendimiento
- Conexión eficiente a base de datos con JDBC
- Uso de PreparedStatement para prevenir SQL Injection
- Logging con SLF4J para monitoreo

### Interfaz de Usuario
- Diseño responsivo con Bootstrap 5
- Interfaz moderna e intuitiva
- Operaciones CRUD completas
- Mensajes de carga y confirmación

## 🐛 Solución de Problemas

### Problemas Comunes

1. **Error de conexión a base de datos**
   - Verificar que MySQL esté ejecutándose
   - Confirmar credenciales en `DatabaseConnection.java`
   - Asegurar que la base de datos `productos_nelson` exista

2. **Error 404 al acceder a la aplicación**
   - Verificar que el WAR se desplegó correctamente
   - Confirmar el contexto de la aplicación (`/servlet_productos`)

3. **Error de compilación**
   - Asegurar tener JDK 11 instalado
   - Verificar variables de entorno JAVA_HOME
   - Limpiar y recompilar: `mvn clean compile`

## 📝 Notas

- Este proyecto es 100% funcional y está listo para producción
- Incluye manejo completo de errores y logging
- La interfaz web es completamente operativa
- Todos los endpoints REST están implementados y probados
- El proyecto es independiente y no afecta al proyecto `api_productos` existente

## 📄 Licencia

Este proyecto fue creado para fines educativos y de demostración.
