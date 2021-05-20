package dan.tp2021.productos.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dan.tp2021.productos.domain.Material;
import dan.tp2021.productos.services.interfaces.ProductoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/producto")
public class ProductoRest {
	//Material es quivalente a producto
	
	@Autowired
	ProductoService productoService;

	@PostMapping
	@ApiOperation(value = "Permite crear un producto/material")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Guardado correctamente"),
			@ApiResponse(code = 400, message = "No se pudo guardar")})
	public ResponseEntity<Material> crearProducto(@RequestBody Material productoN) {
		if(productoN.getUnidad() == null) {
			return ResponseEntity.badRequest().build();
		}
		
		return ResponseEntity.of(productoService.guardarProducto(productoN));
	}
	
	
}
