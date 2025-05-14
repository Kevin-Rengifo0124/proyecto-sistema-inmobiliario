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
            boolean dirCreado = directorio.mkdirs();
            if (!dirCreado) {
                System.err.println("No se pudo crear el directorio " + DIRECTORIO_BASE);
            } else {
                System.out.println("Directorio " + DIRECTORIO_BASE + " creado correctamente");
            }
        }

        // Inicializar todas las estructuras de datos
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

        // Verificar si existe un administrador y crearlo si es necesario
        if (!existeAdministrador()) {
            crearAdministradorInicial();
        }
    }

    /**
     * Verifica si ya existe un administrador en el sistema
     */
    private boolean existeAdministrador() {
        if (listaUsuarios == null || listaUsuarios.isEmpty()) {
            return false;
        }

        for (int i = 0; i < listaUsuarios.size(); i++) {
            Usuario usuario = listaUsuarios.get(i);
            if (usuario.getRol().equals(Usuario.ADMINISTRATIVO)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Crea un administrador por defecto
     */
    private void crearAdministradorInicial() {
        try {
            System.out.println("Creando administrador por defecto...");
            Administrador admin = new Administrador(
                    "Administrador Sistema",
                    "1000000000",
                    "3001234567",
                    "Calle Principal #123",
                    "admin@inmobiliaria.com",
                    "admin123",
                    Usuario.ADMINISTRATIVO);

            if (listaUsuarios == null) {
                listaUsuarios = new ListaEnlazada<>();
            }

            if (listaAdministradores == null) {
                listaAdministradores = new ListaEnlazada<>();
            }

            listaUsuarios.add(admin);
            listaAdministradores.add(admin);

            escribirUsuarios();
            escribirAdministradores();

            System.out.println("Administrador por defecto creado exitosamente");
        } catch (Exception e) {
            System.err.println("Error al crear administrador por defecto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Singleton getINSTANCIA() {
        return INSTANCIA;
    }

    private Queue<Propiedad> inicializarColaPropiedades() {
        Queue<Propiedad> cola = new Queue<>();

        if (listaPropiedades != null) {
            for (int i = 0; i < listaPropiedades.size(); i++) {
                Propiedad propiedad = listaPropiedades.get(i);
                if (propiedad.getEstado().equals(Propiedad.ESTADO_PENDIENTE_ASIGNACION)) {
                    cola.enQueue(propiedad);
                }
            }
        }

        return cola;
    }

    private ListaEnlazada<Usuario> leerUsuarios() {
        ListaEnlazada<Usuario> listaVacia = new ListaEnlazada<>();

        // Verificar que exista el directorio
        File directorio = new File(DIRECTORIO_BASE);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        // Verificar que exista el archivo
        File archivo = new File(DIRECTORIO_BASE + "usuarios.dat");
        if (!archivo.exists()) {
            System.out.println("El archivo usuarios.dat no existe. Se creará una lista vacía.");
            return listaVacia;
        }

        // Leer el archivo
        FileInputStream fileStream = null;
        ObjectInputStream lector = null;

        try {
            fileStream = new FileInputStream(archivo);
            lector = new ObjectInputStream(fileStream);
            ListaEnlazada<Usuario> lista = (ListaEnlazada<Usuario>) lector.readObject();
            return lista;
        } catch (ClassNotFoundException e) {
            System.err.println("Error de compatibilidad de clase al leer usuarios: " + e.getMessage());
            return listaVacia;
        } catch (IOException e) {
            System.err.println("Error de I/O al leer usuarios: " + e.getMessage());
            return listaVacia;
        } finally {
            try {
                if (lector != null) {
                    lector.close();
                }
                if (fileStream != null) {
                    fileStream.close();
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar streams de usuarios: " + e.getMessage());
            }
        }
    }

    public void escribirUsuarios() {
        // Verificar directorio
        File directorio = new File(DIRECTORIO_BASE);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        FileOutputStream fileStream = null;
        ObjectOutputStream escritor = null;

        try {
            fileStream = new FileOutputStream(DIRECTORIO_BASE + "usuarios.dat");
            escritor = new ObjectOutputStream(fileStream);
            escritor.writeObject(listaUsuarios);
        } catch (IOException ex) {
            System.err.println("Error al escribir usuarios: " + ex.getMessage());
        } finally {
            try {
                if (escritor != null) {
                    escritor.close();
                }
                if (fileStream != null) {
                    fileStream.close();
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar streams al escribir usuarios: " + e.getMessage());
            }
        }
    }

    private ListaEnlazada<Administrador> leerAdministradores() {
        ListaEnlazada<Administrador> listaVacia = new ListaEnlazada<>();

        // Verificar directorio
        File directorio = new File(DIRECTORIO_BASE);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        // Verificar archivo
        File archivo = new File(DIRECTORIO_BASE + "administradores.dat");
        if (!archivo.exists()) {
            System.out.println("El archivo administradores.dat no existe. Se creará una lista vacía.");
            return listaVacia;
        }

        // Leer archivo
        FileInputStream fileStream = null;
        ObjectInputStream lector = null;

        try {
            fileStream = new FileInputStream(archivo);
            lector = new ObjectInputStream(fileStream);
            ListaEnlazada<Administrador> lista = (ListaEnlazada<Administrador>) lector.readObject();
            return lista;
        } catch (ClassNotFoundException e) {
            System.err.println("Error de compatibilidad de clase al leer administradores: " + e.getMessage());
            return listaVacia;
        } catch (IOException e) {
            System.err.println("Error de I/O al leer administradores: " + e.getMessage());
            return listaVacia;
        } finally {
            try {
                if (lector != null) {
                    lector.close();
                }
                if (fileStream != null) {
                    fileStream.close();
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar streams de administradores: " + e.getMessage());
            }
        }
    }

    public void escribirAdministradores() {
        // Verificar directorio
        File directorio = new File(DIRECTORIO_BASE);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        FileOutputStream fileStream = null;
        ObjectOutputStream escritor = null;

        try {
            fileStream = new FileOutputStream(DIRECTORIO_BASE + "administradores.dat");
            escritor = new ObjectOutputStream(fileStream);
            escritor.writeObject(listaAdministradores);
        } catch (IOException ex) {
            System.err.println("Error al escribir administradores: " + ex.getMessage());
        } finally {
            try {
                if (escritor != null) {
                    escritor.close();
                }
                if (fileStream != null) {
                    fileStream.close();
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar streams al escribir administradores: " + e.getMessage());
            }
        }
    }

    private ListaEnlazada<Empleado> leerEmpleados() {
        ListaEnlazada<Empleado> listaVacia = new ListaEnlazada<>();

        // Verificar directorio
        File directorio = new File(DIRECTORIO_BASE);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        // Verificar archivo
        File archivo = new File(DIRECTORIO_BASE + "empleados.dat");
        if (!archivo.exists()) {
            System.out.println("El archivo empleados.dat no existe. Se creará una lista vacía.");
            return listaVacia;
        }

        // Leer archivo
        FileInputStream fileStream = null;
        ObjectInputStream lector = null;

        try {
            fileStream = new FileInputStream(archivo);
            lector = new ObjectInputStream(fileStream);
            ListaEnlazada<Empleado> lista = (ListaEnlazada<Empleado>) lector.readObject();
            return lista;
        } catch (ClassNotFoundException e) {
            System.err.println("Error de compatibilidad de clase al leer empleados: " + e.getMessage());
            return listaVacia;
        } catch (IOException e) {
            System.err.println("Error de I/O al leer empleados: " + e.getMessage());
            return listaVacia;
        } finally {
            try {
                if (lector != null) {
                    lector.close();
                }
                if (fileStream != null) {
                    fileStream.close();
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar streams de empleados: " + e.getMessage());
            }
        }
    }

    public void escribirEmpleados() {
        // Verificar directorio
        File directorio = new File(DIRECTORIO_BASE);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        FileOutputStream fileStream = null;
        ObjectOutputStream escritor = null;

        try {
            fileStream = new FileOutputStream(DIRECTORIO_BASE + "empleados.dat");
            escritor = new ObjectOutputStream(fileStream);
            escritor.writeObject(listaEmpleados);
        } catch (IOException ex) {
            System.err.println("Error al escribir empleados: " + ex.getMessage());
        } finally {
            try {
                if (escritor != null) {
                    escritor.close();
                }
                if (fileStream != null) {
                    fileStream.close();
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar streams al escribir empleados: " + e.getMessage());
            }
        }
    }

    private ListaEnlazada<Cliente> leerClientes() {
        ListaEnlazada<Cliente> listaVacia = new ListaEnlazada<>();

        // Verificar directorio
        File directorio = new File(DIRECTORIO_BASE);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        // Verificar archivo
        File archivo = new File(DIRECTORIO_BASE + "clientes.dat");
        if (!archivo.exists()) {
            System.out.println("El archivo clientes.dat no existe. Se creará una lista vacía.");
            return listaVacia;
        }

        // Leer archivo
        FileInputStream fileStream = null;
        ObjectInputStream lector = null;

        try {
            fileStream = new FileInputStream(archivo);
            lector = new ObjectInputStream(fileStream);
            ListaEnlazada<Cliente> lista = (ListaEnlazada<Cliente>) lector.readObject();
            return lista;
        } catch (ClassNotFoundException e) {
            System.err.println("Error de compatibilidad de clase al leer clientes: " + e.getMessage());
            return listaVacia;
        } catch (IOException e) {
            System.err.println("Error de I/O al leer clientes: " + e.getMessage());
            return listaVacia;
        } finally {
            try {
                if (lector != null) {
                    lector.close();
                }
                if (fileStream != null) {
                    fileStream.close();
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar streams de clientes: " + e.getMessage());
            }
        }
    }

    public void escribirClientes() {
        // Verificar directorio
        File directorio = new File(DIRECTORIO_BASE);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        FileOutputStream fileStream = null;
        ObjectOutputStream escritor = null;

        try {
            fileStream = new FileOutputStream(DIRECTORIO_BASE + "clientes.dat");
            escritor = new ObjectOutputStream(fileStream);
            escritor.writeObject(listaClientes);
        } catch (IOException ex) {
            System.err.println("Error al escribir clientes: " + ex.getMessage());
        } finally {
            try {
                if (escritor != null) {
                    escritor.close();
                }
                if (fileStream != null) {
                    fileStream.close();
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar streams al escribir clientes: " + e.getMessage());
            }
        }
    }

    private ListaEnlazada<Sede> leerSedes() {
        ListaEnlazada<Sede> listaVacia = new ListaEnlazada<>();

        // Verificar directorio
        File directorio = new File(DIRECTORIO_BASE);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        // Verificar archivo
        File archivo = new File(DIRECTORIO_BASE + "sedes.dat");
        if (!archivo.exists()) {
            System.out.println("El archivo sedes.dat no existe. Se creará una lista vacía.");
            return listaVacia;
        }

        // Leer archivo
        FileInputStream fileStream = null;
        ObjectInputStream lector = null;

        try {
            fileStream = new FileInputStream(archivo);
            lector = new ObjectInputStream(fileStream);
            ListaEnlazada<Sede> lista = (ListaEnlazada<Sede>) lector.readObject();
            return lista;
        } catch (ClassNotFoundException e) {
            System.err.println("Error de compatibilidad de clase al leer sedes: " + e.getMessage());
            return listaVacia;
        } catch (IOException e) {
            System.err.println("Error de I/O al leer sedes: " + e.getMessage());
            return listaVacia;
        } finally {
            try {
                if (lector != null) {
                    lector.close();
                }
                if (fileStream != null) {
                    fileStream.close();
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar streams de sedes: " + e.getMessage());
            }
        }
    }

    public void escribirSedes() {
        // Verificar directorio
        File directorio = new File(DIRECTORIO_BASE);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        FileOutputStream fileStream = null;
        ObjectOutputStream escritor = null;

        try {
            fileStream = new FileOutputStream(DIRECTORIO_BASE + "sedes.dat");
            escritor = new ObjectOutputStream(fileStream);
            escritor.writeObject(listaSedes);
        } catch (IOException ex) {
            System.err.println("Error al escribir sedes: " + ex.getMessage());
        } finally {
            try {
                if (escritor != null) {
                    escritor.close();
                }
                if (fileStream != null) {
                    fileStream.close();
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar streams al escribir sedes: " + e.getMessage());
            }
        }
    }

    private ListaEnlazada<Propiedad> leerPropiedades() {
        ListaEnlazada<Propiedad> listaVacia = new ListaEnlazada<>();

        // Verificar directorio
        File directorio = new File(DIRECTORIO_BASE);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        // Verificar archivo
        File archivo = new File(DIRECTORIO_BASE + "propiedades.dat");
        if (!archivo.exists()) {
            System.out.println("El archivo propiedades.dat no existe. Se creará una lista vacía.");
            return listaVacia;
        }

        // Leer archivo
        FileInputStream fileStream = null;
        ObjectInputStream lector = null;

        try {
            fileStream = new FileInputStream(archivo);
            lector = new ObjectInputStream(fileStream);
            ListaEnlazada<Propiedad> lista = (ListaEnlazada<Propiedad>) lector.readObject();
            return lista;
        } catch (ClassNotFoundException e) {
            System.err.println("Error de compatibilidad de clase al leer propiedades: " + e.getMessage());
            return listaVacia;
        } catch (IOException e) {
            System.err.println("Error de I/O al leer propiedades: " + e.getMessage());
            return listaVacia;
        } finally {
            try {
                if (lector != null) {
                    lector.close();
                }
                if (fileStream != null) {
                    fileStream.close();
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar streams de propiedades: " + e.getMessage());
            }
        }
    }

    public void escribirPropiedades() {
        // Verificar directorio
        File directorio = new File(DIRECTORIO_BASE);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        FileOutputStream fileStream = null;
        ObjectOutputStream escritor = null;

        try {
            fileStream = new FileOutputStream(DIRECTORIO_BASE + "propiedades.dat");
            escritor = new ObjectOutputStream(fileStream);
            escritor.writeObject(listaPropiedades);
        } catch (IOException ex) {
            System.err.println("Error al escribir propiedades: " + ex.getMessage());
        } finally {
            try {
                if (escritor != null) {
                    escritor.close();
                }
                if (fileStream != null) {
                    fileStream.close();
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar streams al escribir propiedades: " + e.getMessage());
            }
        }
    }

    private ListaEnlazada<Inmueble> leerInmuebles() {
        ListaEnlazada<Inmueble> listaVacia = new ListaEnlazada<>();

        // Verificar directorio
        File directorio = new File(DIRECTORIO_BASE);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        // Verificar archivo
        File archivo = new File(DIRECTORIO_BASE + "inmuebles.dat");
        if (!archivo.exists()) {
            System.out.println("El archivo inmuebles.dat no existe. Se creará una lista vacía.");
            return listaVacia;
        }

        // Leer archivo
        FileInputStream fileStream = null;
        ObjectInputStream lector = null;

        try {
            fileStream = new FileInputStream(archivo);
            lector = new ObjectInputStream(fileStream);
            ListaEnlazada<Inmueble> lista = (ListaEnlazada<Inmueble>) lector.readObject();
            return lista;
        } catch (ClassNotFoundException e) {
            System.err.println("Error de compatibilidad de clase al leer inmuebles: " + e.getMessage());
            return listaVacia;
        } catch (IOException e) {
            System.err.println("Error de I/O al leer inmuebles: " + e.getMessage());
            return listaVacia;
        } finally {
            try {
                if (lector != null) {
                    lector.close();
                }
                if (fileStream != null) {
                    fileStream.close();
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar streams de inmuebles: " + e.getMessage());
            }
        }
    }

    public void escribirInmuebles() {
        // Verificar directorio
        File directorio = new File(DIRECTORIO_BASE);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        FileOutputStream fileStream = null;
        ObjectOutputStream escritor = null;

        try {
            fileStream = new FileOutputStream(DIRECTORIO_BASE + "inmuebles.dat");
            escritor = new ObjectOutputStream(fileStream);
            escritor.writeObject(listaInmuebles);
        } catch (IOException ex) {
            System.err.println("Error al escribir inmuebles: " + ex.getMessage());
        } finally {
            try {
                if (escritor != null) {
                    escritor.close();
                }
                if (fileStream != null) {
                    fileStream.close();
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar streams al escribir inmuebles: " + e.getMessage());
            }
        }
    }

    private ListaEnlazada<Agenda> leerAgendas() {
        ListaEnlazada<Agenda> listaVacia = new ListaEnlazada<>();

        // Verificar directorio
        File directorio = new File(DIRECTORIO_BASE);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        // Verificar archivo
        File archivo = new File(DIRECTORIO_BASE + "agendas.dat");
        if (!archivo.exists()) {
            System.out.println("El archivo agendas.dat no existe. Se creará una lista vacía.");
            return listaVacia;
        }

        // Leer archivo
        FileInputStream fileStream = null;
        ObjectInputStream lector = null;

        try {
            fileStream = new FileInputStream(archivo);
            lector = new ObjectInputStream(fileStream);
            ListaEnlazada<Agenda> lista = (ListaEnlazada<Agenda>) lector.readObject();
            return lista;
        } catch (ClassNotFoundException e) {
            System.err.println("Error de compatibilidad de clase al leer agendas: " + e.getMessage());
            return listaVacia;
        } catch (IOException e) {
            System.err.println("Error de I/O al leer agendas: " + e.getMessage());
            return listaVacia;
        } finally {
            try {
                if (lector != null) {
                    lector.close();
                }
                if (fileStream != null) {
                    fileStream.close();
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar streams de agendas: " + e.getMessage());
            }
        }
    }

    public void escribirAgendas() {
        // Verificar directorio
        File directorio = new File(DIRECTORIO_BASE);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        FileOutputStream fileStream = null;
        ObjectOutputStream escritor = null;

        try {
            fileStream = new FileOutputStream(DIRECTORIO_BASE + "agendas.dat");
            escritor = new ObjectOutputStream(fileStream);
            escritor.writeObject(listaAgendas);
        } catch (IOException ex) {
            System.err.println("Error al escribir agendas: " + ex.getMessage());
        } finally {
            try {
                if (escritor != null) {
                    escritor.close();
                }
                if (fileStream != null) {
                    fileStream.close();
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar streams al escribir agendas: " + e.getMessage());
            }
        }
    }

    private ListaEnlazada<Mensaje> leerMensajes() {
        ListaEnlazada<Mensaje> listaVacia = new ListaEnlazada<>();

        // Verificar directorio
        File directorio = new File(DIRECTORIO_BASE);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        // Verificar archivo
        File archivo = new File(DIRECTORIO_BASE + "mensajes.dat");
        if (!archivo.exists()) {
            System.out.println("El archivo mensajes.dat no existe. Se creará una lista vacía.");
            return listaVacia;
        }

        // Leer archivo
        FileInputStream fileStream = null;
        ObjectInputStream lector = null;

        try {
            fileStream = new FileInputStream(archivo);
            lector = new ObjectInputStream(fileStream);
            ListaEnlazada<Mensaje> lista = (ListaEnlazada<Mensaje>) lector.readObject();
            return lista;
        } catch (ClassNotFoundException e) {
            System.err.println("Error de compatibilidad de clase al leer mensajes: " + e.getMessage());
            return listaVacia;
        } catch (IOException e) {
            System.err.println("Error de I/O al leer mensajes: " + e.getMessage());
            return listaVacia;
        } finally {
            try {
                if (lector != null) {
                    lector.close();
                }
                if (fileStream != null) {
                    fileStream.close();
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar streams de mensajes: " + e.getMessage());
            }
        }
    }

    public void escribirMensajes() {
        // Verificar directorio
        File directorio = new File(DIRECTORIO_BASE);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        FileOutputStream fileStream = null;
        ObjectOutputStream escritor = null;

        try {
            fileStream = new FileOutputStream(DIRECTORIO_BASE + "mensajes.dat");
            escritor = new ObjectOutputStream(fileStream);
            escritor.writeObject(listaMensajes);
        } catch (IOException ex) {
            System.err.println("Error al escribir mensajes: " + ex.getMessage());
        } finally {
            try {
                if (escritor != null) {
                    escritor.close();
                }
                if (fileStream != null) {
                    fileStream.close();
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar streams al escribir mensajes: " + e.getMessage());
            }
        }
    }

    private MatrizSedes leerMatrizSedes() {
        // Verificar directorio
        File directorio = new File(DIRECTORIO_BASE);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        // Verificar archivo
        File archivo = new File(DIRECTORIO_BASE + "matriz_sedes.dat");
        if (!archivo.exists()) {
            System.out.println("El archivo matriz_sedes.dat no existe. Se creará una nueva matriz.");
            return new MatrizSedes();
        }

        // Leer archivo
        FileInputStream fileStream = null;
        ObjectInputStream lector = null;

        try {
            fileStream = new FileInputStream(archivo);
            lector = new ObjectInputStream(fileStream);
            MatrizSedes matriz = (MatrizSedes) lector.readObject();
            return matriz;
        } catch (ClassNotFoundException e) {
            System.err.println("Error de compatibilidad de clase al leer matriz de sedes: " + e.getMessage());
            return new MatrizSedes();
        } catch (IOException e) {
            System.err.println("Error de I/O al leer matriz de sedes: " + e.getMessage());
            return new MatrizSedes();
        } finally {
            try {
                if (lector != null) {
                    lector.close();
                }
                if (fileStream != null) {
                    fileStream.close();
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar streams de matriz de sedes: " + e.getMessage());
            }
        }
    }

    public void escribirMatrizSedes() {
        // Verificar directorio
        File directorio = new File(DIRECTORIO_BASE);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        FileOutputStream fileStream = null;
        ObjectOutputStream escritor = null;

        try {
            fileStream = new FileOutputStream(DIRECTORIO_BASE + "matriz_sedes.dat");
            escritor = new ObjectOutputStream(fileStream);
            escritor.writeObject(matrizSedes);
        } catch (IOException ex) {
            System.err.println("Error al escribir matriz de sedes: " + ex.getMessage());
        } finally {
            try {
                if (escritor != null) {
                    escritor.close();
                }
                if (fileStream != null) {
                    fileStream.close();
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar streams al escribir matriz de sedes: " + e.getMessage());
            }
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
        if (listaUsuarios == null) {
            listaUsuarios = new ListaEnlazada<>();
        }
        return listaUsuarios;
    }

    public ListaEnlazada<Administrador> getListaAdministradores() {
        if (listaAdministradores == null) {
            listaAdministradores = new ListaEnlazada<>();
        }
        return listaAdministradores;
    }

    public ListaEnlazada<Empleado> getListaEmpleados() {
        if (listaEmpleados == null) {
            listaEmpleados = new ListaEnlazada<>();
        }
        return listaEmpleados;
    }

    public ListaEnlazada<Cliente> getListaClientes() {
        if (listaClientes == null) {
            listaClientes = new ListaEnlazada<>();
        }
        return listaClientes;
    }

    public ListaEnlazada<Sede> getListaSedes() {
        if (listaSedes == null) {
            listaSedes = new ListaEnlazada<>();
        }
        return listaSedes;
    }

    public ListaEnlazada<Propiedad> getListaPropiedades() {
        if (listaPropiedades == null) {
            listaPropiedades = new ListaEnlazada<>();
        }
        return listaPropiedades;
    }

    public ListaEnlazada<Inmueble> getListaInmuebles() {
        if (listaInmuebles == null) {
            listaInmuebles = new ListaEnlazada<>();
        }
        return listaInmuebles;
    }

    public ListaEnlazada<Agenda> getListaAgendas() {
        if (listaAgendas == null) {
            listaAgendas = new ListaEnlazada<>();
        }
        return listaAgendas;
    }

    public ListaEnlazada<Mensaje> getListaMensajes() {
        if (listaMensajes == null) {
            listaMensajes = new ListaEnlazada<>();
        }
        return listaMensajes;
    }

    public MatrizSedes getMatrizSedes() {
        if (matrizSedes == null) {
            matrizSedes = new MatrizSedes();
        }
        return matrizSedes;
    }

    public Queue<Propiedad> getColaPropiedades() {
        if (colaPropiedades == null) {
            colaPropiedades = new Queue<>();
        }
        return colaPropiedades;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }
}
