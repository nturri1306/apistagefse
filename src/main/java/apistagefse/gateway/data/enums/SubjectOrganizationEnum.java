/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package apistagefse.gateway.data.enums;

import lombok.Getter;

public enum SubjectOrganizationEnum {

	REGIONE_PIEMONTE("010", "Regione Piemonte"),
	REGIONE_VALLE_AOSTA("020", "Regione Valle d'Aosta"),
	REGIONE_LOMBARDIA("030", "Regione Lombardia"),
	REGIONE_BOLZANO("041", "P.A. Bolzano"),
	REGIONE_TRENTO("042", "P.A. Trento"),
	REGIONE_VENETO("050", "Regione Veneto"),
	REGIONE_FRIULI_VENEZIA_GIULIA("060", "Regione Friuli Venezia Giulia"),
	REGIONE_LIGURIA("070", "Regione Liguria"),
	REGIONE_EMILIA_ROMAGNA("080", "Regione Emilia-Romagna"),
	REGIONE_TOSCANA("090", "Regione Toscana"),
	REGIONE_UMBRIA("100", "Regione Umbria"),
	REGIONE_MARCHE("110", "Regione Marche"),
	REGIONE_LAZIO("120", "Regione Lazio"),
	REGIONE_ABBRUZZO("130", "Regione Abruzzo"),
	REGIONE_MOLISE("140", "Regione Molise"),
	REGIONE_CAMPANIA("150", "Regione Campania"),
	REGIONE_PUGLIA("160", "Regione Puglia"),
	REGIONE_BASILICATA("170", "Regione Basilicata"),
	REGIONE_CALABRIA("180", "Regione Calabria"),
	REGIONE_SICILIA("190", "Regione Sicilia"),
	REGIONE_SARDEGNA("200", "Regione Sardegna"),
	INI("000", "INI"),
	SASN("001", "SASN"),
	MDS("999", "MDS");

	@Getter
	private final String code;
	@Getter
	private final String display;

	SubjectOrganizationEnum(String inCode, String inDisplay) {
		code = inCode;
		display = inDisplay;
	}

	public static SubjectOrganizationEnum getCode(String inCode) {
		SubjectOrganizationEnum out = null;
		for (SubjectOrganizationEnum v: SubjectOrganizationEnum.values()) {
			if (v.getCode().equalsIgnoreCase(inCode)) {
				out = v;
				break;
			}
		}
		return out;
	}
	
	public static SubjectOrganizationEnum getDisplay(String inDisplay) {
		SubjectOrganizationEnum out = null;
		for (SubjectOrganizationEnum v: SubjectOrganizationEnum.values()) {
			if (v.getDisplay().equalsIgnoreCase(inDisplay)) {
				out = v;
				break;
			}
		}
		return out;
	}
}
