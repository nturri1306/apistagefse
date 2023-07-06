/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package apistagefse.gateway.data.enums;

import lombok.Getter;

public enum HealthcareFacilityEnum {

	Ospedale("Ospedale"),
	Prevenzione("Prevenzione"),
	Territorio("Territorio"),
	SistemaTS("SistemaTS"),
	Cittadino("Cittadino");

	@Getter
	private String code;

	private HealthcareFacilityEnum(String inCode) {
		code = inCode;
	}

}