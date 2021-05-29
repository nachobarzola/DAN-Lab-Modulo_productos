package dan.tp2021.productos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class DetalleProvision {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //valor autonumerico
	@Column(name="ID_DETALLE_PROVISION")
	private Integer id;
	
	@OneToOne
	@JoinColumn(name="ID_PRODUCTO")
	private Producto producto;
	
	private Integer cantidad;
	
	@ManyToOne
	@JoinColumn(name="ID_PROVISION")
	private Provision provision;

	
	
	public DetalleProvision(Producto producto, Integer cantidad, Provision provision) {
		super();
		this.producto = producto;
		this.cantidad = cantidad;
		this.provision = provision;
	}

	public DetalleProvision() {
		super();
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	public Producto getProducto() {
		return producto;
	}
	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Provision getProvision() {
		return provision;
	}

	public void setProvision(Provision provision) {
		this.provision = provision;
	}
	
	
	
}
