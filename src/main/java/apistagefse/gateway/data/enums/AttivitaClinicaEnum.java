/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package apistagefse.gateway.data.enums;

import lombok.Getter;

@Getter
public enum AttivitaClinicaEnum {

	PHR("PHR", "Personal Health Record Update"),
	CON("CON", "Consulto"),
	DIS("DIS", "Discharge"),
	ERP("ERP", "Erogazione Prestazione Prenotata"),
	Sistema_TS("Sistema TS", "Documenti sistema TS"),
	INI("INI","Documenti INI");

	private String code;
	private String description;

	private AttivitaClinicaEnum(String inCode, String inDescription) {
		code = inCode;
		description = inDescription;
	}

}