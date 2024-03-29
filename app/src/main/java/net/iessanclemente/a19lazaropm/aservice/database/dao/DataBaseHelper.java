package net.iessanclemente.a19lazaropm.aservice.database.dao;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class DataBaseHelper extends SQLiteOpenHelper {

    private Context context;

    public DataBaseHelper(@Nullable Context context) {
        super(context, DataBaseContract.DATABASE_NAME, null, DataBaseContract.DATABASE_VERSION);
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTecnicosTable =
                "CREATE TABLE IF NOT EXISTS " + DataBaseContract.TecnicosTable.TABLE_NAME + " (" +
                        DataBaseContract.TecnicosTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        DataBaseContract.TecnicosTable.COL_USUARIO + " TEXT NOT NULL," +
                        DataBaseContract.TecnicosTable.COL_CLAVE + " TEXT NOT NULL," +
                        DataBaseContract.TecnicosTable.COL_NAME + " TEXT NOT NULL," +
                        DataBaseContract.TecnicosTable.COL_TELEF + " TEXT NOT NULL," +
                        DataBaseContract.TecnicosTable.COL_CORREO + " TEXT NOT NULL)";
        db.execSQL(createTecnicosTable);

        String createEmpresasTable =
                "CREATE TABLE IF NOT EXISTS " + DataBaseContract.EmpresasTable.TABLE_NAME + " (" +
                        DataBaseContract.EmpresasTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        DataBaseContract.EmpresasTable.COL_NAME + " TEXT NOT NULL UNIQUE ON CONFLICT ROLLBACK," +
                        DataBaseContract.EmpresasTable.COL_DIRECC + " TEXT NOT NULL," +
                        DataBaseContract.EmpresasTable.COL_ID_CONTACTO + " INTEGER NOT NULL REFERENCES " +
                        DataBaseContract.ContactosTable.TABLE_NAME + "(" +
                        DataBaseContract.ContactosTable._ID + "))";
        db.execSQL(createEmpresasTable);

        String createContactosTable =
                "CREATE TABLE IF NOT EXISTS " + DataBaseContract.ContactosTable.TABLE_NAME + " (" +
                        DataBaseContract.ContactosTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        DataBaseContract.ContactosTable.COL_NAME + " TEXT NOT NULL," +
                        DataBaseContract.ContactosTable.COL_TELEF + " TEXT NOT NULL," +
                        DataBaseContract.ContactosTable.COL_CORREO + " TEXT NOT NULL)";
        db.execSQL(createContactosTable);

        String createVitrinasTable =
                "CREATE TABLE IF NOT EXISTS " + DataBaseContract.VitrinasTable.TABLE_NAME + " (" +
                        DataBaseContract.VitrinasTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        DataBaseContract.VitrinasTable.COL_ID_EMPRESA + " INTEGER REFERENCES " +
                        DataBaseContract.EmpresasTable.TABLE_NAME + "(" +
                        DataBaseContract.EmpresasTable._ID + "), " +
                        DataBaseContract.VitrinasTable.COL_ID_TIPO + " INTEGER REFERENCES " +
                        DataBaseContract.TiposVitrinasTable.TABLE_NAME + "(" +
                        DataBaseContract.TiposVitrinasTable._ID + "), " +
                        DataBaseContract.VitrinasTable.COL_ID_LONGITUD + " INTEGER REFERENCES " +
                        DataBaseContract.LongitudesTable.TABLE_NAME + "(" +
                        DataBaseContract.LongitudesTable._ID + "), " +
                        DataBaseContract.VitrinasTable.COL_ID_FABRICANTE + " INTEGER REFERENCES " +
                        DataBaseContract.FabricantesTable.TABLE_NAME + "(" +
                        DataBaseContract.FabricantesTable._ID + "), " +
                        DataBaseContract.VitrinasTable.COL_REFERENCIA + " TEXT," +
                        DataBaseContract.VitrinasTable.COL_INVENTARIO + " TEXT," +
                        DataBaseContract.VitrinasTable.COL_ANHO + " INTEGER," +
                        DataBaseContract.VitrinasTable.COL_CONTRATO + " TEXT)";
        db.execSQL(createVitrinasTable);

        String createTiposVitrinasTable =
                "CREATE TABLE IF NOT EXISTS " + DataBaseContract.TiposVitrinasTable.TABLE_NAME + " (" +
                        DataBaseContract.TiposVitrinasTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        DataBaseContract.TiposVitrinasTable.COL_TIPO + " TEXT NOT NULL UNIQUE ON CONFLICT ROLLBACK)";
        db.execSQL(createTiposVitrinasTable);

        String createFabricantesTable =
                "CREATE TABLE IF NOT EXISTS " + DataBaseContract.FabricantesTable.TABLE_NAME + " (" +
                        DataBaseContract.FabricantesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        DataBaseContract.FabricantesTable.COL_NAME + " TEXT NOT NULL UNIQUE ON CONFLICT ROLLBACK)";
        db.execSQL(createFabricantesTable);

        String createLongitudesTable =
                "CREATE TABLE IF NOT EXISTS " + DataBaseContract.LongitudesTable.TABLE_NAME + " (" +
                        DataBaseContract.LongitudesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        DataBaseContract.LongitudesTable.COL_LONGITUD + " INTEGER NOT NULL UNIQUE ON CONFLICT ROLLBACK," +
                        DataBaseContract.LongitudesTable.COL_GUILLOTINA + " DECIMAL(2,1) NOT NULL)";
        db.execSQL(createLongitudesTable);


        String createTiposLongsFlowsTable =
                "CREATE TABLE IF NOT EXISTS " + DataBaseContract.TiposLongsFlowsTable.TABLE_NAME + " (" +
                        DataBaseContract.TiposLongsFlowsTable.COL_ID_TIPO + " INTEGER REFERENCES " +
                        DataBaseContract.TiposVitrinasTable.TABLE_NAME + "(" +
                        DataBaseContract.TiposVitrinasTable._ID + "), " +
                        DataBaseContract.TiposLongsFlowsTable.COL_ID_LONGITUD + " INTEGER REFERENCES " +
                        DataBaseContract.LongitudesTable.TABLE_NAME + "(" +
                        DataBaseContract.LongitudesTable._ID + "), " +
                        DataBaseContract.TiposLongsFlowsTable.COL_FLOW_MIN + " INTEGER NOT NULL, " +
                        DataBaseContract.TiposLongsFlowsTable.COL_FLOW_RECOM + " INTEGER NOT NULL, " +
                        DataBaseContract.TiposLongsFlowsTable.COL_FLOW_MAX + " INTEGER NOT NULL, " +
                        DataBaseContract.TiposLongsFlowsTable.COL_PRESS_DROP + " INTEGER NOT NULL, " +
                        "PRIMARY KEY (" + DataBaseContract.TiposLongsFlowsTable.COL_ID_TIPO + ", " +
                        DataBaseContract.TiposLongsFlowsTable.COL_ID_LONGITUD + "))";
        db.execSQL(createTiposLongsFlowsTable);

        String createMantenimientosTable =
                "CREATE TABLE IF NOT EXISTS " + DataBaseContract.MantenimientosTable.TABLE_NAME + " (" +
                        DataBaseContract.MantenimientosTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        DataBaseContract.MantenimientosTable.COL_FECHA + " TEXT NOT NULL," +
                        DataBaseContract.MantenimientosTable.COL_ID_VITRINA + " INTEGER REFERENCES " +
                        DataBaseContract.VitrinasTable.TABLE_NAME + " (" +
                        DataBaseContract.VitrinasTable._ID + "), " +
                        DataBaseContract.MantenimientosTable.COL_PUESTA_MARCHA + " BOOLEAN," +
                        DataBaseContract.MantenimientosTable.COL_ID_TECNICO + " INTEGER REFERENCES " +
                        DataBaseContract.TecnicosTable.TABLE_NAME + " (" +
                        DataBaseContract.TecnicosTable._ID + "), " +
                        DataBaseContract.MantenimientosTable.COL_SEGUN_DIN + " BOOLEAN," +
                        DataBaseContract.MantenimientosTable.COL_SEGUN_EN + " BOOLEAN," +
                        DataBaseContract.MantenimientosTable.COL_FUN_CTRL_DIGI + " INTEGER REFERENCES " +
                        DataBaseContract.CualitativosTable.TABLE_NAME + " (" +
                        DataBaseContract.CualitativosTable._ID + "), " +
                        DataBaseContract.MantenimientosTable.COL_VIS_SIST_EXTR + " INTEGER REFERENCES " +
                        DataBaseContract.CualitativosTable.TABLE_NAME + " (" +
                        DataBaseContract.CualitativosTable._ID + "), " +
                        DataBaseContract.MantenimientosTable.COL_PROT_SUPERF + " INTEGER REFERENCES " +
                        DataBaseContract.CualitativosTable.TABLE_NAME + " (" +
                        DataBaseContract.CualitativosTable._ID + "), " +
                        DataBaseContract.MantenimientosTable.COL_JUNTAS + " INTEGER REFERENCES " +
                        DataBaseContract.CualitativosTable.TABLE_NAME + " (" +
                        DataBaseContract.CualitativosTable._ID + "), " +
                        DataBaseContract.MantenimientosTable.COL_FIJACION + " INTEGER REFERENCES " +
                        DataBaseContract.CualitativosTable.TABLE_NAME + " (" +
                        DataBaseContract.CualitativosTable._ID + "), " +
                        DataBaseContract.MantenimientosTable.COL_FUNC_GUILLO + " INTEGER REFERENCES " +
                        DataBaseContract.CualitativosTable.TABLE_NAME + " (" +
                        DataBaseContract.CualitativosTable._ID + "), " +
                        DataBaseContract.MantenimientosTable.COL_ESTADO_GUILLO + " INTEGER REFERENCES " +
                        DataBaseContract.CualitativosTable.TABLE_NAME + " (" +
                        DataBaseContract.CualitativosTable._ID + "), " +
                        DataBaseContract.MantenimientosTable.COL_VAL_FUERZA_GUILLO + " DECIMAL(5,2)," +
                        DataBaseContract.MantenimientosTable.COL_FUERZA_GILLO + " INTEGER REFERENCES " +
                        DataBaseContract.CualitativosTable.TABLE_NAME + " (" +
                        DataBaseContract.CualitativosTable._ID + "), " +
                        DataBaseContract.MantenimientosTable.COL_CTRL_PRESENCIA + " INTEGER REFERENCES " +
                        DataBaseContract.CualitativosTable.TABLE_NAME + " (" +
                        DataBaseContract.CualitativosTable._ID + "), " +
                        DataBaseContract.MantenimientosTable.COL_AUTOPROTECCION + " INTEGER REFERENCES " +
                        DataBaseContract.CualitativosTable.TABLE_NAME + " (" +
                        DataBaseContract.CualitativosTable._ID + "), " +
                        DataBaseContract.MantenimientosTable.COL_GRIFOS_MONORED + " INTEGER REFERENCES " +
                        DataBaseContract.CualitativosTable.TABLE_NAME + " (" +
                        DataBaseContract.CualitativosTable._ID + "), " +
                        DataBaseContract.MantenimientosTable.COL_ID_MEDICION + " INTEGER REFERENCES " +
                        DataBaseContract.MedicionesVolTable.TABLE_NAME + " (" +
                        DataBaseContract.MedicionesVolTable._ID + "), " +
                        DataBaseContract.MantenimientosTable.COL_EVALU_VOL_EXTRAC + " INTEGER REFERENCES " +
                        DataBaseContract.CualitativosTable.TABLE_NAME + " (" +
                        DataBaseContract.CualitativosTable._ID + "), " +
                        DataBaseContract.MantenimientosTable.COL_VAL_LIGHT + " DECIMAL(5,2)," +
                        DataBaseContract.MantenimientosTable.COL_LIGHT + " INTEGER REFERENCES " +
                        DataBaseContract.CualitativosTable.TABLE_NAME + " (" +
                        DataBaseContract.CualitativosTable._ID + "), " +
                        DataBaseContract.MantenimientosTable.COL_VAL_SOUND + " DECIMAL(5,2)," +
                        DataBaseContract.MantenimientosTable.COL_SOUND + " INTEGER REFERENCES " +
                        DataBaseContract.CualitativosTable.TABLE_NAME + " (" +
                        DataBaseContract.CualitativosTable._ID + "), " +
                        DataBaseContract.MantenimientosTable.COL_ACORDE_NORMAS_SI + " BOOLEAN," +
                        DataBaseContract.MantenimientosTable.COL_ACORDE_NORMAS_NO + " BOOLEAN," +
                        DataBaseContract.MantenimientosTable.COL_NECESARIO_REPA_SI + " BOOLEAN," +
                        DataBaseContract.MantenimientosTable.COL_NECESARIO_REPA_NO + " BOOLEAN," +
                        DataBaseContract.MantenimientosTable.COL_COMENTARIO + " TEXT," +
                        DataBaseContract.MantenimientosTable.COL_INSTRUMENTOS + " TEXT)";
        db.execSQL(createMantenimientosTable);

        String createCualitativosTable =
                "CREATE TABLE IF NOT EXISTS " + DataBaseContract.CualitativosTable.TABLE_NAME + " (" +
                        DataBaseContract.CualitativosTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        DataBaseContract.CualitativosTable.COL_CUALITATIVO + " TEXT NOT NULL UNIQUE ON CONFLICT ROLLBACK)";
        db.execSQL(createCualitativosTable);

        String createMedicionesVolTable =
                "CREATE TABLE IF NOT EXISTS " + DataBaseContract.MedicionesVolTable.TABLE_NAME + " (" +
                        DataBaseContract.MedicionesVolTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        DataBaseContract.MedicionesVolTable.COL_VALOR_MEDIO + " DECIMAL(3,2)," +
                        DataBaseContract.MedicionesVolTable.COL_VALOR1 + " DECIMAL(3,2)," +
                        DataBaseContract.MedicionesVolTable.COL_VALOR2 + " DECIMAL(3,2)," +
                        DataBaseContract.MedicionesVolTable.COL_VALOR3 + " DECIMAL(3,2)," +
                        DataBaseContract.MedicionesVolTable.COL_VALOR4 + " DECIMAL(3,2)," +
                        DataBaseContract.MedicionesVolTable.COL_VALOR5 + " DECIMAL(3,2)," +
                        DataBaseContract.MedicionesVolTable.COL_VALOR6 + " DECIMAL(3,2)," +
                        DataBaseContract.MedicionesVolTable.COL_VALOR7 + " DECIMAL(3,2)," +
                        DataBaseContract.MedicionesVolTable.COL_VALOR8 + " DECIMAL(3,2)," +
                        DataBaseContract.MedicionesVolTable.COL_VALOR9 + " DECIMAL(3,2))";
        db.execSQL(createMedicionesVolTable);

        String createInstrumentosTable =
                "CREATE TABLE IF NOT EXISTS " + DataBaseContract.InstrumentosTable.TABLE_NAME + " (" +
                        DataBaseContract.InstrumentosTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        DataBaseContract.InstrumentosTable.COL_NOMBRE + " TEXT," +
                        DataBaseContract.InstrumentosTable.COL_MODELO + " TEXT," +
                        DataBaseContract.InstrumentosTable.COL_MARCA + " TEXT)";
        db.execSQL(createInstrumentosTable);

        //Insertar registros en las diferentes tablas

        //Contactos
        String insertContacto = "INSERT INTO Contactos " +
                "(_id, contacto_nombre, contacto_telef, contacto_correo) " +
                "VALUES (1, 'Montserrat Novo Morandeira', '34981777571', 'montserrat.novo@vertexbioenergy.com');";
        db.execSQL(insertContacto);
        insertContacto = "INSERT INTO Contactos " +
                "(_id, contacto_nombre, contacto_telef, contacto_correo) " +
                "VALUES (2, 'Julio Lorenzana', '-', 'julio.lorenzana@bayer.com');";
        db.execSQL(insertContacto);
        insertContacto = "INSERT INTO Contactos " +
                "(_id, contacto_nombre, contacto_telef, contacto_correo) " +
                "VALUES (3, 'Cristian García', '985268495', '-');";
        db.execSQL(insertContacto);

        //Cualitativos
        String insertCualitativo = "INSERT INTO Cualitativos " +
                "(_id, cualitativo) VALUES (1, 'N.P.');";
        db.execSQL(insertCualitativo);
        insertCualitativo = "INSERT INTO Cualitativos " +
                "(_id, cualitativo) VALUES (2, 'O.K.');";
        db.execSQL(insertCualitativo);
        insertCualitativo = "INSERT INTO Cualitativos " +
                "(_id, cualitativo) VALUES (3,'F.L.');";
        db.execSQL(insertCualitativo);
        insertCualitativo = "INSERT INTO Cualitativos " +
                "(_id, cualitativo) VALUES (4, 'R.R.');";
        db.execSQL(insertCualitativo);

        //Empresas
        String insertEmpresa = "INSERT INTO Empresas " +
                "(_id, empresa_nombre, empresa_direcc, id_contacto)" +
                "VALUES (1, 'Bioetanol Galicia', 'Poligono Industrial Teixeiro, Crta. Nacional 634, Km. 664,3', 1);";
        db.execSQL(insertEmpresa);
        insertEmpresa = "INSERT INTO Empresas " +
                "(_id, empresa_nombre, empresa_direcc, id_contacto)" +
                "VALUES (2, 'Bayer Hispania S.L.', 'Calle Sabino Alonso Fueyo, 77, 33930 Langreo, Asturias', 2);";
        db.execSQL(insertEmpresa);
        insertEmpresa = "INSERT INTO Empresas " +
                "(_id, empresa_nombre, empresa_direcc, id_contacto) " +
                "VALUES (3, 'Crown Food España S.A.U.', 'Polígono Industrial Silvota, 33424 Llanera', 3);";
        db.execSQL(insertEmpresa);

        //Fabricantes
        String insertFabricante = "INSERT INTO Fabricantes (_id, nombre) VALUES (1, 'Kotterman');";
        db.execSQL(insertFabricante);
        insertFabricante = "INSERT INTO Fabricantes (_id, nombre) VALUES (2, 'Purever');";
        db.execSQL(insertFabricante);
        insertFabricante = "INSERT INTO Fabricantes (_id, nombre) VALUES (3, 'Burdinola');";
        db.execSQL(insertFabricante);
        insertFabricante = "INSERT INTO Fabricantes (_id, nombre) VALUES (4, 'Flores Valles');";
        db.execSQL(insertFabricante);
        insertFabricante = "INSERT INTO Fabricantes ( _id, nombre) VALUES (5, 'Wladner');";
        db.execSQL(insertFabricante);
        insertFabricante = "INSERT INTO Fabricantes (_id, nombre) VALUES (6, 'HIB');";
        db.execSQL(insertFabricante);
        insertFabricante = "INSERT INTO Fabricantes (_id, nombre) VALUES (7, 'Labolan');";
        db.execSQL(insertFabricante);
        insertFabricante = "INSERT INTO Fabricantes (_id, nombre) VALUES (8, 'Sumanlab');";
        db.execSQL(insertFabricante);

        //TiposLongsFlows
        String insertTipoLongFlow = "INSERT INTO TiposLongsFlows (\n" +
                "                id_tipo,\n" +
                "                id_longitud,\n" +
                "                flow_min,\n" +
                "                flow_recom,\n" +
                "                flow_max,\n" +
                "                press_drop\n" +
                "        )\n" +
                "        VALUES (\n" +
                "                1,\n" +
                "                1,\n" +
                "                450,\n" +
                "                480,\n" +
                "                1500,\n" +
                "                27\n" +
                "        );";
        db.execSQL(insertTipoLongFlow);
        insertTipoLongFlow = "INSERT INTO TiposLongsFlows (\n" +
                "                id_tipo,\n" +
                "                id_longitud,\n" +
                "                flow_min,\n" +
                "                flow_recom,\n" +
                "                flow_max,\n" +
                "                press_drop\n" +
                "        )\n" +
                "        VALUES (\n" +
                "                1,\n" +
                "                2,\n" +
                "                480,\n" +
                "                600,\n" +
                "                1500,\n" +
                "                35\n" +
                "        );";
        db.execSQL(insertTipoLongFlow);
        insertTipoLongFlow = "INSERT INTO TiposLongsFlows (\n" +
                "                id_tipo,\n" +
                "                id_longitud,\n" +
                "                flow_min,\n" +
                "                flow_recom,\n" +
                "                flow_max,\n" +
                "                press_drop\n" +
                "        )\n" +
                "        VALUES (\n" +
                "                1,\n" +
                "                3,\n" +
                "                680,\n" +
                "                720,\n" +
                "                1500,\n" +
                "                43\n" +
                "        );";
        db.execSQL(insertTipoLongFlow);
        insertTipoLongFlow = "INSERT INTO TiposLongsFlows (\n" +
                "                id_tipo,\n" +
                "                id_longitud,\n" +
                "                flow_min,\n" +
                "                flow_recom,\n" +
                "                flow_max,\n" +
                "                press_drop\n" +
                "        )\n" +
                "        VALUES (\n" +
                "                1,\n" +
                "                4,\n" +
                "                800,\n" +
                "                840,\n" +
                "                1500,\n" +
                "                61\n" +
                "        );";
        db.execSQL(insertTipoLongFlow);
        insertTipoLongFlow = "INSERT INTO TiposLongsFlows (\n" +
                "                id_tipo,\n" +
                "                id_longitud,\n" +
                "                flow_min,\n" +
                "                flow_recom,\n" +
                "                flow_max,\n" +
                "                press_drop\n" +
                "        )\n" +
                "        VALUES (\n" +
                "                1,\n" +
                "                5,\n" +
                "                800,\n" +
                "                840,\n" +
                "                1500,\n" +
                "                61\n" +
                "        );";
        db.execSQL(insertTipoLongFlow);
        insertTipoLongFlow = "INSERT INTO TiposLongsFlows (\n" +
                "                id_tipo,\n" +
                "                id_longitud,\n" +
                "                flow_min,\n" +
                "                flow_recom,\n" +
                "                flow_max,\n" +
                "                press_drop\n" +
                "        )\n" +
                "        VALUES (\n" +
                "                2,\n" +
                "                1,\n" +
                "                660,\n" +
                "                720,\n" +
                "                1500,\n" +
                "                50\n" +
                "        );";
        db.execSQL(insertTipoLongFlow);
        insertTipoLongFlow = "INSERT INTO TiposLongsFlows (\n" +
                "                id_tipo,\n" +
                "                id_longitud,\n" +
                "                flow_min,\n" +
                "                flow_recom,\n" +
                "                flow_max,\n" +
                "                press_drop\n" +
                "        )\n" +
                "        VALUES (\n" +
                "                2,\n" +
                "                2,\n" +
                "                830,\n" +
                "                900,\n" +
                "                1500,\n" +
                "                60\n" +
                "        );";
        db.execSQL(insertTipoLongFlow);
        insertTipoLongFlow = "INSERT INTO TiposLongsFlows (\n" +
                "                id_tipo,\n" +
                "                id_longitud,\n" +
                "                flow_min,\n" +
                "                flow_recom,\n" +
                "                flow_max,\n" +
                "                press_drop\n" +
                "        )\n" +
                "        VALUES (\n" +
                "                2,\n" +
                "                3,\n" +
                "                990,\n" +
                "                1080,\n" +
                "                1500,\n" +
                "                80\n" +
                "        );";
        db.execSQL(insertTipoLongFlow);
        insertTipoLongFlow = "INSERT INTO TiposLongsFlows (\n" +
                "                id_tipo,\n" +
                "                id_longitud,\n" +
                "                flow_min,\n" +
                "                flow_recom,\n" +
                "                flow_max,\n" +
                "                press_drop\n" +
                "        )\n" +
                "        VALUES (\n" +
                "                2,\n" +
                "                4,\n" +
                "                1790,\n" +
                "                1900,\n" +
                "                2000,\n" +
                "                300\n" +
                "        );";
        db.execSQL(insertTipoLongFlow);
        insertTipoLongFlow = "INSERT INTO TiposLongsFlows (\n" +
                "                id_tipo,\n" +
                "                id_longitud,\n" +
                "                flow_min,\n" +
                "                flow_recom,\n" +
                "                flow_max,\n" +
                "                press_drop\n" +
                "        )\n" +
                "        VALUES (\n" +
                "                2,\n" +
                "                5,\n" +
                "                1790,\n" +
                "                1900,\n" +
                "                2000,\n" +
                "                300\n" +
                "        );";
        db.execSQL(insertTipoLongFlow);
        insertTipoLongFlow = "INSERT INTO TiposLongsFlows (\n" +
                "                id_tipo,\n" +
                "                id_longitud,\n" +
                "                flow_min,\n" +
                "                flow_recom,\n" +
                "                flow_max,\n" +
                "                press_drop\n" +
                "        )\n" +
                "        VALUES (\n" +
                "                3,\n" +
                "                1,\n" +
                "                660,\n" +
                "                750,\n" +
                "                2000,\n" +
                "                40\n" +
                "        );";
        db.execSQL(insertTipoLongFlow);
        insertTipoLongFlow = "INSERT INTO TiposLongsFlows (\n" +
                "                id_tipo,\n" +
                "                id_longitud,\n" +
                "                flow_min,\n" +
                "                flow_recom,\n" +
                "                flow_max,\n" +
                "                press_drop\n" +
                "        )\n" +
                "        VALUES (\n" +
                "                3,\n" +
                "                2,\n" +
                "                825,\n" +
                "                940,\n" +
                "                2000,\n" +
                "                50\n" +
                "        );";
        db.execSQL(insertTipoLongFlow);
        insertTipoLongFlow = "INSERT INTO TiposLongsFlows (\n" +
                "                id_tipo,\n" +
                "                id_longitud,\n" +
                "                flow_min,\n" +
                "                flow_recom,\n" +
                "                flow_max,\n" +
                "                press_drop\n" +
                "        )\n" +
                "        VALUES (\n" +
                "                3,\n" +
                "                3,\n" +
                "                990,\n" +
                "                1120,\n" +
                "                2000,\n" +
                "                65\n" +
                "        );";
        db.execSQL(insertTipoLongFlow);
        insertTipoLongFlow = "INSERT INTO TiposLongsFlows (\n" +
                "                id_tipo,\n" +
                "                id_longitud,\n" +
                "                flow_min,\n" +
                "                flow_recom,\n" +
                "                flow_max,\n" +
                "                press_drop\n" +
                "        )\n" +
                "        VALUES (\n" +
                "                3,\n" +
                "                4,\n" +
                "                1155,\n" +
                "                1310,\n" +
                "                2000,\n" +
                "                95\n" +
                "        );";
        db.execSQL(insertTipoLongFlow);
        insertTipoLongFlow = "INSERT INTO TiposLongsFlows (\n" +
                "                id_tipo,\n" +
                "                id_longitud,\n" +
                "                flow_min,\n" +
                "                flow_recom,\n" +
                "                flow_max,\n" +
                "                press_drop\n" +
                "        )\n" +
                "        VALUES (\n" +
                "                3,\n" +
                "                5,\n" +
                "                1155,\n" +
                "                1310,\n" +
                "                2000,\n" +
                "                95\n" +
                "        );";
        db.execSQL(insertTipoLongFlow);

        //Longitudes
        String insertLongitud = "INSERT INTO Longitudes " +
                "(_id, longitud_vitrina, longitud_guillotina) VALUES (1, 1200, 0.9);";
        db.execSQL(insertLongitud);
        insertLongitud = "INSERT INTO Longitudes " +
                "(_id, longitud_vitrina, longitud_guillotina) VALUES (2, 1500, 1.2);";
        db.execSQL(insertLongitud);
        insertLongitud = "INSERT INTO Longitudes " +
                "(_id, longitud_vitrina, longitud_guillotina) VALUES (3, 1800, 1.5);";
        db.execSQL(insertLongitud);
        insertLongitud = "INSERT INTO Longitudes " +
                "(_id, longitud_vitrina, longitud_guillotina) VALUES (4, 2000, 1.7);";
        db.execSQL(insertLongitud);
        insertLongitud = "INSERT INTO Longitudes " +
                "(_id, longitud_vitrina, longitud_guillotina) VALUES (5, 2100, 1.8);";
        db.execSQL(insertLongitud);

        //Mantenimientos
        String insertMantenimiento = "INSERT INTO Mantenimientos (\n" +
                "                _id,\n" +
                "                fecha,\n" +
                "                id_vitrina,\n" +
                "                puesta_marcha,\n" +
                "                id_tecnico,\n" +
                "                segun_din,\n" +
                "                segun_en,\n" +
                "                fun_ctrl_digi,\n" +
                "                vis_sis_extr,\n" +
                "                prot_superf,\n" +
                "                juntas,\n" +
                "                fijacion,\n" +
                "                func_guillo,\n" +
                "                estado_guillo,\n" +
                "                val_fuerza_guillo,\n" +
                "                fuerza_guillo,\n" +
                "                ctrl_presencia,\n" +
                "                autoproteccion,\n" +
                "                grifos_monored,\n" +
                "                id_medicion,\n" +
                "                evalu_vol_extrac,\n" +
                "                val_light,\n" +
                "                light,\n" +
                "                val_sound,\n" +
                "                sound,\n" +
                "                acorde_normas_si,\n" +
                "                acorde_normas_no,\n" +
                "                necesario_repa_si,\n" +
                "                necesario_repa_no,\n" +
                "                comentario,\n" +
                "                instrumentos\n" +
                "        )\n" +
                "        VALUES (\n" +
                "                1,\n" +
                "                '0000-00-00',\n" +
                "                1,\n" +
                "                0,\n" +
                "                2,\n" +
                "                0,\n" +
                "                1,\n" +
                "                2,\n" +
                "                2,\n" +
                "                2,\n" +
                "                2,\n" +
                "                2,\n" +
                "                2,\n" +
                "                2,\n" +
                "                0.0,\n" +
                "                1,\n" +
                "                2,\n" +
                "                2,\n" +
                "                2,\n" +
                "                1,\n" +
                "                2,\n" +
                "                835.0,\n" +
                "                2,\n" +
                "                57.2,\n" +
                "                2,\n" +
                "                1,\n" +
                "                0,\n" +
                "                0,\n" +
                "                1,\n" +
                "                'Solo es una prueba de un comentario para tener que mostrar. Todo fue ok.',\n" +
                "                ''\n" +
                "        );";
        db.execSQL(insertMantenimiento);
        insertMantenimiento = "INSERT INTO Mantenimientos (\n" +
                "                _id,\n" +
                "                fecha,\n" +
                "                id_vitrina,\n" +
                "                puesta_marcha,\n" +
                "                id_tecnico,\n" +
                "                segun_din,\n" +
                "                segun_en,\n" +
                "                fun_ctrl_digi,\n" +
                "                vis_sis_extr,\n" +
                "                prot_superf,\n" +
                "                juntas,\n" +
                "                fijacion,\n" +
                "                func_guillo,\n" +
                "                estado_guillo,\n" +
                "                val_fuerza_guillo,\n" +
                "                fuerza_guillo,\n" +
                "                ctrl_presencia,\n" +
                "                autoproteccion,\n" +
                "                grifos_monored,\n" +
                "                id_medicion,\n" +
                "                evalu_vol_extrac,\n" +
                "                val_light,\n" +
                "                light,\n" +
                "                val_sound,\n" +
                "                sound,\n" +
                "                acorde_normas_si,\n" +
                "                acorde_normas_no,\n" +
                "                necesario_repa_si,\n" +
                "                necesario_repa_no,\n" +
                "                comentario,\n" +
                "                instrumentos\n" +
                "        )\n" +
                "        VALUES (\n" +
                "                2,\n" +
                "                '2019-08-13',\n" +
                "                2,\n" +
                "                0,\n" +
                "                2,\n" +
                "                0,\n" +
                "                1,\n" +
                "                2,\n" +
                "                2,\n" +
                "                2,\n" +
                "                2,\n" +
                "                2,\n" +
                "                2,\n" +
                "                2,\n" +
                "                46.34,\n" +
                "                2,\n" +
                "                2,\n" +
                "                2,\n" +
                "                2,\n" +
                "                2,\n" +
                "                2,\n" +
                "                740.0,\n" +
                "                2,\n" +
                "                55.4,\n" +
                "                2,\n" +
                "                1,\n" +
                "                0,\n" +
                "                0,\n" +
                "                1,\n" +
                "                'Otro comentario de relleno...',\n" +
                "                ''\n" +
                "        );";
        db.execSQL(insertMantenimiento);
        insertMantenimiento = "INSERT INTO Mantenimientos (\n" +
                "                _id,\n" +
                "                fecha,\n" +
                "                id_vitrina,\n" +
                "                puesta_marcha,\n" +
                "                id_tecnico,\n" +
                "                segun_din,\n" +
                "                segun_en,\n" +
                "                fun_ctrl_digi,\n" +
                "                vis_sis_extr,\n" +
                "                prot_superf,\n" +
                "                juntas,\n" +
                "                fijacion,\n" +
                "                func_guillo,\n" +
                "                estado_guillo,\n" +
                "                val_fuerza_guillo,\n" +
                "                fuerza_guillo,\n" +
                "                ctrl_presencia,\n" +
                "                autoproteccion,\n" +
                "                grifos_monored,\n" +
                "                id_medicion,\n" +
                "                evalu_vol_extrac,\n" +
                "                val_light,\n" +
                "                light,\n" +
                "                val_sound,\n" +
                "                sound,\n" +
                "                acorde_normas_si,\n" +
                "                acorde_normas_no,\n" +
                "                necesario_repa_si,\n" +
                "                necesario_repa_no,\n" +
                "                comentario,\n" +
                "                instrumentos\n" +
                "        )\n" +
                "        VALUES (\n" +
                "                3,\n" +
                "                '2018-05-11',\n" +
                "                3,\n" +
                "                0,\n" +
                "                2,\n" +
                "                1,\n" +
                "                0,\n" +
                "                2,\n" +
                "                2,\n" +
                "                2,\n" +
                "                2,\n" +
                "                2,\n" +
                "                2,\n" +
                "                2,\n" +
                "                0.0,\n" +
                "                1,\n" +
                "                2,\n" +
                "                2,\n" +
                "                2,\n" +
                "                3,\n" +
                "                2,\n" +
                "                870.0,\n" +
                "                2,\n" +
                "                59.2,\n" +
                "                2,\n" +
                "                1,\n" +
                "                0,\n" +
                "                0,\n" +
                "                1,\n" +
                "                'Todo ok. Recomendar que contraten servicio mantenimiento anual.',\n" +
                "                ''\n" +
                "        );";
        db.execSQL(insertMantenimiento);

        //MedicionesVol
        String inserMedicionesVol = "INSERT INTO MedicionesVol (\n" +
                "                _id,\n" +
                "                valor_medio,\n" +
                "                valor1,\n" +
                "                valor2,\n" +
                "                valor3,\n" +
                "                valor4,\n" +
                "                valor5,\n" +
                "                valor6,\n" +
                "                valor7,\n" +
                "                valor8,\n" +
                "                valor9\n" +
                "        )\n" +
                "        VALUES (\n" +
                "                1,\n" +
                "                0.5,\n" +
                "                0.54,\n" +
                "                0.52,\n" +
                "                0.54,\n" +
                "                0.54,\n" +
                "                0.46,\n" +
                "                0.5,\n" +
                "                0.51,\n" +
                "                0.39,\n" +
                "                0.47\n" +
                "        );";
        db.execSQL(inserMedicionesVol);
        inserMedicionesVol = "INSERT INTO MedicionesVol (\n" +
                "                _id,\n" +
                "                valor_medio,\n" +
                "                valor1,\n" +
                "                valor2,\n" +
                "                valor3,\n" +
                "                valor4,\n" +
                "                valor5,\n" +
                "                valor6,\n" +
                "                valor7,\n" +
                "                valor8,\n" +
                "                valor9\n" +
                "        )\n" +
                "        VALUES (\n" +
                "                2,\n" +
                "                0.35,\n" +
                "                0.57,\n" +
                "                0.04,\n" +
                "                0.6,\n" +
                "                0.46,\n" +
                "                0.04,\n" +
                "                0.53,\n" +
                "                0.38,\n" +
                "                0.05,\n" +
                "                0.5\n" +
                "        );";
        db.execSQL(inserMedicionesVol);
        inserMedicionesVol = "INSERT INTO MedicionesVol (\n" +
                "                _id,\n" +
                "                valor_medio,\n" +
                "                valor1,\n" +
                "                valor2,\n" +
                "                valor3,\n" +
                "                valor4,\n" +
                "                valor5,\n" +
                "                valor6,\n" +
                "                valor7,\n" +
                "                valor8,\n" +
                "                valor9\n" +
                "        )\n" +
                "        VALUES (\n" +
                "                3,\n" +
                "                0.46,\n" +
                "                0.49,\n" +
                "                0.48,\n" +
                "                0.47,\n" +
                "                0.46,\n" +
                "                0.41,\n" +
                "                0.44,\n" +
                "                0.41,\n" +
                "                0.47,\n" +
                "                0.51\n" +
                "        );";
        db.execSQL(inserMedicionesVol);

        //Tecnicos
        String insertTecnicos = "INSERT INTO Tecnicos (\n" +
                "                _id,\n" +
                "                tecnico_usuario,\n" +
                "                tecnico_clave,\n" +
                "                tecnico_nombre,\n" +
                "                tecnico_telef,\n" +
                "                tecnico_correo\n" +
                "        )\n" +
                "        VALUES (\n" +
                "                1,\n" +
                "                'chachy',\n" +
                "                'Tbz3Ix46i6qKiXAzOFRBfg==\n" +
                "        ',\n" +
                "        'Lázaro Guillermo Perez Montoto',\n" +
                "                '669355999',\n" +
                "                'laguipemo@gmail.com'\n" +
                "                     );";
        db.execSQL(insertTecnicos);
        insertTecnicos = "INSERT INTO Tecnicos (\n" +
                "                _id,\n" +
                "                tecnico_usuario,\n" +
                "                tecnico_clave,\n" +
                "                tecnico_nombre,\n" +
                "                tecnico_telef,\n" +
                "                tecnico_correo\n" +
                "        )\n" +
                "        VALUES (\n" +
                "                2,\n" +
                "                'rafa',\n" +
                "                'pCiTA8FAb/DCXLzksIApVw==\n" +
                "        ',\n" +
                "        'Rafael Romero Bello',\n" +
                "                '626986790',\n" +
                "                'senparada@gmail.com'\n" +
                "                     );";
        db.execSQL(insertTecnicos);
        insertTecnicos = "INSERT INTO Tecnicos (\n" +
                "                         _id,\n" +
                "                         tecnico_usuario,\n" +
                "                         tecnico_clave,\n" +
                "                         tecnico_nombre,\n" +
                "                         tecnico_telef,\n" +
                "                         tecnico_correo\n" +
                "                     )\n" +
                "                     VALUES (\n" +
                "                         3,\n" +
                "                         'admin',\n" +
                "                         'BBPhmWK8aDMCMIHYeWuJBQ==\n" +
                "',\n" +
                "                         'admin',\n" +
                "                         '-',\n" +
                "                         'administracion@atlasromero.com'\n" +
                "                     );";
        db.execSQL(insertTecnicos);


        //TiposVitrinas
        String insertTipoVitrina = "INSERT INTO TiposVitrinas (\n" +
                "                _id,\n" +
                "                tipo_vitrina\n" +
                "        )\n" +
                "        VALUES (\n" +
                "                1,\n" +
                "                'Uso General'\n" +
                "        );";
        db.execSQL(insertTipoVitrina);
        insertTipoVitrina = "INSERT INTO TiposVitrinas (\n" +
                "                _id,\n" +
                "                tipo_vitrina\n" +
                "        )\n" +
                "        VALUES (\n" +
                "                2,\n" +
                "                'Acidos Concentrados'\n" +
                "        );";
        db.execSQL(insertTipoVitrina);
        insertTipoVitrina = "INSERT INTO TiposVitrinas (\n" +
                "                _id,\n" +
                "                tipo_vitrina\n" +
                "        )\n" +
                "        VALUES (\n" +
                "                3,\n" +
                "                'Grandes Montajes'\n" +
                "        );";
        db.execSQL(insertTipoVitrina);

        //Vitrinas
        String insertVitrina = "INSERT INTO Vitrinas (\n" +
                "                _id,\n" +
                "                id_empresa,\n" +
                "                id_tipo,\n" +
                "                id_longitud,\n" +
                "                vitrina_fabricante,\n" +
                "                vitrina_referencia,\n" +
                "                vitrina_inventario,\n" +
                "                vitrina_anho,\n" +
                "                vitrina_contrato\n" +
                "        )\n" +
                "        VALUES (\n" +
                "                1,\n" +
                "                1,\n" +
                "                1,\n" +
                "                1,\n" +
                "                1,\n" +
                "                '2-453-IAHD',\n" +
                "                '-',\n" +
                "                2011,\n" +
                "                'Contratado'\n" +
                "        );";
        db.execSQL(insertVitrina);
        insertVitrina = "INSERT INTO Vitrinas (\n" +
                "                _id,\n" +
                "                id_empresa,\n" +
                "                id_tipo,\n" +
                "                id_longitud,\n" +
                "                vitrina_fabricante,\n" +
                "                vitrina_referencia,\n" +
                "                vitrina_inventario,\n" +
                "                vitrina_anho,\n" +
                "                vitrina_contrato\n" +
                "        )\n" +
                "        VALUES (\n" +
                "                2,\n" +
                "                2,\n" +
                "                2,\n" +
                "                2,\n" +
                "                3,\n" +
                "                'V21-SPACE AC 1500',\n" +
                "                'CABINA 04',\n" +
                "                2007,\n" +
                "                '-'\n" +
                "        );";
        db.execSQL(insertVitrina);
        insertVitrina = "INSERT INTO Vitrinas (\n" +
                "                _id,\n" +
                "                id_empresa,\n" +
                "                id_tipo,\n" +
                "                id_longitud,\n" +
                "                vitrina_fabricante,\n" +
                "                vitrina_referencia,\n" +
                "                vitrina_inventario,\n" +
                "                vitrina_anho,\n" +
                "                vitrina_contrato\n" +
                "        )\n" +
                "        VALUES (\n" +
                "                3,\n" +
                "                3,\n" +
                "                3,\n" +
                "                3,\n" +
                "                4,\n" +
                "                '-',\n" +
                "                '-',\n" +
                "                0,\n" +
                "                'No Contratado'\n" +
                "        );";
        db.execSQL(insertVitrina);
        // Instrumentos
        String insertInstrumento = "INSERT INTO Instrumentos (_id, nombre, modelo, marca) " +
                "VALUES (1, 'Anemómetro + Sondas', '', 'TESTO');";
        db.execSQL(insertInstrumento);
        insertInstrumento = "INSERT INTO Instrumentos (_id, nombre, modelo, marca) " +
                "VALUES (2, 'Anemómetro', '', 'PCE');";
        db.execSQL(insertInstrumento);
        insertInstrumento = "INSERT INTO Instrumentos (_id, nombre, modelo, marca) " +
                "VALUES (3, 'Luxómetro', '', 'PCE');";
        db.execSQL(insertInstrumento);
        insertInstrumento = "INSERT INTO Instrumentos (_id, nombre, modelo, marca) " +
                "VALUES (4, 'Dinamómetro', '', 'PCE');";
        db.execSQL(insertInstrumento);
        insertInstrumento = "INSERT INTO Instrumentos (_id, nombre, modelo, marca) " +
                "VALUES (5, 'Sonómetro', '', 'PCE');";
        db.execSQL(insertInstrumento);
        insertInstrumento = "INSERT INTO Instrumentos (_id, nombre, modelo, marca) " +
                "VALUES (6, 'Sonómetro', 'GM1351', 'COLEMETER');";
        db.execSQL(insertInstrumento);
        insertInstrumento = "INSERT INTO Instrumentos (_id, nombre, modelo, marca) " +
                "VALUES (7, 'Termómetro por infrarrojos', '', 'TESTO');";
        db.execSQL(insertInstrumento);
        insertInstrumento = "INSERT INTO Instrumentos (_id, nombre, modelo, marca) " +
                "VALUES (8, 'Anemómetro de molinete', '', 'TESTO');";
        db.execSQL(insertInstrumento);
        insertInstrumento = "INSERT INTO Instrumentos (_id, nombre, modelo, marca) " +
                "VALUES (9, 'Detector de corriente', '', 'DRAGER');";
        db.execSQL(insertInstrumento);
        insertInstrumento = "INSERT INTO Instrumentos (_id, nombre, modelo, marca) " +
                "VALUES (10, 'Pinza amperimétrica', 'PIN1000', 'XINDAR');";
        db.execSQL(insertInstrumento);
        insertInstrumento = "INSERT INTO Instrumentos (_id, nombre, modelo, marca) " +
                "VALUES (11, 'Multímetro digital', '', 'FLUKE');";
        db.execSQL(insertInstrumento);
        insertInstrumento = "INSERT INTO Instrumentos (_id, nombre, modelo, marca) " +
                "VALUES (12, 'Nivel Laser', 'QUIGO', 'BOSCH');";
        db.execSQL(insertInstrumento);
        insertInstrumento = "INSERT INTO Instrumentos (_id, nombre, modelo, marca) " +
                "VALUES (13, 'Medidor laser', 'PRL 30C', 'BOSCH');";
        db.execSQL(insertInstrumento);
        insertInstrumento = "INSERT INTO Instrumentos (_id, nombre, modelo, marca) " +
                "VALUES (14, 'Nivel laser', 'GCL2-15', 'BOSCH');";
        db.execSQL(insertInstrumento);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DataBaseContract.TecnicosTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DataBaseContract.EmpresasTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DataBaseContract.ContactosTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DataBaseContract.VitrinasTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DataBaseContract.TiposVitrinasTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DataBaseContract.FabricantesTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DataBaseContract.LongitudesTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DataBaseContract.TiposLongsFlowsTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DataBaseContract.MantenimientosTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DataBaseContract.CualitativosTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DataBaseContract.MedicionesVolTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DataBaseContract.InstrumentosTable.TABLE_NAME);

        onCreate(db);

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void backup(String outFileName) {
        // database path, source (input)
        final String inFileName = context.getDatabasePath(DataBaseContract.DATABASE_NAME).toString();
        try {
            // input file and its stream associate
            File inFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(inFile);

            // output associate stream with empty output file that has a filename received
            OutputStream fos = new FileOutputStream(outFileName);

            // transfer bytes from the input file to the output file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            // close streams (input and output)
            fos.flush();
            fos.close();
            fis.close();

            Toast.makeText(context, "Export Completed", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("DataBaseHelper)", Arrays.toString(e.getStackTrace()));
        }
    }

    public void backup() {
        // database path, source (input)
        final String inFilePath = context.getDatabasePath(DataBaseContract.DATABASE_NAME).toString();
        String databaseName = DataBaseContract.DATABASE_NAME.split("\\.")[0];
        try {
            Uri contentUri = MediaStore.Files.getContentUri("external");
            String selection = MediaStore.MediaColumns.RELATIVE_PATH + "=?";
            String[] selectionArgs = new String[]{
                    Environment.DIRECTORY_DOCUMENTS +
                            File.separator +
                            String.format("%s_backup", databaseName)
                            + File.separator
            };
            Cursor cursor = context.getContentResolver()
                    .query(contentUri, null, selection, selectionArgs, null);

            // input file and its associate stream
            File inFile = new File(inFilePath);
            FileInputStream fis = new FileInputStream(inFile);

            // output file using ContentProvider
            ContentValues valuesOutFile = new ContentValues();
            valuesOutFile.put(MediaStore.MediaColumns.DISPLAY_NAME, databaseName);
            valuesOutFile.put(MediaStore.MediaColumns.MIME_TYPE, "application/vdn.sqlite3");
            valuesOutFile.put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_DOCUMENTS +
                            File.separator +
                            String.format("%s_backup", databaseName)
                            + File.separator
            );

            Uri uriOut = null;

            if (cursor.getCount() == 0) {
                // creo el identificador uniforme de recursos, Uri, para el fichero final en el volumen
                // el volumen externo
                uriOut = context.getContentResolver()
                        .insert(MediaStore.Files.getContentUri("external"), valuesOutFile);
            } else {
                while (cursor.moveToNext()) {
                    int columnIndexName = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);
                    String fileName = cursor.getString(columnIndexName);

                    if (fileName.equals(DataBaseContract.DATABASE_NAME)) {
                        int columnIndexId = cursor.getColumnIndex(MediaStore.MediaColumns._ID);
                        long id = cursor.getLong(columnIndexId);

                        uriOut = ContentUris.withAppendedId(contentUri, id);

                        break;
                    }
                }
            }
            // Abro el flujo de salida de datos conectado con el fichero final en el volumen externo
            OutputStream fos = context.getContentResolver().openOutputStream(uriOut);

            // transfer bytes from the input file to the output file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            // close streams (input and output)
            fos.flush();
            fos.close();
            fis.close();

            Toast.makeText(context, "Export Completed", Toast.LENGTH_SHORT).show();

            cursor.close();

        } catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("DataBaseHelper", Arrays.toString(e.getStackTrace()));
        }
    }

    public void importDB(String inFileName) {
        // database path, destination (output)
        final String outFileName = context.getDatabasePath(DataBaseContract.DATABASE_NAME).toString();

        try {
            // input file and its associate stream
            File inFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(inFile);

            // output associate stream with empty output file
            OutputStream fos = new FileOutputStream(outFileName);

            // Transfer bytes from the input file to the output file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            // Close the streams
            fos.flush();
            fos.close();
            fis.close();

            Toast.makeText(context, "Import Completed", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("DataBaseHelper)", Arrays.toString(e.getStackTrace()));
        }
    }

    public void importDB() {
        // database path, destination (output)
        final String outFilePath = context.getDatabasePath(DataBaseContract.DATABASE_NAME).toString();

        String databaseName = DataBaseContract.DATABASE_NAME.split("\\.")[0];

        try {
            // input file using ContentProvider
            Uri uriContentIn = MediaStore.Files.getContentUri("external");
            String selection = MediaStore.MediaColumns.RELATIVE_PATH + "=?";
            String[] selectionArgs = new String[]{
                    Environment.DIRECTORY_DOCUMENTS + String.format("/%s_backup/", databaseName)
            };

            Cursor cursor = context.getContentResolver()
                    .query(uriContentIn, null, selection, selectionArgs, null);

            Uri uriIn = null;

            if (cursor.getCount() == 0) {
                Toast.makeText(
                        context,
                        "No se encontró el fichero de copia en \"" +
                                Environment.DIRECTORY_DOCUMENTS +
                                String.format("/%s_backup/\"", databaseName),
                        Toast.LENGTH_SHORT
                ).show();
            } else {
                while (cursor.moveToNext()) {
                    int columnIndexName = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);
                    String fileInName = cursor.getString(columnIndexName);

                    if (fileInName.equals(String.format("%s.db", databaseName))) {
                        int columnIndexId = cursor.getColumnIndex(MediaStore.MediaColumns._ID);
                        long id = cursor.getLong(columnIndexId);
                        uriIn = ContentUris.withAppendedId(uriContentIn, id);
                        break;
                    }
                }
                if (uriIn == null) {
                    Toast.makeText(
                            context,
                            String.format("No se encontró el fichero de copia \"%s.db\"", databaseName),
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    // input associate stream
                    InputStream fis = context.getContentResolver().openInputStream(uriIn);

                    // output associate stream with empty output file
                    OutputStream fos = new FileOutputStream(outFilePath);

                    // Transfer bytes from the input file to the output file
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }

                    // Close the streams
                    fos.flush();
                    fos.close();
                    fis.close();

                    Toast.makeText(context, "Import Completed", Toast.LENGTH_SHORT).show();
                }
            }
            cursor.close();

        } catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("DataBaseHelper)", Arrays.toString(e.getStackTrace()));
        }
    }

}
