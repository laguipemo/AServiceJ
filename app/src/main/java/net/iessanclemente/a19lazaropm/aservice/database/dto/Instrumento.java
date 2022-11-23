package net.iessanclemente.a19lazaropm.aservice.database.dto;

import java.util.Objects;

public class Instrumento {

    private int id;
    private String nombre;
    private String modelo;
    private String marca;

    public Instrumento() {
    }

    public Instrumento(int id, String nombre, String modelo, String marca) {
        this.id = id;
        this.nombre = nombre;
        this.modelo = modelo;
        this.marca = marca;
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

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    @Override
    public String toString() {
        return "Instrumento{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", modelo='" + modelo + '\'' +
                ", marca='" + marca + '\'' +
                '}';
    }

    public String asString() {
        return String.format("%s %s %s", nombre, modelo, marca);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Instrumento that = (Instrumento) o;
        return id == that.id && nombre.equals(that.nombre) && Objects.equals(modelo, that.modelo) && Objects.equals(marca, that.marca);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, modelo, marca);
    }
}
