package net.iessanclemente.a19lazaropm.aservice.database.dto;

public class Longitud {

    private int id;
    private int longitudVitrina;
    private float logintudGillotina;

    public Longitud() {
    }

    public Longitud(int id, int longitudVitrina, float logintudGillotina) {
        this.id = id;
        this.longitudVitrina = longitudVitrina;
        this.logintudGillotina = logintudGillotina;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLongitudVitrina() {
        return longitudVitrina;
    }

    public void setLongitudVitrina(int longitudVitrina) {
        this.longitudVitrina = longitudVitrina;
    }

    public float getLogintudGillotina() {
        return logintudGillotina;
    }

    public void setLogintudGillotina(int logintudGillotina) {
        this.logintudGillotina = logintudGillotina;
    }

    @Override
    public String toString() {
        return "Longitud{" +
                "id=" + id +
                ", longitudVitrina=" + longitudVitrina +
                ", logintudGillotina=" + logintudGillotina +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Longitud)) return false;

        Longitud longitud = (Longitud) o;

        if (getLongitudVitrina() != longitud.getLongitudVitrina()) return false;
        return Float.compare(longitud.getLogintudGillotina(), getLogintudGillotina()) == 0;
    }

    @Override
    public int hashCode() {
        int result = getLongitudVitrina();
        result = 31 * result + (getLogintudGillotina() != +0.0f ? Float.floatToIntBits(getLogintudGillotina()) : 0);
        return result;
    }
}
