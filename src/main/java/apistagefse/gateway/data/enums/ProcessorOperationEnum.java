/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package apistagefse.gateway.data.enums;

import lombok.Getter;

@Getter
public enum ProcessorOperationEnum {

	PUBLISH("PUBLISH"),
	DELETE("DELETE"),
	REPLACE("REPLACE"),
	UPDATE("UPDATE"),
	READ("READ");

	private String name;

	ProcessorOperationEnum(String pname) {
		name = pname;
	}
}