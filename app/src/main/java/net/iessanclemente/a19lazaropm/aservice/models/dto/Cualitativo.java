package net.iessanclemente.a19lazaropm.aservice.models.dto;

public class Cualitativo {

    private int id;
    private String cualitativo;

    public Cualitativo() {
    }

    public Cualitativo(int id, String cualitativo) {
        this.id = id;
        this.cualitativo = cualitativo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCualitativo() {
        return cualitativo;
    }

    public void setCualitativo(String cualitativo) {
        this.cualitativo = cualitativo;
    }

    @Override
    public String toString() {
        return "Cualitativo{" +
                "id=" + id +
                ", cualitativo='" + cualitativo + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cualitativo)) return false;

        Cualitativo that = (Cualitativo) o;

        return getCualitativo().equals(that.getCualitativo());
    }

    @Override
    public int hashCode() {
        return getCualitativo().hashCode();
    }
}
