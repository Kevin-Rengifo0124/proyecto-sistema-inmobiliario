/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package singleton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

import modelo.Administrador;
import modelo.Agenda;
import modelo.Cliente;
import modelo.Empleado;
import modelo.MatrizSedes;
import modelo.Mensaje;
import modelo.Persona;
import modelo.Propiedad;
import modelo.Sede;
import modelo.Usuario;
import util.ListaEnlazada;
import util.Queue;

public class Singleton {

    // Instancia única del Singleton creada en la inicialización
    private static final Singleton INSTANCIA = new Singleton();

    // Dirección del directorio de datos
    private static final String DIRECTORIO_BASE = "data/";

    // Colecciones de datos
    private ListaEnlazada<Usuario> listaUsuarios;
    private ListaEnlazada<Persona> listaPersonas;
    private ListaEnlazada<Administrador> listaAdministradores;
    private ListaEnlazada<Empleado> listaEmpleados;
    private ListaEnlazada<Cliente> listaClientes;
    private ListaEnlazada<Sede> listaSedes;
    private ListaEnlazada<Propiedad> listaPropiedades;
    private ListaEnlazada<Agenda> listaAgendas;
    private ListaEnlazada<Mensaje> listaMensajes;
    private MatrizSedes matrizSedes;
    private Queue<Propiedad> colaPropiedades;

    // Usuario actual en la sesión
    private Usuario usuarioActual;

    /**
     * Constructor privado que inicializa todos los datos
     */
    private Singleton() {
        // Crear directorio si no existe
        File directorio = new File(DIRECTORIO_BASE);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        // Inicializar todas las colecciones
        this.listaUsuarios = leerUsuarios();
        this.listaPersonas = leerPersonas();
        this.listaAdministradores = leerAdministradores();
        this.listaEmpleados = leerEmpleados();
        this.listaClientes = leerClientes();
        this.listaSedes = leerSedes();
        this.listaPropiedades = leerPropiedades();
        this.listaAgendas = leerAgendas();
        this.listaMensajes = leerMensajes();
        this.matrizSedes = leerMatrizSedes();
        this.colaPropiedades = inicializarColaPropiedades();
    }

    /**
     * Método para obtener la instancia única del Singleton
     *
     * @return Instancia única del Singleton
     */
    public static Singleton getInstancia() {
        return INSTANCIA;
    }

    /**
     * Inicializa la cola de propiedades con las propiedades pendientes de
     * asignación
     *
     * @return Cola de propiedades pendientes
     */
    private Queue<Propiedad> inicializarColaPropiedades() {
        Queue<Propiedad> cola = new Queue<>();
        for (int i = 0; i < listaPropiedades.size(); i++) {
            Propiedad propiedad = listaPropiedades.get(i);
            if (propiedad.getEstado().equals(Propiedad.ESTADO_PENDIENTE_ASIGNACION)) {
                cola.enQueue(propiedad);
            }
        }
        return cola;
    }

    // Métodos de lectura y escritura para cada colección
    private ListaEnlazada<Usuario> leerUsuarios() {
        try {
            FileInputStream archivo = new FileInputStream(DIRECTORIO_BASE + "usuarios.dat");
            ObjectInputStream lector = new ObjectInputStream(archivo);
            ListaEnlazada<Usuario> lista = (ListaEnlazada<Usuario>) lector.readObject();
            lector.close();
            archivo.close();
            return lista;
        } catch (IOException | ClassNotFoundException ex) {
            return new ListaEnlazada<>();
        }
    }

    public void escribirUsuarios() {
        try {
            FileOutputStream archivo = new FileOutputStream(DIRECTORIO_BASE + "usuarios.dat");
            ObjectOutputStream escritor = new ObjectOutputStream(archivo);
            escritor.writeObject(listaUsuarios);
            escritor.close();
            archivo.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private ListaEnlazada<Persona> leerPersonas() {
        try {
            FileInputStream archivo = new FileInputStream(DIRECTORIO_BASE + "personas.dat");
            ObjectInputStream lector = new ObjectInputStream(archivo);
            ListaEnlazada<Persona> lista = (ListaEnlazada<Persona>) lector.readObject();
            lector.close();
            archivo.close();
            return lista;
        } catch (IOException | ClassNotFoundException ex) {
            return new ListaEnlazada<>();
        }
    }

    public void escribirPersonas() {
        try {
            FileOutputStream archivo = new FileOutputStream(DIRECTORIO_BASE + "personas.dat");
            ObjectOutputStream escritor = new ObjectOutputStream(archivo);
            escritor.writeObject(listaPersonas);
            escritor.close();
            archivo.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private ListaEnlazada<Administrador> leerAdministradores() {
        try {
            FileInputStream archivo = new FileInputStream(DIRECTORIO_BASE + "administradores.dat");
            ObjectInputStream lector = new ObjectInputStream(archivo);
            ListaEnlazada<Administrador> lista = (ListaEnlazada<Administrador>) lector.readObject();
            lector.close();
            archivo.close();
            return lista;
        } catch (IOException | ClassNotFoundException ex) {
            return new ListaEnlazada<>();
        }
    }

    public void escribirAdministradores() {
        try {
            FileOutputStream archivo = new FileOutputStream(DIRECTORIO_BASE + "administradores.dat");
            ObjectOutputStream escritor = new ObjectOutputStream(archivo);
            escritor.writeObject(listaAdministradores);
            escritor.close();
            archivo.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private ListaEnlazada<Empleado> leerEmpleados() {
        try {
            FileInputStream archivo = new FileInputStream(DIRECTORIO_BASE + "empleados.dat");
            ObjectInputStream lector = new ObjectInputStream(archivo);
            ListaEnlazada<Empleado> lista = (ListaEnlazada<Empleado>) lector.readObject();
            lector.close();
            archivo.close();
            return lista;
        } catch (IOException | ClassNotFoundException ex) {
            return new ListaEnlazada<>();
        }
    }

    public void escribirEmpleados() {
        try {
            FileOutputStream archivo = new FileOutputStream(DIRECTORIO_BASE + "empleados.dat");
            ObjectOutputStream escritor = new ObjectOutputStream(archivo);
            escritor.writeObject(listaEmpleados);
            escritor.close();
            archivo.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private ListaEnlazada<Cliente> leerClientes() {
        try {
            FileInputStream archivo = new FileInputStream(DIRECTORIO_BASE + "clientes.dat");
            ObjectInputStream lector = new ObjectInputStream(archivo);
            ListaEnlazada<Cliente> lista = (ListaEnlazada<Cliente>) lector.readObject();
            lector.close();
            archivo.close();
            return lista;
        } catch (IOException | ClassNotFoundException ex) {
            return new ListaEnlazada<>();
        }
    }

    public void escribirClientes() {
        try {
            FileOutputStream archivo = new FileOutputStream(DIRECTORIO_BASE + "clientes.dat");
            ObjectOutputStream escritor = new ObjectOutputStream(archivo);
            escritor.writeObject(listaClientes);
            escritor.close();
            archivo.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private ListaEnlazada<Sede> leerSedes() {
        try {
            FileInputStream archivo = new FileInputStream(DIRECTORIO_BASE + "sedes.dat");
            ObjectInputStream lector = new ObjectInputStream(archivo);
            ListaEnlazada<Sede> lista = (ListaEnlazada<Sede>) lector.readObject();
            lector.close();
            archivo.close();
            return lista;
        } catch (IOException | ClassNotFoundException ex) {
            return new ListaEnlazada<>();
        }
    }

    public void escribirSedes() {
        try {
            FileOutputStream archivo = new FileOutputStream(DIRECTORIO_BASE + "sedes.dat");
            ObjectOutputStream escritor = new ObjectOutputStream(archivo);
            escritor.writeObject(listaSedes);
            escritor.close();
            archivo.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private ListaEnlazada<Propiedad> leerPropiedades() {
        try {
            FileInputStream archivo = new FileInputStream(DIRECTORIO_BASE + "propiedades.dat");
            ObjectInputStream lector = new ObjectInputStream(archivo);
            ListaEnlazada<Propiedad> lista = (ListaEnlazada<Propiedad>) lector.readObject();
            lector.close();
            archivo.close();
            return lista;
        } catch (IOException | ClassNotFoundException ex) {
            return new ListaEnlazada<>();
        }
    }

    public void escribirPropiedades() {
        try {
            FileOutputStream archivo = new FileOutputStream(DIRECTORIO_BASE + "propiedades.dat");
            ObjectOutputStream escritor = new ObjectOutputStream(archivo);
            escritor.writeObject(listaPropiedades);
            escritor.close();
            archivo.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private ListaEnlazada<Agenda> leerAgendas() {
        try {
            FileInputStream archivo = new FileInputStream(DIRECTORIO_BASE + "agendas.dat");
            ObjectInputStream lector = new ObjectInputStream(archivo);
            ListaEnlazada<Agenda> lista = (ListaEnlazada<Agenda>) lector.readObject();
            lector.close();
            archivo.close();
            return lista;
        } catch (IOException | ClassNotFoundException ex) {
            return new ListaEnlazada<>();
        }
    }

    public void escribirAgendas() {
        try {
            FileOutputStream archivo = new FileOutputStream(DIRECTORIO_BASE + "agendas.dat");
            ObjectOutputStream escritor = new ObjectOutputStream(archivo);
            escritor.writeObject(listaAgendas);
            escritor.close();
            archivo.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private ListaEnlazada<Mensaje> leerMensajes() {
        try {
            FileInputStream archivo = new FileInputStream(DIRECTORIO_BASE + "mensajes.dat");
            ObjectInputStream lector = new ObjectInputStream(archivo);
            ListaEnlazada<Mensaje> lista = (ListaEnlazada<Mensaje>) lector.readObject();
            lector.close();
            archivo.close();
            return lista;
        } catch (IOException | ClassNotFoundException ex) {
            return new ListaEnlazada<>();
        }
    }

    public void escribirMensajes() {
        try {
            FileOutputStream archivo = new FileOutputStream(DIRECTORIO_BASE + "mensajes.dat");
            ObjectOutputStream escritor = new ObjectOutputStream(archivo);
            escritor.writeObject(listaMensajes);
            escritor.close();
            archivo.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private MatrizSedes leerMatrizSedes() {
        try {
            FileInputStream archivo = new FileInputStream(DIRECTORIO_BASE + "matriz_sedes.dat");
            ObjectInputStream lector = new ObjectInputStream(archivo);
            MatrizSedes matriz = (MatrizSedes) lector.readObject();
            lector.close();
            archivo.close();
            return matriz;
        } catch (IOException | ClassNotFoundException ex) {
            return new MatrizSedes();
        }
    }

    public void escribirMatrizSedes() {
        try {
            FileOutputStream archivo = new FileOutputStream(DIRECTORIO_BASE + "matriz_sedes.dat");
            ObjectOutputStream escritor = new ObjectOutputStream(archivo);
            escritor.writeObject(matrizSedes);
            escritor.close();
            archivo.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Guarda todos los datos en archivos
     */
    public void guardarTodo() {
        escribirUsuarios();
        escribirPersonas();
        escribirAdministradores();
        escribirEmpleados();
        escribirClientes();
        escribirSedes();
        escribirPropiedades();
        escribirAgendas();
        escribirMensajes();
        escribirMatrizSedes();
    }

    // Getters para acceder a las colecciones
    public ListaEnlazada<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public ListaEnlazada<Persona> getListaPersonas() {
        return listaPersonas;
    }

    public ListaEnlazada<Administrador> getListaAdministradores() {
        return listaAdministradores;
    }

    public ListaEnlazada<Empleado> getListaEmpleados() {
        return listaEmpleados;
    }

    public ListaEnlazada<Cliente> getListaClientes() {
        return listaClientes;
    }

    public ListaEnlazada<Sede> getListaSedes() {
        return listaSedes;
    }

    public ListaEnlazada<Propiedad> getListaPropiedades() {
        return listaPropiedades;
    }

    public ListaEnlazada<Agenda> getListaAgendas() {
        return listaAgendas;
    }

    public ListaEnlazada<Mensaje> getListaMensajes() {
        return listaMensajes;
    }

    public MatrizSedes getMatrizSedes() {
        return matrizSedes;
    }

    public Queue<Propiedad> getColaPropiedades() {
        return colaPropiedades;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    // Métodos de utilidad para operaciones comunes
    /**
     * Genera un ID único (UUID)
     *
     * @return UUID como String
     */
    public String generarId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Busca un usuario por su email
     *
     * @param email Email del usuario
     * @return Usuario encontrado o null
     */
    public Usuario buscarUsuarioPorEmail(String email) {
        for (int i = 0; i < listaUsuarios.size(); i++) {
            Usuario usuario = listaUsuarios.get(i);
            if (usuario.getEmail().equals(email)) {
                return usuario;
            }
        }
        return null;
    }

    /**
     * Valida las credenciales de un usuario
     *
     * @param email Email del usuario
     * @param clave Clave del usuario
     * @return Usuario si las credenciales son correctas, null si no
     */
    public Usuario validarCredenciales(String email, String clave) {
        Usuario usuario = buscarUsuarioPorEmail(email);
        if (usuario != null && usuario.getClave().equals(clave)) {
            return usuario;
        }
        return null;
    }

    /**
     * Busca una persona por su cédula
     *
     * @param cedula Cédula de la persona
     * @return Persona encontrada o null
     */
    public Persona buscarPersonaPorCedula(String cedula) {
        for (int i = 0; i < listaPersonas.size(); i++) {
            Persona persona = listaPersonas.get(i);
            if (persona.getCedula().equals(cedula)) {
                return persona;
            }
        }
        return null;
    }

    /**
     * Busca personas por su rol
     *
     * @param rol Rol de las personas
     * @return Lista de personas con ese rol
     */
    public ListaEnlazada<Persona> buscarPersonasPorRol(String rol) {
        ListaEnlazada<Persona> resultado = new ListaEnlazada<>();
        for (int i = 0; i < listaPersonas.size(); i++) {
            Persona persona = listaPersonas.get(i);
            if (persona.getRol().equals(rol)) {
                resultado.add(persona);
            }
        }
        return resultado;
    }

    /**
     * Busca una sede por su ciudad
     *
     * @param ciudad Ciudad de la sede
     * @return Sede encontrada o null
     */
    public Sede buscarSedePorCiudad(String ciudad) {
        for (int i = 0; i < listaSedes.size(); i++) {
            Sede sede = listaSedes.get(i);
            if (sede.getCiudad().equals(ciudad)) {
                return sede;
            }
        }
        return null;
    }

    /**
     * Busca propiedades por su estado
     *
     * @param estado Estado de las propiedades
     * @return Lista de propiedades en ese estado
     */
    public ListaEnlazada<Propiedad> buscarPropiedadesPorEstado(String estado) {
        ListaEnlazada<Propiedad> resultado = new ListaEnlazada<>();
        for (int i = 0; i < listaPropiedades.size(); i++) {
            Propiedad propiedad = listaPropiedades.get(i);
            if (propiedad.getEstado().equals(estado)) {
                resultado.add(propiedad);
            }
        }
        return resultado;
    }

    /**
     * Busca propiedades por su encargado
     *
     * @param empleado Encargado de las propiedades
     * @return Lista de propiedades a cargo de ese empleado
     */
    public ListaEnlazada<Propiedad> buscarPropiedadesPorEncargado(Empleado empleado) {
        ListaEnlazada<Propiedad> resultado = new ListaEnlazada<>();
        for (int i = 0; i < listaPropiedades.size(); i++) {
            Propiedad propiedad = listaPropiedades.get(i);
            if (propiedad.getEncargado() != null
                    && propiedad.getEncargado().getEmail().equals(empleado.getEmail())) {
                resultado.add(propiedad);
            }
        }
        return resultado;
    }

    /**
     * Agrega una propiedad a la cola de propiedades pendientes
     *
     * @param propiedad Propiedad a agregar
     */
    public void agregarPropiedadACola(Propiedad propiedad) {
        colaPropiedades.enQueue(propiedad);
    }

    /**
     * Obtiene la siguiente propiedad en la cola
     *
     * @return Propiedad en el frente de la cola
     */
    public Propiedad siguientePropiedadEnCola() {
        if (colaPropiedades.isEmpty()) {
            return null;
        }
        return colaPropiedades.peek();
    }

    /**
     * Extrae la siguiente propiedad de la cola
     *
     * @return Propiedad extraída
     */
    public Propiedad extraerPropiedadDeCola() {
        if (colaPropiedades.isEmpty()) {
            return null;
        }
        return colaPropiedades.deQueue();
    }

    /**
     * Busca agendas por cliente
     *
     * @param cliente Cliente de las agendas
     * @return Lista de agendas de ese cliente
     */
    public ListaEnlazada<Agenda> buscarAgendasPorCliente(Cliente cliente) {
        ListaEnlazada<Agenda> resultado = new ListaEnlazada<>();
        for (int i = 0; i < listaAgendas.size(); i++) {
            Agenda agenda = listaAgendas.get(i);
            if (agenda.getCliente() != null
                    && agenda.getCliente().getEmail().equals(cliente.getEmail())) {
                resultado.add(agenda);
            }
        }
        return resultado;
    }

    /**
     * Guarda un nuevo usuario
     *
     * @param usuario Usuario a guardar
     * @return true si se guardó exitosamente
     */
    public boolean guardarUsuario(Usuario usuario) {
        // Verificar que no exista ya
        if (buscarUsuarioPorEmail(usuario.getEmail()) != null) {
            return false;
        }
        listaUsuarios.add(usuario);
        escribirUsuarios();
        return true;
    }

    /**
     * Guarda una nueva persona
     *
     * @param persona Persona a guardar
     * @return true si se guardó exitosamente
     */
    public boolean guardarPersona(Persona persona) {
        // Guardar primero como usuario
        if (!guardarUsuario(persona)) {
            return false;
        }
        // Verificar que no exista ya por cédula
        if (buscarPersonaPorCedula(persona.getCedula()) != null) {
            // Eliminar el usuario que acabamos de agregar
            listaUsuarios.remove(persona);
            escribirUsuarios();
            return false;
        }
        listaPersonas.add(persona);
        escribirPersonas();

        // Guardar en la lista específica según su rol
        if (persona.getRol().equals(Usuario.ADMINISTRATIVO)) {
            listaAdministradores.add((Administrador) persona);
            escribirAdministradores();
        } else if (persona.getRol().equals(Usuario.EMPLEADO)) {
            listaEmpleados.add((Empleado) persona);
            escribirEmpleados();
        } else if (persona.getRol().equals(Usuario.CLIENTE)) {
            listaClientes.add((Cliente) persona);
            escribirClientes();
        }

        return true;
    }

    /**
     * Guarda una nueva sede
     *
     * @param sede Sede a guardar
     * @return true si se guardó exitosamente
     */
    public boolean guardarSede(Sede sede) {
        // Verificar que no exista ya
        if (buscarSedePorCiudad(sede.getCiudad()) != null) {
            return false;
        }
        listaSedes.add(sede);
        escribirSedes();
        return true;
    }

    /**
     * Guarda una nueva propiedad
     *
     * @param propiedad Propiedad a guardar
     * @return true si se guardó exitosamente
     */
    public boolean guardarPropiedad(Propiedad propiedad) {
        // Verificar que no exista ya
        for (int i = 0; i < listaPropiedades.size(); i++) {
            if (listaPropiedades.get(i).getId().equals(propiedad.getId())) {
                return false;
            }
        }
        listaPropiedades.add(propiedad);
        escribirPropiedades();

        // Si está pendiente de asignación, agregarla a la cola
        if (propiedad.getEstado().equals(Propiedad.ESTADO_PENDIENTE_ASIGNACION)) {
            agregarPropiedadACola(propiedad);
        }

        return true;
    }

    /**
     * Actualiza una propiedad existente
     *
     * @param propiedad Propiedad a actualizar
     * @return true si se actualizó exitosamente
     */
    public boolean actualizarPropiedad(Propiedad propiedad) {
        for (int i = 0; i < listaPropiedades.size(); i++) {
            if (listaPropiedades.get(i).getId().equals(propiedad.getId())) {
                listaPropiedades.remove(i);
                listaPropiedades.add(propiedad, i);
                escribirPropiedades();
                return true;
            }
        }
        return false;
    }

    /**
     * Guarda una nueva agenda
     *
     * @param agenda Agenda a guardar
     * @return true si se guardó exitosamente
     */
    public boolean guardarAgenda(Agenda agenda) {
        // Verificar que no exista ya
        for (int i = 0; i < listaAgendas.size(); i++) {
            if (listaAgendas.get(i).getId().equals(agenda.getId())) {
                return false;
            }
        }
        listaAgendas.add(agenda);
        escribirAgendas();
        return true;
    }

    /**
     * Guarda un nuevo mensaje
     *
     * @param mensaje Mensaje a guardar
     * @return true si se guardó exitosamente
     */
    public boolean guardarMensaje(Mensaje mensaje) {
        // Verificar que no exista ya
        for (int i = 0; i < listaMensajes.size(); i++) {
            if (listaMensajes.get(i).getId().equals(mensaje.getId())) {
                return false;
            }
        }
        listaMensajes.add(mensaje);
        escribirMensajes();
        return true;
    }
}
