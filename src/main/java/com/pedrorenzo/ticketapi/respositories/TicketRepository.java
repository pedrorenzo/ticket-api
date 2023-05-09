package com.pedrorenzo.ticketapi.respositories;

import com.pedrorenzo.ticketapi.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByCompanyId(Long companyId);

}
