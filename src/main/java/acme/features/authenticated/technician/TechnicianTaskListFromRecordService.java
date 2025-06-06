
package acme.features.authenticated.technician;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenance.MaintenanceRecord;
import acme.entities.maintenance.Task;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskListFromRecordService extends AbstractGuiService<Technician, Task> {

	@Autowired
	private TechnicianTaskRepository repository;


	@Override
	public void authorise() {
		boolean status;
		MaintenanceRecord maintenanceRecord;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(masterId);
		status = super.getRequest().getPrincipal().getActiveRealm().getId() == maintenanceRecord.getTechnician().getId();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Task> object;
		int technicianId;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();
		object = this.repository.findTasksByMaintenanceRecordId(masterId, technicianId);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Task task) {
		Dataset dataset;

		dataset = super.unbindObject(task, "ticker", "type", "priority");
		super.addPayload(dataset, task, "description", "estimatedDuration");

		super.getResponse().addData(dataset);
	}
}
