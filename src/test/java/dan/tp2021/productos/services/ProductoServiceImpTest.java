package dan.tp2021.productos.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import dan.tp2021.productos.domain.Material;
import dan.tp2021.productos.domain.Unidad;
import dan.tp2021.productos.services.dao.ProductoRepository;
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

	@BeforeEach
	void limpiarRepositorios() {
		productoRepo.deleteAll();
	}

	@Test
	void guardarProducto() {
		Material producto1 = new Material();
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
		Optional<Material> optProducto = productoService.guardarProducto(producto1);
		// Verificamos que retorne algo
		assertTrue(optProducto.isPresent());
		// Verificamos que en verdad se guardo
		Optional<Material> optProdReturn = productoRepo.findById(optProducto.get().getId());
		assertTrue(optProdReturn.isPresent());
	}

	@Test
	void guardar_actualizar_producto() {
		Material producto1 = new Material();
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
		Optional<Material> optProd = productoService.guardarProducto(producto1);
		//
		producto1.setPrecio(8000.0);
		// Para que lo actualice debe tener el id
		producto1.setId(optProd.get().getId());
		// Actualizamos el producto
		Optional<Material> optProdActRetur = productoService.actualizarProducto(producto1);
		assertTrue(optProdActRetur.isPresent());
		// Lo buscamos en el repo tiene que haber cambiado su precio
		Optional<Material> productoBuscado = productoRepo.findById(producto1.getId());
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
		Material producto1 = new Material();
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
		Optional<Material> optProd = productoService.guardarProducto(producto1);
		//
		Optional<Material> optProdBuscado = productoService.getProducto(optProd.get().getId());
		assertTrue(optProdBuscado.isPresent());
	}

	@Test
	void get_all_producto() {
		// Producto 1-------------------------
		Material producto1 = new Material();
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
		Material producto2 = new Material();
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
		Material producto3 = new Material();
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
		
	
		List<Material> listaProducto = productoService.getAllProducto();
		assertTrue(listaProducto.size() == 3);
	}
	@Test
	void get_producto_porNombre() {
		// Producto 1-------------------------
		Material producto1 = new Material();
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
		Optional<Material> optProductoBuscado = productoService.getProductoPorNombre(producto1.getNombre());
		assertTrue(optProductoBuscado.isPresent());
		
	}
	@Test
	void get_producto_porRangoStock() {
		// Producto 1-------------------------
		Material producto1 = new Material();
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
		List<Material> listaProductoBuscado = productoService.getProductoPorRangoStock(40,60);
		assertTrue(listaProductoBuscado.size() > 0);
		//Busqueda sin resultado
		List<Material> listaProductoBuscado2 = productoService.getProductoPorRangoStock(60,100);
		assertTrue(listaProductoBuscado2.size() == 0);
		
		
	}
	@Test
	void get_producto_porPrecio() {
		// Producto 1-------------------------
		Material producto1 = new Material();
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
		List<Material> listaProductoBuscado = productoService.getProductoPorPrecio(5000.0);
		assertTrue(listaProductoBuscado.size() > 0);
		//Busqueda sin resultado
		List<Material> listaProductoBuscado2 = productoService.getProductoPorPrecio(40.0);
		assertTrue(listaProductoBuscado2.size() == 0);
		
	}

}
