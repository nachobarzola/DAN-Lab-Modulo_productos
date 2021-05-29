package dan.tp2021.productos.services.dao;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dan.tp2021.productos.domain.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer>{
	
	public Optional<Producto> findByNombre(String nombre);
	
	public List<Producto> findByStockActualBetween(Integer rangoMin,Integer rangoMax);

	public List<Producto> findByPrecio(Double precio);
}
