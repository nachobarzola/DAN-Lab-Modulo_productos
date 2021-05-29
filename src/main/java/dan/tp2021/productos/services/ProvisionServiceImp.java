package dan.tp2021.productos.services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dan.tp2021.productos.domain.DetalleProvision;
import dan.tp2021.productos.domain.MovimientosStock;
import dan.tp2021.productos.domain.Provision;
import dan.tp2021.productos.services.dao.DetalleProvisionRepository;
import dan.tp2021.productos.services.dao.ProvisionRepository;
import dan.tp2021.productos.services.interfaces.ProvisionService;

@Service
public class ProvisionServiceImp implements ProvisionService {

	@Autowired
	ProvisionRepository provisionRepo;

	@Autowired
	DetalleProvisionRepository detalleProvisionRepo;

	// Q_OPTIOMO es el tamaño del lote que se va a pedir provision
	private final Integer Q_OPTIMO = 900; // Valor al azar

	@Override
	public Optional<Provision> generarOrdenDeProvision(List<DetalleProvision> listaDetalleProvision) {
		Provision provision = new Provision();
		// TODO: Para simplificar uso la fecha de provision como la actual(Provision
		// instantanea)
		provision.setFechaProvision(Instant.now());
		//
		provision.setDetalle(listaDetalleProvision);

		// Guardar provision
		Provision provisionGuardada = provisionRepo.save(provision);
		if (provisionGuardada == null) {
			return Optional.empty();
		}

		for (DetalleProvision unDetalle : listaDetalleProvision) {
			unDetalle.setProvision(provisionGuardada);
			if (detalleProvisionRepo.save(unDetalle) == null) {
				return Optional.empty();
			}

		}
		
		return Optional.of(provision);

	}

	@Override
	public Optional<DetalleProvision> generarDetalleProvision(MovimientosStock movStock) {

		DetalleProvision detalleProvision = new DetalleProvision();

		// Calcular cantidad de provision
		Integer cantidadProvision = calcularCantidadProvision(movStock);
		//
		detalleProvision.setCantidad(cantidadProvision);
		detalleProvision.setProducto(movStock.getProducto());

		movStock.setDetalleProvision(detalleProvision);
		movStock.setCantidadEntrada(cantidadProvision);

		// Guardar detalleProvion
		detalleProvision.setProvision(null);
		DetalleProvision detalleProvisionGuardado = detalleProvisionRepo.save(detalleProvision);
		if (detalleProvisionGuardado == null) {
			return Optional.empty();
		}
		return Optional.of(detalleProvisionGuardado);
	}

	private Integer calcularCantidadProvision(MovimientosStock movStock) {
		// Iniciamos con un lote
		Integer multiploQOptiomo = calcularMultiplosQOptiomo(movStock, 1);
		return (multiploQOptiomo * Q_OPTIMO);
	}

	private Integer calcularMultiplosQOptiomo(MovimientosStock movStock, Integer multiploQOptiomo) {
		Integer stockDespuesDeProvision = (movStock.getProducto().getStockActual() - movStock.getCantidadSalida())
				+ (multiploQOptiomo * Q_OPTIMO);
		if (stockDespuesDeProvision > movStock.getProducto().getStockMinimo()) {
			return multiploQOptiomo;
		} else {
			multiploQOptiomo++;
			return calcularMultiplosQOptiomo(movStock, multiploQOptiomo);
		}
	}

}
