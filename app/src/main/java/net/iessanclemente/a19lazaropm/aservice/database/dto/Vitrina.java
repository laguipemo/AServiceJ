package net.iessanclemente.a19lazaropm.aservice.database.dto;

public class Vitrina {

    private int id;
    private int idEmpresa;
    private int idTipo;
    private int idLongitud;
    private int idFabricante;
    private String vitrinaReferencia;
    private String vitrinaInventario;
    private int vitrinaAnho;
    private String vitrinaContrato;

    public Vitrina() {
    }

    public Vitrina(int id, int idEmpresa, int idTipo, int idLongitud, int idFabricante,
                   String vitrinaReferencia, String vitrinaInventario, int vitrinaAnho,
                   String vitrinaContrato) {
        this.id = id;
        this.idEmpresa = idEmpresa;
        this.idTipo = idTipo;
        this.idLongitud = idLongitud;
        this.idFabricante = idFabricante;
        this.vitrinaReferencia = vitrinaReferencia;
        this.vitrinaInventario = vitrinaInventario;
        this.vitrinaAnho = vitrinaAnho;
        this.vitrinaContrato = vitrinaContrato;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }

    public int getIdLongitud() {
        return idLongitud;
    }

    public void setIdLongitud(int idLongitud) {
        this.idLongitud = idLongitud;
    }

    public int getIdFabricante() {
        return idFabricante;
    }

    public void setIdFabricante(int idFabricante) {
        this.idFabricante = idFabricante;
    }

    public String getVitrinaReferencia() {
        return vitrinaReferencia;
    }

    public void setVitrinaReferencia(String vitrinaReferencia) {
        this.vitrinaReferencia = vitrinaReferencia;
    }

    public String getVitrinaInventario() {
        return vitrinaInventario;
    }

    public void setVitrinaInventario(String vitrinaInventario) {
        this.vitrinaInventario = vitrinaInventario;
    }

    public int getVitrinaAnho() {
        return vitrinaAnho;
    }

    public void setVitrinaAnho(int vitrinaAnho) {
        this.vitrinaAnho = vitrinaAnho;
    }

    public String getVitrinaContrato() {
        return vitrinaContrato;
    }

    public void setVitrinaContrato(String vitrinaContrato) {
        this.vitrinaContrato = vitrinaContrato;
    }

    @Override
    public String toString() {
        return "Vitrina{" +
                "id=" + id +
                ", idEmpresa=" + idEmpresa +
                ", idTipo=" + idTipo +
                ", idLongitud=" + idLongitud +
                ", idFabricante=" + idFabricante +
                ", vitrinaReferencia='" + vitrinaReferencia + '\'' +
                ", vitrinaInventario='" + vitrinaInventario + '\'' +
                ", vitrinaAnho=" + vitrinaAnho +
                ", vitrinaContrato='" + vitrinaContrato + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vitrina)) return false;

        Vitrina vitrina = (Vitrina) o;

        if (getIdEmpresa() != vitrina.getIdEmpresa()) return false;
        if (getIdTipo() != vitrina.getIdTipo()) return false;
        if (getIdLongitud() != vitrina.getIdLongitud()) return false;
        if (getIdFabricante() != vitrina.getIdFabricante()) return false;
        if (getVitrinaAnho() != vitrina.getVitrinaAnho()) return false;
        if (!getVitrinaReferencia().equals(vitrina.getVitrinaReferencia())) return false;
        return getVitrinaInventario().equals(vitrina.getVitrinaInventario());
    }

    @Override
    public int hashCode() {
        int result = getIdEmpresa();
        result = 31 * result + getIdTipo();
        result = 31 * result + getIdLongitud();
        result = 31 * result + getIdFabricante();
        result = 31 * result + getVitrinaReferencia().hashCode();
        result = 31 * result + getVitrinaInventario().hashCode();
        result = 31 * result + getVitrinaAnho();
        return result;
    }
}
