package dan.tp2021.productos.services.interfaces;

import java.util.List;
import java.util.Optional;


import dan.tp2021.productos.domain.DetalleProvision;
import dan.tp2021.productos.domain.MovimientosStock;

public interface MovimientoStockService {

	public List<Optional<MovimientosStock>> registrarMovimientoStock(List<Integer> listaIdDetallePedido);

	
	public Optional<MovimientosStock> guardarMovimientoStock(MovimientosStock movStock);
}
