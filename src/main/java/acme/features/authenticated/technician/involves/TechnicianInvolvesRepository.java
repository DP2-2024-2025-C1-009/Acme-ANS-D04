
package acme.features.authenticated.technician.involves;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenance.Involves;
import acme.entities.maintenance.MaintenanceRecord;
import acme.entities.maintenance.Task;

@Repository
public interface TechnicianInvolvesRepository extends AbstractRepository {

	@Query("select i from Involves i where i.maintenanceRecord.id = :masterId")
	Collection<Involves> findInvolvesByMasterId(int masterId);

	@Query("select i from Involves i where i.id = :id")
	Involves findInvolvesById(int id);

	@Query("select m from MaintenanceRecord m where m.id = :id")
	MaintenanceRecord findMaintenanceRecordById(int id);

	@Query("select t from Task t")
	Collection<Task> findAllTasks();

	@Query("select i.maintenanceRecord from Involves i where i.id = :id")
	MaintenanceRecord findMaintenanceRecordByInvolvesId(int id);

	@Query("select t from Task t where t.id = :taskId")
	Task findTaskById(int taskId);

	@Query("select i from Involves i where i.maintenanceRecord.technician.id = :technicianId")
	Collection<Involves> findInvolvesByTechnicianId(int technicianId);

	@Query("select m from MaintenanceRecord m where m.draftMode = true")
	Collection<MaintenanceRecord> findAllDraftMaintenanceRecords();

	@Query("select m from MaintenanceRecord m")
	Collection<MaintenanceRecord> findAllMaintenanceRecords();
}
