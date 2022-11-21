package net.iessanclemente.a19lazaropm.aservice.modelslists;

public class ElementListVitrinas {
    private int id;
    private String empresa;
    private String fabricante;
    private String tipoVitrina;
    private int longitudVitrina;
    private float longitudGuillotina;
    private String referenciaVitrina;
    private String inventarioVitrina;
    private int anhoVitrina;
    private String contrato;

    public ElementListVitrinas(String empresa, int id, String fabricante, String tipoVitrina, int longitudVitrina,
                               float longitudGuillotina, String referenciaVitrina,
                               String inventarioVitrina, int anhoVitrina, String contrato) {

        this.id = id;
        this.empresa = empresa;
        this.fabricante = fabricante;
        this.tipoVitrina = tipoVitrina;
        this.longitudVitrina = longitudVitrina;
        this.longitudGuillotina = longitudGuillotina;
        this.referenciaVitrina = referenciaVitrina;
        this.inventarioVitrina = inventarioVitrina;
        this.anhoVitrina = anhoVitrina;
        this.contrato = contrato;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getFabricante() {return fabricante; }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public String getTipoVitrina() {
        return tipoVitrina;
    }

    public void setTipoVitrina(String tipoVitrina) {
        this.tipoVitrina = tipoVitrina;
    }

    public int getLongitudVitrina() {
        return longitudVitrina;
    }

    public void setLongitudVitrina(int longitudVitrina) {
        this.longitudVitrina = longitudVitrina;
    }

    public float getLongitudGuillotina() {
        return longitudGuillotina;
    }

    public void setLongitudGuillotina(float longitudGuillotina) {
        this.longitudGuillotina = longitudGuillotina;
    }

    public String getReferenciaVitrina() {
        return referenciaVitrina;
    }

    public void setReferenciaVitrina(String referenciaVitrina) {
        this.referenciaVitrina = referenciaVitrina;
    }

    public String getInventarioVitrina() {
        return inventarioVitrina;
    }

    public void setInventarioVitrina(String inventarioVitrina) {
        this.inventarioVitrina = inventarioVitrina;
    }

    public int getAnhoVitrina() {
        return anhoVitrina;
    }

    public void setAnhoVitrina(int anhoVitrina) {
        this.anhoVitrina = anhoVitrina;
    }

    public String getContrato() {
        return contrato;
    }

    public void setContrato(String contrato) {
        this.contrato = contrato;
    }

    @Override
    public String toString() {
        return "ElementListVitrinas{" +
                "id=" + id +
                ", empresa='" + empresa + '\'' +
                ", fabricante='" + fabricante + '\'' +
                ", tipoVitrina='" + tipoVitrina + '\'' +
                ", longitudVitrina=" + longitudVitrina +
                ", longitudGuillotina=" + longitudGuillotina +
                ", referenciaVitrina='" + referenciaVitrina + '\'' +
                ", inventarioVitrina='" + inventarioVitrina + '\'' +
                ", anhoVitrina=" + anhoVitrina +
                ", contrato='" + contrato + '\'' +
                '}';
    }
}
