package dan.tp2021.productos.domain;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class MovimientosStock {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //valor autonumerico
	@Column(name="ID_MOVIMIENTO_STOCK")
	private Integer id;
	
	@OneToOne
	@JoinColumn(name="ID_DETALLE_PEDIDO")
	@Column(name="DETALLE_PEDIDO")
	private DetallePedido detallePedido;
	
	@OneToOne
	@JoinColumn(name="ID_DETALLE_PEDIDO")
	@Column(name="DETALLE_PROVISION")
	private DetalleProvision detalleProvision;
	
	@OneToOne
	@JoinColumn(name="ID_MATERIAL")
	private Material material;
	
	@Column(name="CANTIDAD_ENTRADA")
	private Integer cantidadEntrada;
	
	@Column(name="CANTIDAD_SALIDA")
	private Integer cantidadSalida;
	
	@Temporal(TemporalType.DATE)
	private Instant fecha;
	
	
	
	public MovimientosStock(DetallePedido detallePedido, DetalleProvision detalleProvision, Material material,
			Integer cantidadEntrada, Integer cantidadSalida, Instant fecha) {
		super();
		this.detallePedido = detallePedido;
		this.detalleProvision = detalleProvision;
		this.material = material;
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
	public Material getMaterial() {
		return material;
	}
	public void setMaterial(Material material) {
		this.material = material;
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
