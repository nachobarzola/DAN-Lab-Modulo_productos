package dan.tp2021.productos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.springframework.transaction.annotation.Transactional;


@Entity
@Transactional(readOnly= true)
public class DetallePedido {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //valor autonumerico
	@Column(name="ID_DETALLE_PEDIDO")
	private Integer id;
	
	@OneToOne
	@JoinColumn(name="ID_MATERIAL")
	private Material material;
	
	private Integer cantidad;

	

	public DetallePedido(Material material, Integer cantidad) {
		super();
		this.material = material;
		this.cantidad = cantidad;
	}
	
	
	public DetallePedido() {
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
	public Material getMaterial() {
		return material;
	}
	public void setMaterial(Material material) {
		this.material = material;
	}
	
	
	
}
