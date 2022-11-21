package net.iessanclemente.a19lazaropm.aservice.database.dto;

public class Contacto {

    private int id;
    private String contactoNombre;
    private String contactoTelef;
    private String contactoCorreo;

    public Contacto() {
    }

    public Contacto(int id, String contactoNombre, String contactoTelef, String contactoCorreo) {
        this.id = id;
        this.contactoNombre = contactoNombre;
        this.contactoTelef = contactoTelef;
        this.contactoCorreo = contactoCorreo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContactoNombre() {
        return contactoNombre;
    }

    public void setContactoNombre(String contactoNombre) {
        this.contactoNombre = contactoNombre;
    }

    public String getContactoTelef() {
        return contactoTelef;
    }

    public void setContactoTelef(String contactoTelef) {
        this.contactoTelef = contactoTelef;
    }

    public String getContactoCorreo() {
        return contactoCorreo;
    }

    public void setContactoCorreo(String contactoCorreo) {
        this.contactoCorreo = contactoCorreo;
    }

    @Override
    public String toString() {
        return "Contacto{" +
                "id=" + id +
                ", contactoNombre='" + contactoNombre + '\'' +
                ", contactoTelef='" + contactoTelef + '\'' +
                ", contactoCorreo='" + contactoCorreo + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contacto)) return false;

        Contacto contacto = (Contacto) o;

        if (!getContactoNombre().equals(contacto.getContactoNombre())) return false;
        if (!getContactoTelef().equals(contacto.getContactoTelef())) return false;
        return getContactoCorreo().equals(contacto.getContactoCorreo());
    }

    @Override
    public int hashCode() {
        int result = getContactoNombre().hashCode();
        result = 31 * result + getContactoTelef().hashCode();
        result = 31 * result + getContactoCorreo().hashCode();
        return result;
    }
}
