package org.example.report;

import org.example.domain.report.BookedReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookedReportRepository extends JpaRepository<BookedReport, Long> {}