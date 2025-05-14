package singleton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import modelo.Administrador;
import modelo.Agenda;
import modelo.Cliente;
import modelo.Empleado;
import modelo.Inmueble;
import modelo.MatrizSedes;
import modelo.Mensaje;
import modelo.Persona;
import modelo.Propiedad;
import modelo.Sede;
import modelo.Usuario;
import util.implementacion.ListaEnlazada;
import util.implementacion.Queue;

public class Singleton {

    private static Singleton INSTANCIA = new Singleton();
    
    // Variable final static para la ruta de archivos
    private static final String DIRECTORIO_BASE = "data/";
    
    private ListaEnlazada<Usuario> listaUsuarios;
    private ListaEnlazada<Administrador> listaAdministradores;
    private ListaEnlazada<Empleado> listaEmpleados;
    private ListaEnlazada<Cliente> listaClientes;
    private ListaEnlazada<Sede> listaSedes;
    private ListaEnlazada<Propiedad> listaPropiedades;
    private ListaEnlazada<Inmueble> listaInmuebles;
    private ListaEnlazada<Agenda> listaAgendas;
    private ListaEnlazada<Mensaje> listaMensajes;
    private MatrizSedes matrizSedes;
    private Queue<Propiedad> colaPropiedades;
    
    private Usuario usuarioActual;
    
    private Singleton() {
        // Crear directorio si no existe
        File directorio = new File(DIRECTORIO_BASE);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
        
        this.listaUsuarios = leerUsuarios();
        this.listaAdministradores = leerAdministradores();
        this.listaEmpleados = leerEmpleados();
        this.listaClientes = leerClientes();
        this.listaSedes = leerSedes();
        this.listaPropiedades = leerPropiedades();
        this.listaInmuebles = leerInmuebles();
        this.listaAgendas = leerAgendas();
        this.listaMensajes = leerMensajes();
        this.matrizSedes = leerMatrizSedes();
        this.colaPropiedades = inicializarColaPropiedades();
    }
    
    public static Singleton getINSTANCIA() {
        return INSTANCIA;
    }
    
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
    
    private ListaEnlazada<Usuario> leerUsuarios() {
        try {
            FileInputStream archivo = new FileInputStream(DIRECTORIO_BASE + "usuarios.dat");
            ObjectInputStream lector = new ObjectInputStream(archivo);
            ListaEnlazada<Usuario> lista = (ListaEnlazada<Usuario>) lector.readObject();
            lector.close();
            return lista;
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return new ListaEnlazada<Usuario>();
        }
    }
    
    public void escribirUsuarios() {
        try {
            FileOutputStream archivo = new FileOutputStream(DIRECTORIO_BASE + "usuarios.dat");
            ObjectOutputStream escritor = new ObjectOutputStream(archivo);
            escritor.writeObject(listaUsuarios);
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
            return lista;
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return new ListaEnlazada<Administrador>();
        }
    }
    
    public void escribirAdministradores() {
        try {
            FileOutputStream archivo = new FileOutputStream(DIRECTORIO_BASE + "administradores.dat");
            ObjectOutputStream escritor = new ObjectOutputStream(archivo);
            escritor.writeObject(listaAdministradores);
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
            return lista;
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return new ListaEnlazada<Empleado>();
        }
    }
    
    public void escribirEmpleados() {
        try {
            FileOutputStream archivo = new FileOutputStream(DIRECTORIO_BASE + "empleados.dat");
            ObjectOutputStream escritor = new ObjectOutputStream(archivo);
            escritor.writeObject(listaEmpleados);
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
            return lista;
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return new ListaEnlazada<Cliente>();
        }
    }
    
    public void escribirClientes() {
        try {
            FileOutputStream archivo = new FileOutputStream(DIRECTORIO_BASE + "clientes.dat");
            ObjectOutputStream escritor = new ObjectOutputStream(archivo);
            escritor.writeObject(listaClientes);
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
            return lista;
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return new ListaEnlazada<Sede>();
        }
    }
    
    public void escribirSedes() {
        try {
            FileOutputStream archivo = new FileOutputStream(DIRECTORIO_BASE + "sedes.dat");
            ObjectOutputStream escritor = new ObjectOutputStream(archivo);
            escritor.writeObject(listaSedes);
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
            return lista;
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return new ListaEnlazada<Propiedad>();
        }
    }
    
    public void escribirPropiedades() {
        try {
            FileOutputStream archivo = new FileOutputStream(DIRECTORIO_BASE + "propiedades.dat");
            ObjectOutputStream escritor = new ObjectOutputStream(archivo);
            escritor.writeObject(listaPropiedades);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private ListaEnlazada<Inmueble> leerInmuebles() {
        try {
            FileInputStream archivo = new FileInputStream(DIRECTORIO_BASE + "inmuebles.dat");
            ObjectInputStream lector = new ObjectInputStream(archivo);
            ListaEnlazada<Inmueble> lista = (ListaEnlazada<Inmueble>) lector.readObject();
            lector.close();
            return lista;
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return new ListaEnlazada<Inmueble>();
        }
    }
    
    public void escribirInmuebles() {
        try {
            FileOutputStream archivo = new FileOutputStream(DIRECTORIO_BASE + "inmuebles.dat");
            ObjectOutputStream escritor = new ObjectOutputStream(archivo);
            escritor.writeObject(listaInmuebles);
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
            return lista;
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return new ListaEnlazada<Agenda>();
        }
    }
    
    public void escribirAgendas() {
        try {
            FileOutputStream archivo = new FileOutputStream(DIRECTORIO_BASE + "agendas.dat");
            ObjectOutputStream escritor = new ObjectOutputStream(archivo);
            escritor.writeObject(listaAgendas);
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
            return lista;
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return new ListaEnlazada<Mensaje>();
        }
    }
    
    public void escribirMensajes() {
        try {
            FileOutputStream archivo = new FileOutputStream(DIRECTORIO_BASE + "mensajes.dat");
            ObjectOutputStream escritor = new ObjectOutputStream(archivo);
            escritor.writeObject(listaMensajes);
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
            return matriz;
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return new MatrizSedes();
        }
    }
    
    public void escribirMatrizSedes() {
        try {
            FileOutputStream archivo = new FileOutputStream(DIRECTORIO_BASE + "matriz_sedes.dat");
            ObjectOutputStream escritor = new ObjectOutputStream(archivo);
            escritor.writeObject(matrizSedes);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void guardarTodo() {
        escribirUsuarios();
        escribirAdministradores();
        escribirEmpleados();
        escribirClientes();
        escribirSedes();
        escribirPropiedades();
        escribirInmuebles();
        escribirAgendas();
        escribirMensajes();
        escribirMatrizSedes();
    }

    public ListaEnlazada<Usuario> getListaUsuarios() {
        return listaUsuarios;
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
    
    public ListaEnlazada<Inmueble> getListaInmuebles() {
        return listaInmuebles;
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
}