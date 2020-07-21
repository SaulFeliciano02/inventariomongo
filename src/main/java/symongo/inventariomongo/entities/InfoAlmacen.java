package symongo.inventariomongo.entities;

public class InfoAlmacen {
    private String codigoAlmacen;
    private int balanceActual;

    public InfoAlmacen(String codigoAlmacen, int balanceActual){
        this.codigoAlmacen = codigoAlmacen;
        this.balanceActual = balanceActual;
    }

    public int getBalanceActual() {
        return balanceActual;
    }

    public String getCodigoAlmacen() {
        return codigoAlmacen;
    }

    public void setBalanceActual(int balanceActual) {
        this.balanceActual = balanceActual;
    }

    public void setCodigoAlmacen(String codigoAlmacen) {
        this.codigoAlmacen = codigoAlmacen;
    }
}
