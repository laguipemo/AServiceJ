package net.iessanclemente.a19lazaropm.aservice.adapters;

public class ElementListEmpresas {
    private String nombreEmpresa;
    private String direccEmpresa;
    private String nombreContacto;

    public ElementListEmpresas() {
    }

    public ElementListEmpresas(String nombreEmpresa, String direccEmpresa, String nombreContacto) {
        this.nombreEmpresa = nombreEmpresa;
        this.direccEmpresa = direccEmpresa;
        this.nombreContacto = nombreContacto;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getDireccEmpresa() {
        return direccEmpresa;
    }

    public void setDireccEmpresa(String direccEmpresa) {
        this.direccEmpresa = direccEmpresa;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }

    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    @Override
    public String toString() {
        return "ElementListEmpresas{" +
                "nombreEmpresa='" + nombreEmpresa + '\'' +
                ", direccEmpresa='" + direccEmpresa + '\'' +
                ", nombreContacto='" + nombreContacto + '\'' +
                '}';
    }
}
