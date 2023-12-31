package edu.ncsu.csc.itrust2.services.security;

import edu.ncsu.csc.itrust2.models.security.LogEntry;
import edu.ncsu.csc.itrust2.repositories.security.LogEntryRepository;
import edu.ncsu.csc.itrust2.services.Service;

import java.time.ZonedDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class LogEntryService extends Service {

    private final LogEntryRepository repository;

    @Override
    protected JpaRepository getRepository() {
        return repository;
    }

    public List<LogEntry> findAllForUser(final String user) {
        return repository.findByPrimaryUserOrSecondaryUser(user);
    }

    public List<LogEntry> findByDateRange(
            final String user, final ZonedDateTime startDate, final ZonedDateTime endDate) {

        final List<LogEntry> withinRange = repository.findByTimeBetween(startDate, endDate);

        return withinRange.stream()
                .filter(e -> e.getPrimaryUser().equals(user) || e.getSecondaryUser().equals(user))
                .toList();
    }
}
