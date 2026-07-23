package seov.se_app.common.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "company_calendar")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CompanyCalendar extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Ngày */
    @Column(nullable = false, unique = true)
    private LocalDate workDate;

    /**
     * Có làm việc hay không*/
    @Column(nullable = false)
    private Boolean workingDay;

    /*** Ghi chú*/
    private String description;

}