package app.rest.invit.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.rest.invit.entities.EventEntity;

@Repository
public interface EventRepository extends PagingAndSortingRepository<EventEntity, Long> {
    List<EventEntity> getByUserIdOrderByCreatedAtDesc(long userId);

    @Query(value = "SELECT * FROM events p WHERE p.exposure_id = :exposure and p.expires_at > :now ORDER BY created_at DESC LIMIT 20", nativeQuery = true)
    List<EventEntity> getLastPublicEvents(@Param("exposure") long exposureId, @Param("now") Date now);

    EventEntity findByEventId(String eventId);
}
