package apistagefse.gateway.imported.validator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExtractedInfoDTO {
	
	private String templateIdSchematron; //Schematron
	
	private String typeIdExtension; //Schema
}
