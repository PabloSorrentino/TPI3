package calendarios;

import calendarios.servicios.GugleMapas;
import calendarios.servicios.PositionService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


import static calendarios.Pending.*;

public class Usuario {

  private final List<Calendario> calendarios;
  private final String email;

  public Usuario(String email) {

    calendarios = new ArrayList<>();
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  public void agregarCalendario(Calendario calendario) {
    calendarios.add(calendario);
  }

  public List<Evento> eventosEntreFechas(LocalDateTime inicio, LocalDateTime fin) {

    return calendarios.stream()
        .map(calendario -> calendario.eventosEntreFechas(inicio, fin))
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  public boolean llegaATiempoAlProximoEvento() {
    // TODO implementar
    return pending();
  }

  public boolean tieneCalendario(Calendario calendario) {
    return calendarios.contains(calendario);
  }
}
