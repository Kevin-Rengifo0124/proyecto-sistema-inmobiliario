package dao.implementacion;

import dao.MatrizSedesDAO;
import excepciones.SedeExistenteException;
import modelo.MatrizSedes;
import modelo.Sede;
import singleton.Singleton;

public class MatrizSedesDAOImpl implements MatrizSedesDAO {

    private MatrizSedes matrizSedes;

    public MatrizSedesDAOImpl() {
        this.matrizSedes = Singleton.getINSTANCIA().getMatrizSedes();
    }

    @Override
    public void agregarSede(Sede sede, int fila, int columna) throws SedeExistenteException {
        // Verificar que no exista una sede en la misma ciudad
        if (buscarSedePorCiudad(sede.getCiudad()) != null) {
            throw new SedeExistenteException(sede.getCiudad());
        }

        boolean resultado = matrizSedes.agregarSede(sede, fila, columna);
        if (!resultado) {
            throw new RuntimeException("No se pudo agregar la sede a la matriz en la posición (" + fila + "," + columna + ")");
        }

        Singleton.getINSTANCIA().escribirMatrizSedes();
    }

    @Override
    public Sede obtenerSede(int fila, int columna) {
        return matrizSedes.obtenerSede(fila, columna);
    }

    @Override
    public boolean removerSede(int fila, int columna) {
        boolean resultado = matrizSedes.removerSede(fila, columna);
        if (resultado) {
            Singleton.getINSTANCIA().escribirMatrizSedes();
        }
        return resultado;
    }

    @Override
    public Sede buscarSedePorCiudad(String ciudad) {
        return matrizSedes.buscarSedePorCiudad(ciudad);
    }

    @Override
    public int[] getPosicionSede(String ciudad) {
        return matrizSedes.getPosicionSede(ciudad);
    }

    @Override
    public int getEstadoCasilla(int fila, int columna) {
        return matrizSedes.getEstadoCasilla(fila, columna);
    }

    @Override
    public MatrizSedes obtenerMatriz() {
        return matrizSedes;
    }

    @Override
    public void guardarMatriz(MatrizSedes matriz) {
        this.matrizSedes = matriz;
        Singleton.getINSTANCIA().escribirMatrizSedes();
    }
}
