package net.iessanclemente.a19lazaropm.aservice.database.dto;

public class Medicion {

    private int id;
    private float valorMedio;
    private float valor1;
    private float valor2;
    private float valor3;
    private float valor4;
    private float valor5;
    private float valor6;
    private float valor7;
    private float valor8;
    private float valor9;

    public Medicion() {
    }

    public Medicion(int id, float valorMedio, float valor1, float valor2, float valor3,
                    float valor4, float valor5, float valor6, float valor7, float valor8,
                    float valor9) {
        this.id = id;
        this.valorMedio = valorMedio;
        this.valor1 = valor1;
        this.valor2 = valor2;
        this.valor3 = valor3;
        this.valor4 = valor4;
        this.valor5 = valor5;
        this.valor6 = valor6;
        this.valor7 = valor7;
        this.valor8 = valor8;
        this.valor9 = valor9;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getValorMedio() {
        return valorMedio;
    }

    public void setValorMedio(float valorMedio) {
        this.valorMedio = valorMedio;
    }

    public float getValor1() {
        return valor1;
    }

    public void setValor1(float valor1) {
        this.valor1 = valor1;
    }

    public float getValor2() {
        return valor2;
    }

    public void setValor2(float valor2) {
        this.valor2 = valor2;
    }

    public float getValor3() {
        return valor3;
    }

    public void setValor3(float valor3) {
        this.valor3 = valor3;
    }

    public float getValor4() {
        return valor4;
    }

    public void setValor4(float valor4) {
        this.valor4 = valor4;
    }

    public float getValor5() {
        return valor5;
    }

    public void setValor5(float valor5) {
        this.valor5 = valor5;
    }

    public float getValor6() {
        return valor6;
    }

    public void setValor6(float valor6) {
        this.valor6 = valor6;
    }

    public float getValor7() {
        return valor7;
    }

    public void setValor7(float valor7) {
        this.valor7 = valor7;
    }

    public float getValor8() {
        return valor8;
    }

    public void setValor8(float valor8) {
        this.valor8 = valor8;
    }

    public float getValor9() {
        return valor9;
    }

    public void setValor9(float valor9) {
        this.valor9 = valor9;
    }

    @Override
    public String toString() {
        return "Medicion{" +
                "id=" + id +
                ", valorMedio=" + valorMedio +
                ", valor1=" + valor1 +
                ", valor2=" + valor2 +
                ", valor3=" + valor3 +
                ", valor4=" + valor4 +
                ", valor5=" + valor5 +
                ", valor6=" + valor6 +
                ", valor7=" + valor7 +
                ", valor8=" + valor8 +
                ", valor9=" + valor9 +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Medicion)) return false;

        Medicion medicion = (Medicion) o;

        if (Float.compare(medicion.getValorMedio(), getValorMedio()) != 0) return false;
        if (Float.compare(medicion.getValor1(), getValor1()) != 0) return false;
        if (Float.compare(medicion.getValor2(), getValor2()) != 0) return false;
        if (Float.compare(medicion.getValor3(), getValor3()) != 0) return false;
        if (Float.compare(medicion.getValor4(), getValor4()) != 0) return false;
        if (Float.compare(medicion.getValor5(), getValor5()) != 0) return false;
        if (Float.compare(medicion.getValor6(), getValor6()) != 0) return false;
        if (Float.compare(medicion.getValor7(), getValor7()) != 0) return false;
        if (Float.compare(medicion.getValor8(), getValor8()) != 0) return false;
        return Float.compare(medicion.getValor9(), getValor9()) == 0;
    }

    @Override
    public int hashCode() {
        int result = (getValorMedio() != +0.0f ? Float.floatToIntBits(getValorMedio()) : 0);
        result = 31 * result + (getValor1() != +0.0f ? Float.floatToIntBits(getValor1()) : 0);
        result = 31 * result + (getValor2() != +0.0f ? Float.floatToIntBits(getValor2()) : 0);
        result = 31 * result + (getValor3() != +0.0f ? Float.floatToIntBits(getValor3()) : 0);
        result = 31 * result + (getValor4() != +0.0f ? Float.floatToIntBits(getValor4()) : 0);
        result = 31 * result + (getValor5() != +0.0f ? Float.floatToIntBits(getValor5()) : 0);
        result = 31 * result + (getValor6() != +0.0f ? Float.floatToIntBits(getValor6()) : 0);
        result = 31 * result + (getValor7() != +0.0f ? Float.floatToIntBits(getValor7()) : 0);
        result = 31 * result + (getValor8() != +0.0f ? Float.floatToIntBits(getValor8()) : 0);
        result = 31 * result + (getValor9() != +0.0f ? Float.floatToIntBits(getValor9()) : 0);
        return result;
    }
}
