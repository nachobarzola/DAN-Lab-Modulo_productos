package dan.tp2021.productos.services;

import dan.tp2021.productos.domain.Producto;

import dan.tp2021.productos.domain.Unidad;
import dan.tp2021.productos.services.dao.ProductoRepository;
import dan.tp2021.productos.services.dao.UnidadRepository;
import dan.tp2021.productos.services.interfaces.MovimientoStockService;
import dan.tp2021.productos.services.interfaces.ProductoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.jms.JMSException;
import javax.jms.MapMessage;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;


@Service
public class ProductoServiceImp implements ProductoService {

	@Autowired
	MovimientoStockService movimientoStockService;
	
	@Autowired
	ProductoRepository productoRepo;

	@Autowired
	UnidadRepository unidadRepo;
	
	@Autowired
	JmsTemplate jms; //jms: java message service

	@Transactional
	@Override
	public Optional<Producto> guardarProducto(Producto producto) {
		// Primeros guardamos la unidad
		Optional<Unidad> optUnidadGuardada = this.guardarUnidad(producto.getUnidad());
		if(optUnidadGuardada.isEmpty()) {
			return Optional.empty();
		}
		//Le asignamos la unidad al producto
		producto.setUnidad(optUnidadGuardada.get());
		try {
			//Guardamos el producto
			producto = productoRepo.save(producto);
			return Optional.of(producto);
		} catch (Exception e) {
			//e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Optional.empty();
		}
	}
	@Override
	public Optional<Unidad> guardarUnidad(Unidad unidad) {
		Unidad unidadGuardada = null;
		try {
			unidadGuardada = unidadRepo.save(unidad);
			return Optional.of(unidadGuardada);
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Optional.empty();
		}
	}
	

	@Override
	public Optional<Producto> actualizarProducto(Producto producto) {
		actualizarUnidad(producto.getUnidad());
		return Optional.of(productoRepo.save(producto));
	}

	@Override
	public Optional<Unidad> actualizarUnidad(Unidad unidad) {
		return Optional.of(unidadRepo.save(unidad));
	}

	@Override
	public Optional<Producto> getProducto(Integer idProducto) {
		return productoRepo.findById(idProducto);
	}

	@Override
	public List<Producto> getAllProducto() {
		return productoRepo.findAll();
	}

	@Override
	public Optional<Producto> getProductoPorNombre(String nombreProducto) {
		return productoRepo.findByNombre(nombreProducto);
	}

	@Override
	public List<Producto>  getProductoPorRangoStock(Integer rangoMin, Integer rangoMax) {
		return productoRepo.findByStockActualBetween(rangoMin, rangoMax);
	}

	@Override
	public List<Producto>  getProductoPorPrecio(Double precio) {
		return productoRepo.findByPrecio(precio);
	}
	

	@JmsListener(destination = "COLA_PEDIDOS")
	public void recepcionPedidoColaPedidos(MapMessage msg) throws JmsException {
		/*Cada vez que llegue un pedido a la cola de pedido, 
		 * el microservicio de productos escuchará sobre dicha cola y 
		 * se registra un movimiento de stock del producto y además 
		 * actualizará el stock actual en la tabla de productos. 
		 * Si se llegó a un stock debajo del mínimo se crea una 
		 * nueva orden de provisión.
		 */	
		try {
			//Integer idPedido = msg.getInt("idPedido");
			//No se como se mandara la fecha
			//Double fechaPedido = msg.getDouble("fechaPedido");
			//Integer idDetallePedido= msg.getInt("idDetallePedido");
			//System.out.println("El id detalle pedido es: "+idDetallePedido);
			//Registramos un movimiento de stock
			//movimientoStockService.registrarMovimientoStock(idDetallePedido);
			
			List<Integer> listaIdDetallePedido = new ArrayList<>();
			
			Integer cantidadDetalle = msg.getInt("cantidadDetalle");

			for(int i=1;i<=cantidadDetalle;i++){
			    Integer idDetallePedido = msg.getInt("idDetallePedido"+i);
			    listaIdDetallePedido.add(idDetallePedido);

			}
			System.out.println("Lei el pedido:"+cantidadDetalle);
			  movimientoStockService.registrarMovimientoStock(listaIdDetallePedido);
			
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
	}

}
