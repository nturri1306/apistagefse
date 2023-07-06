package apistagefse.gateway.imported.validator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SchematronValidationResultDTO {

	private Boolean validSchematron;
	
	private Boolean validXML;
	
	private String message;
	
	private List<SchematronFailedAssertionDTO> failedAssertions;
	
}