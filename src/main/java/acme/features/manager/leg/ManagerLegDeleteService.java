
package acme.features.manager.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.legs.Leg;
import acme.entities.legs.LegStatus;
import acme.realms.Manager;

@GuiService
public class ManagerLegDeleteService extends AbstractGuiService<Manager, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerLegRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int legId;
		Leg leg;

		legId = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(legId);
		Manager current = (Manager) super.getRequest().getPrincipal().getActiveRealm();
		status = leg != null && leg.isDraftMode() && leg.getFlight().getManager().equals(current);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Leg leg;
		int id;

		id = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(id);

		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {
		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "departureAirport", "arrivalAirport", "aircraft");
	}

	@Override
	public void validate(final Leg leg) {
		;
	}

	@Override
	public void perform(final Leg leg) {
		this.repository.delete(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		int managerId;
		Manager manager;
		Dataset dataset;
		Collection<Aircraft> aircrafts;
		Collection<Airport> departureAirports;
		Collection<Airport> arrivalAirports;
		SelectChoices aircraftChoices;
		SelectChoices arrivalAirportChoices;
		SelectChoices departureAirportChoices;
		SelectChoices legStatusChoices;

		managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		manager = this.repository.findManagerById(managerId);
		aircrafts = this.repository.findAircraftsByAirlineId(manager.getAirline().getId());
		aircraftChoices = SelectChoices.from(aircrafts, "numberRegistration", leg.getAircraft());

		departureAirports = this.repository.findAllAirports();
		arrivalAirports = this.repository.findAllAirports();
		departureAirportChoices = SelectChoices.from(departureAirports, "airportName", leg.getDepartureAirport());
		arrivalAirportChoices = SelectChoices.from(arrivalAirports, "airportName", leg.getArrivalAirport());

		legStatusChoices = SelectChoices.from(LegStatus.class, leg.getStatus());

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "draftMode");
		dataset.put("status", legStatusChoices.getSelected().getKey());
		dataset.put("legStatuses", legStatusChoices);
		dataset.put("duration", leg.getDuration());
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("departureAirport", departureAirportChoices.getSelected().getKey());
		dataset.put("departureAirports", departureAirportChoices);
		dataset.put("arrivalAirport", arrivalAirportChoices.getSelected().getKey());
		dataset.put("arrivalAirports", arrivalAirportChoices);
		super.getResponse().addData(dataset);
	}
}
