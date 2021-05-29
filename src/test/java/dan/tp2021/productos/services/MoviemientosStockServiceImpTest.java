package dan.tp2021.productos.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import dan.tp2021.productos.domain.DetallePedido;
import dan.tp2021.productos.domain.Producto;
import dan.tp2021.productos.domain.MovimientosStock;
import dan.tp2021.productos.domain.Unidad;
import dan.tp2021.productos.services.dao.DetallePedidoRepository;
import dan.tp2021.productos.services.dao.DetalleProvisionRepository;
import dan.tp2021.productos.services.dao.MovimientosStockRepository;
import dan.tp2021.productos.services.dao.ProductoRepository;
import dan.tp2021.productos.services.dao.ProvisionRepository;
import dan.tp2021.productos.services.dao.UnidadRepository;
import dan.tp2021.productos.services.interfaces.MovimientoStockService;

@SpringBootTest
class MoviemientosStockServiceImpTest {

	@Autowired
	MovimientoStockService movStockService;

	// ----------------------Repositorios-------------------------------
	@Autowired
	MovimientosStockRepository movStockRepo;

	@Autowired
	ProductoRepository productoRepo;

	@Autowired
	DetalleProvisionRepository detalleProvisionRepo;

	@Autowired
	ProvisionRepository provisionRepo;

	@Autowired
	DetallePedidoRepository detallePedidoRepo;

	@Autowired
	UnidadRepository unidadRepo;

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
	void resgistrarMovimientoStock_sinGeneracionDeProvision() {
		// Unidad
		Unidad unidad1 = new Unidad("Entera");
		unidadRepo.save(unidad1);
		// Producto
		Producto producto1 = new Producto();
		producto1.setDescripcion("Ladrillos Huecos");
		producto1.setNombre("Ladrillos");
		producto1.setPrecio(50.00);
		producto1.setStockActual(1000);
		producto1.setStockMinimo(200);
		producto1.setUnidad(unidad1);
		producto1 = productoRepo.save(producto1);
		//
		DetallePedido detallePedido = new DetallePedido();
		detallePedido.setProducto(producto1);
		detallePedido.setCantidad(600);
		detallePedido = detallePedidoRepo.save(detallePedido);
		// -------------------------------------------------------

		Optional<MovimientosStock> optMovStock = movStockService.registrarMovimientoStock(detallePedido.getId());
		assertTrue(optMovStock.isPresent());
		assertEquals(0, optMovStock.get().getCantidadEntrada());
		assertEquals(600, optMovStock.get().getCantidadSalida());
		// vemos que no haya generado orden de provision
		assertNull(optMovStock.get().getDetalleProvision());

		// Vemos si actualizo el producto
		Optional<Producto> producto = productoRepo.findById(producto1.getId());
		assertEquals(400, producto.get().getStockActual());
		assertEquals(200, producto.get().getStockMinimo()); // no tiene que cambiar
	}

	@Test
	void resgistrarMovimientoStock_conGeneracionDeProvision() {
		// Unidad
		Unidad unidad1 = new Unidad("Entera");
		unidadRepo.save(unidad1);
		// Producto
		Producto producto1 = new Producto();
		producto1.setDescripcion("Ladrillos Huecos");
		producto1.setNombre("Ladrillos");
		producto1.setPrecio(50.00);
		producto1.setStockActual(1000);
		producto1.setStockMinimo(200);
		producto1.setUnidad(unidad1);
		producto1 = productoRepo.save(producto1);
		//
		DetallePedido detallePedido = new DetallePedido();
		detallePedido.setProducto(producto1);
		detallePedido.setCantidad(850);
		detallePedido = detallePedidoRepo.save(detallePedido);
		// -------------------------------------------------------

		Optional<MovimientosStock> optMovStock = movStockService.registrarMovimientoStock(detallePedido.getId());
		assertTrue(optMovStock.isPresent());
		assertEquals(900, optMovStock.get().getCantidadEntrada());
		assertEquals(850, optMovStock.get().getCantidadSalida());
		// vemos que Si se haya generado orden de provision
		assertNotNull(optMovStock.get().getDetalleProvision());
		assertNotNull(optMovStock.get().getDetalleProvision().getProvision());
		// Chequeamos el valor de aprovionamiento
		assertEquals(900, optMovStock.get().getDetalleProvision().getCantidad());

		// Vemos si actualizo el producto
		Optional<Producto> producto = productoRepo.findById(producto1.getId());
		assertEquals(1050, producto.get().getStockActual());
		assertEquals(200, producto.get().getStockMinimo()); // no tiene que cambiar

	}

	@Test
	void resgistrarMovimientoStock_conGeneracionDeProvision_2() {
		// Unidad
		Unidad unidad1 = new Unidad("Entera");
		unidadRepo.save(unidad1);
		// Producto
		Producto producto1 = new Producto();
		producto1.setDescripcion("Ladrillos Huecos");
		producto1.setNombre("Ladrillos");
		producto1.setPrecio(50.00);
		producto1.setStockActual(1000);
		producto1.setStockMinimo(200);
		producto1.setUnidad(unidad1);
		producto1 = productoRepo.save(producto1);
		//
		DetallePedido detallePedido = new DetallePedido();
		detallePedido.setProducto(producto1);
		detallePedido.setCantidad(2000);
		detallePedido = detallePedidoRepo.save(detallePedido);
		// -------------------------------------------------------

		Optional<MovimientosStock> optMovStock = movStockService.registrarMovimientoStock(detallePedido.getId());
		assertTrue(optMovStock.isPresent());
		assertEquals(1800, optMovStock.get().getCantidadEntrada());
		assertEquals(2000, optMovStock.get().getCantidadSalida());
		// vemos que Si se haya generado orden de provision
		assertNotNull(optMovStock.get().getDetalleProvision());
		assertNotNull(optMovStock.get().getDetalleProvision().getProvision());
		// Chequeamos el valor de aprovionamiento
		assertEquals(1800, optMovStock.get().getDetalleProvision().getCantidad());

		// Vemos si actualizo el producto
		Optional<Producto> producto = productoRepo.findById(producto1.getId());
		assertEquals(800, producto.get().getStockActual());
		assertEquals(200, producto.get().getStockMinimo()); // no tiene que cambiar

	}

}
