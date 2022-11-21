package net.iessanclemente.a19lazaropm.aservice.database.dto;

public class TipoVitrina {

    private int id;
    private String tipoVitrina;

    public TipoVitrina() {
    }

    public TipoVitrina(int id, String tipoVitrina) {
        this.id = id;
        this.tipoVitrina = tipoVitrina;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipoVitrina() {
        return tipoVitrina;
    }

    public void setTipoVitrina(String tipoVitrina) {
        this.tipoVitrina = tipoVitrina;
    }

    @Override
    public String toString() {
        return "TipoVitrina{" +
                "id=" + id +
                ", tipoVitrina='" + tipoVitrina + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TipoVitrina)) return false;

        TipoVitrina that = (TipoVitrina) o;

        return getTipoVitrina().equals(that.getTipoVitrina());
    }

    @Override
    public int hashCode() {
        return getTipoVitrina().hashCode();
    }
}
