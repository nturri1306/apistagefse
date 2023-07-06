/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package apistagefse.gateway.data.enums;

import lombok.Getter;

@Getter
public enum IniActivityEnum {

	CREATE("CREATE", "Creazione"),
	READ("READ", "Lettura"),
	UPDATE("UPDATE", "Aggiornamento"),
	DELETE("DELETE", "Cancellazione");

	private String code;
	private String description;

	private IniActivityEnum(String inCode, String inDescription) {
		code = inCode;
		description = inDescription;
	}

}