
package acme.entities.aircraft;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidNumberRegistration;
import acme.entities.airline.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidNumberRegistration
public class Aircraft extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(min = 1, max = 50, message = "{acme.validation.aircraft.model-length}")
	@Automapped
	private String				model;

	@Mandatory
	@ValidString(min = 1, max = 50, message = "{acme-validation.aircraft.number-registration-length}")
	@Column(unique = true)
	private String				numberRegistration;

	@Mandatory
	@ValidNumber(min = 1, max = 255, message = "{acme.validation.aircraft.number-passengers-range}")
	@Automapped
	private Integer				numberPassengers;

	@Mandatory
	@ValidNumber(min = 2000, max = 50000, message = "{acme.validation.aircraft.load-weigth-range}")
	@Automapped
	private Integer				loadWeight;

	@Mandatory
	@Automapped
	private AircraftStatus		status;

	@Optional
	@ValidString(max = 255, message = "{acme.validation.aircraft.optional-details-length}")
	@Automapped
	private String				optionalDetails;

	// Relationships -------------------------------------------------------------

	@Mandatory //
	@Valid
	@ManyToOne
	private Airline				airline;

	//Enum


	public enum AircraftStatus {
		ACTIVE, MAINTENANCE
	}

}
