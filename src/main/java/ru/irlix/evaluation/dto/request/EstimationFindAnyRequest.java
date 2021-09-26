package ru.irlix.evaluation.dto.request;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstimationFindAnyRequest extends EstimationPageRequest {

    private String text;

    private Long status;

    private Instant beginDate;

    private Instant endDate;

    @Override
    public String toString() {
        return "EstimationFindAnyRequest(" +
                "page=" + getPage() + ", " +
                "size=" + getSize() + ", " +
                "nameSortField=" + getNameSortField() + ", " +
                "sortAsc=" + getSortAsc() + ", " +
                "text='" + text +
                ", status=" + status +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
                ')';
    }
}
