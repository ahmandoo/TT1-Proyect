package modelo;

import java.util.Map;

public class DatosSolicitud {
	private Map<Integer, Integer> nums;
	private int ancho;
	private int generaciones;
	private int infectadosInit;
	private int vacunadosInit;
	private int porcentajeViajeros;


	public DatosSolicitud(Map<Integer, Integer> nums) {
		this.nums = nums;
	}

	public DatosSolicitud(Map<Integer, Integer> nums, int ancho, int generaciones, int infectadosInit, int vacunadosInit, int porcentajeViajeros) {
		this.nums = nums;
		this.ancho = ancho;
		this.generaciones = generaciones;
		this.infectadosInit = infectadosInit;
		this.vacunadosInit = vacunadosInit;
		this.porcentajeViajeros = porcentajeViajeros;
	}

	public Map<Integer, Integer> getNums() {
		return nums;
	}

	public int getAncho() {
		return ancho;
	}

	public int getGeneraciones() {
		return generaciones;
	}

	public int getInfectadosInit() {
		return infectadosInit;
	}

	public int getVacunadosInit() {
		return vacunadosInit;
	}

	public int getPorcentajeViajeros() {
		return porcentajeViajeros;
	}
}