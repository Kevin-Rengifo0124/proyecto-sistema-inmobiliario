package dao.implementacion;

import dao.MensajeDAO;
import excepciones.MensajePendienteException;
import modelo.Cliente;
import modelo.Mensaje;
import modelo.Propiedad;
import singleton.Singleton;
import util.implementacion.ListaEnlazada;

public class MensajeDAOImpl implements MensajeDAO {

    private ListaEnlazada<Mensaje> listaMensajes;

    public MensajeDAOImpl() {
        this.listaMensajes = Singleton.getINSTANCIA().getListaMensajes();
    }

    @Override
    public void guardar(Mensaje mensaje) throws MensajePendienteException {
        // Verificar si ya hay un mensaje pendiente de este cliente para esta propiedad
        if (mensaje.getRemitente() != null && mensaje.getPropiedad() != null) {
            for (int i = 0; i < listaMensajes.size(); i++) {
                Mensaje m = listaMensajes.get(i);
                if (m.getRemitente().equals(mensaje.getRemitente())
                        && m.getPropiedad().getId().equals(mensaje.getPropiedad().getId())
                        && !m.isRespondido()) {
                    throw new MensajePendienteException();
                }
            }
        }

        listaMensajes.add(mensaje);
        mensaje.getPropiedad().agregarMensaje(mensaje);
        Singleton.getINSTANCIA().escribirMensajes();
        Singleton.getINSTANCIA().escribirPropiedades();
    }

    @Override
    public void actualizar(Mensaje mensaje) {
        for (int i = 0; i < listaMensajes.size(); i++) {
            if (listaMensajes.get(i).getId().equals(mensaje.getId())) {
                listaMensajes.remove(i);
                listaMensajes.add(mensaje, i);
                Singleton.getINSTANCIA().escribirMensajes();
                return;
            }
        }
    }

    @Override
    public void eliminar(String id) {
        for (int i = 0; i < listaMensajes.size(); i++) {
            if (listaMensajes.get(i).getId().equals(id)) {
                listaMensajes.remove(i);
                Singleton.getINSTANCIA().escribirMensajes();
                return;
            }
        }
    }

    @Override
    public Mensaje buscarPorId(String id) {
        for (int i = 0; i < listaMensajes.size(); i++) {
            if (listaMensajes.get(i).getId().equals(id)) {
                return listaMensajes.get(i);
            }
        }
        return null;
    }

    @Override
    public ListaEnlazada<Mensaje> listarTodos() {
        return listaMensajes;
    }

    @Override
    public ListaEnlazada<Mensaje> listarPorPropiedad(Propiedad propiedad) {
        ListaEnlazada<Mensaje> resultado = new ListaEnlazada<>();
        for (int i = 0; i < listaMensajes.size(); i++) {
            Mensaje m = listaMensajes.get(i);
            if (m.getPropiedad().getId().equals(propiedad.getId())) {
                resultado.add(m);
            }
        }
        return resultado;
    }

    @Override
    public ListaEnlazada<Mensaje> listarPorRemitente(String emailRemitente) {
        ListaEnlazada<Mensaje> resultado = new ListaEnlazada<>();
        for (int i = 0; i < listaMensajes.size(); i++) {
            Mensaje m = listaMensajes.get(i);
            if (m.getRemitente().equals(emailRemitente)) {
                resultado.add(m);
            }
        }
        return resultado;
    }

    @Override
    public ListaEnlazada<Mensaje> listarPorDestinatario(String emailDestinatario) {
        ListaEnlazada<Mensaje> resultado = new ListaEnlazada<>();
        for (int i = 0; i < listaMensajes.size(); i++) {
            Mensaje m = listaMensajes.get(i);
            if (m.getDestinatario().equals(emailDestinatario)) {
                resultado.add(m);
            }
        }
        return resultado;
    }

    @Override
    public void marcarRespondido(Mensaje mensaje) {
        mensaje.setRespondido(true);
        actualizar(mensaje);
    }

    @Override
    public boolean tieneMensajePendiente(Cliente cliente, Propiedad propiedad) {
        for (int i = 0; i < listaMensajes.size(); i++) {
            Mensaje m = listaMensajes.get(i);
            if (m.getRemitente().equals(cliente.getEmail())
                    && m.getPropiedad().getId().equals(propiedad.getId())
                    && !m.isRespondido()) {
                return true;
            }
        }
        return false;
    }
}
