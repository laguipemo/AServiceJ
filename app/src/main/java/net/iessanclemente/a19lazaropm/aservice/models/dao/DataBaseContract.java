package net.iessanclemente.a19lazaropm.aservice.models.dao;

import android.provider.BaseColumns;

public class DataBaseContract {
    public static final String DATABASE_NAME = "atlas.db";
    public static final int DATABASE_VERSION = 1;

    // Tabla para técnicos de atlas
    public static class TecnicosTable implements BaseColumns {
        public static final String TABLE_NAME = "Tecnicos";

        public static final String COL_USUARIO = "tecnico_usuario";
        public static final String COL_CLAVE = "tecnico_clave";
        public static final String COL_NAME = "tecnico_nombre";
        public static final String COL_TELEF = "tecnico_telef";
        public static final String COL_CORREO = "tecnico_correo";
    }

    // Tabla para empresas clientes
    public static class EmpresasTable implements BaseColumns {
        public static final String TABLE_NAME = "Empresas";

        public static final String COL_NAME = "empresa_nombre";
        public static final String COL_DIRECC = "empresa_direcc";
        public static final String COL_ID_CONTACTO = "id_contacto";
    }

    // Tabla para contactos en empresas clientes
    public static class ContactosTable implements BaseColumns {
        public static final String TABLE_NAME = "Contactos";

        public static final String COL_NAME = "contacto_nombre";
        public static final String COL_TELEF = "contacto_telef";
        public static final String COL_CORREO = "contacto_correo";
    }

    // Tabla para vitrinas
    public static class VitrinasTable implements BaseColumns {
        public static final String TABLE_NAME = "Vitrinas";

        public static final String COL_ID_EMPRESA = "id_empresa";
        public static final String COL_ID_FABRICANTE = "vitrina_fabricante";
        public static final String COL_ID_TIPO = "id_tipo";
        public static final String COL_ID_LONGITUD = "id_longitud";
        public static final String COL_REFERENCIA = "vitrina_referencia";
        public static final String COL_INVENTARIO = "vitrina_inventario";
        public static final String COL_ANHO = "vitrina_anho";
        public static final String COL_CONTRATO = "vitrina_contrato";
    }

    // Tabla para tipos de vitrinas
    public static class TiposVitrinasTable implements BaseColumns {
        public static final String TABLE_NAME = "TiposVitrinas";

        public static final String COL_TIPO = "tipo_vitrina";
    }

    // Tabla para fabricantes de las vitrinas
    public static class FabricantesTable implements BaseColumns {
        public static final String TABLE_NAME = "Fabricantes";

        public static final String COL_NAME = "nombre";
    }

    // Tabla para tipos de longitudes
    public static class LongitudesTable implements BaseColumns {
        public static final String TABLE_NAME = "Longitudes";

        public static final String COL_LONGITUD = "longitud_vitrina";
        public static final String COL_GUILLOTINA = "longitud_guillotina";
    }

    // Tabla auxiliar para los casos de la tipos de vitrinas - longitud con los flujos o caudales
        public static class TiposLongsFlowsTable implements BaseColumns {
        public static final String TABLE_NAME = "TiposLongsFlows";

        public static final String COL_ID_TIPO = "id_tipo";
        public static final String COL_ID_LONGITUD = "id_longitud";
        public static final String COL_FLOW_MIN = "flow_min";
        public static final String COL_FLOW_RECOM= "flow_recom";
        public static final String COL_FLOW_MAX = "flow_max";
        public static final String COL_PRESS_DROP = "press_drop";
    }

    // Tabla par los mantenimientos
    public static class MantenimientosTable implements BaseColumns {
        public static final String TABLE_NAME = "Mantenimientos";

        public static final String COL_FECHA = "fecha";
        public static final String COL_ID_VITRINA = "id_vitrina";
        public static final String COL_PUESTA_MARCHA = "puesta_marcha";
        public static final String COL_ID_TECNICO = "id_tecnico";
        public static final String COL_SEGUN_DIN = "segun_din";
        public static final String COL_SEGUN_EN = "segun_en";
        public static final String COL_FUN_CTRL_DIGI = "fun_ctrl_digi";
        public static final String COL_VIS_SIST_EXTR = "vis_sis_extr";
        public static final String COL_PROT_SUPERF = "prot_superf";
        public static final String COL_JUNTAS = "juntas";
        public static final String COL_FIJACION = "fijacion";
        public static final String COL_FUNC_GUILLO = "func_guillo";
        public static final String COL_ESTADO_GUILLO = "estado_guillo";
        public static final String COL_VAL_FUERZA_GUILLO = "val_fuerza_guillo";
        public static final String COL_FUERZA_GILLO = "fuerza_guillo";
        public static final String COL_CTRL_PRESENCIA = "ctrl_presencia";
        public static final String COL_AUTOPROTECCION = "autoproteccion";
        public static final String COL_GRIFOS_MONORED = "grifos_monored";
        public static final String COL_ID_MEDICION = "id_medicion";
        public static final String COL_EVALU_VOL_EXTRAC = "evalu_vol_extrac";
        public static final String COL_ACORDE_NORMAS_SI = "acorde_normas_si";
        public static final String COL_ACORDE_NORMAS_NO = "acorde_normas_no";
        public static final String COL_NECESARIO_REPA_SI = "necesario_repa_si";
        public static final String COL_NECESARIO_REPA_NO = "necesario_repa_no";
        public static final String COL_COMENTARIO = "comentario";
    }

    // Tabla par los valores cualitativos
    public static class CualitativosTable implements BaseColumns {
        public static final String TABLE_NAME = "Cualitativos";

        public static final String COL_CUALITATIVO = "cualitativo";
    }

    // Tabla par las mediciones volumen
    public static class MedicionesVolTable implements BaseColumns {
        public static final String TABLE_NAME = "MedicionesVol";

        public static final String COL_VALOR_MEDIO = "valor_medio";
        public static final String COL_VALOR1 = "valor1";
        public static final String COL_VALOR2 = "valor2";
        public static final String COL_VALOR3 = "valor3";
        public static final String COL_VALOR4 = "valor4";
        public static final String COL_VALOR5 = "valor5";
        public static final String COL_VALOR6 = "valor6";
        public static final String COL_VALOR7 = "valor7";
        public static final String COL_VALOR8 = "valor8";
        public static final String COL_VALOR9 = "valor9";
    }

}
