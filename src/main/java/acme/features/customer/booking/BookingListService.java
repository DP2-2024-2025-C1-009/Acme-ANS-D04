
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.realms.Customer;

@GuiService
public class BookingListService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private BookingRepository bookingRepository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Booking> res;
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		res = this.bookingRepository.findBookingByCustomerId(customerId);

		super.getBuffer().addData(res);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset data = super.unbindObject(booking, "locatorCode", "purchaseTime", "flightClass", "prize", "lastNibble");

		data.put("flightClass", booking.getFlightClass() != null ? booking.getFlightClass().toString() : "");
		data.put("prize", booking.getPrize() != null ? booking.getPrize() : "");
		data.put("lastNibble", booking.getLastNibble() != null ? booking.getLastNibble() : "");

		super.getResponse().addData(data);
	}

}
