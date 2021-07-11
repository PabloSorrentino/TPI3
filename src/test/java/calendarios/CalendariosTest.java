package calendarios;

import calendarios.servicios.GugleMapas;
import calendarios.servicios.PositionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.time.temporal.ChronoUnit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static java.time.temporal.ChronoUnit.*;
import static calendarios.Pending.*;
import static org.junit.jupiter.api.Assertions.*;


class CalendariosTest {



  Ubicacion utnMedrano = new Ubicacion(-34.5984145, -58.4222096);
  Ubicacion utnCampus = new Ubicacion(-34.6591644, -58.4694862);

  // 2. Permitir que en cada calendario se agenden múltiples eventos

  @Test
  void unCalendarioPermiteAgendarUnEvento() {
    Calendario calendario = new Calendario();
    Evento seguimientoDeTP = crearEventoSimpleEnMedrano("Seguimiento de TP", LocalDateTime.of(2021, 10, 1, 15, 30), Duration.of(30, MINUTES));
    calendario.agendar(seguimientoDeTP);
    assertTrue(calendario.estaAgendado(seguimientoDeTP));
  }

  @Test
  void unCalendarioPermiteAgendarDosEvento() {
    Calendario calendario = new Calendario();
    LocalDateTime inicio = LocalDateTime.of(2021, 10, 1, 15, 30);

    Evento seguimientoDeTPA = crearEventoSimpleEnMedrano("Seguimiento de TPA", inicio, Duration.of(30, MINUTES));
    Evento practicaParcial = crearEventoSimpleEnMedrano("Practica para el primer parcial", inicio.plusMinutes(60), Duration.of(90, MINUTES));
    Evento cierrePrimerCuatri = crearEventoSimpleEnMedrano("Cierre primer cuatri", inicio.plusDays(3), Duration.ofDays(7));


    calendario.agendar(seguimientoDeTPA);
    calendario.agendar(practicaParcial);

    assertTrue(calendario.estaAgendado(seguimientoDeTPA));
    assertTrue(calendario.estaAgendado(practicaParcial));
    assertFalse(calendario.estaAgendado(cierrePrimerCuatri));
  }

  // 3. Permitir que los eventos registren nombre, fecha y hora de inicio y fin, ubicación, invitades (otros usuaries)

  @Test
  void unEventoPuedeTenerMultiplesInvitades() {
    LocalDateTime diaIngeniero = LocalDateTime.of(2021, 6, 16, 0, 0, 0);
    List<Usuario> invitados = crearListaInvitados();
    Evento evento = crearEventoSimple("Dia del Ingeniero", diaIngeniero, diaIngeniero.plusHours(24), utnMedrano, invitados);

   assertEquals("Dia del Ingeniero", evento.getNombre());
   assertEquals(diaIngeniero, evento.getFechaInicio());
   assertEquals(LocalDateTime.of(2021, 6, 17, 0, 0, 0), evento.getFechaFin());
   assertEquals(utnMedrano, evento.getUbicacion());
   assertEquals(invitados.size(), evento.getInvitados().size());
  }


  // 4. Permitir listar los próximos eventos entre dos fechas
  @Test
  void sePuedeListarUnEventoEntreDosFechasParaUnCalendario() {
    // Nota: Esto es opcional pero puede ayudar a resolver el siguiente item.
    // Borrar este test si no se utiliza

    Calendario calendario = new Calendario();
    Evento tpRedes = crearEventoSimpleEnMedrano("TP de Redes", LocalDateTime.of(2020, 4, 3, 16, 0), Duration.of(2, HOURS));

    calendario.agendar(tpRedes);

    List<Evento> eventos = calendario.eventosEntreFechas(
        LocalDate.of(2020, 4, 1).atStartOfDay(),
        LocalDate.of(2020, 4, 4).atStartOfDay());

    assertEquals(eventos, Arrays.asList(tpRedes));
  }

  @Test
  void sePuedeListarUnEventoEntreDosFechasParaUneUsuarie() {
    Usuario rene = crearUsuario("rene@gugle.com.ar");
    Calendario calendario = new Calendario();
    rene.agregarCalendario(calendario);

    Evento tpRedes = crearEventoSimpleEnMedrano("TP de Redes", LocalDateTime.of(2020, 4, 3, 16, 0), Duration.of(2, HOURS));

    calendario.agendar(tpRedes);

    List<Evento> eventos = rene.eventosEntreFechas(
        LocalDate.of(2020, 4, 1).atStartOfDay(),
        LocalDate.of(2020, 4, 4).atStartOfDay());

    assertEquals(eventos, Arrays.asList(tpRedes));
  }

  @Test
  void noSeListaUnEventoSiNoEstaEntreLasFechasIndicadasParaUneUsuarie() {
    Usuario dani = crearUsuario("dani@gugle.com.ar");
    Calendario calendario = new Calendario();
    dani.agregarCalendario(calendario);

    Evento tpRedes = crearEventoSimpleEnMedrano("TP de Redes", LocalDateTime.of(2020, 4, 3, 16, 0), Duration.of(1, HOURS));

    calendario.agendar(tpRedes);

    List<Evento> eventos = dani.eventosEntreFechas(
        LocalDate.of(2020, 5, 8).atStartOfDay(),
        LocalDate.of(2020, 5, 16).atStartOfDay());

    assertTrue(eventos.isEmpty());
  }


  @Test
  void sePuedenListarMultiplesEventoEntreDosFechasParaUneUsuarieConCoincidenciaParcial() {
    Usuario usuario = crearUsuario("rene@gugle.com.ar");
    Calendario calendario = new Calendario();
    usuario.agregarCalendario(calendario);

    Evento tpRedes = crearEventoSimpleEnMedrano("TP de Redes", LocalDateTime.of(2020, 4, 3, 16, 0), Duration.of(2, HOURS));
    Evento tpDeGestion = crearEventoSimpleEnMedrano("TP de Gestión", LocalDateTime.of(2020, 4, 5, 18, 30), Duration.of(2, HOURS));
    Evento tpDeDds = crearEventoSimpleEnMedrano("TP de DDS", LocalDateTime.of(2020, 4, 12, 16, 0), Duration.of(2, HOURS));

    calendario.agendar(tpRedes);
    calendario.agendar(tpDeGestion);
    calendario.agendar(tpDeDds);

    List<Evento> eventos = usuario.eventosEntreFechas(
        LocalDate.of(2020, 4, 1).atStartOfDay(),
        LocalDate.of(2020, 4, 6).atStartOfDay());

    assertEquals(eventos, Arrays.asList(tpRedes, tpDeGestion));
  }


  @Test
  void sePuedenListarMultiplesEventoEntreDosFechasParaUneUsuarieConCoincidenciaTotal() {
    Usuario juli = crearUsuario("juli@gugle.com.ar");
    Calendario calendario = new Calendario();


    Evento tpRedes = crearEventoSimpleEnMedrano("TP de Redes", LocalDateTime.of(2020, 4, 3, 16, 0), Duration.of(2, HOURS));
    Evento tpDeGestion = crearEventoSimpleEnMedrano("TP de Gestión", LocalDateTime.of(2020, 4, 5, 18, 30), Duration.of(30, MINUTES));
    Evento tpDeDds = crearEventoSimpleEnMedrano("TP de DDS", LocalDateTime.of(2020, 4, 12, 16, 0), Duration.of(1, HOURS));

    calendario.agendar(tpRedes);
    calendario.agendar(tpDeGestion);
    calendario.agendar(tpDeDds);

    juli.agregarCalendario(calendario);

    List<Evento> eventos = juli.eventosEntreFechas(
        LocalDate.of(2020, 4, 1).atStartOfDay(),
        LocalDateTime.of(2020, 4, 12, 21, 0));

    assertEquals(eventos, Arrays.asList(tpRedes, tpDeGestion, tpDeDds));
  }

  @Test
  void sePuedenListarEventosDeMultiplesCalendarios() {
    Usuario juli = crearUsuario("juli@gugle.com.ar");


    Evento tpRedes = crearEventoSimpleEnMedrano("TP de Redes", LocalDateTime.of(2020, 4, 3, 16, 0), Duration.of(2, HOURS));
    Evento tpDeGestion = crearEventoSimpleEnCampus("TP de Gestión", LocalDateTime.of(2020, 4, 5, 18, 30), Duration.of(30, MINUTES));
    Evento tpDeDds = crearEventoSimpleEnMedrano("TP de DDS", LocalDateTime.of(2020, 4, 12, 16, 0), Duration.of(1, HOURS));
    Evento tpDeEconomia = crearEventoSimpleEnCampus("TP de Economia", LocalDateTime.of(2020, 4, 23, 19, 45), Duration.of(30, MINUTES));
   /*este queda afuera*/ Evento afterOffice = crearEventoSimpleEnCampus("AfterOffice", LocalDateTime.of(2020, 4, 23, 20, 45), Duration.of(2, HOURS));

    Calendario calendarioFacultad = new Calendario();
    calendarioFacultad.agendar(tpRedes);
    calendarioFacultad.agendar(tpDeGestion);


    Calendario calendarioLaboral = new Calendario();
    calendarioLaboral.agendar(tpDeDds);
    calendarioLaboral.agendar(tpDeEconomia);
    calendarioLaboral.agendar(afterOffice);

    juli.agregarCalendario(calendarioLaboral);
    juli.agregarCalendario(calendarioFacultad);

    List<Evento> eventos = juli.eventosEntreFechas(
        LocalDate.of(2020, 4, 1).atStartOfDay(),
        LocalDateTime.of(2020, 4, 23, 20, 15));

    assertEquals(eventos, Arrays.asList(tpDeDds, tpDeEconomia,tpRedes, tpDeGestion ));

  }


  /**
   * @return une usuarie con el mail dado
   */
  Usuario crearUsuario(String email) {
    return new Usuario(email);
  }

  /*
   * @return Un calendario sin ningún evento agendado aún
   */
  Calendario crearCalendarioVacio() {
    return new Calendario();
  }

  Evento crearEventoSimpleEnMedrano(String nombre, LocalDateTime inicio, Duration duracion) {
    return crearEventoSimple("Seguimiento de TPA", inicio, inicio.plus(duracion), utnMedrano, Collections.emptyList());
  }

  Evento crearEventoSimpleEnCampus(String nombre, LocalDateTime inicio, Duration duracion) {
    return crearEventoSimple("Seguimiento de TPA", inicio, inicio.plus(duracion), utnCampus, Collections.emptyList());
  }

  /**
   * @return un evento sin invtades que no se repite, que tenga el nombre, fecha de inicio y fin, ubicación dados
   */
  Evento crearEventoSimple(String nombre, LocalDateTime inicio, LocalDateTime fin, Ubicacion ubicacion, List<Usuario> invitados) {
    return new Evento(nombre, inicio, fin, ubicacion, invitados);
  }


  /**
   * @retun una lista de invitados aleatoria
   */

  List<Usuario> crearListaInvitados() {
    Usuario ingeniero1 = crearUsuario("ingeniero1@frba.utn.edu.ar");
    Usuario ingeniero2 = crearUsuario("ingeniero2@frba.utn.edu.ar");
    Usuario ayudante1 = crearUsuario("ayudante1@frba.utn.edu.ar");
    Usuario ayudante4 = crearUsuario("ingeniero4@frba.utn.edu.ar");
    Usuario bedel = crearUsuario("bedelia@frba.utn.edu.ar");
    Usuario profesorDDS1 = crearUsuario("dds_1_profe@frba.utn.edu.ar");
    Usuario profesorDDS2 = crearUsuario("dds_2_profe@frba.utn.edu.ar");

    List<Usuario> invitados = new ArrayList<>();
    invitados.add(ingeniero1);
    invitados.add(ingeniero2);
    invitados.add(ayudante1);
    invitados.add(ayudante4);
    invitados.add(bedel);
    invitados.add(profesorDDS1);
    invitados.add(profesorDDS2);

    return invitados;
  }

}