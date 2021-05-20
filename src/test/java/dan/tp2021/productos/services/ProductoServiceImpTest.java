package dan.tp2021.productos.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import dan.tp2021.productos.domain.Material;
import dan.tp2021.productos.domain.Unidad;
import dan.tp2021.productos.services.dao.ProductoRepository;
import dan.tp2021.productos.services.interfaces.ProductoService;

@SpringBootTest
class ProductoServiceImpTest {
	
	@Autowired
	ProductoService productoService;
	
	@Autowired
	ProductoRepository productoRepo;
	
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
		//Verificamos que retorne algo
		assertTrue(optProducto.isPresent());
		//Verificamos que en verdad se guardo
		Optional<Material> optProdReturn = productoRepo.findById(optProducto.get().getId());
		assertTrue(optProdReturn.isPresent());
	}
	@Test
	void actualizarProducto() {
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
		//Para que lo actualice debe tener el id
		producto1.setId(optProd.get().getId());
		//Actualizamos el producto
		Optional<Material> optProdActRetur = productoService.actualizarProducto(producto1);
		assertTrue(optProdActRetur.isPresent());
		//Lo buscamos en el repo tiene que haber cambiado su precio
		Optional<Material> productoBuscado = productoRepo.findById(producto1.getId());
		assertEquals(producto1.getPrecio(), productoBuscado.get().getPrecio());
	}
	
	

}
