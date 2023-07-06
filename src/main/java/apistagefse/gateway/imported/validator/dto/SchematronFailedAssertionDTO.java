/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package apistagefse.gateway.imported.validator.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SchematronFailedAssertionDTO {
	
	private String location;
	
	private String text;
	
	private String test;
}