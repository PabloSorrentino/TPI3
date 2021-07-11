package calendarios;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static calendarios.Pending.pending;

public class Calendario {

  private final List<Evento> eventos = new ArrayList<>();


  public void agendar(Evento evento) {
    eventos.add(evento);
  }

  public boolean estaAgendado(Evento evento) {
    return eventos.contains(evento);
  }

  public List<Evento> getEventos() {
    return eventos;
  }

  public List<Evento> eventosEntreFechas(LocalDateTime inicio, LocalDateTime fin) {
    return eventos.stream()
        .filter(evento -> evento.estaEntreDosFechas(inicio, fin))
        .collect(Collectors.toList());
  }

  public List<Evento> eventosSolapadosCon(Evento evento) {
    return eventos.stream()
        .filter(e -> evento.estaSolapadoCon(e) && !e.equals(evento))
        .collect(Collectors.toList());
  }

  public Duration cuantoFaltaPara(Evento evento) {
    return evento.cuantoFalta();
  }


}
