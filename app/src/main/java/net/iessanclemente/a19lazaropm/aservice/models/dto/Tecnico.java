package net.iessanclemente.a19lazaropm.aservice.models.dto;

public class Tecnico {

    private int id;
    private String tecnicoUsuario;
    private String tecnicoClave;
    private String tecnicoNombre;
    private String tecnicoTelef;
    private String tecnicoCorreo;

    public Tecnico() {
    }

    public Tecnico(int id, String tecnicoUsuario, String tecnicoClave, String tecnicoNombre,
                   String tecnicoTelef, String tecnicoCorreo) {
        this.id = id;
        this.tecnicoUsuario = tecnicoUsuario;
        this.tecnicoClave = tecnicoClave;
        this.tecnicoNombre = tecnicoNombre;
        this.tecnicoTelef = tecnicoTelef;
        this.tecnicoCorreo = tecnicoCorreo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTecnicoUsuario() {
        return tecnicoUsuario;
    }

    public void setTecnicoUsuario(String tecnicoUsuario) {
        this.tecnicoUsuario = tecnicoUsuario;
    }

    public String getTecnicoClave() {
        return tecnicoClave;
    }

    public void setTecnicoClave(String tecnicoClave) {
        this.tecnicoClave = tecnicoClave;
    }

    public String getTecnicoNombre() {
        return tecnicoNombre;
    }

    public void setTecnicoNombre(String tecnicoNombre) {
        this.tecnicoNombre = tecnicoNombre;
    }

    public String getTecnicoTelef() {
        return tecnicoTelef;
    }

    public void setTecnicoTelef(String tecnicoTelef) {
        this.tecnicoTelef = tecnicoTelef;
    }

    public String getTecnicoCorreo() {
        return tecnicoCorreo;
    }

    public void setTecnicoCorreo(String tecnicoCorreo) {
        this.tecnicoCorreo = tecnicoCorreo;
    }

    @Override
    public String toString() {
        return "Tecnico{" +
                "id=" + id +
                ", tecnicoUsuario='" + tecnicoUsuario + '\'' +
                ", tecnicoClave='" + tecnicoClave + '\'' +
                ", tecnicoNombre='" + tecnicoNombre + '\'' +
                ", tecnicoTelef='" + tecnicoTelef + '\'' +
                ", tecnicoCorreo='" + tecnicoCorreo + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tecnico)) return false;

        Tecnico tecnico = (Tecnico) o;

        if (!getTecnicoUsuario().equals(tecnico.getTecnicoUsuario())) return false;
        if (!getTecnicoClave().equals(tecnico.getTecnicoClave())) return false;
        return getTecnicoNombre().equals(tecnico.getTecnicoNombre());
    }

    @Override
    public int hashCode() {
        int result = getTecnicoUsuario().hashCode();
        result = 31 * result + getTecnicoClave().hashCode();
        result = 31 * result + getTecnicoNombre().hashCode();
        return result;
    }
}
