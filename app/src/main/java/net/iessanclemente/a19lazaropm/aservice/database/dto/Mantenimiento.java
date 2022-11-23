package net.iessanclemente.a19lazaropm.aservice.database.dto;

public class Mantenimiento {

    private int id;
    private String fecha; // para sqlite manejo como string YYYY-MM-DD
    private int idVitrina;
    private boolean puestaMarcha;
    private int idTecnico;
    private boolean segunDin;
    private boolean segunEn;
    private int funCtrlDigi;
    private int visSistExtr;
    private int protSuperf;
    private int juntas;
    private int fijacion;
    private int funcGuillo;
    private int estadoGuillo;
    private float valFuerzaGuillo;
    private int fuerzaGuillo;
    private int ctrlPresencia;
    private int autoproteccion;
    private int grifosMonored;
    private int idMedicion;
    private int evaluVolExtrac;
    private float valLight;
    private int light;
    private float valSound;
    private int sound;
    private boolean isAcordeNormasReguSi;
    private boolean isAcordeNormasReguNo;
    private boolean isNecesarioRepaSi;
    private boolean isNecesarioRepaNo;
    private String comentario;
    private String instrumentosMedida;


    public Mantenimiento() {
    }

    public Mantenimiento(int id, String fecha, int idVitrina, boolean puestaMarcha, int idTecnico,
                         boolean segunDin, boolean segunEn, int funCtrlDigi, int visSistExtr,
                         int protSuperf, int juntas, int fijacion, int funcGuillo, int estadoGuillo,
                         float valFuerzaGuillo, int fuerzaGuillo, int ctrlPresencia, int autoproteccion,
                         int grifosMonored, int idMedicion, int evaluVolExtrac, float valLight, int light,
                         float valSound, int sound, boolean isAcordeNormasReguSi, boolean isAcordeNormasReguNo,
                         boolean isNecesarioRepaSi, boolean isNecesarioRepaNo, String comentario,
                         String instrumentosMedida
    ) {
        this.id = id;
        this.fecha = fecha;
        this.idVitrina = idVitrina;
        this.puestaMarcha = puestaMarcha;
        this.idTecnico = idTecnico;
        this.segunDin = segunDin;
        this.segunEn = segunEn;
        this.funCtrlDigi = funCtrlDigi;
        this.visSistExtr = visSistExtr;
        this.protSuperf = protSuperf;
        this.juntas = juntas;
        this.fijacion = fijacion;
        this.funcGuillo = funcGuillo;
        this.estadoGuillo = estadoGuillo;
        this.valFuerzaGuillo = valFuerzaGuillo;
        this.fuerzaGuillo = fuerzaGuillo;
        this.ctrlPresencia = ctrlPresencia;
        this.autoproteccion = autoproteccion;
        this.grifosMonored = grifosMonored;
        this.idMedicion = idMedicion;
        this.evaluVolExtrac = evaluVolExtrac;
        this.valLight = valLight;
        this.light = light;
        this.valSound = valSound;
        this.sound = sound;
        this.isAcordeNormasReguSi = isAcordeNormasReguSi;
        this.isAcordeNormasReguNo = isAcordeNormasReguNo;
        this.isNecesarioRepaSi = isNecesarioRepaSi;
        this.isNecesarioRepaNo = isNecesarioRepaNo;
        this.comentario = comentario;
        this.instrumentosMedida = instrumentosMedida;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getIdVitrina() {
        return idVitrina;
    }

    public void setIdVitrina(int idVitrina) {
        this.idVitrina = idVitrina;
    }

    public boolean isPuestaMarcha() {
        return puestaMarcha;
    }

    public void setPuestaMarcha(boolean puestaMarcha) {
        this.puestaMarcha = puestaMarcha;
    }

    public int getIdTecnico() {
        return idTecnico;
    }

    public void setIdTecnico(int idTecnico) {
        this.idTecnico = idTecnico;
    }

    public boolean isSegunDin() {
        return segunDin;
    }

    public void setSegunDin(boolean segunDin) {
        this.segunDin = segunDin;
    }

    public boolean isSegunEn() {
        return segunEn;
    }

    public void setSegunEn(boolean segunEn) {
        this.segunEn = segunEn;
    }

    public int getFunCtrlDigi() {
        return funCtrlDigi;
    }

    public void setFunCtrlDigi(int funCtrlDigi) {
        this.funCtrlDigi = funCtrlDigi;
    }

    public int getVisSistExtr() {
        return visSistExtr;
    }

    public void setVisSistExtr(int visSistExtr) {
        this.visSistExtr = visSistExtr;
    }

    public int getProtSuperf() {
        return protSuperf;
    }

    public void setProtSuperf(int protSuperf) {
        this.protSuperf = protSuperf;
    }

    public int getJuntas() {
        return juntas;
    }

    public void setJuntas(int juntas) {
        this.juntas = juntas;
    }

    public int getFijacion() {
        return fijacion;
    }

    public void setFijacion(int fijacion) {
        this.fijacion = fijacion;
    }

    public int getFuncGuillo() {
        return funcGuillo;
    }

    public void setFuncGuillo(int funcGuillo) {
        this.funcGuillo = funcGuillo;
    }

    public int getEstadoGuillo() {
        return estadoGuillo;
    }

    public void setEstadoGuillo(int estadoGuillo) {
        this.estadoGuillo = estadoGuillo;
    }

    public float getValFuerzaGuillo() {
        return valFuerzaGuillo;
    }

    public void setValFuerzaGuillo(float valFuerzaGuillo) {
        this.valFuerzaGuillo = valFuerzaGuillo;
    }

    public int getFuerzaGuillo() {
        return fuerzaGuillo;
    }

    public void setFuerzaGuillo(int fuerzaGuillo) {
        this.fuerzaGuillo = fuerzaGuillo;
    }

    public int getCtrlPresencia() {
        return ctrlPresencia;
    }

    public void setCtrlPresencia(int ctrlPresencia) {
        this.ctrlPresencia = ctrlPresencia;
    }

    public int getAutoproteccion() {
        return autoproteccion;
    }

    public void setAutoproteccion(int autoproteccion) {
        this.autoproteccion = autoproteccion;
    }

    public int getGrifosMonored() {
        return grifosMonored;
    }

    public void setGrifosMonored(int grifosMonored) {
        this.grifosMonored = grifosMonored;
    }

    public int getIdMedicion() {
        return idMedicion;
    }

    public void setIdMedicion(int idMedicion) {
        this.idMedicion = idMedicion;
    }

    public int getEvaluVolExtrac() {
        return evaluVolExtrac;
    }

    public void setEvaluVolExtracn(int evaluVolExtrac) {
        this.evaluVolExtrac = evaluVolExtrac;
    }

    public float getValLight() { return valLight; }

    public void setValLight(float valLight) { this.valLight = valLight; }

    public int getLight() { return light; }

    public void setLight(int light) { this.light = light; }

    public float getValSound() { return valSound; }

    public void setValSound(float valSound) { this.valSound = valSound; }

    public int getSound() { return sound; }

    public void setSound(int sound) { this.sound = sound; }

    public boolean isAcordeNormasReguSi() {
        return isAcordeNormasReguSi;
    }

    public void setAcordeNormasReguSi(boolean acordeNormasReguSi) {
        isAcordeNormasReguSi = acordeNormasReguSi;
    }

    public boolean isAcordeNormasReguNo() {
        return isAcordeNormasReguNo;
    }

    public void setAcordeNormasReguNo(boolean acordeNormasReguNo) {
        isAcordeNormasReguNo = acordeNormasReguNo;
    }

    public boolean isNecesarioRepaSi() {
        return isNecesarioRepaSi;
    }

    public void setNecesarioRepaSi(boolean necesarioRepaSi) {
        isNecesarioRepaSi = necesarioRepaSi;
    }

    public boolean isNecesarioRepaNo() {
        return isNecesarioRepaNo;
    }

    public void setNecesarioRepaNo(boolean necesarioRepaNo) {
        isNecesarioRepaNo = necesarioRepaNo;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getInstrumentosMedida() { return instrumentosMedida; }

    public void setInstrumentosMedida(String instrumentosMedida) {
        this.instrumentosMedida = instrumentosMedida;
    }

    @Override
    public String toString() {
        return "Mantenimiento{" +
                "id=" + id +
                ", fecha='" + fecha + '\'' +
                ", idVitrina=" + idVitrina +
                ", puestaMarcha=" + puestaMarcha +
                ", idTecnico=" + idTecnico +
                ", segunDin=" + segunDin +
                ", segunEn=" + segunEn +
                ", funCtrlDigi=" + funCtrlDigi +
                ", visSistExtr=" + visSistExtr +
                ", protSuperf=" + protSuperf +
                ", juntas=" + juntas +
                ", fijacion=" + fijacion +
                ", funcGuillo=" + funcGuillo +
                ", estadoGuillo=" + estadoGuillo +
                ", valFuerzaGuillo=" + valFuerzaGuillo +
                ", fuerzaGuillo=" + fuerzaGuillo +
                ", ctrlPresencia=" + ctrlPresencia +
                ", autoproteccion=" + autoproteccion +
                ", grifosMonored=" + grifosMonored +
                ", idMedicion=" + idMedicion +
                ", evaluVolExtrac=" + evaluVolExtrac +
                ", valLight=" + valLight +
                ", light=" + light +
                ", valSound=" + valSound +
                ", sound=" + sound +
                ", isAcordeNormasReguSi=" + isAcordeNormasReguSi +
                ", isAcordeNormasReguNo=" + isAcordeNormasReguNo +
                ", isNecesarioRepaSi=" + isNecesarioRepaSi +
                ", isNecesarioRepaNo=" + isNecesarioRepaNo +
                ", comentario='" + comentario + '\'' +
                ", instrumentosMedida='" + instrumentosMedida + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mantenimiento)) return false;

        Mantenimiento that = (Mantenimiento) o;

        if (getId() != that.getId()) return false;
        if (getIdVitrina() != that.getIdVitrina()) return false;
        if (isPuestaMarcha() != that.isPuestaMarcha()) return false;
        if (getIdTecnico() != that.getIdTecnico()) return false;
        if (isSegunDin() != that.isSegunDin()) return false;
        if (isSegunEn() != that.isSegunEn()) return false;
        if (getFunCtrlDigi() != that.getFunCtrlDigi()) return false;
        if (getVisSistExtr() != that.getVisSistExtr()) return false;
        if (getProtSuperf() != that.getProtSuperf()) return false;
        if (getJuntas() != that.getJuntas()) return false;
        if (getFijacion() != that.getFijacion()) return false;
        if (getFuncGuillo() != that.getFuncGuillo()) return false;
        if (getEstadoGuillo() != that.getEstadoGuillo()) return false;
        if (Float.compare(that.getValFuerzaGuillo(), getValFuerzaGuillo()) != 0) return false;
        if (getFuerzaGuillo() != that.getFuerzaGuillo()) return false;
        if (getCtrlPresencia() != that.getCtrlPresencia()) return false;
        if (getAutoproteccion() != that.getAutoproteccion()) return false;
        if (getGrifosMonored() != that.getGrifosMonored()) return false;
        if (getIdMedicion() != that.getIdMedicion()) return false;
        if (getEvaluVolExtrac() != that.getEvaluVolExtrac()) return false;
        if (getValLight() != that.getValLight()) return false;
        if (getLight() != that.getLight()) return false;
        if (getValSound() != that.getValSound()) return false;
        if (getSound() != that.getSound()) return false;
        if (isAcordeNormasReguSi() != that.isAcordeNormasReguSi()) return false;
        if (isAcordeNormasReguNo() != that.isAcordeNormasReguNo()) return false;
        if (isNecesarioRepaSi() != that.isNecesarioRepaSi()) return false;
        if (isNecesarioRepaNo() != that.isNecesarioRepaNo()) return false;
        if (!getInstrumentosMedida().equalsIgnoreCase(that.getInstrumentosMedida())) return false;
        return getFecha().equals(that.getFecha());
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getFecha().hashCode();
        result = 31 * result + getIdVitrina();
        result = 31 * result + (isPuestaMarcha() ? 1 : 0);
        result = 31 * result + getIdTecnico();
        result = 31 * result + (isSegunDin() ? 1 : 0);
        result = 31 * result + (isSegunEn() ? 1 : 0);
        result = 31 * result + getFunCtrlDigi();
        result = 31 * result + getVisSistExtr();
        result = 31 * result + getProtSuperf();
        result = 31 * result + getJuntas();
        result = 31 * result + getFijacion();
        result = 31 * result + getFuncGuillo();
        result = 31 * result + getEstadoGuillo();
        result = 31 * result + (getValFuerzaGuillo() != +0.0f ? Float.floatToIntBits(getValFuerzaGuillo()) : 0);
        result = 31 * result + getFuerzaGuillo();
        result = 31 * result + getCtrlPresencia();
        result = 31 * result + getAutoproteccion();
        result = 31 * result + getGrifosMonored();
        result = 31 * result + getIdMedicion();
        result = 31 * result + getEvaluVolExtrac();
        result = 31 * result + (getValLight() != +0.0f ? Float.floatToIntBits(getValLight()) : 0);
        result = 31 * result + getLight();
        result = 31 * result + (getValSound() != +0.0f ? Float.floatToIntBits(getValSound()) : 0);
        result = 31 * result + getSound();
        result = 31 * result + (isAcordeNormasReguSi() ? 1 : 0);
        result = 31 * result + (isAcordeNormasReguNo() ? 1 : 0);
        result = 31 * result + (isNecesarioRepaSi() ? 1 : 0);
        result = 31 * result + (isNecesarioRepaNo() ? 1 : 0);
        return result;
    }
}
