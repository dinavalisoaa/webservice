package mg.mbds.webservice.repository;

import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import mg.mbds.webservice.model.Room;
import mg.mbds.webservice.enums.RoomType;
import mg.mbds.webservice.model.Stay;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class RoomSpecification {

    private RoomSpecification() {}

    public static Specification<Room> isAvailableOn(LocalDate date) {
        return (root, query, cb) -> {
            Subquery<Long> occupancy = query.subquery(Long.class);
            Root<Stay> stay = occupancy.from(Stay.class);
            occupancy.select(cb.count(stay))
                    .where(
                            cb.equal(stay.get("room"), root),
                            cb.lessThanOrEqualTo(stay.get("startDate"), date),
                            cb.or(
                                    cb.isNull(stay.get("endDate")),
                                    cb.greaterThanOrEqualTo(stay.get("endDate"), date)
                            )
                    );

            return cb.and(
                    cb.equal(root.get("underMaintenance"), false),
                    cb.lt(occupancy, root.get("capacity").as(Long.class))
            );
        };
    }

    public static Specification<Room> hasType(RoomType type) {
        return (root, query, cb) ->
                type == null ? null : cb.equal(root.get("type"), type);
    }

    public static Specification<Room> hasMinCapacity(Integer minCapacity) {
        return (root, query, cb) ->
                minCapacity == null ? null : cb.greaterThanOrEqualTo(root.get("capacity"), minCapacity);
    }

    public static Specification<Room> hasPriceGreaterThanOrEqual(Double minPrice) {
        return (root, query, cb) ->
                minPrice == null ? null : cb.greaterThanOrEqualTo(root.get("pricePerNight"), minPrice);
    }

    public static Specification<Room> hasPriceLessThanOrEqual(Double maxPrice) {
        return (root, query, cb) ->
                maxPrice == null ? null : cb.lessThanOrEqualTo(root.get("pricePerNight"), maxPrice);
    }
}
