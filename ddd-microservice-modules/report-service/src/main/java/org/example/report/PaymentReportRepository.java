package org.example.report;

import org.example.domain.report.PaymentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentReportRepository extends JpaRepository<PaymentReport, Long> {}
