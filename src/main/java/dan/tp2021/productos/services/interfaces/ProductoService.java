package dan.tp2021.productos.services.interfaces;

import java.util.Optional;

import dan.tp2021.productos.domain.Material;

public interface ProductoService {
	public Optional<Material> guardarProducto(Material producto);

}
