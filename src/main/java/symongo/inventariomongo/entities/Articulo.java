package symongo.inventariomongo.entities;

public class Articulo {
    public int codigoArticulo;
    public String descripcion;
    public int balanceActual;
    public String unidad;

    public Articulo(){}

    public Articulo(int codigoArticulo, String descripcion, int balanceActual, String unidad){
        this.codigoArticulo = codigoArticulo;
        this.descripcion = descripcion;
        this.balanceActual = balanceActual;
        this.unidad = unidad;
    }

    public int getCodigoArticulo() {
        return codigoArticulo;
    }

    public int getBalanceActual() {
        return balanceActual;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setCodigoArticulo(int codigoArticulo) {
        this.codigoArticulo = codigoArticulo;
    }

    public void setBalanceActual(int balanceActual) {
        this.balanceActual = balanceActual;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }
}
