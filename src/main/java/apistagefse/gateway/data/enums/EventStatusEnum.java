/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package apistagefse.gateway.data.enums;

import lombok.Getter;

public enum EventStatusEnum {

	SUCCESS("SUCCESS"),
	BLOCKING_ERROR("BLOCKING_ERROR"),
	NON_BLOCKING_ERROR("NON_BLOCKING_ERROR"),
	ASYNC_RETRY("ASYNC_RETRY");

	@Getter
	private final String name;

	EventStatusEnum(String inName) {
		name = inName;
	}

}
