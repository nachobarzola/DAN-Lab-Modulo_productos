package dan.tp2021.productos.domain;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;


@Entity
public class MovimientosStock {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //valor autonumerico
	@Column(name="ID_MOVIMIENTO_STOCK")
	private Integer id;
	

	@OneToOne
	@JoinColumn(name="ID_DETALLE_PEDIDO")
	private DetallePedido detallePedido;
	
	@OneToOne
	@JoinColumn(name="ID_DETALLE_PROVISION")
	private DetalleProvision detalleProvision;
	
	@OneToOne
	@JoinColumn(name="ID_PRODUCTO")
	private Producto producto;
	
	@Column(name="CANTIDAD_ENTRADA")
	private Integer cantidadEntrada;
	
	@Column(name="CANTIDAD_SALIDA")
	private Integer cantidadSalida;
	
	private Instant fecha;
	
	
	
	public MovimientosStock(DetallePedido detallePedido, DetalleProvision detalleProvision, Producto producto,
			Integer cantidadEntrada, Integer cantidadSalida, Instant fecha) {
		super();
		this.detallePedido = detallePedido;
		this.detalleProvision = detalleProvision;
		this.producto = producto;
		this.cantidadEntrada = cantidadEntrada;
		this.cantidadSalida = cantidadSalida;
		this.fecha = fecha;
	}
	
	public MovimientosStock() {
		super();
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public DetallePedido getDetallePedido() {
		return detallePedido;
	}
	public void setDetallePedido(DetallePedido detallePedido) {
		this.detallePedido = detallePedido;
	}
	public DetalleProvision getDetalleProvision() {
		return detalleProvision;
	}
	public void setDetalleProvision(DetalleProvision detalleProvision) {
		this.detalleProvision = detalleProvision;
	}
	public Producto getProducto() {
		return producto;
	}
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	public Integer getCantidadEntrada() {
		return cantidadEntrada;
	}
	public void setCantidadEntrada(Integer cantidadEntrada) {
		this.cantidadEntrada = cantidadEntrada;
	}
	public Integer getCantidadSalida() {
		return cantidadSalida;
	}
	public void setCantidadSalida(Integer cantidadSalida) {
		this.cantidadSalida = cantidadSalida;
	}
	public Instant getFecha() {
		return fecha;
	}
	public void setFecha(Instant fecha) {
		this.fecha = fecha;
	}
	
	
}
