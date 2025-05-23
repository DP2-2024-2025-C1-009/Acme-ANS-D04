
package acme.features.customer.passenger;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

@GuiService
public class PassengerUpdateService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private PassengerRepository passengerRepository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int id = super.getResponse().getData("id", int.class);
		Passenger passenger = this.passengerRepository.findPassengerById(id);
		super.getBuffer().addData(passenger);
	}

	@Override
	public void bind(final Passenger passenger) {
		super.bindObject(passenger, "fullName", "email", "passport", "birthDate", "specialNeeds");
	}

	@Override
	public void validate(final Passenger passenger) {
		List<Passenger> res = this.passengerRepository.findAllPassengers();

		if (res.contains(passenger))
			super.state(false, "passenger", "passenger.already-published");

		boolean confirmation;
		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Passenger passenger) {
		this.passengerRepository.save(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset data = super.unbindObject(passenger, "fullName", "email", "passport", "birthDate", "specialNeeds");
		super.getResponse().addData(data);
	}

}
