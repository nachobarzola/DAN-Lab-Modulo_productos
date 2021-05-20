package dan.tp2021.productos.services.interfaces;

import java.util.Optional;
import java.util.List;

import dan.tp2021.productos.domain.Material;
import dan.tp2021.productos.domain.Unidad;

public interface ProductoService {
	public Optional<Material> guardarProducto(Material producto);
	
	public Optional<Unidad> guardarUnidad(Unidad unidad);
	
	public Optional<Material> actualizarProducto(Material producto);
	
	public Optional<Unidad> actualizarUnidad(Unidad unidad);
	
	public Optional<Material> getProducto(Integer idProducto);
	
	public Optional<Material> getProductoPorNombre(String nombreProducto);
	
	public List<Material> getProductoPorRangoStock(Integer rangoMin, Integer rangoMax);
	
	public List<Material> getProductoPorPrecio(Double precio);
	
	public List<Material> getAllProducto();

}
