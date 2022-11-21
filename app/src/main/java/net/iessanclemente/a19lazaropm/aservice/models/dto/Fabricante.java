package net.iessanclemente.a19lazaropm.aservice.models.dto;

public class Fabricante {

    private int id;
    private String nombre;

    public Fabricante() {
    }

    public Fabricante(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Fabricante{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Fabricante)) return false;

        Fabricante that = (Fabricante) o;

        return getNombre().equals(that.getNombre());
    }

    @Override
    public int hashCode() {
        return getNombre().hashCode();
    }
}
