package dan.tp2021.productos.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import dan.tp2021.productos.domain.Producto;
import dan.tp2021.productos.domain.Unidad;
import dan.tp2021.productos.services.dao.DetallePedidoRepository;
import dan.tp2021.productos.services.dao.DetalleProvisionRepository;
import dan.tp2021.productos.services.dao.MovimientosStockRepository;
import dan.tp2021.productos.services.dao.ProductoRepository;
import dan.tp2021.productos.services.dao.ProvisionRepository;
import dan.tp2021.productos.services.dao.UnidadRepository;
import dan.tp2021.productos.services.interfaces.ProductoService;

@SpringBootTest
class ProductoServiceImpTest {

	@Autowired
	ProductoService productoService;

	@Autowired
	ProductoRepository productoRepo;

	@Autowired
	UnidadRepository unidadRepo;
	
	@Autowired
	MovimientosStockRepository movStockRepo;

	@Autowired
	DetalleProvisionRepository detalleProvisionRepo;

	@Autowired
	ProvisionRepository provisionRepo;

	@Autowired
	DetallePedidoRepository detallePedidoRepo;


	@BeforeEach
	void limpiarRepositorios() {	
		movStockRepo.deleteAll();
		detalleProvisionRepo.deleteAll();
		provisionRepo.deleteAll();
		detallePedidoRepo.deleteAll();
		productoRepo.deleteAll();
		unidadRepo.deleteAll();
	}

	@Test
	void guardarProducto() {
		Producto producto1 = new Producto();
		producto1.setNombre("Marco de puertas");
		producto1.setDescripcion("Son marcos de metal");
		producto1.setPrecio(5000.0);
		producto1.setStockActual(50);
		producto1.setStockMinimo(10);
		//
		Unidad unidad1 = new Unidad();
		unidad1.setDescripcion("Enteros");
		producto1.setUnidad(unidad1);
		//
		Optional<Producto> optProducto = productoService.guardarProducto(producto1);
		// Verificamos que retorne algo
		assertTrue(optProducto.isPresent());
		// Verificamos que en verdad se guardo
		Optional<Producto> optProdReturn = productoRepo.findById(optProducto.get().getId());
		assertTrue(optProdReturn.isPresent());
	}

	@Test
	void guardar_actualizar_producto() {
		Producto producto1 = new Producto();
		producto1.setNombre("Marco de puertas");
		producto1.setDescripcion("Son marcos de metal");
		producto1.setPrecio(5000.0);
		producto1.setStockActual(50);
		producto1.setStockMinimo(10);
		//
		Unidad unidad1 = new Unidad();
		unidad1.setDescripcion("Enteros");
		producto1.setUnidad(unidad1);
		//
		Optional<Producto> optProd = productoService.guardarProducto(producto1);
		//
		producto1.setPrecio(8000.0);
		// Para que lo actualice debe tener el id
		producto1.setId(optProd.get().getId());
		// Actualizamos el producto
		Optional<Producto> optProdActRetur = productoService.actualizarProducto(producto1);
		assertTrue(optProdActRetur.isPresent());
		// Lo buscamos en el repo tiene que haber cambiado su precio
		Optional<Producto> productoBuscado = productoRepo.findById(producto1.getId());
		assertEquals(producto1.getPrecio(), productoBuscado.get().getPrecio());
	}

	@Test
	void guardar_unidad() {
		Unidad unidad1 = new Unidad();
		unidad1.setDescripcion("Enteros");
		//
		Optional<Unidad> optUnidad = productoService.guardarUnidad(unidad1);
		// Verificamos que retorne algo
		assertTrue(optUnidad.isPresent());
		// Verificamos que este guardado en el repo
		Optional<Unidad> optUnidadReturn = unidadRepo.findById(optUnidad.get().getId());
		assertTrue(optUnidadReturn.isPresent());
	}

	@Test
	void guardar_actualizar_unidad() {
		Unidad unidad1 = new Unidad();
		unidad1.setDescripcion("Enteros");
		//
		Optional<Unidad> optUnidad = productoService.guardarUnidad(unidad1);
		// Verificamos que retorne algo
		assertTrue(optUnidad.isPresent());
		// Para que lo actualice debe tener el id
		unidad1.setId(optUnidad.get().getId());
		unidad1.setDescripcion("m3");
		// Actualizamos unidad
		Optional<Unidad> optUnidadActRetur = productoService.actualizarUnidad(unidad1);
		assertTrue(optUnidadActRetur.isPresent());
		// Lo buscamos en el repo tiene que haber cambiado su precio
		Optional<Unidad> unidadBuscada = unidadRepo.findById(unidad1.getId());
		assertEquals(unidad1.getDescripcion(), unidadBuscada.get().getDescripcion());

	}

	@Test
	void get_producto_Id() {
		Producto producto1 = new Producto();
		producto1.setNombre("Marco de puertas");
		producto1.setDescripcion("Son marcos de metal");
		producto1.setPrecio(5000.0);
		producto1.setStockActual(50);
		producto1.setStockMinimo(10);
		//
		Unidad unidad1 = new Unidad();
		unidad1.setDescripcion("Enteros");
		producto1.setUnidad(unidad1);
		//
		Optional<Producto> optProd = productoService.guardarProducto(producto1);
		//
		Optional<Producto> optProdBuscado = productoService.getProducto(optProd.get().getId());
		assertTrue(optProdBuscado.isPresent());
	}

	@Test
	void get_all_producto() {
		// Producto 1-------------------------
		Producto producto1 = new Producto();
		producto1.setNombre("Marco de puertas");
		producto1.setDescripcion("Son marcos de metal");
		producto1.setPrecio(5000.0);
		producto1.setStockActual(50);
		producto1.setStockMinimo(10);
		//
		Unidad unidad1 = new Unidad();
		unidad1.setDescripcion("Enteros");
		producto1.setUnidad(unidad1);
		//
		productoService.guardarProducto(producto1);
		// Producto 2-------------------------
		Producto producto2 = new Producto();
		producto2.setNombre("Ventana");
		producto2.setDescripcion("Ventana de metal");
		producto2.setPrecio(7000.0);
		producto2.setStockActual(25);
		producto2.setStockMinimo(5);
		//
		Unidad unidad2 = new Unidad();
		unidad2.setDescripcion("Enteros");
		producto2.setUnidad(unidad2);
		//
		productoService.guardarProducto(producto2);
		// Producto 3-------------------------
		Producto producto3 = new Producto();
		producto3.setNombre("Piedras");
		producto3.setDescripcion("Piedras de construccion");
		producto3.setPrecio(500.0);
		producto3.setStockActual(100);
		producto3.setStockMinimo(10);
		//
		Unidad unidad3 = new Unidad();
		unidad3.setDescripcion("Kg");
		producto3.setUnidad(unidad3);
		//
		productoService.guardarProducto(producto3);
		//----------------------------------
		
	
		List<Producto> listaProducto = productoService.getAllProducto();
		assertTrue(listaProducto.size() == 3);
	}
	@Test
	void get_producto_porNombre() {
		// Producto 1-------------------------
		Producto producto1 = new Producto();
		producto1.setNombre("Marco de puertas");
		producto1.setDescripcion("Son marcos de metal");
		producto1.setPrecio(5000.0);
		producto1.setStockActual(50);
		producto1.setStockMinimo(10);
		//
		Unidad unidad1 = new Unidad();
		unidad1.setDescripcion("Enteros");
		producto1.setUnidad(unidad1);
		//
		productoService.guardarProducto(producto1);
		Optional<Producto> optProductoBuscado = productoService.getProductoPorNombre(producto1.getNombre());
		assertTrue(optProductoBuscado.isPresent());
		
	}
	@Test
	void get_producto_porRangoStock() {
		// Producto 1-------------------------
		Producto producto1 = new Producto();
		producto1.setNombre("Marco de puertas");
		producto1.setDescripcion("Son marcos de metal");
		producto1.setPrecio(5000.0);
		producto1.setStockActual(50);
		producto1.setStockMinimo(10);
		//
		Unidad unidad1 = new Unidad();
		unidad1.setDescripcion("Enteros");
		producto1.setUnidad(unidad1);
		//
		productoService.guardarProducto(producto1);
		//Busqueda con resultado
		List<Producto> listaProductoBuscado = productoService.getProductoPorRangoStock(40,60);
		assertTrue(listaProductoBuscado.size() > 0);
		//Busqueda sin resultado
		List<Producto> listaProductoBuscado2 = productoService.getProductoPorRangoStock(60,100);
		assertTrue(listaProductoBuscado2.size() == 0);
		
		
	}
	@Test
	void get_producto_porPrecio() {
		// Producto 1-------------------------
		Producto producto1 = new Producto();
		producto1.setNombre("Marco de puertas");
		producto1.setDescripcion("Son marcos de metal");
		producto1.setPrecio(5000.0);
		producto1.setStockActual(50);
		producto1.setStockMinimo(10);
		//
		Unidad unidad1 = new Unidad();
		unidad1.setDescripcion("Enteros");
		producto1.setUnidad(unidad1);
		//
		productoService.guardarProducto(producto1);
		//Busqueda con resultado
		List<Producto> listaProductoBuscado = productoService.getProductoPorPrecio(5000.0);
		assertTrue(listaProductoBuscado.size() > 0);
		//Busqueda sin resultado
		List<Producto> listaProductoBuscado2 = productoService.getProductoPorPrecio(40.0);
		assertTrue(listaProductoBuscado2.size() == 0);
		
	}
	
	

}
