package net.iessanclemente.a19lazaropm.aservice.models.dto;

public class Empresa {

    private int id;
    private String empresaNombre;
    private String empresaDirecc;
    private int idContacto;

    public Empresa() {
    }

    public Empresa(int id, String empresaNombre, String empresaDirecc, int idContacto) {
        this.id = id;
        this.empresaNombre = empresaNombre;
        this.empresaDirecc = empresaDirecc;
        this.idContacto = idContacto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmpresaNombre() {
        return empresaNombre;
    }

    public void setEmpresaNombre(String empresaNombre) {
        this.empresaNombre = empresaNombre;
    }

    public String getEmpresaDirecc() {
        return empresaDirecc;
    }

    public void setEmpresaDirecc(String empresaDirecc) {
        this.empresaDirecc = empresaDirecc;
    }

    public int getIdContacto() {
        return idContacto;
    }

    public void setIdContacto(int idContacto) {
        this.idContacto = idContacto;
    }

    @Override
    public String toString() {
        return "Empresa{" +
                "id=" + id +
                ", empresaNombre='" + empresaNombre + '\'' +
                ", empresaDirecc='" + empresaDirecc + '\'' +
                ", idContacto=" + idContacto +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Empresa)) return false;

        Empresa empresa = (Empresa) o;

        if (!getEmpresaNombre().equals(empresa.getEmpresaNombre())) return false;
        return getEmpresaDirecc().equals(empresa.getEmpresaDirecc());
    }

    @Override
    public int hashCode() {
        int result = getEmpresaNombre().hashCode();
        result = 31 * result + getEmpresaDirecc().hashCode();
        return result;
    }
}
