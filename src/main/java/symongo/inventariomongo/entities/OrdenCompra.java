package symongo.inventariomongo.entities;

public class OrdenCompra {
    private int codigoOrdenCompra;
    private int codigoSuplidor;
    private String fechaOrden;
    private int montoTotal;
    private int codigoArticulo;
    private int precioSuplidor;
    private int precioCompra;

    public OrdenCompra(int codigoOrdenCompra, int codigoSuplidor, String fechaOrden, int montoTotal, int codigoArticulo, int precioSuplidor, int precioCompra){
        this.codigoOrdenCompra = codigoOrdenCompra;
        this.codigoSuplidor = codigoSuplidor;
        this.fechaOrden = fechaOrden;
        this.montoTotal = montoTotal;
        this.codigoArticulo = codigoArticulo;
        this.precioSuplidor = precioSuplidor;
        this.precioCompra = precioCompra;
    }

    public int getCodigoOrdenCompra() {
        return codigoOrdenCompra;
    }

    public int getCodigoSuplidor() {
        return codigoSuplidor;
    }

    public int getCodigoArticulo() {
        return codigoArticulo;
    }

    public String getFechaOrden() {
        return fechaOrden;
    }

    public int getMontoTotal() {
        return montoTotal;
    }

    public int getPrecioCompra() {
        return precioCompra;
    }

    public int getPrecioSuplidor() {
        return precioSuplidor;
    }

    public void setCodigoArticulo(int codigoArticulo) {
        this.codigoArticulo = codigoArticulo;
    }

    public void setCodigoOrdenCompra(int codigoOrdenCompra) {
        this.codigoOrdenCompra = codigoOrdenCompra;
    }

    public void setCodigoSuplidor(int codigoSuplidor) {
        this.codigoSuplidor = codigoSuplidor;
    }

    public void setFechaOrden(String fechaOrden) {
        this.fechaOrden = fechaOrden;
    }

    public void setMontoTotal(int montoTotal) {
        this.montoTotal = montoTotal;
    }

    public void setPrecioCompra(int precioCompra) {
        this.precioCompra = precioCompra;
    }

    public void setPrecioSuplidor(int precioSuplidor) {
        this.precioSuplidor = precioSuplidor;
    }
}
