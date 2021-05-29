package dan.tp2021.productos.services.interfaces;

import java.util.List;
import java.util.Optional;

import dan.tp2021.productos.domain.DetalleProvision;
import dan.tp2021.productos.domain.MovimientosStock;
import dan.tp2021.productos.domain.Provision;

public interface ProvisionService {

	public Optional<DetalleProvision> generarDetalleProvision(MovimientosStock movStock);

	public Optional<Provision> generarOrdenDeProvision(List<DetalleProvision> listaDetalleProvision);
	
}
