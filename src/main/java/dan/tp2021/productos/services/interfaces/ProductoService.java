package dan.tp2021.productos.services.interfaces;

import java.util.Optional;

import javax.jms.MapMessage;

import java.util.List;

import dan.tp2021.productos.domain.Producto;
import dan.tp2021.productos.domain.MovimientosStock;
import dan.tp2021.productos.domain.Unidad;

public interface ProductoService {
	public Optional<Producto> guardarProducto(Producto producto);
	
	public Optional<Unidad> guardarUnidad(Unidad unidad);
	
	public Optional<Producto> actualizarProducto(Producto producto);
	
	public Optional<Unidad> actualizarUnidad(Unidad unidad);
	
	public Optional<Producto> getProducto(Integer idProducto);
	
	public Optional<Producto> getProductoPorNombre(String nombreProducto);
	
	public List<Producto> getProductoPorRangoStock(Integer rangoMin, Integer rangoMax);
	
	public List<Producto> getProductoPorPrecio(Double precio);
	
	public List<Producto> getAllProducto();
	

	

}
