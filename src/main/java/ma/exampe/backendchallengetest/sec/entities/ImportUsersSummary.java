package ma.exampe.backendchallengetest.sec.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class ImportUsersSummary {
    private int totalRecords;
    private int importedRecords;
    private int failedRecords;
}
