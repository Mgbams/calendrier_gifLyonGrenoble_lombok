package fr.orsys.fx.calendrier_gif.business;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Jour {

	@Id
	private LocalDate date;
	
	@Min(20)
	@Max(50)
	private int nbPoints;
	
	@OneToOne
	private Gif gif;
	
	public Jour(LocalDate date, int nbPoints) {
		this.date = date;
		this.nbPoints = nbPoints;
	}

	public String toString() {
        return date.getDayOfMonth() + "/" +  date.getMonthValue();
    }
	
}