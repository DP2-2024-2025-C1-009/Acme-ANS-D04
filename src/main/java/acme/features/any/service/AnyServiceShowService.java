
package acme.features.any.service;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.service.Service;

@GuiService
public class AnyServiceShowService extends AbstractGuiService<Any, Service> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyServiceRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		Service service;

		id = super.getRequest().getData("id", int.class);
		service = this.repository.findServiceById(id);
		status = service != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Service service;
		int id;

		id = super.getRequest().getData("id", int.class);
		service = this.repository.findServiceById(id);

		super.getBuffer().addData(service);
	}

	@Override
	public void unbind(final Service service) {
		Dataset dataset;

		dataset = super.unbindObject(service, "name", "photoLink", "averageDwellTime", "promotionCode", "discountedMoney");

		super.getResponse().addData(dataset);
	}

}
