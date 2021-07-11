package calendarios;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static calendarios.Pending.pending;
import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {

  // 1. Permitir que une usuarie tenga muchos calendarios

  @Test
  void uneUsuarieTieneMuchosCalendarios() {
    Usuario rene = crearUsuario("rene@gugle.com.ar");
    Calendario calendario1 = crearCalendarioVacio();
    Calendario calendario2 = crearCalendarioVacio();
    Calendario calendario3 = crearCalendarioVacio();

    rene.agregarCalendario(calendario1);
    rene.agregarCalendario(calendario2);

    assertTrue(rene.tieneCalendario(calendario1));
    assertTrue(rene.tieneCalendario(calendario2));
    assertFalse(rene.tieneCalendario(calendario3));
  }


  Calendario crearCalendarioVacio() {

    return new Calendario();
  }


  /**
   * @return une usuarie con el mail dado
   */
  Usuario crearUsuario(String email) {
    return new Usuario(email);
  }

}
