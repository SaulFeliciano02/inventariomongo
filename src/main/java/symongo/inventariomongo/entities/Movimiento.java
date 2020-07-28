package symongo.inventariomongo.entities;

public class Movimiento {
    private int codigoMovimiento;
    private String tipoMovimiento;
    private int codigoArticulo;
    private int cantidad;
    private String fecha;

    public Movimiento(int codigoMovimiento, String tipoMovimiento, int codigoArticulo, int cantidad, String fecha){
        this.codigoMovimiento = codigoMovimiento;
        this.tipoMovimiento = tipoMovimiento;
        this.codigoArticulo = codigoArticulo;
        this.cantidad = cantidad;
        this.fecha = fecha;
    }

    public int getCodigoArticulo() {
        return codigoArticulo;
    }

    public int getCodigoMovimiento() {
        return codigoMovimiento;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getFecha() {
        return fecha;
    }

    public void setCodigoArticulo(int codigoArticulo) {
        this.codigoArticulo = codigoArticulo;
    }

    public void setCodigoMovimiento(int codigoMovimiento) {
        this.codigoMovimiento = codigoMovimiento;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }
}
