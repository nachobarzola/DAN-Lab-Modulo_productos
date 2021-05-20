package dan.tp2021.productos.services.dao;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dan.tp2021.productos.domain.Material;

@Repository
public interface ProductoRepository extends JpaRepository<Material, Integer>{
	
	public Optional<Material> findByNombre(String nombre);
	
	public List<Material> findByStockActualBetween(Integer rangoMin,Integer rangoMax);

	public List<Material> findByPrecio(Double precio);
}
