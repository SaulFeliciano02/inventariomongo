package symongo.inventariomongo.entities;

public class Suplidor {
    private int codigoArticulo;
    private int codigoSuplidor;
    private int tiempoEntrega;
    private int precioCompra;

    public Suplidor(int codigoArticulo, int codigoSuplidor, int tiempoEntrega, int precioCompra){
        this.codigoArticulo = codigoArticulo;
        this.codigoSuplidor = codigoSuplidor;
        this.tiempoEntrega = tiempoEntrega;
        this.precioCompra = precioCompra;
    }

    public int getCodigoArticulo() {
        return codigoArticulo;
    }

    public int getPrecioCompra() {
        return precioCompra;
    }

    public int getCodigoSuplidor() {
        return codigoSuplidor;
    }

    public int getTiempoEntrega() {
        return tiempoEntrega;
    }

    public void setCodigoArticulo(int codigoArticulo) {
        this.codigoArticulo = codigoArticulo;
    }

    public void setPrecioCompra(int precioCompra) {
        this.precioCompra = precioCompra;
    }

    public void setCodigoSuplidor(int codigoSuplidor) {
        this.codigoSuplidor = codigoSuplidor;
    }

    public void setTiempoEntrega(int tiempoEntrega) {
        this.tiempoEntrega = tiempoEntrega;
    }
}
