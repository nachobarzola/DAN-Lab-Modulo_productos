package dan.tp2021.productos.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.ActiveProfiles;
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
@ActiveProfiles("testing")
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

	@Test
	@Order(1)
	void limpiarRepositorios() {
		movStockRepo.deleteAll();
		detalleProvisionRepo.deleteAll();
		provisionRepo.deleteAll();
		detallePedidoRepo.deleteAll();
		productoRepo.deleteAll();
		unidadRepo.deleteAll();
	}

	@Test
	@Sql("/insert-data-testing1.sql")
	void resgistrarMovimientoStock_sinGeneracionDeProvision() {
		// Unidad
		Unidad unidad1 = new Unidad("Entera");
		unidad1.setId(1);

		// Producto 1
		Producto producto1 = new Producto();
		producto1.setId(1);
		producto1.setUnidad(unidad1);

		// Detalle pedido1
		DetallePedido detallePedido1 = new DetallePedido();
		detallePedido1.setId(1);
		detallePedido1.setProducto(producto1);
		// Producto 2
		Producto producto2 = new Producto();
		producto2.setId(2);
		producto2.setUnidad(unidad1);
		// Detalle pedido2
		DetallePedido detallePedido2 = new DetallePedido();
		detallePedido2.setProducto(producto2);
		detallePedido2.setId(2);

		// -------------------------------------------------------
		List<Integer> listaIdDetallePedidoDelPedido = new ArrayList<>();
		listaIdDetallePedidoDelPedido.add(detallePedido1.getId());
		listaIdDetallePedidoDelPedido.add(detallePedido2.getId());

		List<Optional<MovimientosStock>> listaOptMovStock = movStockService
				.registrarMovimientoStock(listaIdDetallePedidoDelPedido);
		// ----------------------Movimiento 1--> Producto 1
		// Para el movimiento stock 1
		assertTrue(listaOptMovStock.get(0).isPresent());
		assertEquals(0, listaOptMovStock.get(0).get().getCantidadEntrada());
		assertEquals(600, listaOptMovStock.get(0).get().getCantidadSalida());
		// vemos que no haya generado orden de provision
		assertNull(listaOptMovStock.get(0).get().getDetalleProvision());
		// Vemos si actualizo el producto 1
		Optional<Producto> optProducto1 = productoRepo.findById(producto1.getId());
		assertEquals(400, optProducto1.get().getStockActual());
		assertEquals(200, optProducto1.get().getStockMinimo()); // no tiene que cambiar

		// ----------------------Movimiento 2--> Producto 2
		// Para el movimiento stock 2
		assertTrue(listaOptMovStock.get(1).isPresent());
		assertEquals(0, listaOptMovStock.get(1).get().getCantidadEntrada());
		assertEquals(50, listaOptMovStock.get(1).get().getCantidadSalida());
		// vemos que no haya generado orden de provision
		assertNull(listaOptMovStock.get(1).get().getDetalleProvision());
		// Vemos si actualizo el producto 2
		Optional<Producto> optProducto2 = productoRepo.findById(producto2.getId());
		assertEquals(50, optProducto2.get().getStockActual());
		assertEquals(5, optProducto2.get().getStockMinimo()); // no tiene que cambiar

		// TODO: No se si es la forma correcta de hacerlo, pero con el beforceEach no
		// anduvo.
		limpiarRepositorios();
	}

	@Test
	@Sql("/insert-data-testing2.sql")
	void resgistrarMovimientoStock_conGeneracionDeProvision_enProducto1() {
		// Unidad
		Unidad unidad1 = new Unidad("Entera");
		unidad1.setId(1);

		// Producto 1
		Producto producto1 = new Producto();
		producto1.setId(1);
		producto1.setUnidad(unidad1);

		// Detalle pedido1
		DetallePedido detallePedido1 = new DetallePedido();
		detallePedido1.setId(1);
		detallePedido1.setProducto(producto1);
		// Producto 2
		Producto producto2 = new Producto();
		producto2.setId(2);
		producto2.setUnidad(unidad1);
		// Detalle pedido2
		DetallePedido detallePedido2 = new DetallePedido();
		detallePedido2.setProducto(producto2);
		detallePedido2.setId(2);
		// -------------------------------------------------------
		List<Integer> listaIdDetallePedidoDelPedido = new ArrayList<>();
		listaIdDetallePedidoDelPedido.add(detallePedido1.getId());
		listaIdDetallePedidoDelPedido.add(detallePedido2.getId());

		List<Optional<MovimientosStock>> listaOptMovStock = movStockService
				.registrarMovimientoStock(listaIdDetallePedidoDelPedido);

		// ----------------------Movimiento 1--> Producto 1
		assertTrue(listaOptMovStock.get(0).isPresent());
		assertEquals(900, listaOptMovStock.get(0).get().getCantidadEntrada());
		assertEquals(850, listaOptMovStock.get(0).get().getCantidadSalida());
		// vemos que Si se haya generado orden de provision
		assertNotNull(listaOptMovStock.get(0).get().getDetalleProvision());
		assertNotNull(listaOptMovStock.get(0).get().getDetalleProvision().getProvision());
		// Chequeamos el valor de aprovionamiento
		assertEquals(900, listaOptMovStock.get(0).get().getDetalleProvision().getCantidad());

		// Vemos si actualizo el producto
		Optional<Producto> optProducto1 = productoRepo.findById(producto1.getId());
		assertEquals(1050, optProducto1.get().getStockActual());
		assertEquals(200, optProducto1.get().getStockMinimo()); // no tiene que cambiar

		// ----------------------Movimiento 2--> Producto 2
		// Para el movimiento stock 2
		assertTrue(listaOptMovStock.get(1).isPresent());
		assertEquals(0, listaOptMovStock.get(1).get().getCantidadEntrada());
		assertEquals(50, listaOptMovStock.get(1).get().getCantidadSalida());
		// vemos que no haya generado orden de provision
		assertNull(listaOptMovStock.get(1).get().getDetalleProvision());
		// Vemos si actualizo el producto 2
		Optional<Producto> optProducto2 = productoRepo.findById(producto2.getId());
		assertEquals(50, optProducto2.get().getStockActual());
		assertEquals(5, optProducto2.get().getStockMinimo()); // no tiene que cambiar
		
		// TODO: No se si es la forma correcta de hacerlo, pero con el beforceEach no
		// anduvo.
		limpiarRepositorios();
	}

	@Test
	@Sql("/insert-data-testing3.sql")
	void resgistrarMovimientoStock_conGeneracionDeProvision_enAmbosProductos() {
		// Unidad
		Unidad unidad1 = new Unidad("Entera");
		unidad1.setId(1);

		// Producto 1
		Producto producto1 = new Producto();
		producto1.setId(1);
		producto1.setUnidad(unidad1);

		// Detalle pedido1
		DetallePedido detallePedido1 = new DetallePedido();
		detallePedido1.setId(1);
		detallePedido1.setProducto(producto1);
		// Producto 2
		Producto producto2 = new Producto();
		producto2.setId(2);
		producto2.setUnidad(unidad1);
		// Detalle pedido2
		DetallePedido detallePedido2 = new DetallePedido();
		detallePedido2.setProducto(producto2);
		detallePedido2.setId(2);

		// -------------------------------------------------------
		List<Integer> listaIdDetallePedidoDelPedido = new ArrayList<>();
		listaIdDetallePedidoDelPedido.add(detallePedido1.getId());
		listaIdDetallePedidoDelPedido.add(detallePedido2.getId());

		List<Optional<MovimientosStock>> listaOptMovStock = movStockService
				.registrarMovimientoStock(listaIdDetallePedidoDelPedido);

		// ----------------------Movimiento 1--> Producto 1
		assertTrue(listaOptMovStock.get(0).isPresent());
		assertEquals(1800, listaOptMovStock.get(0).get().getCantidadEntrada());
		assertEquals(2000, listaOptMovStock.get(0).get().getCantidadSalida());
		// vemos que Si se haya generado orden de provision
		assertNotNull(listaOptMovStock.get(0).get().getDetalleProvision());
		assertNotNull(listaOptMovStock.get(0).get().getDetalleProvision().getProvision());
		// Chequeamos el valor de aprovionamiento
		assertEquals(1800, listaOptMovStock.get(0).get().getDetalleProvision().getCantidad());

		// Vemos si actualizo el producto
		Optional<Producto> producto = productoRepo.findById(producto1.getId());
		assertEquals(800, producto.get().getStockActual());
		assertEquals(200, producto.get().getStockMinimo()); // no tiene que cambiar

		// ----------------------Movimiento 2--> Producto 2
		// Para el movimiento stock 2
		assertTrue(listaOptMovStock.get(1).isPresent());
		assertEquals(900, listaOptMovStock.get(1).get().getCantidadEntrada());
		assertEquals(98, listaOptMovStock.get(1).get().getCantidadSalida());
		// vemos que se haya generado orden de provision
		assertNotNull(listaOptMovStock.get(1).get().getDetalleProvision());
		assertNotNull(listaOptMovStock.get(1).get().getDetalleProvision().getProvision());
		// Chequeamos el valor de aprovionamiento
		assertEquals(900, listaOptMovStock.get(1).get().getDetalleProvision().getCantidad());

		// Vemos si actualizo el producto 2
		Optional<Producto> optProducto2 = productoRepo.findById(producto2.getId());
		assertEquals(902, optProducto2.get().getStockActual());
		assertEquals(5, optProducto2.get().getStockMinimo()); // no tiene que cambiar
		
		//TODO: No se si es la forma correcta de hacerlo, pero con el beforceEach no anduvo.
		limpiarRepositorios();
	}

}
