package dan.tp2021.productos.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dan.tp2021.productos.domain.DetallePedido;
import dan.tp2021.productos.domain.Material;
import dan.tp2021.productos.services.interfaces.ProductoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/producto")
public class ProductoRest {
	// Material es quivalente a producto

	@Autowired
	ProductoService productoService;

	@PostMapping
	@ApiOperation(value = "Permite crear un producto/material")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Guardado correctamente"),
			@ApiResponse(code = 400, message = "No se pudo guardar") })
	public ResponseEntity<Material> crearProducto(@RequestBody Material productoN) {
		// Verificamos que tenga la unidad
		if (productoN.getUnidad() == null) {
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.of(productoService.guardarProducto(productoN));
	}

	@PutMapping
	@ApiOperation(value = "Permite actualizar un producto/material y tambien su unidad, los mismo deben tener sus ids en el body del json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Actualizado correctamente"),
			@ApiResponse(code = 400, message = "No se pudo actualizar") })
	public ResponseEntity<Material> actualizarProducto(@RequestBody Material producto) {
		// Verificamos que tenga la unidad
		if (producto.getUnidad() == null) {
			return ResponseEntity.badRequest().build();
		}
		// Verificamos que tanto producto como unidad tengas sus ids
		if (producto.getId() == null || producto.getUnidad().getId() == null) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.of(productoService.actualizarProducto(producto));
	}

	@GetMapping(path = "/detallePedido")
	@ApiOperation(value = "Permite consultar si hay stock dado una lista de detalles de pedido")
	public ResponseEntity<?> getHayStockDisponible(@RequestBody List<DetallePedido> detalle) {

		List<DetallePedido> listaDetalle = new ArrayList<DetallePedido>();
		for (DetallePedido deta : detalle) {

			Optional<Material> mat = productoService.getProducto(deta.getMaterial().getId());
			if (mat.isPresent()) {
				if (mat.get().getStockActual() >= deta.getCantidad()) {
					listaDetalle.add(deta);
				}

			} else {
				return ResponseEntity.notFound().build();
			}
		}

		if (listaDetalle.size() == detalle.size()) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.badRequest().build();
		}

	}

	@GetMapping(path = "/{id}")
	@ApiOperation(value = "Permite obtener un producto/material dada la id como variable de path.")
	public ResponseEntity<Material> getProducto(@PathVariable Integer id) {
		return ResponseEntity.of(productoService.getProducto(id));
	}

	@GetMapping(path = "/nombre")
	@ApiOperation(value = "Permite obtener el producto dado su nombre como query string. /nombre?nombreProducto=")
	public ResponseEntity<Material> getProductoPorNombre(@RequestParam(required = true) String nombreProducto) {
		return ResponseEntity.of(productoService.getProductoPorNombre(nombreProducto));
	}

	@GetMapping
	@ApiOperation(value = "Permite obtener el producto dado su rango de stock o precio como query string")
	public ResponseEntity<List<Material>> getProductoPorRangoOPrecio(@RequestParam(required = false) Integer rangoMin,
			@RequestParam(required = false) Integer rangoMax, @RequestParam(required = false) Double precio) {

		List<Material> respuesta1 = new ArrayList<>();
		List<Material> respuesta2 = new ArrayList<>();
		// No se ingresa ningun parametro
		if ((rangoMin == null || rangoMax == null) && precio == null) {
			return ResponseEntity.badRequest().build();
		}
		// Si se ingresa la dupla de busqueda por rango
		else if (rangoMin != null && rangoMax != null && precio == null) {
			respuesta1 = productoService.getProductoPorRangoStock(rangoMin, rangoMax);
			return ResponseEntity.ok(respuesta1);

		} // Si se ingresa solo el parametro de busqueda por precio
		else if ((rangoMin == null && rangoMax == null) && precio != null) {
			respuesta2 = productoService.getProductoPorPrecio(precio);
			return ResponseEntity.ok(respuesta2);
		} // Si se ingresan los tres parametros
		else if (rangoMin != null && rangoMax != null && precio != null) {
			System.out.println("Entro al if de los tres parametros ingresados\n");
			List<Material> respuestaFinal = new ArrayList<>();
			respuesta1 = productoService.getProductoPorRangoStock(rangoMin, rangoMax);
			respuesta2 = productoService.getProductoPorPrecio(precio);

			// Obtenemos la intercepcion entre lista respuesta1 y respuesta2
			for (Material unMaterial : respuesta1) {
				if (respuesta2.contains(unMaterial)) {
					respuestaFinal.add(unMaterial);
				}
			}
			return ResponseEntity.ok(respuestaFinal);

		} else {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping(path = "/all")
	@ApiOperation(value = "Permite obtener todos los productos/materiales almacenados")
	public ResponseEntity<List<Material>> getProducto() {
		return ResponseEntity.ok(productoService.getAllProducto());
	}

}
