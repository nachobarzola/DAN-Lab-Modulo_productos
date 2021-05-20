package dan.tp2021.productos.services;

import dan.tp2021.productos.domain.Material;
import dan.tp2021.productos.domain.Unidad;
import dan.tp2021.productos.services.dao.ProductoRepository;
import dan.tp2021.productos.services.dao.UnidadRepository;
import dan.tp2021.productos.services.interfaces.ProductoService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductoServiceImp implements ProductoService {

	@Autowired
	ProductoRepository productoRepo;

	@Autowired
	UnidadRepository unidadRepo;

	@Override
	public Optional<Material> guardarProducto(Material producto) {

		// Primeros guardamos la unidad
		Unidad unidadGuardada = unidadRepo.save(producto.getUnidad());
		if (unidadGuardada == null) {
			// no se pudo guardar
			return Optional.empty();
		}
		producto.setUnidad(unidadGuardada);
		producto = productoRepo.save(producto);
		if (producto == null) {
			// no se pudo guardar
			return Optional.empty();
		}
		return Optional.of(producto);
	}

	@Override
	public Optional<Material> actualizarProducto(Material producto) {
		actualizarUnidad(producto.getUnidad());
		return Optional.of(productoRepo.save(producto));
	}

	@Override
	public Optional<Unidad> actualizarUnidad(Unidad unidad) {
		return Optional.of(unidadRepo.save(unidad));
	}

	@Override
	public Optional<Material> getProducto(Integer idProducto) {
		return productoRepo.findById(idProducto);
	}

	@Override
	public List<Material> getAllProducto() {
		return productoRepo.findAll();
	}

	@Override
	public Optional<Material> getProductoPorNombre(String nombreProducto) {
		return productoRepo.findByNombre(nombreProducto);
	}

	@Override
	public List<Material>  getProductoPorRangoStock(Integer rangoMin, Integer rangoMax) {
		return productoRepo.findByStockActualBetween(rangoMin, rangoMax);
	}

	@Override
	public List<Material>  getProductoPorPrecio(Double precio) {
		return productoRepo.findByPrecio(precio);
	}
	
	
	
	
	
}
