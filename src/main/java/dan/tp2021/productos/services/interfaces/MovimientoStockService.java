package dan.tp2021.productos.services.interfaces;

import java.util.Optional;


import dan.tp2021.productos.domain.DetalleProvision;
import dan.tp2021.productos.domain.MovimientosStock;

public interface MovimientoStockService {

	public Optional<MovimientosStock> registrarMovimientoStock(Integer idDetallePedido);

	public Optional<DetalleProvision> generarOrdenProvision(MovimientosStock movStock);
	
	public Optional<MovimientosStock> guardarMovimientoStock(MovimientosStock movStock);
}
