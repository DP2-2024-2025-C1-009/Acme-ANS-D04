
package acme.features.authenticated.technician.maintenanceRecord;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenance.MaintenanceRecord;
import acme.entities.maintenance.Status;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordUpdateService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		MaintenanceRecord maintenanceRecord;
		Technician technician;

		masterId = super.getRequest().getData("id", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(masterId);
		technician = maintenanceRecord == null ? null : maintenanceRecord.getTechnician();
		status = maintenanceRecord != null && maintenanceRecord.isDraftMode() && super.getRequest().getPrincipal().hasRealm(technician);

		if (status) {
			String method;
			int aircraftId;
			Aircraft aircraft;

			method = super.getRequest().getMethod();

			if (method.equals("GET"))
				status = true;
			else {
				aircraftId = super.getRequest().getData("aircraft", int.class);
				aircraft = this.repository.findAircraftById(aircraftId);
				status = aircraftId == 0 || aircraft != null;
			}
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		MaintenanceRecord maintenanceRecord;
		int id = super.getRequest().getData("id", int.class);

		maintenanceRecord = this.repository.findMaintenanceRecordById(id);

		super.getBuffer().addData(maintenanceRecord);
	}

	@Override
	public void bind(final MaintenanceRecord maintenanceRecord) {
		Aircraft aircraft;
		int aircraftId;
		Date currentMoment;

		aircraftId = super.getRequest().getData("aircraft", int.class);
		aircraft = this.repository.findAircraftById(aircraftId);
		currentMoment = MomentHelper.getCurrentMoment();

		super.bindObject(maintenanceRecord, "ticker", "status", "nextInspectionDueDate", "estimatedCost", "notes");
		maintenanceRecord.setMoment(currentMoment);
		maintenanceRecord.setAircraft(aircraft);
	}

	@Override
	public void validate(final MaintenanceRecord maintenanceRecord) {
		MaintenanceRecord existMaintenanceRecord;
		boolean validTicker;
		Date minimumNextInspection;
		boolean validNextInspection;

		existMaintenanceRecord = this.repository.findMaintenanceRecordByTicker(maintenanceRecord.getTicker());
		validTicker = existMaintenanceRecord == null || existMaintenanceRecord.getId() == maintenanceRecord.getId();
		if (!validTicker)
			super.state(validTicker, "ticker", "acme.validation.task-record.ticker.duplicated.message");

		minimumNextInspection = MomentHelper.deltaFromMoment(maintenanceRecord.getMoment(), 1L, ChronoUnit.HOURS);
		validNextInspection = maintenanceRecord.getNextInspectionDueDate() == null ? //
			false : //
			MomentHelper.isAfterOrEqual(maintenanceRecord.getNextInspectionDueDate(), minimumNextInspection);

		super.state(validNextInspection, "nextInspectionDueDate", "acme.validation.maintenance-record.moment-next-inspection.update.messsage");
	}

	@Override
	public void perform(final MaintenanceRecord maintenanceRecord) {
		this.repository.save(maintenanceRecord);
	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		Dataset dataset;
		SelectChoices statusChoices;
		SelectChoices aircraftChoices;
		Collection<Aircraft> aircrafts;

		statusChoices = SelectChoices.from(Status.class, maintenanceRecord.getStatus());
		aircrafts = this.repository.findAllAircrafts();
		aircraftChoices = SelectChoices.from(aircrafts, "numberRegistration", maintenanceRecord.getAircraft());

		dataset = super.unbindObject(maintenanceRecord, "ticker", "moment", "nextInspectionDueDate", "estimatedCost", "notes", "draftMode");
		dataset.put("status", statusChoices.getSelected().getKey());
		dataset.put("statuses", statusChoices);
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);

		super.getResponse().addData(dataset);
	}

}
