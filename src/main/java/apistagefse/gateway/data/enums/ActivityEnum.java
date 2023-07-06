/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package apistagefse.gateway.data.enums;

import lombok.Getter;

public enum ActivityEnum {

	VERIFICA("V"), VALIDATION("P");

	@Getter
	private String code;

	private ActivityEnum(String inCode) {
		code = inCode;
	}

}
