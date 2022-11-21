package net.iessanclemente.a19lazaropm.aservice.database.dto;

public class TipoLongFlow {

    private int id;
    private int idTipoVitrina;
    private int idLongintud;
    private int flowMin;
    private int flowRecom;
    private int flowMax;
    private int pressDrop;

    public TipoLongFlow() {
    }

    public TipoLongFlow(int id, int idTipoVitrina, int idLongintud, int flowMin, int flowRecom,
                        int flowMax, int pressDrop) {
        this.id = id;
        this.idTipoVitrina = idTipoVitrina;
        this.idLongintud = idLongintud;
        this.flowMin = flowMin;
        this.flowRecom = flowRecom;
        this.flowMax = flowMax;
        this.pressDrop = pressDrop;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdTipoVitrina() {
        return idTipoVitrina;
    }

    public void setIdTipoVitrina(int idTipoVitrina) {
        this.idTipoVitrina = idTipoVitrina;
    }

    public int getIdLongintud() {
        return idLongintud;
    }

    public void setIdLongintud(int idLongintud) {
        this.idLongintud = idLongintud;
    }

    public int getFlowMin() {
        return flowMin;
    }

    public void setFlowMin(int flowMin) {
        this.flowMin = flowMin;
    }

    public int getFlowRecom() {
        return flowRecom;
    }

    public void setFlowRecom(int flowRecom) {
        this.flowRecom = flowRecom;
    }

    public int getFlowMax() {
        return flowMax;
    }

    public void setFlowMax(int flowMax) {
        this.flowMax = flowMax;
    }

    public int getPressDrop() {
        return pressDrop;
    }

    public void setPressDrop(int pressDrop) {
        this.pressDrop = pressDrop;
    }

    @Override
    public String toString() {
        return "TipoLongFlow{" +
                "id=" + id +
                ", idTipoVitrina=" + idTipoVitrina +
                ", idLongintud=" + idLongintud +
                ", flowMin=" + flowMin +
                ", flowRecom=" + flowRecom +
                ", flowMax=" + flowMax +
                ", pressDrop=" + pressDrop +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TipoLongFlow)) return false;

        TipoLongFlow that = (TipoLongFlow) o;

        if (getId() != that.getId()) return false;
        if (getIdTipoVitrina() != that.getIdTipoVitrina()) return false;
        if (getIdLongintud() != that.getIdLongintud()) return false;
        if (getFlowMin() != that.getFlowMin()) return false;
        if (getFlowRecom() != that.getFlowRecom()) return false;
        if (getFlowMax() != that.getFlowMax()) return false;
        return getPressDrop() == that.getPressDrop();
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getIdTipoVitrina();
        result = 31 * result + getIdLongintud();
        result = 31 * result + getFlowMin();
        result = 31 * result + getFlowRecom();
        result = 31 * result + getFlowMax();
        result = 31 * result + getPressDrop();
        return result;
    }
}
