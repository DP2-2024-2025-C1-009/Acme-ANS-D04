
package acme.features.manager.leg;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import acme.entities.legs.Leg;
import acme.realms.Manager;

@Repository
public interface ManagerLegRepository extends AbstractRepository {

	@Query("select l from Leg l where l.flight.manager.id = :managerId")
	Collection<Leg> findLegsByManagerId(int managerId);

	@Query("select l from Leg l where l.id = :masterId")
	Leg findLegById(int masterId);

	@Query("select l from Leg l where l.flight.id = :flightId ORDER BY l.scheduledDeparture ASC")
	Collection<Leg> findLegsByFlightId(int flightId);

	@Query("select f from Flight f where f.id = :masterId")
	Flight findFlightById(int masterId);

	@Query("select m from Manager m where m.id = :masterId")
	Manager findManagerById(int masterId);

	@Query("select a from Aircraft a where a.airline.id = :airlineId")
	Collection<Aircraft> findAircraftsByAirlineId(int airlineId);

	@Query("select a from Aircraft a where a.id = :id")
	Aircraft findAircraftById(int id);

	@Query("select a from Airport a where a.id = :id")
	Airport findAirportById(int id);

	@Query("select a from Airport a")
	Collection<Airport> findAllAirports();

	@Query("SELECT COUNT(c) FROM Claim c WHERE c.leg.id = :legId")
	int countClaimsByLegId(@Param("legId") int legId);
}
