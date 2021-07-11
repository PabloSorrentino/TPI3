package calendarios;



import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static calendarios.Pending.*;

public class Evento {


  private String nombre;
  private LocalDateTime fechaInicio;
  private LocalDateTime fechaFin;
  private Duration duracion; //horas
  private Ubicacion ubicacion;
  private List<Usuario> invitados;

  public Evento(String nombre, LocalDateTime fechaInicio, LocalDateTime fechaFin, Ubicacion ubicacion, List<Usuario> invitados) {
    this.nombre = nombre;
    this.fechaInicio = fechaInicio;
    this.fechaFin = fechaFin;
    this.ubicacion = ubicacion;
    this.invitados = invitados;
  }

  private LocalDateTime getInicio() {
    return fechaInicio;
  }

  private LocalDateTime getFin() {
    return this.fechaInicio.plus(duracion);
  }


  public Duration cuantoFalta() {
    // Este es un ejemplo de cómo se puede obtener una duración
    // Modificar en caso de que sea necesario
    return Duration.ofHours(LocalDateTime.now().until(getInicio(), ChronoUnit.HOURS));
  }

  public boolean estaEntreDosFechas(LocalDateTime otroInicio, LocalDateTime otroFin) {
    return fechaInicio.compareTo(otroInicio) >= 0 && fechaFin.compareTo(otroFin) <= 0;
  }


  public boolean estaSolapadoCon(Evento otro) {
    return this.compartenHorarioDeInicioOFin(otro)
        || this.otroEventoArrancaAntesyTerminaDespuesQueElInicio(otro)
        || this.otroEventoArrancaDespuesyTerminaAntesQueElFin(otro)
        || this.otroEventoEmpiezAntesYTerminaDurante(otro)
        || this.otroEventoEmpiezaDuranteYTerminaDespues(otro);
  }

  private boolean compartenHorarioDeInicioOFin(Evento otro) {
    return this.getInicio().isEqual(otro.getInicio()) || this.getFin().isEqual(otro.getFin());
  }

  private boolean otroEventoArrancaAntesyTerminaDespuesQueElInicio(Evento otro) {
    return otro.getInicio().isBefore(this.getInicio()) && otro.getFin().isAfter(this.getInicio());
  }

  private boolean otroEventoArrancaDespuesyTerminaAntesQueElFin(Evento otro) {
    return otro.getInicio().isAfter(this.getInicio()) && otro.getInicio().isBefore(this.getFin());
  }

  private boolean otroEventoEmpiezAntesYTerminaDurante(Evento otro) {
    return otro.getInicio().isBefore(this.getInicio()) && otro.getFin().isAfter(this.getInicio()) && otro.getFin().isBefore(this.getFin());
  }

  private boolean otroEventoEmpiezaDuranteYTerminaDespues(Evento otro) {
    return otro.getInicio().isAfter(this.getInicio()) && otro.getInicio().isBefore(this.getFin()) && otro.getFin().isAfter(this.getFin());
  }


  public String getNombre() {
    return nombre;
  }

  public LocalDateTime getFechaInicio() {
    return fechaInicio;
  }

  public LocalDateTime getFechaFin() {
    return fechaFin;
  }

  public Duration getDuracion() {
    return duracion;
  }

  public Ubicacion getUbicacion() {
    return ubicacion;
  }

  public List<Usuario> getInvitados() {
    return invitados;
  }
}
