package dan.tp2021.productos.rest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import dan.tp2021.productos.domain.Material;
import dan.tp2021.productos.domain.Unidad;
import dan.tp2021.productos.services.dao.ProductoRepository;
import dan.tp2021.productos.services.dao.UnidadRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ProductoRestTest {

	private String ENDPOINT_PRODUCTO = "/api/producto";
	private RestTemplate restTemplate = new RestTemplate();

	@Autowired
	TestRestTemplate testRestTemplate;

	@Autowired
	ProductoRepository productoRepo;

	@Autowired
	UnidadRepository unidadRepo;

	@LocalServerPort
	String puerto;

	@BeforeEach
	void limpiarRepositorios() {
		productoRepo.deleteAll();
	}

	@Test
	void crear_producto_conUnidad() {
		String server = "http://localhost:" + puerto + ENDPOINT_PRODUCTO;
		// Creo un Producto
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
		HttpEntity<Material> requestProducto = new HttpEntity<>(producto1);
		ResponseEntity<Material> respuesta = testRestTemplate.exchange(server, HttpMethod.POST, requestProducto,
				Material.class);

		assertTrue(respuesta.getStatusCode().equals(HttpStatus.OK));
		// Chequeo que este persistido
		Optional<Material> optProd = productoRepo.findById(respuesta.getBody().getId());
		assertTrue(optProd.isPresent());
	}

	@Test
	void crear_producto_sinUnidad() {
		String server = "http://localhost:" + puerto + ENDPOINT_PRODUCTO;
		// Creo un Producto
		Material producto1 = new Material();
		producto1.setNombre("Marco de puertas");
		producto1.setDescripcion("Son marcos de metal");
		producto1.setPrecio(5000.0);
		producto1.setStockActual(50);
		producto1.setStockMinimo(10);

		//
		HttpEntity<Material> requestProducto = new HttpEntity<>(producto1);
		ResponseEntity<Material> respuesta = testRestTemplate.exchange(server, HttpMethod.POST, requestProducto,
				Material.class);

		assertTrue(respuesta.getStatusCode().equals(HttpStatus.BAD_REQUEST));
	}

	@Test
	void actualizar_producto() {
		String server = "http://localhost:" + puerto + ENDPOINT_PRODUCTO;
		Material producto1 = new Material();
		producto1.setNombre("Marco de puertas");
		producto1.setDescripcion("Son marcos de metal");
		producto1.setPrecio(5000.0);
		producto1.setStockActual(50);
		producto1.setStockMinimo(10);
		//
		Unidad unidad1 = new Unidad();
		unidad1.setDescripcion("Enteros");
		unidad1 = unidadRepo.save(unidad1);
		producto1.setUnidad(unidad1);
		//

		Material prod = productoRepo.save(producto1);
		//
		producto1.setPrecio(8000.0);
		// Para que lo actualice debe tener el id
		producto1.setId(prod.getId());
		// Actualizamos el producto

		HttpEntity<Material> requestProducto = new HttpEntity<>(producto1);
		ResponseEntity<Material> respuesta = testRestTemplate.exchange(server, HttpMethod.PUT, requestProducto,
				Material.class);
		// Verificamos lo retornado
		assertTrue(respuesta.getStatusCode().equals(HttpStatus.OK));
		assertEquals(producto1.getPrecio(), respuesta.getBody().getPrecio());
		// Lo buscamos en el repo tiene que haber cambiado su precio
		Optional<Material> productoBuscado = productoRepo.findById(producto1.getId());
		assertEquals(producto1.getPrecio(), productoBuscado.get().getPrecio());

	}

	@Test
	void actualizar_producto_sinIds_noActualiza() {
		String server = "http://localhost:" + puerto + ENDPOINT_PRODUCTO;
		Material producto1 = new Material();
		producto1.setNombre("Marco de puertas");
		producto1.setDescripcion("Son marcos de metal");
		producto1.setPrecio(5000.0);
		producto1.setStockActual(50);
		producto1.setStockMinimo(10);
		//
		Unidad unidad1 = new Unidad();
		unidad1.setDescripcion("Enteros");
		unidad1 = unidadRepo.save(unidad1);
		producto1.setUnidad(unidad1);
		//
		producto1 = productoRepo.save(producto1);
		producto1.setPrecio(8000.0);
		//
		producto1.setId(null); // Para que actualice no debe ser null
		// Actualizamos el producto

		HttpEntity<Material> requestProducto = new HttpEntity<>(producto1);
		ResponseEntity<Material> respuesta = testRestTemplate.exchange(server, HttpMethod.PUT, requestProducto,
				Material.class);

		// Verificamos lo retornado
		assertTrue(respuesta.getStatusCode().equals(HttpStatus.BAD_REQUEST));

		// ----------Otra prueba: ahora el id de unidad es null
		producto1.getUnidad().setId(null);
		HttpEntity<Material> requestProducto2 = new HttpEntity<>(producto1);
		ResponseEntity<Material> respuesta2 = testRestTemplate.exchange(server, HttpMethod.PUT, requestProducto2,
				Material.class);

		// Verificamos lo retornado
		assertTrue(respuesta2.getStatusCode().equals(HttpStatus.BAD_REQUEST));
	}

	@Test
	void actualizar_producto_sinUnidad_noActualiza() {
		String server = "http://localhost:" + puerto + ENDPOINT_PRODUCTO;
		Material producto1 = new Material();
		producto1.setNombre("Marco de puertas");
		producto1.setDescripcion("Son marcos de metal");
		producto1.setPrecio(5000.0);
		producto1.setStockActual(50);
		producto1.setStockMinimo(10);
		//
		Unidad unidad1 = new Unidad();
		unidad1.setDescripcion("Enteros");
		unidad1 = unidadRepo.save(unidad1);
		producto1.setUnidad(unidad1);
		//
		producto1 = productoRepo.save(producto1);
		producto1.setPrecio(8000.0);
		// Le sacamos la unidad
		producto1.setUnidad(null);

		// Actualizamos el producto

		HttpEntity<Material> requestProducto = new HttpEntity<>(producto1);
		ResponseEntity<Material> respuesta = testRestTemplate.exchange(server, HttpMethod.PUT, requestProducto,
				Material.class);

		// Verificamos lo retornado
		assertTrue(respuesta.getStatusCode().equals(HttpStatus.BAD_REQUEST));

	}

	@Test
	void get_producto_porId() {
		String server = "http://localhost:" + puerto + ENDPOINT_PRODUCTO;
		Material producto1 = new Material();
		producto1.setNombre("Marco de puertas");
		producto1.setDescripcion("Son marcos de metal");
		producto1.setPrecio(5000.0);
		producto1.setStockActual(50);
		producto1.setStockMinimo(10);
		//
		Unidad unidad1 = new Unidad();
		unidad1.setDescripcion("Enteros");
		unidadRepo.save(unidad1);
		producto1.setUnidad(unidad1);
		productoRepo.save(producto1);
		//

		server = server + "/" + producto1.getId();
		ResponseEntity<Material> respuesta = testRestTemplate.exchange(server, HttpMethod.GET, null, Material.class);

		// Verificamos lo retornado
		assertTrue(respuesta.getStatusCode().equals(HttpStatus.OK));
		Material productoRetornado = respuesta.getBody();
		assertEquals(producto1.getId(), productoRetornado.getId());
		assertEquals(producto1.getDescripcion(), productoRetornado.getDescripcion());
		assertEquals(producto1.getPrecio(), productoRetornado.getPrecio());
		assertEquals(producto1.getStockActual(), productoRetornado.getStockActual());
		assertEquals(producto1.getStockMinimo(), productoRetornado.getStockMinimo());
		assertEquals(producto1.getUnidad().getId(), productoRetornado.getUnidad().getId());
		assertEquals(producto1.getUnidad().getDescripcion(), productoRetornado.getUnidad().getDescripcion());
	}

	@Test
	void get_producto_porNombre() {
		String server = "http://localhost:" + puerto + ENDPOINT_PRODUCTO;
		Material producto1 = new Material();
		producto1.setNombre("Marco de puertas");
		producto1.setDescripcion("Son marcos de metal");
		producto1.setPrecio(5000.0);
		producto1.setStockActual(50);
		producto1.setStockMinimo(10);
		//
		Unidad unidad1 = new Unidad();
		unidad1.setDescripcion("Enteros");
		unidadRepo.save(unidad1);
		producto1.setUnidad(unidad1);
		productoRepo.save(producto1);
		//

		server = server + "/nombre?nombreProducto=" + producto1.getNombre();
		ResponseEntity<Material> respuesta = testRestTemplate.exchange(server, HttpMethod.GET, null, Material.class);

		// Verificamos lo retornado
		assertTrue(respuesta.getStatusCode().equals(HttpStatus.OK));

		Material productoRetornado = respuesta.getBody();
		assertEquals(producto1.getId(), productoRetornado.getId());
		assertEquals(producto1.getDescripcion(), productoRetornado.getDescripcion());
		assertEquals(producto1.getPrecio(), productoRetornado.getPrecio());
		assertEquals(producto1.getStockActual(), productoRetornado.getStockActual());
		assertEquals(producto1.getStockMinimo(), productoRetornado.getStockMinimo());
		assertEquals(producto1.getUnidad().getId(), productoRetornado.getUnidad().getId());
		assertEquals(producto1.getUnidad().getDescripcion(), productoRetornado.getUnidad().getDescripcion());
	}

	@Test
	void get_producto_porRangoSctock_y_precio() {
		String server = "http://localhost:" + puerto + ENDPOINT_PRODUCTO;
		// CREACION DE PRODUCTOS
		// Producto 1--------------------------
		Material producto1 = new Material();
		producto1.setNombre("Marco de puertas");
		producto1.setDescripcion("Son marcos de metal");
		producto1.setPrecio(5000.0);
		producto1.setStockActual(50);
		producto1.setStockMinimo(10);
		//
		Unidad unidad1 = new Unidad();
		unidad1.setDescripcion("Enteros");
		unidadRepo.save(unidad1);
		producto1.setUnidad(unidad1);
		productoRepo.save(producto1);
		// Producto 2--------------------------
		Material producto2 = new Material();
		producto2.setNombre("Ventana");
		producto2.setDescripcion("Ventana de metal");
		producto2.setPrecio(7000.0);
		producto2.setStockActual(25);
		producto2.setStockMinimo(5);
		//
		Unidad unidad2 = new Unidad();
		unidad2.setDescripcion("Enteros");
		unidadRepo.save(unidad2);
		producto2.setUnidad(unidad2);
		productoRepo.save(producto2);
		// Producto 3--------------------------
		Material producto3 = new Material();
		producto3.setNombre("Piedras");
		producto3.setDescripcion("Piedras de construccion");
		producto3.setPrecio(500.0);
		producto3.setStockActual(100);
		producto3.setStockMinimo(10);
		//
		Unidad unidad3 = new Unidad();
		unidad3.setDescripcion("Kg");
		unidadRepo.save(unidad3);
		producto3.setUnidad(unidad3);
		productoRepo.save(producto3);
		// --------------------------------------
		// Caso 1: no se ingresa ningun parametro
		ResponseEntity<Material> respuesta = testRestTemplate.exchange(server, HttpMethod.GET, null, Material.class);
		assertTrue(respuesta.getStatusCode().equals(HttpStatus.BAD_REQUEST));

		// Caso 2: Si ingresa la dupla de busqueda por rango
		String server2 = server + "?rangoMin=" + 0 + "&rangoMax=" + 55;
		ResponseEntity<Material[]> respuesta2 = testRestTemplate.getForEntity(server2, Material[].class);
		assertTrue(respuesta2.getStatusCode().equals(HttpStatus.OK));
		assertTrue(respuesta2.getBody().length == 2);

		// Caso 3: Solo se ingresa precio
		String server3 = server + "?precio=" + producto3.getPrecio();
		ResponseEntity<Material[]> respuesta3 = testRestTemplate.getForEntity(server3, Material[].class);

		assertTrue(respuesta3.getStatusCode().equals(HttpStatus.OK));
		assertTrue(respuesta3.getBody().length == 1);
		assertEquals(respuesta3.getBody()[0].getPrecio(), producto3.getPrecio());

		// Caso 4: Se ingresa la dupla de rango y ademas precio
		String server4 = server + "?rangoMin=" + 0 + "&rangoMax=" + 55 + "&precio=" + producto2.getPrecio();
		ResponseEntity<Material[]> respuesta4 = testRestTemplate.getForEntity(server4, Material[].class);
		assertTrue(respuesta4.getStatusCode().equals(HttpStatus.OK));
		assertTrue(respuesta4.getBody().length == 1);

		// Casos extras de error:
		// Caso 5: no se ingresa rangoMin
		String server5 = server + "?rangoMax=" + 55 + "&precio=" + producto2.getPrecio();
		ResponseEntity<Material[]> respuesta5 = testRestTemplate.getForEntity(server5, Material[].class);
		assertTrue(respuesta5.getStatusCode().equals(HttpStatus.BAD_REQUEST));
		assertNull(respuesta5.getBody());

		// Caso 6: no se ingresa rangoMax
		String server6 = server + "?rangoMin=" + 1 + "&precio=" + producto2.getPrecio();
		ResponseEntity<Material[]> respuesta6 = testRestTemplate.getForEntity(server6, Material[].class);
		assertTrue(respuesta6.getStatusCode().equals(HttpStatus.BAD_REQUEST));
		assertNull(respuesta6.getBody());
	}
	@Test 
	void get_all_producto() {
		String server = "http://localhost:" + puerto + ENDPOINT_PRODUCTO+"/all";
		// CREACION DE PRODUCTOS
		// Producto 1--------------------------
		Material producto1 = new Material();
		producto1.setNombre("Marco de puertas");
		producto1.setDescripcion("Son marcos de metal");
		producto1.setPrecio(5000.0);
		producto1.setStockActual(50);
		producto1.setStockMinimo(10);
		//
		Unidad unidad1 = new Unidad();
		unidad1.setDescripcion("Enteros");
		unidadRepo.save(unidad1);
		producto1.setUnidad(unidad1);
		productoRepo.save(producto1);
		// Producto 2--------------------------
		Material producto2 = new Material();
		producto2.setNombre("Ventana");
		producto2.setDescripcion("Ventana de metal");
		producto2.setPrecio(7000.0);
		producto2.setStockActual(25);
		producto2.setStockMinimo(5);
		//
		Unidad unidad2 = new Unidad();
		unidad2.setDescripcion("Enteros");
		unidadRepo.save(unidad2);
		producto2.setUnidad(unidad2);
		productoRepo.save(producto2);
		// Producto 3--------------------------
		Material producto3 = new Material();
		producto3.setNombre("Piedras");
		producto3.setDescripcion("Piedras de construccion");
		producto3.setPrecio(500.0);
		producto3.setStockActual(100);
		producto3.setStockMinimo(10);
		//
		Unidad unidad3 = new Unidad();
		unidad3.setDescripcion("Kg");
		unidadRepo.save(unidad3);
		producto3.setUnidad(unidad3);
		productoRepo.save(producto3);
		// --------------------------------------
		ResponseEntity<Material[]> respuesta = testRestTemplate.getForEntity(server, Material[].class);
		assertTrue(respuesta.getStatusCode().equals(HttpStatus.OK));
		assertTrue(respuesta.getBody().length == 3);
	}

}