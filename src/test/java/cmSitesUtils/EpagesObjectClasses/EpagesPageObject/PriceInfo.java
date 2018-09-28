package cmSitesUtils.EpagesObjectClasses.EpagesPageObject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PriceInfo {

	@SerializedName("quantity")
	@Expose
	private Quantity quantity;
	@SerializedName("taxClass")
	@Expose
	private TaxClass taxClass;
	@SerializedName("price")
	@Expose
	private Price price;
	@SerializedName("depositPrice")
	@Expose
	private Object depositPrice;
	@SerializedName("ecoParticipationPrice")
	@Expose
	private Object ecoParticipationPrice;
	@SerializedName("priceWithDeposits")
	@Expose
	private PriceWithDeposits priceWithDeposits;
	@SerializedName("manufacturerPrice")
	@Expose
	private Object manufacturerPrice;
	@SerializedName("basePrice")
	@Expose
	private Object basePrice;

	public Quantity getQuantity() {
		return quantity;
	}

	public void setQuantity(Quantity quantity) {
		this.quantity = quantity;
	}

	public TaxClass getTaxClass() {
		return taxClass;
	}

	public void setTaxClass(TaxClass taxClass) {
		this.taxClass = taxClass;
	}

	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}

	public Object getDepositPrice() {
		return depositPrice;
	}

	public void setDepositPrice(Object depositPrice) {
		this.depositPrice = depositPrice;
	}

	public Object getEcoParticipationPrice() {
		return ecoParticipationPrice;
	}

	public void setEcoParticipationPrice(Object ecoParticipationPrice) {
		this.ecoParticipationPrice = ecoParticipationPrice;
	}

	public PriceWithDeposits getPriceWithDeposits() {
		return priceWithDeposits;
	}

	public void setPriceWithDeposits(PriceWithDeposits priceWithDeposits) {
		this.priceWithDeposits = priceWithDeposits;
	}

	public Object getManufacturerPrice() {
		return manufacturerPrice;
	}

	public void setManufacturerPrice(Object manufacturerPrice) {
		this.manufacturerPrice = manufacturerPrice;
	}

	public Object getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(Object basePrice) {
		this.basePrice = basePrice;
	}

}