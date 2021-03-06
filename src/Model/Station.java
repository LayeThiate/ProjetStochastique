package Model;

import java.awt.List;
import java.util.ArrayList;

public class Station {
	
	public String description;
	public String code;
	public int id;
	public int capaMax;
	public int disponible;
	public int nbPlaceDispo;
	public int nbVeloInitial;
	public double coutAjout;
	public double coutManque;
	public double coutTempsPerdu;
	public double latitude;
	public double longitude;
	public ArrayList<Integer> demande;
	
	public Station() {
		super();
		this.demande = new ArrayList<Integer>();
	}

	public Station(String description, String code, int id, int capaMax, int disponible, int nbPlaceDispo,
			int nbVeloInitial, int coutAjout, int coutManque, int coutTempsPerdu, float latitude, float longitude) {
		super();
		this.description = description;
		this.code = code;
		this.id = id;
		this.capaMax = capaMax;
		this.disponible = disponible;
		this.nbPlaceDispo = nbPlaceDispo;
		this.nbVeloInitial = nbVeloInitial;
		this.coutAjout = coutAjout;
		this.coutManque = coutManque;
		this.coutTempsPerdu = coutTempsPerdu;
		this.latitude = latitude;
		this.longitude = longitude;
		this.demande = new ArrayList<Integer>();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCapaMax() {
		return capaMax;
	}

	public void setCapaMax(int capaMax) {
		this.capaMax = capaMax;
	}

	public int getDisponible() {
		return disponible;
	}

	public void setDisponible(int disponible) {
		this.disponible = disponible;
	}

	public int getNbPlaceDispo() {
		return nbPlaceDispo;
	}

	public void setNbPlaceDispo(int nbPlaceDispo) {
		this.nbPlaceDispo = nbPlaceDispo;
	}

	public int getNbVeloInitial() {
		return nbVeloInitial;
	}

	public void setNbVeloInitial(int nbVeloInitial) {
		this.nbVeloInitial = nbVeloInitial;
	}

	public double getCoutAjout() {
		return coutAjout;
	}

	public void setCoutAjout(int coutAjout) {
		this.coutAjout = coutAjout;
	}

	public double getCoutManque() {
		return coutManque;
	}

	public void setCoutManque(int coutManque) {
		this.coutManque = coutManque;
	}

	public double getCoutTempsPerdu() {
		return coutTempsPerdu;
	}

	public void setCoutTempsPerdu(int coutTempsPerdu) {
		this.coutTempsPerdu = coutTempsPerdu;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || this.getClass() != obj.getClass())
			return false;
		return ((Station) obj).id == this.id;
	}

}
