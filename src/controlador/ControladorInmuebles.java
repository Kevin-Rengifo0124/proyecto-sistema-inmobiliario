package controlador;

import dao.PropiedadDAO;
import modelo.Inmueble;
import modelo.Propiedad;
import singleton.Singleton;
import util.implementacion.ListaEnlazada;

public class ControladorInmuebles {
    
    private PropiedadDAO propiedadDAO;
    private ListaEnlazada<Inmueble> listaInmuebles;
    
    public ControladorInmuebles(PropiedadDAO propiedadDAO) {
        this.propiedadDAO = propiedadDAO;
        this.listaInmuebles = Singleton.getINSTANCIA().getListaInmuebles();
    }
    
    /**
     * Registra un nuevo inmueble
     */
    public void registrarInmueble(Inmueble inmueble) {
        listaInmuebles.add(inmueble);
        Singleton.getINSTANCIA().escribirInmuebles();
    }
    
    /**
     * Actualiza un inmueble existente
     */
    public void actualizarInmueble(Inmueble inmueble) {
        // Buscar y actualizar el inmueble
        for (int i = 0; i < listaInmuebles.size(); i++) {
            if (listaInmuebles.get(i).getId().equals(inmueble.getId())) {
                listaInmuebles.remove(i);
                listaInmuebles.add(inmueble, i);
                Singleton.getINSTANCIA().escribirInmuebles();
                return;
            }
        }
    }
    
    /**
     * Elimina un inmueble
     */
    public void eliminarInmueble(String id) {
        // Verificar que el inmueble no esté asociado a alguna propiedad
        ListaEnlazada<Propiedad> propiedades = propiedadDAO.listarTodas();
        for (int i = 0; i < propiedades.size(); i++) {
            if (propiedades.get(i).getInmueble() != null && 
                propiedades.get(i).getInmueble().getId().equals(id)) {
                throw new RuntimeException("No se puede eliminar el inmueble porque está asociado a una propiedad");
            }
        }
        
        // Eliminar el inmueble
        for (int i = 0; i < listaInmuebles.size(); i++) {
            if (listaInmuebles.get(i).getId().equals(id)) {
                listaInmuebles.remove(i);
                Singleton.getINSTANCIA().escribirInmuebles();
                return;
            }
        }
    }
    
    /**
     * Busca un inmueble por su ID
     */
    public Inmueble buscarInmueble(String id) {
        for (int i = 0; i < listaInmuebles.size(); i++) {
            if (listaInmuebles.get(i).getId().equals(id)) {
                return listaInmuebles.get(i);
            }
        }
        return null;
    }
    
    /**
     * Lista todos los inmuebles
     */
    public ListaEnlazada<Inmueble> listarTodos() {
        return listaInmuebles;
    }
    
    /**
     * Crea una nueva propiedad con un inmueble asociado
     */
    public void crearPropiedadConInmueble(Propiedad propiedad, Inmueble inmueble) {
        // Registrar inmueble si no existe
        if (buscarInmueble(inmueble.getId()) == null) {
            registrarInmueble(inmueble);
        }
        
        // Asociar inmueble a la propiedad
        propiedad.setInmueble(inmueble);
        
        // Guardar la propiedad
        propiedadDAO.guardar(propiedad);
    }
}