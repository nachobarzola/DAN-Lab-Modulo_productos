package dan.tp2021.productos.services;

import dan.tp2021.productos.domain.Material;
import dan.tp2021.productos.domain.Unidad;
import dan.tp2021.productos.services.dao.ProductoRepository;
import dan.tp2021.productos.services.dao.UnidadRepository;
import dan.tp2021.productos.services.interfaces.ProductoService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductoServiceImp implements ProductoService{
	
	@Autowired
	ProductoRepository productoRepo;
	
	@Autowired
	UnidadRepository unidadRepo;

	@Override
	public Optional<Material> guardarProducto(Material producto) {
		
		//Primeros guardamos la unidad
		Unidad unidadGuardada = unidadRepo.save(producto.getUnidad());
		if(unidadGuardada == null) {
			//no se pudo guardar
			return Optional.empty();
		}
		producto.setUnidad(unidadGuardada);
		producto = productoRepo.save(producto);
		if(producto == null) {
			//no se pudo guardar
			return Optional.empty();
		}
		return Optional.of(producto);
	}

}
