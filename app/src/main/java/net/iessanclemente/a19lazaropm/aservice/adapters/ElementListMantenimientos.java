package net.iessanclemente.a19lazaropm.aservice.adapters;

public class ElementListMantenimientos {
    private int id;
    private String empresa;
    private int idVitrina;
    private String fecha;
    private float volumenExtraccion;
    private String comentario;

    public ElementListMantenimientos(int id, String empresa, int idVitrina, String fecha, float volumenExtraccion, String comentario) {
        this.id = id;
        this.empresa = empresa;
        this.idVitrina = idVitrina;
        this.fecha = fecha;
        this.volumenExtraccion = volumenExtraccion;
        this.comentario = comentario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public int getIdVitrina() {
        return idVitrina;
    }

    public void setIdVitrina(int idVitrina) {
        this.idVitrina = idVitrina;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public float getVolumenExtraccion() {
        return volumenExtraccion;
    }

    public void setVolumenExtraccion(float volumenExtraccion) {
        this.volumenExtraccion = volumenExtraccion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    @Override
    public String toString() {
        return "ElementListMantenimientos{" +
                "id=" + id +
                ", empresa='" + empresa + '\'' +
                ", idVitrina=" + idVitrina +
                ", fecha='" + fecha + '\'' +
                ", volumenExtraccion=" + volumenExtraccion +
                ", comentario='" + comentario + '\'' +
                '}';
    }
}