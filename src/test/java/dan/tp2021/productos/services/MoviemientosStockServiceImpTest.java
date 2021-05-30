package dan.tp2021.productos.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
		// Producto 1
		Producto producto1 = new Producto();
		producto1.setDescripcion("Ladrillos Huecos");
		producto1.setNombre("Ladrillos");
		producto1.setPrecio(50.00);
		producto1.setStockActual(1000);
		producto1.setStockMinimo(200);
		producto1.setUnidad(unidad1);
		producto1 = productoRepo.save(producto1);
		// Detalle pedido1
		DetallePedido detallePedido1 = new DetallePedido();
		detallePedido1.setProducto(producto1);
		detallePedido1.setCantidad(600);
		detallePedido1 = detallePedidoRepo.save(detallePedido1);
		// Producto 2
		Producto producto2 = new Producto();
		producto2.setDescripcion("Ventana de metal con vidrio en el centro");
		producto2.setNombre("Ventana");
		producto2.setPrecio(5000.00);
		producto2.setStockActual(100);
		producto2.setStockMinimo(5);
		producto2.setUnidad(unidad1);
		producto2 = productoRepo.save(producto2);
		// Detalle pedido2
		DetallePedido detallePedido2 = new DetallePedido();
		detallePedido2.setProducto(producto2);
		detallePedido2.setCantidad(50);
		detallePedido2 = detallePedidoRepo.save(detallePedido2);

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
	}

	@Test
	void resgistrarMovimientoStock_conGeneracionDeProvision_enProducto1() {
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
		DetallePedido detallePedido1 = new DetallePedido();
		detallePedido1.setProducto(producto1);
		detallePedido1.setCantidad(850);
		detallePedido1 = detallePedidoRepo.save(detallePedido1);
		// Producto 2
		Producto producto2 = new Producto();
		producto2.setDescripcion("Ventana de metal con vidrio en el centro");
		producto2.setNombre("Ventana");
		producto2.setPrecio(5000.00);
		producto2.setStockActual(100);
		producto2.setStockMinimo(5);
		producto2.setUnidad(unidad1);
		producto2 = productoRepo.save(producto2);
		// Detalle pedido2
		DetallePedido detallePedido2 = new DetallePedido();
		detallePedido2.setProducto(producto2);
		detallePedido2.setCantidad(50);
		detallePedido2 = detallePedidoRepo.save(detallePedido2);
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

	}

	@Test
	void resgistrarMovimientoStock_conGeneracionDeProvision_enAmbosProductos() {
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
		DetallePedido detallePedido1 = new DetallePedido();
		detallePedido1.setProducto(producto1);
		detallePedido1.setCantidad(2000);
		detallePedido1 = detallePedidoRepo.save(detallePedido1);
		// Producto 2
		Producto producto2 = new Producto();
		producto2.setDescripcion("Ventana de metal con vidrio en el centro");
		producto2.setNombre("Ventana");
		producto2.setPrecio(5000.00);
		producto2.setStockActual(100);
		producto2.setStockMinimo(5);
		producto2.setUnidad(unidad1);
		producto2 = productoRepo.save(producto2);
		// Detalle pedido2
		DetallePedido detallePedido2 = new DetallePedido();
		detallePedido2.setProducto(producto2);
		detallePedido2.setCantidad(98);
		detallePedido2 = detallePedidoRepo.save(detallePedido2);
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

	}

}
