package dan.tp2021.productos.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dan.tp2021.productos.domain.DetallePedido;
import dan.tp2021.productos.domain.DetalleProvision;
import dan.tp2021.productos.domain.Material;
import dan.tp2021.productos.domain.MovimientosStock;
import dan.tp2021.productos.domain.Provision;
import dan.tp2021.productos.services.dao.DetallePedidoRepository;
import dan.tp2021.productos.services.dao.DetalleProvisionRepository;
import dan.tp2021.productos.services.dao.MovimientosStockRepository;
import dan.tp2021.productos.services.dao.ProductoRepository;
import dan.tp2021.productos.services.dao.ProvisionRepository;
import dan.tp2021.productos.services.interfaces.MovimientoStockService;
import dan.tp2021.productos.services.interfaces.ProductoService;

@Service
public class MovimientoStockServiceImp implements MovimientoStockService {
	
	//Q_OPTIOMO es el tamaño del lote que se va a pedir provision
	private final Integer Q_OPTIMO = 900; //Valor al azar

	@Autowired
	DetallePedidoRepository detallePedidoRepo;

	@Autowired
	ProductoService productoService;
	
	@Autowired
	ProvisionRepository provisionRepo;
	
	@Autowired
	DetalleProvisionRepository detalleProvisionRepo;
	
	@Autowired
	MovimientosStockRepository movimientoStockRepo;

	@Override
	public Optional<MovimientosStock> registrarMovimientoStock(Integer idDetallePedido) {
		MovimientosStock movStockNuevo = new MovimientosStock();
		//
		movStockNuevo.setFecha(Instant.now()); // fecha actual
		// Tengo que buscar el detalle pedido asociado al pedido en el repo
		Optional<DetallePedido> optDetallePedidoBuscado = detallePedidoRepo.findById(idDetallePedido);
		// Si no lo encuentra
		if (optDetallePedidoBuscado.isEmpty()) {
			return null;
		}
		// Si lo encuentra, setemos los demas datos
		DetallePedido detallePedido = optDetallePedidoBuscado.get();
		//
		movStockNuevo.setDetallePedido(detallePedido);
		movStockNuevo.setCantidadSalida(detallePedido.getCantidad());
		movStockNuevo.setCantidadEntrada(0);
		Material producto = detallePedido.getMaterial();
		movStockNuevo.setMaterial(producto);

		// Debemos generar una provision??
		Integer stockLuegoDelPedido = producto.getStockActual() - detallePedido.getCantidad();
		if (stockLuegoDelPedido < producto.getStockMinimo()) {
			// Generamos orden de provision
			generarOrdenProvision(movStockNuevo);
			// Cambiar valores de producto
			// El stock es stock luego del pedido mas la provision.
			producto.setStockActual(stockLuegoDelPedido +movStockNuevo.getCantidadEntrada());
			// Actualizar producto/material
			productoService.actualizarProducto(producto);

		} else {
			// ----No se genera orden de provision
			// Cambiar valor de stock producto
			producto.setStockActual(stockLuegoDelPedido);
			// Actualizar producto/material
			productoService.actualizarProducto(producto);
		}

		// Guardar movimientosStock
		return guardarMovimientoStock(movStockNuevo);
	}

	@Override
	public Optional<DetalleProvision> generarOrdenProvision(MovimientosStock movStock) {
		Provision provision = new Provision();
		// TODO: Para simplificar uso la fecha de provision como la actual(Provision instantanea)
		provision.setFechaProvision(Instant.now());
		//
		DetalleProvision detalleProvision = new DetalleProvision();
		
		//Calcular cantidad de provision
		Integer cantidadProvision = calcularCantidadProvision(movStock);
		//
		detalleProvision.setCantidad(cantidadProvision);
		detalleProvision.setMaterial(movStock.getMaterial());
		List<DetalleProvision> listaDetalleProvision = new ArrayList<>();
		listaDetalleProvision.add(detalleProvision);
		provision.setDetalle(listaDetalleProvision); //TODO: no entiendo porque recibe una lista
		movStock.setDetalleProvision(detalleProvision);
		movStock.setCantidadEntrada(cantidadProvision);
		//Guardar provision
		Provision provisionGuardada = provisionRepo.save(provision);
		if(provisionGuardada == null) {
			return Optional.empty();
		}
		//Guardar detalleProvion
		detalleProvision.setProvision(provisionGuardada);
		DetalleProvision detalleProvisionGuardado = detalleProvisionRepo.save(detalleProvision);
		if(detalleProvisionGuardado == null) {
			return Optional.empty();
		}
		return Optional.of(detalleProvisionGuardado);
	}

	@Override
	public Optional<MovimientosStock> guardarMovimientoStock(MovimientosStock movStock) {
		return Optional.of(movimientoStockRepo.save(movStock));
	}
	
	private Integer calcularCantidadProvision(MovimientosStock movStock) {
		//Iniciamos con un lote
		Integer multiploQOptiomo = calcularMultiplosQOptiomo(movStock, 1);
		return (multiploQOptiomo*Q_OPTIMO);
	}
	
	private Integer calcularMultiplosQOptiomo(MovimientosStock movStock, Integer multiploQOptiomo) {
		Integer stockDespuesDeProvision = (movStock.getMaterial().getStockActual() 
				- movStock.getCantidadSalida()) + (multiploQOptiomo * Q_OPTIMO);
		if(stockDespuesDeProvision > movStock.getMaterial().getStockMinimo()) {
			return multiploQOptiomo;
		}
		else {
			multiploQOptiomo++;
			return calcularMultiplosQOptiomo(movStock, multiploQOptiomo);
		}
	}

}
