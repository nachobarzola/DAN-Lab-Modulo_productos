package dan.tp2021.productos.services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dan.tp2021.productos.domain.DetallePedido;
import dan.tp2021.productos.domain.DetalleProvision;
import dan.tp2021.productos.domain.Producto;
import dan.tp2021.productos.domain.MovimientosStock;
import dan.tp2021.productos.domain.Provision;
import dan.tp2021.productos.services.dao.DetallePedidoRepository;
import dan.tp2021.productos.services.dao.DetalleProvisionRepository;
import dan.tp2021.productos.services.dao.MovimientosStockRepository;
import dan.tp2021.productos.services.dao.ProvisionRepository;
import dan.tp2021.productos.services.interfaces.MovimientoStockService;
import dan.tp2021.productos.services.interfaces.ProductoService;
import dan.tp2021.productos.services.interfaces.ProvisionService;

@Service
public class MovimientoStockServiceImp implements MovimientoStockService {

	@Autowired
	ProductoService productoService;

	@Autowired
	ProvisionService provisionService;

	@Autowired
	DetallePedidoRepository detallePedidoRepo;

	@Autowired
	MovimientosStockRepository movimientoStockRepo;

	@Override
	public List<Optional<MovimientosStock>> registrarMovimientoStock(List<Integer> listaIdDetallePedido) {
		List<DetalleProvision> listaDetalleProvision = new ArrayList<>();
		List<Optional<MovimientosStock>> listaMovimientoStock = new ArrayList<>();

		for (Integer idDetallePedido : listaIdDetallePedido) {

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
			Producto producto = detallePedido.getProducto();
			movStockNuevo.setProducto(producto);

			// Debemos generar una provision??
			Integer stockLuegoDelPedido = producto.getStockActual() - detallePedido.getCantidad();
			if (stockLuegoDelPedido < producto.getStockMinimo()) {
				// Generamos orden de provision

				listaDetalleProvision.add(provisionService.generarDetalleProvision(movStockNuevo).get());
				// Cambiar valores de producto
				// El stock es stock luego del pedido mas la provision.
				producto.setStockActual(stockLuegoDelPedido + movStockNuevo.getCantidadEntrada());
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
			listaMovimientoStock.add(guardarMovimientoStock(movStockNuevo));
		}
		if (!listaDetalleProvision.isEmpty()) {
			provisionService.generarOrdenDeProvision(listaDetalleProvision);
		}
		return listaMovimientoStock;

	}

	@Override
	public Optional<MovimientosStock> guardarMovimientoStock(MovimientosStock movStock) {
		return Optional.of(movimientoStockRepo.save(movStock));
	}

}
