package calendarios;


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static calendarios.Pending.*;
public class EventoRecurrente {
/*


  private String nombre;
  private LocalDateTime fechaInicio;
  private Duration duracion; //horas
  private Ubicacion ubicacion;
  private List<Usuario> invitados;

  public EventoRecurrente(String nombre, LocalDateTime fechaInicio, Duration duracion, Ubicacion ubicacion, List<Usuario> invitados) {
    this.nombre = nombre;
    this.fechaInicio = fechaInicio;
    this.duracion = duracion;
    this.ubicacion = ubicacion;
    this.invitados = invitados;
  }

  private LocalDateTime getInicio() {
    return fechaInicio;
  }
  public Duration cuantoFalta() {
    // Este es un ejemplo de cómo se puede obtener una duración
    // Modificar en caso de que sea necesario
    return Duration.ofHours(LocalDateTime.now().until(getInicio(), ChronoUnit.HOURS));
  }

  public boolean estaSolapadoCon(Evento otro) {
    return  this.compartenHorarioDeInicioOFin(otro)
        || this.otroEventoArrancaAntesyTerminaDespuesQueElInicio(otro)
        || this.otroEventoArrancaDespuesyTerminaAntesQueElFin(otro);
  }

  private LocalDateTime getFin(){
    return this.fechaInicio.plus(duracion);
  }

  private boolean compartenHorarioDeInicioOFin (Evento otro) {
    return this.getInicio().isEqual(otro.getInicio()) || this.getFin().isEqual(otro.getFin());
  }

  private boolean otroEventoArrancaAntesyTerminaDespuesQueElInicio (Evento otro) {
    return otro.getInicio().isBefore(this.getInicio()) && otro.getFin().isAfter(this.getInicio());
  }

  private boolean otroEventoArrancaDespuesyTerminaAntesQueElFin (Evento otro) {
    return otro.getInicio().isAfter(this.getInicio()) && otro.getInicio().isBefore(this.getFin());
  }
*/

}
