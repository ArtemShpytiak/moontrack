package analytics.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import analytics.model.user.ARealmUser;

@Entity
@Table(name = "iaps")
public class IAPPurchase {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "id")
	private int id;

	@ManyToOne
	@JoinColumn(name = "r_user")
	private ARealmUser realmUser;

	@Column(name = "date")
	private Date date;

	@Column(name = "iap")
	private int iap;

	@Column(name = "price")
	private float price;

	@Column(name = "respondStatus")
	private int respondStatus = -1;
	//0 - OK
	//223
	
	@Column(name = "sandbox")
	private Boolean isSandBox = false;

//	@Column(name = "platform") deprecated
//	private byte platform;
	
	@Column(name = "paymentProvider")
	private byte paymentProvider;
	
	@Column(name = "platform_id")
	private byte platformID;
	
	@Column(name = "os_id")
	private byte osID;

	public IAPPurchase() {
	}

	public IAPPurchase(ARealmUser realmUser, Date date, int iap, float price, int respondStatus,
			Boolean isSandBox, byte paymentProvider, byte osID, byte platformID) {
		super();
		this.realmUser = realmUser;
		this.date = date;
		this.iap = iap;
		this.price = price;
		this.respondStatus = respondStatus;
		this.isSandBox = isSandBox;
		this.paymentProvider = paymentProvider;
		this.osID = osID;
		this.platformID = platformID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getIap() {
		return iap;
	}

	public void setIap(int iap) {
		this.iap = iap;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getRespondStatus() {
		return respondStatus;
	}

	public void setRespondStatus(int respondStatus) {
		this.respondStatus = respondStatus;
	}

	public boolean isSandBox() {
		return isSandBox;
	}

	public void setSandBox(boolean isSandBox) {
		this.isSandBox = isSandBox;
	}

}
