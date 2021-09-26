package ru.irlix.evaluation.dto.request;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstimationFilterRequest extends EstimationPageRequest {

    private String name;

    private String client;

    private Long status;

    private Instant beginDate;

    private Instant endDate;

    private String creator;

    @Override
    public String toString() {
        return "EstimationFilterRequest(" +
                "page=" + getPage() + ", " +
                "size=" + getSize() + ", " +
                "nameSortField=" + getNameSortField() + ", " +
                "sortAsc=" + getSortAsc() + ", " +
                "name=" + name + ", " +
                "client=" + client + ", " +
                "status=" + status + ", " +
                "beginDate=" + beginDate + ", " +
                "endDate=" + endDate + ", " +
                "creator=" + creator +
                ')';
    }
}
