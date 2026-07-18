package app.turnofacil.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    List<Appointment> findAllByOrderByAppointmentDateAscStartTimeAsc();

    @Query("""
            select count(a) from Appointment a
            where a.employee.id = :employeeId
              and a.appointmentDate = :date
              and a.status <> app.turnofacil.appointment.AppointmentStatus.CANCELLED
              and a.startTime < :endTime
              and a.endTime > :startTime
              and (:excludedId is null or a.id <> :excludedId)
            """)
    long countEmployeeOverlaps(@Param("employeeId") UUID employeeId,
                               @Param("date") LocalDate date,
                               @Param("startTime") LocalTime startTime,
                               @Param("endTime") LocalTime endTime,
                               @Param("excludedId") UUID excludedId);
}
