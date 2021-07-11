package calendarios;

import calendarios.servicios.GugleMapas;
import calendarios.servicios.PositionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.time.temporal.ChronoUnit.HOURS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class EventosTest {

  private PositionService positionService;
  private GugleMapas gugleMapas;

  Ubicacion utnMedrano = new Ubicacion(-34.5984145, -58.4222096);
  Ubicacion utnCampus = new Ubicacion(-34.6591644, -58.4694862);

  @BeforeEach
  void initFileSystem() {
    positionService = mock(PositionService.class);
    gugleMapas = mock(GugleMapas.class);
  }

  // 5. Permitir saber cuánto falta para un cierto calendarios.evento (por ejemplo, 15 horas)

  @Test
  void unEventoSabeCuantoFalta() {
    LocalDateTime inicio = LocalDateTime.now().plusDays(60);
    Evento parcialDds = crearEventoSimpleEnMedrano("Parcial DDS", inicio, Duration.of(2, HOURS));

    assertTrue(parcialDds.cuantoFalta().compareTo(Duration.of(60, ChronoUnit.DAYS)) <= 0);
    assertTrue(parcialDds.cuantoFalta().compareTo(Duration.of(59, ChronoUnit.DAYS)) >= 0);
  }

  // 7. Permitir agendar eventos con repeticiones, con una frecuencia diaria, semanal, mensual o anual

  @Test
  void sePuedenAgendarYListarEventosRecurrrentes() {
    Usuario usuario = crearUsuario("rene@gugle.com.ar");

    // TODO completar
    fail("Agregar uno evento recurrente que se repita los martes a las 19 y dure 45 minutos y " +
        "por tanto deberá aparecer dos veces entre el lunes 14 a las 9 y el lunes 28 a las 21");

    List<Evento> eventos = usuario.eventosEntreFechas(
        LocalDateTime.of(2020, 9, 14, 9, 0),
        LocalDateTime.of(2020, 9, 28, 21, 0));

    assertEquals(eventos.size(), 2);
  }

  @Test
  void unEventoRecurrenteSabeCuantoFaltaParaSuProximaRepeticion() {
    // TODO completar
    Evento unRecurrente = fail("crear un evento recurrente que se repita, a partir de hoy, cada 15 días, y arranque una hora antes de la hora actual");

    assertTrue(unRecurrente.cuantoFalta().compareTo(Duration.of(15, ChronoUnit.DAYS)) <= 0);
    assertTrue(unRecurrente.cuantoFalta().compareTo(Duration.of(14, ChronoUnit.DAYS)) >= 0);
  }


  // 6. Permitir saber si dos eventos están solapado, y en tal caso, con qué otros eventos del calendario

  @Test
  void sePuedeSaberSiUnEventoEstaSolapadoCuandoEstaParcialmenteIncluido() {
    // TODO: esto es opcional pero probablemente ayuda a implementar el requerimiento principal
    Evento recuperatorioSistemasDeGestion = crearEventoSimpleEnMedrano("Recuperatorio Sistemas de Gestion", LocalDateTime.of(2021, 6, 19, 9, 0), Duration.of(2, HOURS));
    Evento tpOperativos = crearEventoSimpleEnMedrano("Entrega de Operativos", LocalDateTime.of(2021, 6, 19, 10, 0), Duration.of(2, HOURS));

    assertTrue(recuperatorioSistemasDeGestion.estaSolapadoCon(tpOperativos));
    assertTrue(tpOperativos.estaSolapadoCon(recuperatorioSistemasDeGestion));
  }

  @Test
  void sePuedeSaberSiUnEventoEstaSolapadoCuandoEstaTotalmenteIncluido() {
    // TODO: esto es opcional pero probablemente ayuda a implementar el requerimiento principal
    Evento recuperatorioSistemasDeGestion = crearEventoSimpleEnMedrano("Recuperatorio Sistemas de Gestion", LocalDateTime.of(2021, 6, 19, 9, 0), Duration.of(4, HOURS));
    Evento tpOperativos = crearEventoSimpleEnMedrano("Entrega de Operativos", LocalDateTime.of(2021, 6, 19, 10, 0), Duration.of(2, HOURS));

    assertTrue(recuperatorioSistemasDeGestion.estaSolapadoCon(tpOperativos));
    assertTrue(tpOperativos.estaSolapadoCon(recuperatorioSistemasDeGestion));
  }


  @Test
  void sePuedeSaberSiUnEventoEstaSolapadoCuandoNoEstaSolapado() {
    // TODO: esto es opcional pero probablemente ayuda a implementar el requerimiento principal
    Evento recuperatorioSistemasDeGestion = crearEventoSimpleEnMedrano("Recuperatorio Sistemas de Gestion", LocalDateTime.of(2021, 6, 19, 9, 0), Duration.of(3, HOURS));
    Evento tpOperativos = crearEventoSimpleEnMedrano("Entrega de Operativos", LocalDateTime.of(2021, 6, 19, 18, 0), Duration.of(2, HOURS));

    assertFalse(recuperatorioSistemasDeGestion.estaSolapadoCon(tpOperativos));
    assertFalse(tpOperativos.estaSolapadoCon(recuperatorioSistemasDeGestion));
  }

  @Test
  void sePuedeSaberConQueEventosEstaSolapado() {
    Evento recuperatorioSistemasDeGestion = crearEventoSimpleEnMedrano("Recuperatorio Sistemas de Gestion", LocalDateTime.of(2021, 6, 19, 9, 0), Duration.of(2, HOURS));
    Evento tpOperativos = crearEventoSimpleEnMedrano("Entrega de Operativos", LocalDateTime.of(2021, 6, 19, 10, 0), Duration.of(2, HOURS));
    Evento tramiteEnElBanco = crearEventoSimpleEnMedrano("Tramite en el banco", LocalDateTime.of(2021, 6, 19, 9, 0), Duration.of(4, HOURS));

    Calendario calendario = crearCalendarioVacio();

    calendario.agendar(recuperatorioSistemasDeGestion);
    calendario.agendar(tpOperativos);

    assertEquals(Arrays.asList(recuperatorioSistemasDeGestion, tpOperativos), calendario.eventosSolapadosCon(tramiteEnElBanco));
  }


  // 9. Permitir asignarle a un evento varios recordatorios, que se enviarán cuando falte un cierto tiempo


  @Test
  void proximoEvento() {
    // TODO completar
    fail("Pendiente");
  }

  // 8. Permitir saber si le usuarie llega al evento más próximo a tiempo, tomando en cuenta la ubicación actual de le usuarie y destino.


  @Test
  void llegaATiempoAlProximoEventoCuandoNoHayEventos() {
    Usuario feli = crearUsuario("feli@gugle.com.ar");
    assertTrue(feli.llegaATiempoAlProximoEvento());
  }

  @Test
  void llegaATiempoAlProximoEventoCuandoHayUnEventoCercano() {
    Usuario feli = crearUsuario("feli@gugle.com.ar");
    Calendario calendario = crearCalendarioVacio();
    feli.agregarCalendario(calendario);

    fail("mockear al Position Service para que diga que ya está en medrano y a GugleMaps para que diga que tarda 0 minutos en llegar");

    calendario.agendar(crearEventoSimpleEnMedrano("Parcial", LocalDateTime.now().plusMinutes(30), Duration.of(2, HOURS)));

    assertTrue(feli.llegaATiempoAlProximoEvento());
  }

  @Test
  void noLlegaATiempoAlProximoEventoCuandoHayUnEventoFísicamenteLejano() {
    Usuario feli = crearUsuario("feli@gugle.com.ar");
    Calendario calendario = crearCalendarioVacio();
    feli.agregarCalendario(calendario);

    fail("mockear al Position Service para que diga que está en Medrano y a GugleMaps para que diga que tarda 0 minutos en llegar");

    calendario.agendar(crearEventoSimpleEnMedrano("Parcial", LocalDateTime.now().plusMinutes(30), Duration.of(2, HOURS)));

    assertFalse(feli.llegaATiempoAlProximoEvento());
  }


  @Test
  void llegaATiempoAlProximoEventoCuandoHayUnEventoCercanoAunqueAlSiguienteNoLlegue() {
    Usuario feli = crearUsuario("feli@gugle.com.ar");
    Calendario calendario = crearCalendarioVacio();
    feli.agregarCalendario(calendario);

    fail("mockear al Position Service para que diga que está en Medrano y a GugleMaps para que diga que tarda 0 minutos en llegar a Medrano y 1:30 horas en llegar a Campus");

    calendario.agendar(crearEventoSimpleEnMedrano("Parcial", LocalDateTime.now().plusMinutes(30), Duration.of(3, HOURS)));
    calendario.agendar(crearEventoSimpleEnCampus("Final", LocalDateTime.now().plusMinutes(45), Duration.of(1, HOURS)));

    assertTrue(feli.llegaATiempoAlProximoEvento());
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
