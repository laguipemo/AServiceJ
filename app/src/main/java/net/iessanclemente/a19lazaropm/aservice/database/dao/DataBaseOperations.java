package net.iessanclemente.a19lazaropm.aservice.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.iessanclemente.a19lazaropm.aservice.database.dto.Cualitativo;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Fabricante;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Tecnico;
import net.iessanclemente.a19lazaropm.aservice.database.dto.TipoVitrina;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Contacto;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Empresa;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Longitud;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Mantenimiento;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Medicion;
import net.iessanclemente.a19lazaropm.aservice.database.dto.TipoLongFlow;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Vitrina;

import java.util.ArrayList;
import java.util.List;


/**
 * Clase auxiliar (con patron singleton) que implementa las operaciones CRUD sobre las entidades
 * definidas.
 */
public class DataBaseOperations {

    private static DataBaseHelper dataBase;
    private static final DataBaseOperations instance = new DataBaseOperations();

    private DataBaseOperations() {
    }

    public static DataBaseOperations getInstance(Context context) {
        if (dataBase == null) {
            dataBase = new DataBaseHelper(context);
        }
        return instance;
    }

    public void close() {
        dataBase.close();
    }

    /*
        CRUD sobre la entidad Tecnico
     */
    public long insertTecnico(Tecnico tecnico) {
        //Creo instancia para escribir en la base de datos
        SQLiteDatabase db = dataBase.getWritableDatabase();
        //Creo el objeto ContentValues para colocar los datos a insertar
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseContract.TecnicosTable.COL_USUARIO, tecnico.getTecnicoNombre());
        contentValues.put(DataBaseContract.TecnicosTable.COL_CLAVE, tecnico.getTecnicoClave());
        contentValues.put(DataBaseContract.TecnicosTable.COL_NAME, tecnico.getTecnicoNombre());
        contentValues.put(DataBaseContract.TecnicosTable.COL_TELEF, tecnico.getTecnicoTelef());
        contentValues.put(DataBaseContract.TecnicosTable.COL_CORREO, tecnico.getTecnicoCorreo());

        return db.insert(
                DataBaseContract.TecnicosTable.TABLE_NAME, null, contentValues);
    }

    public List<Tecnico> selectTecnicos() {
        //String con secuencia SQL para la búsqueda
        String sqlSelectTecnicos = "SELECT * FROM " + DataBaseContract.TecnicosTable.TABLE_NAME;
        return selectTecnicos(sqlSelectTecnicos);
    }

    public List<Tecnico> selectTecnicos(String sqlSelectTecnicos) {
        //Declaro lista de tecnicos seleccionados que se devolverán.
        List<Tecnico> tecnicosSelected = new ArrayList<>();
        //Creo instancia para leer de la base de datos
        SQLiteDatabase db = dataBase.getReadableDatabase();
        //Para realizar una consulta se utilza el médodo:
        //     rawQuery(string_con_consulta_select_parametrizada, lista_de_string_con_los_parámetros
        //en este caso concreto, la consulta es sencilla y al no utilizar parámetros pues el segundo
        //argumento del método (lista con parámetros) es null
        Cursor cursor = db.rawQuery(sqlSelectTecnicos, null);
        //Si el cursor almacena algún elemento, pues devuelve true con el método moveToFirst()
        if (cursor.moveToFirst()) {
            //Recupero el primer elemento del cursor y repito esta operación mientras que el cursor
            //devuelva true con el método moveToNext(), es decir, mientras tenga un siguiente elemento
            do {
                int id = cursor.getInt(0);
                String tecnicoUsuario = cursor.getString(1);
                String tecnicoClave = cursor.getString(2);
                String tecnicoName = cursor.getString(3);
                String tecnicoTelef = cursor.getString(4);
                String tecnicoCorreo = cursor.getString(5);
                //Construyo objeto Tecnico y lo añado a la lista
                Tecnico tecnico = new Tecnico(
                        id, tecnicoUsuario, tecnicoClave, tecnicoName, tecnicoTelef, tecnicoCorreo);
                tecnicosSelected.add(tecnico);
            } while (cursor.moveToNext());
        }
        //Cierro el cursor para liberar recursos
        cursor.close();
        return tecnicosSelected;
    }

    public Tecnico selectTecnicoWithId(int idToSelect) {
        Tecnico tecnico = null;
        //Creo instancia para leer de la base de datos
        SQLiteDatabase db = dataBase.getReadableDatabase();
        //String con la consulta SQL a realizar, faltaría el parámetro en la posición ? que intruduce
        //en la rawQuery como un array de string
        String sqlSelect = String.format("SELECT * FROM %s WHERE %s=?",
                DataBaseContract.TecnicosTable.TABLE_NAME,
                DataBaseContract.TecnicosTable._ID);
        //Creo array de strings con los parámetros para la consulta parametrizada anterior
        String[] selectArguments = {String.valueOf(idToSelect)};
        //Realizo al consulta almacenado el resultdo en un cursor
        Cursor cursor = db.rawQuery(sqlSelect, selectArguments);
        //Si el cursor almacena algún elemento, pues devuelve true con el método moveToFirst()
        if (cursor.moveToFirst()) {
            //Recupero el primer elemento del cursor, que además el único que debe haber (no debe
            // haber duplicados) y la consulta es para recuperar el elemento de id indicado.
            int id = cursor.getInt(0);
            String tecnicoUsuario = cursor.getString(1);
            String tecnicoClave = cursor.getString(2);
            String tecnicoName = cursor.getString(3);
            String tecnicoTelef = cursor.getString(4);
            String tecnicoCorreo = cursor.getString(5);
            //Construyo objeto Tecnico y lo añado a la lista
            tecnico = new Tecnico(
                    id, tecnicoUsuario, tecnicoClave, tecnicoName, tecnicoTelef, tecnicoCorreo);
        }
        cursor.close();
        return tecnico;
    }

    public Tecnico selectTecnicoWithName(String nameTecnico) {
        Tecnico tecnico = null;
        //Creo instancia para leer de la base de datos
        SQLiteDatabase db = dataBase.getReadableDatabase();
        //String con la consulta SQL a realizar, faltaría el parámetro en la posición ? que intruduce
        //en la rawQuery como un array de string
        String sqlSelect = String.format("SELECT * FROM %s WHERE %s=?",
                DataBaseContract.TecnicosTable.TABLE_NAME,
                DataBaseContract.TecnicosTable.COL_NAME);
        //Creo array de strings con los parámetros para la consulta parametrizada anterior
        String[] selectArguments = {nameTecnico};
        //Realizo al consulta almacenado el resultado en un cursor
        Cursor cursor = db.rawQuery(sqlSelect, selectArguments);
        //Si el cursor almacena algún elemento, pues devuelve true con el método moveToFirst()
        if (cursor.moveToFirst()) {
            //Recupero el primer elemento del cursor, que además el único que debe haber (no debe
            // haber duplicados) y la consulta es para recuperar el elemento de id indicado.
            int id = cursor.getInt(0);
            String tecnicoUsuario = cursor.getString(1);
            String tecnicoClave = cursor.getString(2);
            String tecnicoName = cursor.getString(3);
            String tecnicoTelef = cursor.getString(4);
            String tecnicoCorreo = cursor.getString(5);
            //Construyo objeto Tecnico y lo añado a la lista
            tecnico = new Tecnico(
                    id, tecnicoUsuario, tecnicoClave, tecnicoName, tecnicoTelef, tecnicoCorreo);
        }
        cursor.close();
        return tecnico;
    }

    public Tecnico selectTecnicoWithUsuario(String userTecnico) {
        Tecnico tecnico = null;
        //Creo instancia para leer de la base de datos
        SQLiteDatabase db = dataBase.getReadableDatabase();
        //String con la consulta SQL a realizar, faltaría el parámetro en la posición ? que intruduce
        //en la rawQuery como un array de string
        String sqlSelect = String.format("SELECT * FROM %s WHERE %s=?",
                DataBaseContract.TecnicosTable.TABLE_NAME,
                DataBaseContract.TecnicosTable.COL_USUARIO);
        //Creo array de strings con los parámetros para la consulta parametrizada anterior
        String[] selectArguments = {userTecnico};
        //Realizo al consulta almacenado el resultado en un cursor
        Cursor cursor = db.rawQuery(sqlSelect, selectArguments);
        //Si el cursor almacena algún elemento, pues devuelve true con el método moveToFirst()
        if (cursor.moveToFirst()) {
            //Recupero el primer elemento del cursor, que además el único que debe haber (no debe
            // haber duplicados) y la consulta es para recuperar el elemento de id indicado.
            int id = cursor.getInt(0);
            String tecnicoUsuario = cursor.getString(1);
            String tecnicoClave = cursor.getString(2);
            String tecnicoName = cursor.getString(3);
            String tecnicoTelef = cursor.getString(4);
            String tecnicoCorreo = cursor.getString(5);
            //Construyo objeto Tecnico y lo añado a la lista
            tecnico = new Tecnico(
                    id, tecnicoUsuario, tecnicoClave, tecnicoName, tecnicoTelef, tecnicoCorreo);
        }
        cursor.close();
        return tecnico;
    }

    public boolean updateTecnico(Tecnico tecnico) {
        //Obtengo instancia de la base de datos para escritura
        SQLiteDatabase db = dataBase.getWritableDatabase();
        //Creo objeto ContentValues con los valores del nuevo elemento
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseContract.TecnicosTable.COL_USUARIO, tecnico.getTecnicoUsuario());
        contentValues.put(DataBaseContract.TecnicosTable.COL_CLAVE, tecnico.getTecnicoClave());
        contentValues.put(DataBaseContract.TecnicosTable.COL_NAME, tecnico.getTecnicoNombre());
        contentValues.put(DataBaseContract.TecnicosTable.COL_TELEF, tecnico.getTecnicoTelef());
        contentValues.put(DataBaseContract.TecnicosTable.COL_CORREO, tecnico.getTecnicoCorreo());
        //Para actualizar un elemento se utiliza el médodo:
        //   update(nombre_tabla, valores, criterio_seleccion, parametros_criterio_seleccion)
        String criterioSeleccion = String.format("%s=?", DataBaseContract.TecnicosTable._ID);
        //array de string con los parámetros del criterio de selección
        String[] paramCriterioSeleccion = {String.valueOf(tecnico.getId())};
        //Realizo la actualización
        int updated = db.update(
                DataBaseContract.TecnicosTable.TABLE_NAME,
                contentValues,
                criterioSeleccion,
                paramCriterioSeleccion);
        return updated > 0; // true si se actualizó algo
    }

    public boolean deleteTecnico(int id) {
        //obtengo instancia de la base de datos para escritura
        SQLiteDatabase db = dataBase.getWritableDatabase();
        //Para borrar se utilizar el método:
        //  delete(nombre_tabla, criterio_seleccion, parametros_criterio_seleccion
        //Defino la string con el criterio de seleccion parametrizado.
        String criterioSeleccion = String.format("%s=?", DataBaseContract.TecnicosTable._ID);
        //array de string con los valores de los parámetros para completar la query parametrizada
        String[] paramCriterioSeleccion = {String.valueOf(id)};
        //ejecuto el borrado
        int deleted = db.delete(
                DataBaseContract.TecnicosTable.TABLE_NAME, criterioSeleccion, paramCriterioSeleccion);
        return deleted > 0;
    }

    public int getIdOfTecnicoWhithName(String name) {
        int id = -1;
        for (Tecnico tec : selectTecnicos()) {
            if (tec.getTecnicoNombre().trim().equalsIgnoreCase(name.trim())) {
                id = tec.getId();
                break;
            }
        }
        return id;
    }

    public String getNombreTecnicoWithId(int idTecnico) {
        return selectTecnicoWithId(idTecnico).getTecnicoNombre();
    }

    /*
        CRUD sobre la entidad Empresas
     */
    public long insertEmpresa(Empresa empresa) {
        //Creo instancia para escribir en la base de datos
        SQLiteDatabase db = dataBase.getWritableDatabase();
        //Creo el objeto ContentValues para colocar los datos a insertar
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseContract.EmpresasTable.COL_NAME, empresa.getEmpresaNombre());
        contentValues.put(DataBaseContract.EmpresasTable.COL_DIRECC, empresa.getEmpresaDirecc());
        contentValues.put(DataBaseContract.EmpresasTable.COL_ID_CONTACTO, empresa.getIdContacto());

        return db.insert(
                DataBaseContract.EmpresasTable.TABLE_NAME, null, contentValues);
    }

    public List<Empresa> selectEmpresas() {
        //String con secuencia SQL para la búsqueda
        String sqlSelectEmpresas = "SELECT * FROM " + DataBaseContract.EmpresasTable.TABLE_NAME;
        return selectEmpresas(sqlSelectEmpresas);
    }

    public List<Empresa> selectEmpresas(String sqlSelectEmpresas) {
        //Declaro lista de empresas seleccionados que se devolverán.
        List<Empresa> empresasSelected = new ArrayList<>();
        //Creo instancia para leer de la base de datos
        SQLiteDatabase db = dataBase.getReadableDatabase();
        //Para realizar una consulta se utilza el médodo:
        //     rawQuery(string_con_consulta_select_parametrizada, lista_de_string_con_los_parámetros
        //en este caso concreto, la consulta es sencilla y al no utilizar parámetros pues el segundo
        //argumento del método (lista con parámetros) es null
        Cursor cursor = db.rawQuery(sqlSelectEmpresas, null);
        //Si el cursor almacena algún elemento, pues devuelve true con el método moveToFirst()
        if (cursor.moveToFirst()) {
            //Recupero el primer elemento del cursor y repito esta operación mientras que el cursor
            //devuelva true con el método moveToNext(), es decir, mientras tenga un siguiente elemento
            do {
                int id = cursor.getInt(0);
                String empresaName = cursor.getString(1);
                String empresaDirecc = cursor.getString(2);
                int empresaIdContacto = cursor.getInt(3);
                //Construyo objeto Empresa y lo añado a la lista
                Empresa empresa = new Empresa(
                        id, empresaName, empresaDirecc, empresaIdContacto);
                empresasSelected.add(empresa);
            } while (cursor.moveToNext());
        }
        //Cierro el cursor para liberar recursos
        cursor.close();
        return empresasSelected;
    }

    public Empresa selectEmpresaWithId(int idToSelect) {
        Empresa empresa = null;
        //Creo instancia para leer de la base de datos
        SQLiteDatabase db = dataBase.getReadableDatabase();
        //String con la consulta SQL a realizar, faltaría el parámetro en la posición ? que intruduce
        //en la rawQuery como un array de string
        String sqlSelect = String.format("SELECT * FROM %s WHERE %s=?",
                DataBaseContract.EmpresasTable.TABLE_NAME,
                DataBaseContract.EmpresasTable._ID);
        //Creo array de strings con los parámetros para la consulta parametrizada anterior
        String[] selectArguments = {String.valueOf(idToSelect)};
        //Realizo al consulta almacenado el resultado en un cursor
        Cursor cursor = db.rawQuery(sqlSelect, selectArguments);
        //Si el cursor almacena algún elemento, pues devuelve true con el método moveToFirst()
        if (cursor.moveToFirst()) {
            //Recupero el primer elemento del cursor, que además el único que debe haber (no debe
            // haber duplicados) y la consulta es para recuperar el elemento de id indicado.
            int id = cursor.getInt(0);
            String empresaName = cursor.getString(1);
            String empresaDirecc = cursor.getString(2);
            int empresaIdContacto = cursor.getInt(3);
            //Construyo objeto Empresa y lo añado a la lista
            empresa = new Empresa(
                    id, empresaName, empresaDirecc, empresaIdContacto);
        }
        cursor.close();
        return empresa;
    }

    public Empresa selectEmpresaWithName(String nameEmpresa) {
        Empresa empresa = null;
        //Creo instancia para leer de la base de datos
        SQLiteDatabase db = dataBase.getReadableDatabase();
        //String con la consulta SQL a realizar, faltaría el parámetro en la posición ? que intruduce
        //en la rawQuery como un array de string
        String sqlSelect = String.format("SELECT * FROM %s WHERE %s=?",
                DataBaseContract.EmpresasTable.TABLE_NAME,
                DataBaseContract.EmpresasTable.COL_NAME);
        //Creo array de strings con los parámetros para la consulta parametrizada anterior
        String[] selectArguments = {nameEmpresa};
        //Realizo al consulta almacenado el resultado en un cursor
        Cursor cursor = db.rawQuery(sqlSelect, selectArguments);
        //Si el cursor almacena algún elemento, pues devuelve true con el método moveToFirst()
        if (cursor.moveToFirst()) {
            //Recupero el primer elemento del cursor, que además el único que debe haber (no debe
            // haber duplicados) y la consulta es para recuperar el elemento de id indicado.
            int id = cursor.getInt(0);
            String empresaName = cursor.getString(1);
            String empresaDirecc = cursor.getString(2);
            int empresaIdContacto = cursor.getInt(3);
            //Construyo objeto Empresa y lo añado a la lista
            empresa = new Empresa(
                    id, empresaName, empresaDirecc, empresaIdContacto);
        }
        cursor.close();
        return empresa;
    }


    public boolean updateEmpresa(Empresa empresa) {
        //Obtengo instancia de la base de datos para escritura
        SQLiteDatabase db = dataBase.getWritableDatabase();
        //Creo objeto ContentValues con los valores del nuevo elemento
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseContract.EmpresasTable.COL_NAME, empresa.getEmpresaNombre());
        contentValues.put(DataBaseContract.EmpresasTable.COL_DIRECC, empresa.getEmpresaDirecc());
        contentValues.put(DataBaseContract.EmpresasTable.COL_ID_CONTACTO, empresa.getIdContacto());
        //Para actualizar un elemento se utiliza el médodo:
        //   update(nombre_tabla, valores, criterio_seleccion, parametros_criterio_seleccion)
        String criterioSeleccion = String.format("%s=?", DataBaseContract.EmpresasTable._ID);
        //array de string con los parámetros del criterio de selección
        String[] paramCriterioSeleccion = {String.valueOf(empresa.getId())};
        //Realizo la actualización
        int updated = db.update(
                DataBaseContract.EmpresasTable.TABLE_NAME,
                contentValues,
                criterioSeleccion,
                paramCriterioSeleccion);
        return updated > 0; // true si se actualizó algo
    }

    public boolean deleteEmpresa(int id) {
        //obtengo instancia de la base de datos para escritura
        SQLiteDatabase db = dataBase.getWritableDatabase();
        //Para borrar se utilizar el método:
        //  delete(nombre_tabla, criterio_seleccion, parametros_criterio_seleccion
        //Defino la string con el criterio de seleccion parametrizado.
        String criterioSeleccion = String.format("%s=?", DataBaseContract.EmpresasTable._ID);
        //array de string con los valores de los parámetros para completar la query parametrizada
        String[] paramCriterioSeleccion = {String.valueOf(id)};
        //ejecuto el borrado
        int deleted = db.delete(
                DataBaseContract.EmpresasTable.TABLE_NAME, criterioSeleccion, paramCriterioSeleccion);
        return deleted > 0;
    }

    public int getIdOfEmpresaWhithName(String name) {
        int id = -1;
        for (Empresa emp : selectEmpresas()) {
            if (emp.getEmpresaNombre().trim().equalsIgnoreCase(name.trim())) {
                id = emp.getId();
                break;
            }
        }
        return id;
    }

    public boolean existEmpresa(Empresa empresa) {
        boolean exist = false;
        for (Empresa emp: selectEmpresas()) {
            if (emp.equals(empresa)) {
                exist = true;
                break;
            }
        }
        return exist;
    }

    /*
        CRUD sobre la entidad Contacto
     */
    public long insertContacto(Contacto contacto) {
        //Creo instancia para escribir en la base de datos
        SQLiteDatabase db = dataBase.getWritableDatabase();
        //Creo el objeto ContentValues para colocar los datos a insertar
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseContract.ContactosTable.COL_NAME, contacto.getContactoNombre());
        contentValues.put(DataBaseContract.ContactosTable.COL_TELEF, contacto.getContactoTelef());
        contentValues.put(DataBaseContract.ContactosTable.COL_CORREO, contacto.getContactoCorreo());

        return db.insert(
                DataBaseContract.ContactosTable.TABLE_NAME, null, contentValues);
    }

    public List<Contacto> selectContactos() {
        //String con secuencia SQL para la búsqueda
        String sqlSelectContactos = "SELECT * FROM " + DataBaseContract.EmpresasTable.TABLE_NAME;
        return selectContactos(sqlSelectContactos);
    }

    public List<Contacto> selectContactos(String sqlSelectContactos) {
        //Declaro lista de contactos seleccionados que se devolverán.
        List<Contacto> contactosSelected = new ArrayList<>();
        //Creo instancia para leer de la base de datos
        SQLiteDatabase db = dataBase.getReadableDatabase();
        //Para realizar una consulta se utilza el médodo:
        //     rawQuery(string_con_consulta_select_parametrizada, lista_de_string_con_los_parámetros
        //en este caso concreto, la consulta es sencilla y al no utilizar parámetros pues el segundo
        //argumento del método (lista con parámetros) es null
        Cursor cursor = db.rawQuery(sqlSelectContactos, null);
        //Si el cursor almacena algún elemento, pues devuelve true con el método moveToFirst()
        if (cursor.moveToFirst()) {
            //Recupero el primer elemento del cursor y repito esta operación mientras que el cursor
            //devuelva true con el método moveToNext(), es decir, mientras tenga un siguiente elemento
            do {
                int id = cursor.getInt(0);
                String contactoName = cursor.getString(1);
                String contactoTelef = cursor.getString(2);
                String contactoCorreo = cursor.getString(3);
                //Construyo objeto Contacto y lo añado a la lista
                Contacto contacto = new Contacto(
                        id, contactoName, contactoTelef, contactoCorreo);
                contactosSelected.add(contacto);
            } while (cursor.moveToNext());
        }
        //Cierro el cursor para liberar recursos
        cursor.close();
        return contactosSelected;
    }

    public Contacto selectContactoWithId(int idToSelect) {
        Contacto contacto = null;
        //Creo instancia para leer de la base de datos
        SQLiteDatabase db = dataBase.getReadableDatabase();
        //String con la consulta SQL a realizar, faltaría el parámetro en la posición ? que intruduce
        //en la rawQuery como un array de string
        String sqlSelect = String.format("SELECT * FROM %s WHERE %s=?",
                DataBaseContract.ContactosTable.TABLE_NAME,
                DataBaseContract.ContactosTable._ID);
        //Creo array de strings con los parámetros para la consulta parametrizada anterior
        String[] selectArguments = {String.valueOf(idToSelect)};
        //Realizo al consulta almacenado el resultado en un cursor
        Cursor cursor = db.rawQuery(sqlSelect, selectArguments);
        //Si el cursor almacena algún elemento, pues devuelve true con el método moveToFirst()
        if (cursor.moveToFirst()) {
            //Recupero el primer elemento del cursor, que además el único que debe haber (no debe
            // haber duplicados) y la consulta es para recuperar el elemento de id indicado.
            int id = cursor.getInt(0);
            String contactoName = cursor.getString(1);
            String contactoTelef = cursor.getString(2);
            String contactoCorreo = cursor.getString(3);
            //Construyo objeto Contacto y lo añado a la lista
            contacto = new Contacto(
                    id, contactoName, contactoTelef, contactoCorreo);
        }
        cursor.close();
        return contacto;
    }

    public boolean updateContacto(Contacto contacto) {
        //Obtengo instancia de la base de datos para escritura
        SQLiteDatabase db = dataBase.getWritableDatabase();
        //Creo objeto ContentValues con los valores del nuevo elemento
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseContract.ContactosTable.COL_NAME, contacto.getContactoNombre());
        contentValues.put(DataBaseContract.ContactosTable.COL_TELEF, contacto.getContactoTelef());
        contentValues.put(DataBaseContract.ContactosTable.COL_CORREO, contacto.getContactoCorreo());
        //Para actualizar un elemento se utiliza el médodo:
        //   update(nombre_tabla, valores, criterio_seleccion, parametros_criterio_seleccion)
        String criterioSeleccion = String.format("%s=?", DataBaseContract.ContactosTable._ID);
        //array de string con los parámetros del criterio de selección
        String[] paramCriterioSeleccion = {String.valueOf(contacto.getId())};
        //Realizo la actualización
        int updated = db.update(
                DataBaseContract.ContactosTable.TABLE_NAME,
                contentValues,
                criterioSeleccion,
                paramCriterioSeleccion);
        return updated > 0; // true si se actualizó algo
    }

    public boolean deleteContacto(int id) {
        //obtengo instancia de la base de datos para escritura
        SQLiteDatabase db = dataBase.getWritableDatabase();
        //Para borrar se utilizar el método:
        //  delete(nombre_tabla, criterio_seleccion, parametros_criterio_seleccion
        //Defino la string con el criterio de seleccion parametrizado.
        String criterioSeleccion = String.format("%s=?", DataBaseContract.ContactosTable._ID);
        //array de string con los valores de los parámetros para completar la query parametrizada
        String[] paramCriterioSeleccion = {String.valueOf(id)};
        //ejecuto el borrado
        int deleted = db.delete(
                DataBaseContract.ContactosTable.TABLE_NAME, criterioSeleccion, paramCriterioSeleccion);
        return deleted > 0;
    }

    public int getIdOfContactoWhithName(String name) {
        int id = -1;
        for (Contacto contacto : selectContactos()) {
            if (contacto.getContactoNombre().trim().equalsIgnoreCase(name.trim())) {
                id = contacto.getId();
                break;
            }
        }
        return id;
    }

    public boolean existContacto(Contacto c) {
        boolean exist = false;
        for (Contacto contacto: selectContactos()) {
            if (contacto.equals(c)) {
                exist = true;
                break;
            }
        }
        return exist;
    }

    /*
        CRUD sobre la entidad Vitrina
     */
    public long insertVitrina(Vitrina vitrina) {
        //Creo instancia para escribir en la base de datos
        SQLiteDatabase db = dataBase.getWritableDatabase();
        //Creo el objeto ContentValues para colocar los datos a insertar
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseContract.VitrinasTable.COL_ID_EMPRESA, vitrina.getIdEmpresa());
        contentValues.put(DataBaseContract.VitrinasTable.COL_ID_FABRICANTE, vitrina.getIdFabricante());
        contentValues.put(DataBaseContract.VitrinasTable.COL_ID_TIPO, vitrina.getIdTipo());
        contentValues.put(DataBaseContract.VitrinasTable.COL_ID_LONGITUD, vitrina.getIdLongitud());
        contentValues.put(DataBaseContract.VitrinasTable.COL_REFERENCIA, vitrina.getVitrinaReferencia());
        contentValues.put(DataBaseContract.VitrinasTable.COL_INVENTARIO, vitrina.getVitrinaInventario());
        contentValues.put(DataBaseContract.VitrinasTable.COL_ANHO, vitrina.getVitrinaAnho());
        contentValues.put(DataBaseContract.VitrinasTable.COL_CONTRATO, vitrina.getVitrinaContrato());

        return db.insert(
                DataBaseContract.VitrinasTable.TABLE_NAME, null, contentValues);
    }

    public List<Vitrina> selectVitrinas() {
        //String con secuencia SQL para la búsqueda
        String sqlSelectVitrinas = "SELECT * FROM " + DataBaseContract.VitrinasTable.TABLE_NAME;
        return selectVitrinas(sqlSelectVitrinas);
    }

    public List<Vitrina> selectVitrinas(String sqlSelectVitrinas) {
        //Declaro lista de vitrinas seleccionados que se devolverán.
        List<Vitrina> vitrinasSelected = new ArrayList<>();
        //Creo instancia para leer de la base de datos
        SQLiteDatabase db = dataBase.getReadableDatabase();
        //Para realizar una consulta se utilza el médodo:
        //     rawQuery(string_con_consulta_select_parametrizada, lista_de_string_con_los_parámetros
        //en este caso concreto, la consulta es sencilla y al no utilizar parámetros pues el segundo
        //argumento del método (lista con parámetros) es null
        Cursor cursor = db.rawQuery(sqlSelectVitrinas, null);
        //Si el cursor almacena algún elemento, pues devuelve true con el método moveToFirst()
        if (cursor.moveToFirst()) {
            //Recupero el primer elemento del cursor y repito esta operación mientras que el cursor
            //devuelva true con el método moveToNext(), es decir, mientras tenga un siguiente elemento
            do {
                int id = cursor.getInt(0);
                int vitrinaIdEmpresa = cursor.getInt(1);
                int vitrinaIdFabricante = cursor.getInt(2);
                int vitrinaIdTipo = cursor.getInt(3);
                int vitrinaIdLongitud = cursor.getInt(4);
                String vitrinaReferencia = cursor.getString(5);
                String vitrinaInventario = cursor.getString(6);
                int vitrinaAnho = cursor.getInt(7);
                String vitrinaContrato = cursor.getString(8);
                //Construyo objeto Vitrina y lo añado a la lista
                Vitrina vitrina = new Vitrina(
                        id, vitrinaIdEmpresa, vitrinaIdFabricante, vitrinaIdTipo, vitrinaIdLongitud,
                        vitrinaReferencia, vitrinaInventario, vitrinaAnho, vitrinaContrato);
                vitrinasSelected.add(vitrina);
            } while (cursor.moveToNext());
        }
        //Cierro el cursor para liberar recursos
        cursor.close();
        return vitrinasSelected;
    }

    public Vitrina selectVitrinaWithId(int idToSelect) {
        Vitrina vitrina = null;
        //Creo instancia para leer de la base de datos
        SQLiteDatabase db = dataBase.getReadableDatabase();
        //String con la consulta SQL a realizar, faltaría el parámetro en la posición ? que intruduce
        //en la rawQuery como un array de string
        String sqlSelect = String.format("SELECT * FROM %s WHERE %s=?",
                DataBaseContract.VitrinasTable.TABLE_NAME,
                DataBaseContract.VitrinasTable._ID);
        //Creo array de strings con los parámetros para la consulta parametrizada anterior
        String[] selectArguments = {String.valueOf(idToSelect)};
        //Realizo al consulta almacenado el resultado en un cursor
        Cursor cursor = db.rawQuery(sqlSelect, selectArguments);
        //Si el cursor almacena algún elemento, pues devuelve true con el método moveToFirst()
        if (cursor.moveToFirst()) {
            //Recupero el primer elemento del cursor, que además el único que debe haber (no debe
            // haber duplicados) y la consulta es para recuperar el elemento de id indicado.
            int id = cursor.getInt(0);
            int vitrinaIdEmpresa = cursor.getInt(1);
            int vitrinaIdFabricante = cursor.getInt(2);
            int vitrinaIdTipo = cursor.getInt(3);
            int vitrinaIdLongitud = cursor.getInt(4);
            String vitrinaReferencia = cursor.getString(5);
            String vitrinaInventario = cursor.getString(6);
            int vitrinaAnho = cursor.getInt(7);
            String vitrinaContrato = cursor.getString(8);
            //Construyo objeto Vitrina y lo añado a la lista
            vitrina = new Vitrina(
                    id, vitrinaIdEmpresa, vitrinaIdFabricante, vitrinaIdTipo, vitrinaIdLongitud,
                    vitrinaReferencia, vitrinaInventario, vitrinaAnho, vitrinaContrato);
        }
        cursor.close();
        return vitrina;
    }

    public boolean updateVitrina(Vitrina vitrina) {
        //Obtengo instancia de la base de datos para escritura
        SQLiteDatabase db = dataBase.getWritableDatabase();
        //Creo objeto ContentValues con los valores del nuevo elemento
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseContract.VitrinasTable.COL_ID_EMPRESA, vitrina.getIdEmpresa());
        contentValues.put(DataBaseContract.VitrinasTable.COL_ID_FABRICANTE, vitrina.getIdFabricante());
        contentValues.put(DataBaseContract.VitrinasTable.COL_ID_TIPO, vitrina.getIdTipo());
        contentValues.put(DataBaseContract.VitrinasTable.COL_ID_LONGITUD, vitrina.getIdLongitud());
        contentValues.put(DataBaseContract.VitrinasTable.COL_REFERENCIA, vitrina.getVitrinaReferencia());
        contentValues.put(DataBaseContract.VitrinasTable.COL_INVENTARIO, vitrina.getVitrinaInventario());
        contentValues.put(DataBaseContract.VitrinasTable.COL_ANHO, vitrina.getVitrinaAnho());
        contentValues.put(DataBaseContract.VitrinasTable.COL_CONTRATO, vitrina.getVitrinaContrato());
        //Para actualizar un elemento se utiliza el médodo:
        //   update(nombre_tabla, valores, criterio_seleccion, parametros_criterio_seleccion)
        String criterioSeleccion = String.format("%s=?", DataBaseContract.VitrinasTable._ID);
        //array de string con los parámetros del criterio de selección
        String[] paramCriterioSeleccion = {String.valueOf(vitrina.getId())};
        //Realizo la actualización
        int updated = db.update(
                DataBaseContract.VitrinasTable.TABLE_NAME,
                contentValues,
                criterioSeleccion,
                paramCriterioSeleccion);
        return updated > 0; // true si se actualizó algo
    }

    public boolean deleteVitrina(int id) {
        //obtengo instancia de la base de datos para escritura
        SQLiteDatabase db = dataBase.getWritableDatabase();
        //Para borrar se utilizar el método:
        //  delete(nombre_tabla, criterio_seleccion, parametros_criterio_seleccion
        //Defino la string con el criterio de seleccion parametrizado.
        String criterioSeleccion = String.format("%s=?", DataBaseContract.VitrinasTable._ID);
        //array de string con los valores de los parámetros para completar la query parametrizada
        String[] paramCriterioSeleccion = {String.valueOf(id)};
        //ejecuto el borrado
        int deleted = db.delete(
                DataBaseContract.VitrinasTable.TABLE_NAME, criterioSeleccion, paramCriterioSeleccion);
        return deleted > 0;
    }

    public List<Integer> getIdsOfVitrinasForEmpresa(int idEmpresa) {
        List<Integer> listIds = new ArrayList<>();
        for (Vitrina vitrina : selectVitrinas()) {
            if (vitrina.getIdEmpresa() == idEmpresa) {
                listIds.add(vitrina.getId());
                break;
            }
        }
        return listIds;
    }

    public boolean existVitrina(Vitrina v) {
        boolean exist = false;
        for (Vitrina vitrina: selectVitrinas()) {
            if (vitrina.equals(v)) {
                exist = true;
                break;
            }
        }
        return exist;
    }


    /*
        CRUD sobre la entidad TipoVitrina
     */
    public long insertTipoVitrina(TipoVitrina tipoVitrina) {
        //Creo instancia para escribir en la base de datos
        SQLiteDatabase db = dataBase.getWritableDatabase();
        //Creo el objeto ContentValues para colocar los datos a insertar
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseContract.TiposVitrinasTable.COL_TIPO, tipoVitrina.getTipoVitrina());

        return db.insert(
                DataBaseContract.TiposVitrinasTable.TABLE_NAME, null, contentValues);
    }

    public List<TipoVitrina> selectTiposVitrinas() {
        //String con secuencia SQL para la búsqueda
        String sqlSelectTiposVitrinas = "SELECT * FROM " + DataBaseContract.TiposVitrinasTable.TABLE_NAME;
        return selectTiposVitrinas(sqlSelectTiposVitrinas);
    }

    public List<TipoVitrina> selectTiposVitrinas(String sqlSelectTiposVitrinas) {
        //Declaro lista de vitrinas seleccionados que se devolverán.
        List<TipoVitrina> tiposVitrinasSelected = new ArrayList<>();
        //Creo instancia para leer de la base de datos
        SQLiteDatabase db = dataBase.getReadableDatabase();
        //Para realizar una consulta se utilza el médodo:
        //     rawQuery(string_con_consulta_select_parametrizada, lista_de_string_con_los_parámetros
        //en este caso concreto, la consulta es sencilla y al no utilizar parámetros pues el segundo
        //argumento del método (lista con parámetros) es null
        Cursor cursor = db.rawQuery(sqlSelectTiposVitrinas, null);
        //Si el cursor almacena algún elemento, pues devuelve true con el método moveToFirst()
        if (cursor.moveToFirst()) {
            //Recupero el primer elemento del cursor y repito esta operación mientras que el cursor
            //devuelva true con el método moveToNext(), es decir, mientras tenga un siguiente elemento
            do {
                int id = cursor.getInt(0);
                String tipo = cursor.getString(1);
                //Construyo objeto TipoVitrina y lo añado a la lista
                TipoVitrina tipoVitrina = new TipoVitrina(id, tipo);
                tiposVitrinasSelected.add(tipoVitrina);
            } while (cursor.moveToNext());
        }
        //Cierro el cursor para liberar recursos
        cursor.close();
        return tiposVitrinasSelected;
    }

    public TipoVitrina selectTipoVitrinaWithId(int idToSelect) {
        TipoVitrina tipoVitrina = null;
        //Creo instancia para leer de la base de datos
        SQLiteDatabase db = dataBase.getReadableDatabase();
        //String con la consulta SQL a realizar, faltaría el parámetro en la posición ? que intruduce
        //en la rawQuery como un array de string
        String sqlSelect = String.format("SELECT * FROM %s WHERE %s=?",
                DataBaseContract.TiposVitrinasTable.TABLE_NAME,
                DataBaseContract.TiposVitrinasTable._ID);
        //Creo array de strings con los parámetros para la consulta parametrizada anterior
        String[] selectArguments = {String.valueOf(idToSelect)};
        //Realizo al consulta almacenado el resultado en un cursor
        Cursor cursor = db.rawQuery(sqlSelect, selectArguments);
        //Si el cursor almacena algún elemento, pues devuelve true con el método moveToFirst()
        if (cursor.moveToFirst()) {
            //Recupero el primer elemento del cursor, que además el único que debe haber (no debe
            // haber duplicados) y la consulta es para recuperar el elemento de id indicado.
            int id = cursor.getInt(0);
            String tipo = cursor.getString(1);
            //Construyo objeto Vitrina y lo añado a la lista
            tipoVitrina = new TipoVitrina(id, tipo);
        }
        cursor.close();
        return tipoVitrina;
    }

    public boolean updateTipoVitrina(TipoVitrina tipoVitrina) {
        //Obtengo instancia de la base de datos para escritura
        SQLiteDatabase db = dataBase.getWritableDatabase();
        //Creo objeto ContentValues con los valores del nuevo elemento
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseContract.TiposVitrinasTable.COL_TIPO, tipoVitrina.getTipoVitrina());
        //Para actualizar un elemento se utiliza el médodo:
        //   update(nombre_tabla, valores, criterio_seleccion, parametros_criterio_seleccion)
        String criterioSeleccion = String.format("%s=?", DataBaseContract.TiposVitrinasTable._ID);
        //array de string con los parámetros del criterio de selección
        String[] paramCriterioSeleccion = {String.valueOf(tipoVitrina.getId())};
        //Realizo la actualización
        int updated = db.update(
                DataBaseContract.TiposVitrinasTable.TABLE_NAME,
                contentValues,
                criterioSeleccion,
                paramCriterioSeleccion);

        return updated > 0; // true si se actualizó algo
    }

    public boolean deleteTipoVitrina(int id) {
        //obtengo instancia de la base de datos para escritura
        SQLiteDatabase db = dataBase.getWritableDatabase();
        //Para borrar se utilizar el método:
        //  delete(nombre_tabla, criterio_seleccion, parametros_criterio_seleccion
        //Defino la string con el criterio de seleccion parametrizado.
        String criterioSeleccion = String.format("%s=?", DataBaseContract.TiposVitrinasTable._ID);
        //array de string con los valores de los parámetros para completar la query parametrizada
        String[] paramCriterioSeleccion = {String.valueOf(id)};
        //ejecuto el borrado
        int deleted = db.delete(
                DataBaseContract.TiposVitrinasTable.TABLE_NAME, criterioSeleccion, paramCriterioSeleccion);
        return deleted > 0;
    }

    public List<Integer> getIdsOfTiposVitrinasForEmpresa(int idEmpresa) {
        List<Integer> listIdsTiposVitrinas = new ArrayList<>();
        for (int idVitrina: getIdsOfVitrinasForEmpresa(idEmpresa)) {
            listIdsTiposVitrinas.add(selectVitrinaWithId(idVitrina).getIdTipo());
        }
        return listIdsTiposVitrinas;
    }

    public int getIdOfVitrinaTipo(String tipo) {
        int idTipoVitrina = -1;
        for (TipoVitrina tipoVitrina: selectTiposVitrinas()) {
            if (tipoVitrina.getTipoVitrina().equals(tipo)) {
                idTipoVitrina = tipoVitrina.getId();
                break;
            }
        }
        return idTipoVitrina;
    }


    /*
        CRUD sobre la entidad Fabricante
     */
    public long insertFabricante(Fabricante fabricante) {
        //Creo instancia para escribir en la base de datos
        SQLiteDatabase db = dataBase.getWritableDatabase();
        //Creo el objeto ContentValues para colocar los datos a insertar
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseContract.FabricantesTable.COL_NAME, fabricante.getNombre());

        return db.insert(
                DataBaseContract.FabricantesTable.TABLE_NAME, null, contentValues);
    }

    public List<Fabricante> selectFabricantes() {
        //String con secuencia SQL para la búsqueda
        String sqlSelectFabricantes = "SELECT * FROM " + DataBaseContract.FabricantesTable.TABLE_NAME;
        return selectFabricantes(sqlSelectFabricantes);
    }

    public List<Fabricante> selectFabricantes(String sqlSelectFabricantes) {
        //Declaro lista de vitrinas seleccionados que se devolverán.
        List<Fabricante> fabricantesSelected = new ArrayList<>();
        //Creo instancia para leer de la base de datos
        SQLiteDatabase db = dataBase.getReadableDatabase();
        //Para realizar una consulta se utilza el médodo:
        //     rawQuery(string_con_consulta_select_parametrizada, lista_de_string_con_los_parámetros
        //en este caso concreto, la consulta es sencilla y al no utilizar parámetros pues el segundo
        //argumento del método (lista con parámetros) es null
        Cursor cursor = db.rawQuery(sqlSelectFabricantes, null);
        //Si el cursor almacena algún elemento, pues devuelve true con el método moveToFirst()
        if (cursor.moveToFirst()) {
            //Recupero el primer elemento del cursor y repito esta operación mientras que el cursor
            //devuelva true con el método moveToNext(), es decir, mientras tenga un siguiente elemento
            do {
                int id = cursor.getInt(0);
                String nombre = cursor.getString(1);
                //Construyo objeto Fabricante y lo añado a la lista
                Fabricante fabricante = new Fabricante(id, nombre);
                fabricantesSelected.add(fabricante);
            } while (cursor.moveToNext());
        }
        //Cierro el cursor para liberar recursos
        cursor.close();
        return fabricantesSelected;
    }

    public Fabricante selectFabricanteWithId(int idToSelect) {
        Fabricante fabricante = null;
        //Creo instancia para leer de la base de datos
        SQLiteDatabase db = dataBase.getReadableDatabase();
        //String con la consulta SQL a realizar, faltaría el parámetro en la posición ? que intruduce
        //en la rawQuery como un array de string
        String sqlSelect = String.format("SELECT * FROM %s WHERE %s=?",
                DataBaseContract.FabricantesTable.TABLE_NAME,
                DataBaseContract.FabricantesTable._ID);
        //Creo array de strings con los parámetros para la consulta parametrizada anterior
        String[] selectArguments = {String.valueOf(idToSelect)};
        //Realizo al consulta almacenado el resultado en un cursor
        Cursor cursor = db.rawQuery(sqlSelect, selectArguments);
        //Si el cursor almacena algún elemento, pues devuelve true con el método moveToFirst()
        if (cursor.moveToFirst()) {
            //Recupero el primer elemento del cursor, que además el único que debe haber (no debe
            // haber duplicados) y la consulta es para recuperar el elemento de id indicado.
            int id = cursor.getInt(0);
            String nombre = cursor.getString(1);
            //Construyo objeto Vitrina y lo añado a la lista
            fabricante = new Fabricante(id, nombre);
        }
        cursor.close();
        return fabricante;
    }

    public boolean updateFabricante(Fabricante fabricante) {
        //Obtengo instancia de la base de datos para escritura
        SQLiteDatabase db = dataBase.getWritableDatabase();
        //Creo objeto ContentValues con los valores del nuevo elemento
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseContract.FabricantesTable.COL_NAME, fabricante.getNombre());
        //Para actualizar un elemento se utiliza el médodo:
        //   update(nombre_tabla, valores, criterio_seleccion, parametros_criterio_seleccion)
        String criterioSeleccion = String.format("%s=?", DataBaseContract.FabricantesTable._ID);
        //array de string con los parámetros del criterio de selección
        String[] paramCriterioSeleccion = {String.valueOf(fabricante.getId())};
        //Realizo la actualización
        int updated = db.update(
                DataBaseContract.FabricantesTable.TABLE_NAME,
                contentValues,
                criterioSeleccion,
                paramCriterioSeleccion);
        return updated > 0; // true si se actualizó algo
    }

    public boolean deleteFabricante(int id) {
        //obtengo instancia de la base de datos para escritura
        SQLiteDatabase db = dataBase.getWritableDatabase();
        //Para borrar se utilizar el método:
        //  delete(nombre_tabla, criterio_seleccion, parametros_criterio_seleccion
        //Defino la string con el criterio de seleccion parametrizado.
        String criterioSeleccion = String.format("%s=?", DataBaseContract.FabricantesTable._ID);
        //array de string con los valores de los parámetros para completar la query parametrizada
        String[] paramCriterioSeleccion = {String.valueOf(id)};
        //ejecuto el borrado
        int deleted = db.delete(
                DataBaseContract.FabricantesTable.TABLE_NAME, criterioSeleccion, paramCriterioSeleccion);
        return deleted > 0;
    }

    public String getNameFabricanteWithIdFabricante(int idFabricante) {
        return selectFabricanteWithId(idFabricante).getNombre();
    }

    public int getIdOfFabricanteWithName(String nameFabricante) {
        int id = -1;
        for (Fabricante fabricante: selectFabricantes()) {
            if (fabricante.getNombre().trim().equalsIgnoreCase(nameFabricante.trim())) {
                id = fabricante.getId();
                break;
            }
        }
        return id;
    }

    public boolean existFabricante(Fabricante fabricante) {
        boolean exist = false;
        for (Fabricante fab: selectFabricantes()) {
            if (fab.equals(fabricante)) {
                exist = true;
                break;
            }
        }
        return exist;
    }


    /*
        CRUD sobre la entidad Longitud
     */
    public long insertLongitud(Longitud longitud) {
        //Creo instancia para escribir en la base de datos
        SQLiteDatabase db = dataBase.getWritableDatabase();
        //Creo el objeto ContentValues para colocar los datos a insertar
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseContract.LongitudesTable.COL_LONGITUD, longitud.getLogintudGillotina());
        contentValues.put(DataBaseContract.LongitudesTable.COL_GUILLOTINA, longitud.getLogintudGillotina());

        return db.insert(
                DataBaseContract.LongitudesTable.TABLE_NAME, null, contentValues);
    }

    public List<Longitud> selectLongitudes() {
        //String con secuencia SQL para la búsqueda
        String sqlSelectLongitudes = "SELECT * FROM " + DataBaseContract.LongitudesTable.TABLE_NAME;
        return selectLongitudes(sqlSelectLongitudes);
    }

    public List<Longitud> selectLongitudes(String sqlSelectLongitudes) {
        //Declaro lista de vitrinas seleccionados que se devolverán.
        List<Longitud> longitudesSelected = new ArrayList<>();
        //Creo instancia para leer de la base de datos
        SQLiteDatabase db = dataBase.getReadableDatabase();
        //Para realizar una consulta se utilza el médodo:
        //     rawQuery(string_con_consulta_select_parametrizada, lista_de_string_con_los_parámetros
        //en este caso concreto, la consulta es sencilla y al no utilizar parámetros pues el segundo
        //argumento del método (lista con parámetros) es null
        Cursor cursor = db.rawQuery(sqlSelectLongitudes, null);
        //Si el cursor almacena algún elemento, pues devuelve true con el método moveToFirst()
        if (cursor.moveToFirst()) {
            //Recupero el primer elemento del cursor y repito esta operación mientras que el cursor
            //devuelva true con el método moveToNext(), es decir, mientras tenga un siguiente elemento
            do {
                int id = cursor.getInt(0);
                int longitudVitrina = cursor.getInt(1);
                float longitudGillotina = cursor.getFloat(2);
                //Construyo objeto Longitud y lo añado a la lista
                Longitud longitud = new Longitud(id, longitudVitrina, longitudGillotina);
                longitudesSelected.add(longitud);
            } while (cursor.moveToNext());
        }
        //Cierro el cursor para liberar recursos
        cursor.close();
        return longitudesSelected;
    }

    public Longitud selectLongitudWithId(int idToSelect) {
        Longitud longitud = null;
        //Creo instancia para leer de la base de datos
        SQLiteDatabase db = dataBase.getReadableDatabase();
        //String con la consulta SQL a realizar, faltaría el parámetro en la posición ? que intruduce
        //en la rawQuery como un array de string
        String sqlSelect = String.format("SELECT * FROM %s WHERE %s=?",
                DataBaseContract.LongitudesTable.TABLE_NAME,
                DataBaseContract.LongitudesTable._ID);
        //Creo array de strings con los parámetros para la consulta parametrizada anterior
        String[] selectArguments = {String.valueOf(idToSelect)};
        //Realizo al consulta almacenado el resultado en un cursor
        Cursor cursor = db.rawQuery(sqlSelect, selectArguments);
        //Si el cursor almacena algún elemento, pues devuelve true con el método moveToFirst()
        if (cursor.moveToFirst()) {
            //Recupero el primer elemento del cursor, que además el único que debe haber (no debe
            // haber duplicados) y la consulta es para recuperar el elemento de id indicado.
            int id = cursor.getInt(0);
            int longitudVitrina = cursor.getInt(1);
            float longitudGuillotina = cursor.getFloat(2);
            //Construyo objeto Longitud y lo añado a la lista
            longitud = new Longitud(id, longitudVitrina, longitudGuillotina);
        }
        cursor.close();
        return longitud;
    }

    public boolean updateLongitud(Longitud longitud) {
        //Obtengo instancia de la base de datos para escritura
        SQLiteDatabase db = dataBase.getWritableDatabase();
        //Creo objeto ContentValues con los valores del nuevo elemento
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseContract.LongitudesTable.COL_LONGITUD, longitud.getLongitudVitrina());
        contentValues.put(DataBaseContract.LongitudesTable.COL_LONGITUD, longitud.getLogintudGillotina());
        //Para actualizar un elemento se utiliza el médodo:
        //   update(nombre_tabla, valores, criterio_seleccion, parametros_criterio_seleccion)
        String criterioSeleccion = String.format("%s=?", DataBaseContract.LongitudesTable._ID);
        //array de string con los parámetros del criterio de selección
        String[] paramCriterioSeleccion = {String.valueOf(longitud.getId())};
        //Realizo la actualización
        int updated = db.update(
                DataBaseContract.LongitudesTable.TABLE_NAME,
                contentValues,
                criterioSeleccion,
                paramCriterioSeleccion);
        return updated > 0; // true si se actualizó algo
    }

    public boolean deleteLongitud(int id) {
        //obtengo instancia de la base de datos para escritura
        SQLiteDatabase db = dataBase.getWritableDatabase();
        //Para borrar se utilizar el método:
        //  delete(nombre_tabla, criterio_seleccion, parametros_criterio_seleccion
        //Defino la string con el criterio de seleccion parametrizado.
        String criterioSeleccion = String.format("%s=?", DataBaseContract.LongitudesTable._ID);
        //array de string con los valores de los parámetros para completar la query parametrizada
        String[] paramCriterioSeleccion = {String.valueOf(id)};
        //ejecuto el borrado
        int deleted = db.delete(
                DataBaseContract.LongitudesTable.TABLE_NAME, criterioSeleccion, paramCriterioSeleccion);
        return deleted > 0;
    }

    public int getIdOfLongitudWithName(int longitudVitrina) {
        int id = -1;
        for (Longitud longitud: selectLongitudes()) {
            if (longitud.getLongitudVitrina() == longitudVitrina) {
                id = longitud.getId();
                break;
            }
        }
        return id;
    }

    public int getIdLongitudForLongVitrina(int longitudVitrina) {
        int id = -1;
        for (Longitud longitud: selectLongitudes()) {
            if (longitud.getLongitudVitrina() == longitudVitrina) {
                id = longitud.getId();
                break;
            }
        }
        return id;
    }

    public int getLongitudVitrinaWithIdLongitud(int idLongitud) {
        return selectLongitudWithId(idLongitud).getLongitudVitrina();
    }

    public float getGuillotinaWithIdLongitud(int idLongitud) {
       return selectLongitudWithId(idLongitud).getLogintudGillotina();
    }

    public float getGuillotinaForLongVitrina(int longitudVitrina) {
        float guillotina = 0F;
        for (Longitud longitud: selectLongitudes()) {
            if (longitud.getLongitudVitrina() == longitudVitrina) {
                guillotina = longitud.getLogintudGillotina();
                break;
            }
        }
        return guillotina;
    }

    /*
        CRUD sobre la entidad TipoVitrinaLongitud
     */
    public long insertTipoLongFlow(TipoLongFlow tipoLongFlow) {
        //Creo instancia para escribir en la base de datos
        SQLiteDatabase db = dataBase.getWritableDatabase();
        //Creo el objeto ContentValues para colocar los datos a insertar
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseContract.TiposLongsFlowsTable.COL_ID_TIPO, tipoLongFlow.getIdTipoVitrina());
        contentValues.put(DataBaseContract.TiposLongsFlowsTable.COL_ID_LONGITUD, tipoLongFlow.getIdLongintud());
        contentValues.put(DataBaseContract.TiposLongsFlowsTable.COL_FLOW_MIN, tipoLongFlow.getFlowMin());
        contentValues.put(DataBaseContract.TiposLongsFlowsTable.COL_FLOW_RECOM, tipoLongFlow.getFlowRecom());
        contentValues.put(DataBaseContract.TiposLongsFlowsTable.COL_FLOW_MAX, tipoLongFlow.getFlowMax());
        contentValues.put(DataBaseContract.TiposLongsFlowsTable.COL_PRESS_DROP, tipoLongFlow.getPressDrop());

        return db.insert(
                DataBaseContract.TiposLongsFlowsTable.TABLE_NAME, null, contentValues);
    }

    public List<TipoLongFlow> selectTipoLongFlow() {
        //String con secuencia SQL para la búsqueda
        String sqlSelectTipoLongFlow = "SELECT * FROM " + DataBaseContract.LongitudesTable.TABLE_NAME;
        return this.selectTipoLongFlow(sqlSelectTipoLongFlow);
    }

    public List<TipoLongFlow> selectTipoLongFlow(String sqlSelectTipoLongFlow) {
        //Declaro lista de tipos de vitrinas y longitud con flows seleccionados que se devolverán.
        List<TipoLongFlow> tiposLongsFlowsSelected = new ArrayList<>();
        //Creo instancia para leer de la base de datos
        SQLiteDatabase db = dataBase.getReadableDatabase();
        //Para realizar una consulta se utilza el médodo:
        //     rawQuery(string_con_consulta_select_parametrizada, lista_de_string_con_los_parámetros
        //en este caso concreto, la consulta es sencilla y al no utilizar parámetros pues el segundo
        //argumento del método (lista con parámetros) es null
        Cursor cursor = db.rawQuery(sqlSelectTipoLongFlow, null);
        //Si el cursor almacena algún elemento, pues devuelve true con el método moveToFirst()
        if (cursor.moveToFirst()) {
            //Recupero el primer elemento del cursor y repito esta operación mientras que el cursor
            //devuelva true con el método moveToNext(), es decir, mientras tenga un siguiente elemento
            do {
                int id = cursor.getInt(0);
                int idTipoVitrina = cursor.getInt(1);
                int idLongitud = cursor.getInt(2);
                int flowMin = cursor.getInt(3);
                int flowRecom = cursor.getInt(4);
                int flowMax = cursor.getInt(5);
                int pressDrop = cursor.getInt(6);
                //Construyo objeto TipoLongFlow y lo añado a la lista
                TipoLongFlow tipoVitrinaLongitud = new TipoLongFlow(
                        id, idTipoVitrina, idLongitud, flowMin, flowRecom, flowMax, pressDrop);
                tiposLongsFlowsSelected.add(tipoVitrinaLongitud);
            } while (cursor.moveToNext());
        }
        //Cierro el cursor para liberar recursos
        cursor.close();
        return tiposLongsFlowsSelected;
    }

    public TipoLongFlow selectTipoLongFlowWithId(int idToSelect) {
        TipoLongFlow tipoLongFlow = null;
        //Creo instancia para leer de la base de datos
        SQLiteDatabase db = dataBase.getReadableDatabase();
        //String con la consulta SQL a realizar, faltaría el parámetro en la posición ? que intruduce
        //en la rawQuery como un array de string
        String sqlSelect = String.format("SELECT * FROM %s WHERE %s=?",
                DataBaseContract.TiposLongsFlowsTable.TABLE_NAME,
                DataBaseContract.TiposLongsFlowsTable._ID);
        //Creo array de strings con los parámetros para la consulta parametrizada anterior
        String[] selectArguments = {String.valueOf(idToSelect)};
        //Realizo al consulta almacenado el resultado en un cursor
        Cursor cursor = db.rawQuery(sqlSelect, selectArguments);
        //Si el cursor almacena algún elemento, pues devuelve true con el método moveToFirst()
        if (cursor.moveToFirst()) {
            //Recupero el primer elemento del cursor, que además el único que debe haber (no debe
            // haber duplicados) y la consulta es para recuperar el elemento de id indicado.
            int id = cursor.getInt(0);
            int idTipoVitrina = cursor.getInt(1);
            int idLongitud = cursor.getInt(2);
            int flowMin = cursor.getInt(3);
            int flowRecom = cursor.getInt(4);
            int flowMax = cursor.getInt(5);
            int pressDrop = cursor.getInt(6);
            //Construyo objeto Longitud y lo añado a la lista
            tipoLongFlow = new TipoLongFlow(
                    id, idTipoVitrina, idLongitud, flowMin, flowRecom, flowMax, pressDrop);
        }
        cursor.close();
        return tipoLongFlow;
    }

    public boolean updateTipoLongFlow(TipoLongFlow tipoLongFlow) {
        //Obtengo instancia de la base de datos para escritura
        SQLiteDatabase db = dataBase.getWritableDatabase();
        //Creo objeto ContentValues con los valores del nuevo elemento
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseContract.TiposLongsFlowsTable.COL_ID_TIPO, tipoLongFlow.getIdTipoVitrina());
        contentValues.put(DataBaseContract.TiposLongsFlowsTable.COL_ID_LONGITUD, tipoLongFlow.getIdLongintud());
        contentValues.put(DataBaseContract.TiposLongsFlowsTable.COL_FLOW_MIN, tipoLongFlow.getFlowMin());
        contentValues.put(DataBaseContract.TiposLongsFlowsTable.COL_FLOW_RECOM, tipoLongFlow.getFlowRecom());
        contentValues.put(DataBaseContract.TiposLongsFlowsTable.COL_FLOW_MAX, tipoLongFlow.getFlowMax());
        contentValues.put(DataBaseContract.TiposLongsFlowsTable.COL_PRESS_DROP, tipoLongFlow.getPressDrop());
        //Para actualizar un elemento se utiliza el médodo:
        //   update(nombre_tabla, valores, criterio_seleccion, parametros_criterio_seleccion)
        String criterioSeleccion = String.format("%s=?", DataBaseContract.TiposLongsFlowsTable._ID);
        //array de string con los parámetros del criterio de selección
        String[] paramCriterioSeleccion = {String.valueOf(tipoLongFlow.getId())};
        //Realizo la actualización
        int updated = db.update(
                DataBaseContract.TiposLongsFlowsTable.TABLE_NAME,
                contentValues,
                criterioSeleccion,
                paramCriterioSeleccion);
        return updated > 0; // true si se actualizó algo
    }

    public boolean deleteTipoLongFlow(int id) {
        //obtengo instancia de la base de datos para escritura
        SQLiteDatabase db = dataBase.getWritableDatabase();
        //Para borrar se utilizar el método:
        //  delete(nombre_tabla, criterio_seleccion, parametros_criterio_seleccion
        //Defino la string con el criterio de seleccion parametrizado.
        String criterioSeleccion = String.format("%s=?", DataBaseContract.TiposLongsFlowsTable._ID);
        //array de string con los valores de los parámetros para completar la query parametrizada
        String[] paramCriterioSeleccion = {String.valueOf(id)};
        //ejecuto el borrado
        int deleted = db.delete(
                DataBaseContract.TiposLongsFlowsTable.TABLE_NAME, criterioSeleccion, paramCriterioSeleccion);
        return deleted > 0;
    }


    /*
        CRUD sobre la entidad Cualitativo
     */
    public long insertCualitativo(Cualitativo cualitativo) {
        //Creo instancia para escribir en la base de datos
        SQLiteDatabase db = dataBase.getWritableDatabase();
        //Creo el objeto ContentValues para colocar los datos a insertar
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseContract.CualitativosTable.COL_CUALITATIVO, cualitativo.getCualitativo());

        return db.insert(
                DataBaseContract.CualitativosTable.TABLE_NAME, null, contentValues);
    }

    public List<Cualitativo> selectCualitativos() {
        //String con secuencia SQL para la búsqueda
        String sqlSelectCualitativos = "SELECT * FROM " + DataBaseContract.CualitativosTable.TABLE_NAME;
        return selectCualitativos(sqlSelectCualitativos);
    }

    public List<Cualitativo> selectCualitativos(String sqlSelectCualitativos) {
        //Declaro lista de Cualitativos seleccionados que se devolverán.
        List<Cualitativo> cualitativosSelected = new ArrayList<>();
        //Creo instancia para leer de la base de datos
        SQLiteDatabase db = dataBase.getReadableDatabase();
        //Para realizar una consulta se utilza el médodo:
        //     rawQuery(string_con_consulta_select_parametrizada, lista_de_string_con_los_parámetros
        //en este caso concreto, la consulta es sencilla y al no utilizar parámetros pues el segundo
        //argumento del método (lista con parámetros) es null
        Cursor cursor = db.rawQuery(sqlSelectCualitativos, null);
        //Si el cursor almacena algún elemento, pues devuelve true con el método moveToFirst()
        if (cursor.moveToFirst()) {
            //Recupero el primer elemento del cursor y repito esta operación mientras que el cursor
            //devuelva true con el método moveToNext(), es decir, mientras tenga un siguiente elemento
            do {
                int id = cursor.getInt(0);
                String evaluCualitativo = cursor.getString(1);
                //Construyo objeto Cualitativo y lo añado a la lista
                Cualitativo cualitativo = new Cualitativo(id, evaluCualitativo);
                cualitativosSelected.add(cualitativo);
            } while (cursor.moveToNext());
        }
        //Cierro el cursor para liberar recursos
        cursor.close();
        return cualitativosSelected;
    }

    public Cualitativo selectCualitativoWithId(int idToSelect) {
        Cualitativo cualitativo = null;
        //Creo instancia para leer de la base de datos
        SQLiteDatabase db = dataBase.getReadableDatabase();
        //String con la consulta SQL a realizar, faltaría el parámetro en la posición ? que intruduce
        //en la rawQuery como un array de string
        String sqlSelect = String.format("SELECT * FROM %s WHERE %s=?",
                DataBaseContract.CualitativosTable.TABLE_NAME,
                DataBaseContract.CualitativosTable._ID);
        //Creo array de strings con los parámetros para la consulta parametrizada anterior
        String[] selectArguments = {String.valueOf(idToSelect)};
        //Realizo al consulta almacenado el resultado en un cursor
        Cursor cursor = db.rawQuery(sqlSelect, selectArguments);
        //Si el cursor almacena algún elemento, pues devuelve true con el método moveToFirst()
        if (cursor.moveToFirst()) {
            //Recupero el primer elemento del cursor, que además el único que debe haber (no debe
            // haber duplicados) y la consulta es para recuperar el elemento de id indicado.
            int id = cursor.getInt(0);
            String evalCualitativo = cursor.getString(1);
            //Construyo objeto Cualitativo y lo añado a la lista
            cualitativo = new Cualitativo(id, evalCualitativo);
        }
        cursor.close();
        return cualitativo;
    }

    public boolean updateCualitativo(Cualitativo cualitativo) {
        //Obtengo instancia de la base de datos para escritura
        SQLiteDatabase db = dataBase.getWritableDatabase();
        //Creo objeto ContentValues con los valores del nuevo elemento
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseContract.CualitativosTable.COL_CUALITATIVO, cualitativo.getCualitativo());
        //Para actualizar un elemento se utiliza el médodo:
        //   update(nombre_tabla, valores, criterio_seleccion, parametros_criterio_seleccion)
        String criterioSeleccion = String.format("%s=?", DataBaseContract.CualitativosTable._ID);
        //array de string con los parámetros del criterio de selección
        String[] paramCriterioSeleccion = {String.valueOf(cualitativo.getId())};
        //Realizo la actualización
        int updated = db.update(
                DataBaseContract.CualitativosTable.TABLE_NAME,
                contentValues,
                criterioSeleccion,
                paramCriterioSeleccion);
        return updated > 0; // true si se actualizó algo
    }

    public boolean deleteCualitativo(int id) {
        //obtengo instancia de la base de datos para escritura
        SQLiteDatabase db = dataBase.getWritableDatabase();
        //Para borrar se utilizar el método:
        //  delete(nombre_tabla, criterio_seleccion, parametros_criterio_seleccion
        //Defino la string con el criterio de seleccion parametrizado.
        String criterioSeleccion = String.format("%s=?", DataBaseContract.LongitudesTable._ID);
        //array de string con los valores de los parámetros para completar la query parametrizada
        String[] paramCriterioSeleccion = {String.valueOf(id)};
        //ejecuto el borrado
        int deleted = db.delete(
                DataBaseContract.CualitativosTable.TABLE_NAME, criterioSeleccion, paramCriterioSeleccion);
        return deleted > 0;
    }

    public int getIdOfCualitativoWithEvalu(String evaluCualitativo) {
        int id = -1;
        for (Cualitativo cualitativo: selectCualitativos()) {
            if (cualitativo.getCualitativo().equals(evaluCualitativo)) {
                id = cualitativo.getId();
                break;
            }
        }
        return id;
    }

    public String getCualitativoWithId(int idCualitativo) {
        String evaluCualitativo = "";
        for (Cualitativo cualitativo: selectCualitativos()) {
            if (cualitativo.getId() == idCualitativo) {
                evaluCualitativo = cualitativo.getCualitativo();
                break;
            }
        }
        return evaluCualitativo;
    }


    /*
        CRUD sobre la entidad Mantenimiento
     */
    public long insertMantenimiento(Mantenimiento mantenimiento) {
        //Creo instancia para escribir en la base de datos
        SQLiteDatabase db = dataBase.getWritableDatabase();
        //Creo el objeto ContentValues para colocar los datos a insertar
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseContract.MantenimientosTable.COL_FECHA, mantenimiento.getFecha());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_ID_VITRINA, mantenimiento.getIdVitrina());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_PUESTA_MARCHA, mantenimiento.isPuestaMarcha());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_ID_TECNICO, mantenimiento.getIdTecnico());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_SEGUN_DIN, mantenimiento.isSegunDin());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_SEGUN_EN, mantenimiento.isSegunEn());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_FUN_CTRL_DIGI, mantenimiento.getFunCtrlDigi());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_VIS_SIST_EXTR, mantenimiento.getVisSistExtr());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_PROT_SUPERF, mantenimiento.getProtSuperf());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_JUNTAS, mantenimiento.getJuntas());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_FIJACION, mantenimiento.getFijacion());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_FUNC_GUILLO, mantenimiento.getFijacion());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_ESTADO_GUILLO, mantenimiento.getFijacion());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_VAL_FUERZA_GUILLO, mantenimiento.getValFuerzaGuillo());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_FUERZA_GILLO, mantenimiento.getFuerzaGuillo());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_CTRL_PRESENCIA, mantenimiento.getCtrlPresencia());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_AUTOPROTECCION, mantenimiento.getAutoproteccion());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_GRIFOS_MONORED, mantenimiento.getGrifosMonored());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_ID_MEDICION, mantenimiento.getIdMedicion());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_EVALU_VOL_EXTRAC, mantenimiento.getEvaluVolExtrac());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_ACORDE_NORMAS_SI, mantenimiento.isAcordeNormasReguSi());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_ACORDE_NORMAS_NO, mantenimiento.isAcordeNormasReguNo());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_NECESARIO_REPA_SI, mantenimiento.isNecesarioRepaSi());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_NECESARIO_REPA_NO, mantenimiento.isNecesarioRepaNo());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_COMENTARIO, mantenimiento.getComentario());

        return db.insert(
                DataBaseContract.MantenimientosTable.TABLE_NAME, null, contentValues);
    }

    public List<Mantenimiento> selectMantenimientos() {
        //String con secuencia SQL para la búsqueda
        String sqlSelectMantenimientos = "SELECT * FROM " + DataBaseContract.MantenimientosTable.TABLE_NAME;
        return selectMantenimientos(sqlSelectMantenimientos);
    }

    public List<Mantenimiento> selectMantenimientos(String sqlSelectMantenimientos) {
        //Declaro lista de mantenimientos seleccionados que se devolverán.
        List<Mantenimiento> mantenimientoSelected = new ArrayList<>();
        //Creo instancia para leer de la base de datos
        SQLiteDatabase db = dataBase.getReadableDatabase();
        //Para realizar una consulta se utiliza el médodo:
        //     rawQuery(string_con_consulta_select_parametrizada, lista_de_string_con_los_parámetros
        //en este caso concreto, la consulta es sencilla y al no utilizar parámetros pues el segundo
        //argumento del método (lista con parámetros) es null
        Cursor cursor = db.rawQuery(sqlSelectMantenimientos, null);
        //Si el cursor almacena algún elemento, pues devuelve true con el método moveToFirst()
        if (cursor.moveToFirst()) {
            //Recupero el primer elemento del cursor y repito esta operación mientras que el cursor
            //devuelva true con el método moveToNext(), es decir, mientras tenga un siguiente elemento
            do {
                int id = cursor.getInt(0);
                String fecha = cursor.getString(1);
                int idVitrina = cursor.getInt(2);
                boolean puestaMarcha = cursor.getInt(3) > 0;
                int idTecnico = cursor.getInt(4);
                boolean segunDin = cursor.getInt(5) > 0;
                boolean segunEn = cursor.getInt(6) > 0;
                int funCtrlDigi = cursor.getInt(7);
                int visSistExtr = cursor.getInt(8);
                int protSuperf = cursor.getInt(9);
                int juntas = cursor.getInt(10);
                int fijacion = cursor.getInt(11);
                int funcGuillo = cursor.getInt(12);
                int estadoGuillo = cursor.getInt(13);
                float valFuerzaGuillo = cursor.getFloat(14);
                int fuerzaGuillo = cursor.getInt(15);
                int ctrlPresencia = cursor.getInt(16);
                int autoproteccion = cursor.getInt(17);
                int grifosMonored = cursor.getInt(18);
                int idMedicion = cursor.getInt(19);
                int evaluVolExtrac = cursor.getInt(20);
                boolean isAcordeNormasReguSi = cursor.getInt(21) > 0;
                boolean isAcordeNormasReguNo = cursor.getInt(22) > 0;
                boolean isNecesarioRepaSi = cursor.getInt(23) > 0;
                boolean isNecesarioRepaNo = cursor.getInt(24) > 0;
                String comentario = cursor.getString(25);
                //Construyo objeto Mantenimiento y lo añado a la lista
                Mantenimiento mantenimiento = new Mantenimiento(
                        id, fecha, idVitrina, puestaMarcha, idTecnico, segunDin, segunEn, funCtrlDigi,
                        visSistExtr, protSuperf, juntas, fijacion, funcGuillo, estadoGuillo,
                        valFuerzaGuillo, fuerzaGuillo, ctrlPresencia, autoproteccion, grifosMonored,
                        idMedicion, evaluVolExtrac, isAcordeNormasReguSi, isAcordeNormasReguNo, isNecesarioRepaSi,
                        isNecesarioRepaNo, comentario);
                mantenimientoSelected.add(mantenimiento);
            } while (cursor.moveToNext());
        }
        //Cierro el cursor para liberar recursos
        cursor.close();
        return mantenimientoSelected;
    }

    public Mantenimiento selectMantenimientoWithId(int idToSelect) {
        Mantenimiento mantenimiento = null;
        //Creo instancia para leer de la base de datos
        SQLiteDatabase db = dataBase.getReadableDatabase();
        //String con la consulta SQL a realizar, faltaría el parámetro en la posición ? que intruduce
        //en la rawQuery como un array de string
        String sqlSelect = String.format("SELECT * FROM %s WHERE %s=?",
                DataBaseContract.MantenimientosTable.TABLE_NAME,
                DataBaseContract.MantenimientosTable._ID);
        //Creo array de strings con los parámetros para la consulta parametrizada anterior
        String[] selectArguments = {String.valueOf(idToSelect)};
        //Realizo al consulta almacenado el resultado en un cursor
        Cursor cursor = db.rawQuery(sqlSelect, selectArguments);
        //Si el cursor almacena algún elemento, pues devuelve true con el método moveToFirst()
        if (cursor.moveToFirst()) {
            //Recupero el primer elemento del cursor, que además el único que debe haber (no debe
            // haber duplicados) y la consulta es para recuperar el elemento de id indicado.
            int id = cursor.getInt(0);
            String fecha = cursor.getString(1);
            int idVitrina = cursor.getInt(2);
            boolean puestaMarcha = cursor.getInt(3) > 0;
            int idTecnico = cursor.getInt(4);
            boolean segunDin = cursor.getInt(5) > 0;
            boolean segunEn = cursor.getInt(6) > 0;
            int funCtrlDigi = cursor.getInt(7);
            int visSistExtr = cursor.getInt(8);
            int protSuperf = cursor.getInt(9);
            int juntas = cursor.getInt(10);
            int fijacion = cursor.getInt(11);
            int funcGuillo = cursor.getInt(12);
            int estadoGuillo = cursor.getInt(13);
            float valFuerzaGuillo = cursor.getFloat(14);
            int fuerzaGuillo = cursor.getInt(15);
            int ctrlPresencia = cursor.getInt(16);
            int autoproteccion = cursor.getInt(17);
            int grifosMonored = cursor.getInt(18);
            int idMedicion = cursor.getInt(19);
            int evaluVolExtrac = cursor.getInt(20);
            boolean isAcordeNormasReguSi = cursor.getInt(21) > 0;
            boolean isAcordeNormasReguNo = cursor.getInt(22) > 0;
            boolean isNecesarioRepaSi = cursor.getInt(23) > 0;
            boolean isNecesarioRepaNo = cursor.getInt(24) > 0;
            String comentario = cursor.getString(25);
            //Construyo objeto Mantenimiento para devolverlo
            mantenimiento = new Mantenimiento(
                    id, fecha, idVitrina, puestaMarcha, idTecnico, segunDin, segunEn, funCtrlDigi,
                    visSistExtr, protSuperf, juntas, fijacion, funcGuillo, estadoGuillo,
                    valFuerzaGuillo, fuerzaGuillo, ctrlPresencia, autoproteccion, grifosMonored,
                    idMedicion, evaluVolExtrac, isAcordeNormasReguSi, isAcordeNormasReguNo, isNecesarioRepaSi,
                    isNecesarioRepaNo, comentario);
        }
        cursor.close();
        return mantenimiento;
    }

    public boolean updateMantenimiento(Mantenimiento mantenimiento) {
        //Obtengo instancia de la base de datos para escritura
        SQLiteDatabase db = dataBase.getWritableDatabase();
        //Creo objeto ContentValues con los valores del nuevo elemento
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseContract.MantenimientosTable.COL_FECHA, mantenimiento.getFecha());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_ID_VITRINA, mantenimiento.getIdVitrina());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_PUESTA_MARCHA, mantenimiento.isPuestaMarcha());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_ID_TECNICO, mantenimiento.getIdTecnico());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_SEGUN_DIN, mantenimiento.isSegunDin());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_SEGUN_EN, mantenimiento.isSegunEn());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_FUN_CTRL_DIGI, mantenimiento.getFunCtrlDigi());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_VIS_SIST_EXTR, mantenimiento.getVisSistExtr());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_PROT_SUPERF, mantenimiento.getProtSuperf());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_JUNTAS, mantenimiento.getJuntas());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_FIJACION, mantenimiento.getFijacion());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_FUNC_GUILLO, mantenimiento.getFijacion());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_ESTADO_GUILLO, mantenimiento.getFijacion());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_VAL_FUERZA_GUILLO, mantenimiento.getValFuerzaGuillo());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_FUERZA_GILLO, mantenimiento.getFuerzaGuillo());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_CTRL_PRESENCIA, mantenimiento.getCtrlPresencia());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_AUTOPROTECCION, mantenimiento.getAutoproteccion());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_GRIFOS_MONORED, mantenimiento.getGrifosMonored());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_ID_MEDICION, mantenimiento.getIdMedicion());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_EVALU_VOL_EXTRAC, mantenimiento.getEvaluVolExtrac());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_ACORDE_NORMAS_SI, mantenimiento.isAcordeNormasReguSi());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_ACORDE_NORMAS_NO, mantenimiento.isAcordeNormasReguNo());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_NECESARIO_REPA_SI, mantenimiento.isNecesarioRepaSi());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_NECESARIO_REPA_NO, mantenimiento.isNecesarioRepaNo());
        contentValues.put(DataBaseContract.MantenimientosTable.COL_COMENTARIO, mantenimiento.getComentario());
        //Para actualizar un elemento se utiliza el médodo:
        //   update(nombre_tabla, valores, criterio_seleccion, parametros_criterio_seleccion)
        String criterioSeleccion = String.format("%s=?", DataBaseContract.MantenimientosTable._ID);
        //array de string con los parámetros del criterio de selección
        String[] paramCriterioSeleccion = {String.valueOf(mantenimiento.getId())};
        //Realizo la actualización
        int updated = db.update(
                DataBaseContract.MantenimientosTable.TABLE_NAME,
                contentValues,
                criterioSeleccion,
                paramCriterioSeleccion);
        return updated > 0; // true si se actualizó algo
    }

    public boolean deleteMantenimiento(int id) {
        //obtengo instancia de la base de datos para escritura
        SQLiteDatabase db = dataBase.getWritableDatabase();
        //Para borrar se utilizar el método:
        //  delete(nombre_tabla, criterio_seleccion, parametros_criterio_seleccion
        //Defino la string con el criterio de seleccion parametrizado.
        String criterioSeleccion = String.format("%s=?", DataBaseContract.MantenimientosTable._ID);
        //array de string con los valores de los parámetros para completar la query parametrizada
        String[] paramCriterioSeleccion = {String.valueOf(id)};
        //ejecuto el borrado
        int deleted = db.delete(
                DataBaseContract.MantenimientosTable.TABLE_NAME, criterioSeleccion, paramCriterioSeleccion);
        return deleted > 0;
    }

    public List<Integer> getIdsOfMantenimientosForVitrina(int idVitrina) {
        List<Integer> listIds = new ArrayList<>();
        for (Mantenimiento mantenimiento : selectMantenimientos()) {
            if (mantenimiento.getIdVitrina() == idVitrina) {
                listIds.add(mantenimiento.getId());
                break;
            }
        }
        return listIds;
    }

    public boolean existMantemiento(Mantenimiento m) {
        boolean exist = false;
        for (Mantenimiento mantenimiento: selectMantenimientos()) {
            if (mantenimiento.equals(m)) {
                exist = true;
                break;
            }
        }
        return exist;
    }


    /*
        CRUD sobre la entidad Mantenimiento
     */
    public long insertMediciones(Medicion medicion) {
        //Creo instancia para escribir en la base de datos
        SQLiteDatabase db = dataBase.getWritableDatabase();
        //Creo el objeto ContentValues para colocar los datos a insertar
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseContract.MedicionesVolTable.COL_VALOR_MEDIO, medicion.getValorMedio());
        contentValues.put(DataBaseContract.MedicionesVolTable.COL_VALOR1, medicion.getValor1());
        contentValues.put(DataBaseContract.MedicionesVolTable.COL_VALOR2, medicion.getValor2());
        contentValues.put(DataBaseContract.MedicionesVolTable.COL_VALOR3, medicion.getValor3());
        contentValues.put(DataBaseContract.MedicionesVolTable.COL_VALOR4, medicion.getValor4());
        contentValues.put(DataBaseContract.MedicionesVolTable.COL_VALOR5, medicion.getValor5());
        contentValues.put(DataBaseContract.MedicionesVolTable.COL_VALOR6, medicion.getValor6());
        contentValues.put(DataBaseContract.MedicionesVolTable.COL_VALOR7, medicion.getValor7());
        contentValues.put(DataBaseContract.MedicionesVolTable.COL_VALOR8, medicion.getValor8());
        contentValues.put(DataBaseContract.MedicionesVolTable.COL_VALOR9, medicion.getValor9());

        return db.insert(
                DataBaseContract.MedicionesVolTable.TABLE_NAME, null, contentValues);
    }

    public List<Medicion> selectMediciones() {
        //String con secuencia SQL para la búsqueda
        String sqlSelectMediciones = "SELECT * FROM " + DataBaseContract.MantenimientosTable.TABLE_NAME;
        return selectMediciones(sqlSelectMediciones);
    }

    public List<Medicion> selectMediciones(String sqlSelectMediciones) {
        //Declaro lista de mediciones seleccionados que se devolverán.
        List<Medicion> medicionesSelected = new ArrayList<>();
        //Creo instancia para leer de la base de datos
        SQLiteDatabase db = dataBase.getReadableDatabase();
        //Para realizar una consulta se utiliza el médodo:
        //     rawQuery(string_con_consulta_select_parametrizada, lista_de_string_con_los_parámetros
        //en este caso concreto, la consulta es sencilla y al no utilizar parámetros pues el segundo
        //argumento del método (lista con parámetros) es null
        Cursor cursor = db.rawQuery(sqlSelectMediciones, null);
        //Si el cursor almacena algún elemento, pues devuelve true con el método moveToFirst()
        if (cursor.moveToFirst()) {
            //Recupero el primer elemento del cursor y repito esta operación mientras que el cursor
            //devuelva true con el método moveToNext(), es decir, mientras tenga un siguiente elemento
            do {
                int id = cursor.getInt(0);
                float valorMedio = cursor.getFloat(1);
                float valor1 = cursor.getFloat(2);
                float valor2 = cursor.getFloat(3);
                float valor3 = cursor.getFloat(4);
                float valor4 = cursor.getFloat(5);
                float valor5 = cursor.getFloat(6);
                float valor6 = cursor.getFloat(7);
                float valor7 = cursor.getFloat(8);
                float valor8 = cursor.getFloat(9);
                float valor9 = cursor.getFloat(1);

                //Construyo objeto Medicion y lo añado a la lista
                Medicion medicion = new Medicion(
                        id, valorMedio, valor1, valor2, valor3, valor4, valor5, valor6, valor7, valor8, valor9);
                medicionesSelected.add(medicion);
            } while (cursor.moveToNext());
        }
        //Cierro el cursor para liberar recursos
        cursor.close();
        return medicionesSelected;
    }

    public Medicion selectMedicionWithId(int idToSelect) {
        Medicion medicion = null;
        //Creo instancia para leer de la base de datos
        SQLiteDatabase db = dataBase.getReadableDatabase();
        //String con la consulta SQL a realizar, faltaría el parámetro en la posición ? que intruduce
        //en la rawQuery como un array de string
        String sqlSelect = String.format("SELECT * FROM %s WHERE %s=?",
                DataBaseContract.MedicionesVolTable.TABLE_NAME,
                DataBaseContract.MedicionesVolTable._ID);
        //Creo array de strings con los parámetros para la consulta parametrizada anterior
        String[] selectArguments = {String.valueOf(idToSelect)};
        //Realizo al consulta almacenado el resultado en un cursor
        Cursor cursor = db.rawQuery(sqlSelect, selectArguments);
        //Si el cursor almacena algún elemento, pues devuelve true con el método moveToFirst()
        if (cursor.moveToFirst()) {
            //Recupero el primer elemento del cursor, que además el único que debe haber (no debe
            // haber duplicados) y la consulta es para recuperar el elemento de id indicado.
            int id = cursor.getInt(0);
            float valorMedio = cursor.getFloat(1);
            float valor1 = cursor.getFloat(2);
            float valor2 = cursor.getFloat(3);
            float valor3 = cursor.getFloat(4);
            float valor4 = cursor.getFloat(5);
            float valor5 = cursor.getFloat(6);
            float valor6 = cursor.getFloat(7);
            float valor7 = cursor.getFloat(8);
            float valor8 = cursor.getFloat(9);
            float valor9 = cursor.getFloat(1);

            //Construyo objeto Medicion y lo añado a la lista
            medicion = new Medicion(
                    id, valorMedio, valor1, valor2, valor3, valor4, valor5, valor6, valor7, valor8, valor9);
        }
        cursor.close();
        return medicion;
    }

    public boolean updateMedicion(Medicion medicion) {
        //Obtengo instancia de la base de datos para escritura
        SQLiteDatabase db = dataBase.getWritableDatabase();
        //Creo objeto ContentValues con los valores del nuevo elemento
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseContract.MedicionesVolTable.COL_VALOR_MEDIO, medicion.getValorMedio());
        contentValues.put(DataBaseContract.MedicionesVolTable.COL_VALOR1, medicion.getValor1());
        contentValues.put(DataBaseContract.MedicionesVolTable.COL_VALOR2, medicion.getValor2());
        contentValues.put(DataBaseContract.MedicionesVolTable.COL_VALOR3, medicion.getValor3());
        contentValues.put(DataBaseContract.MedicionesVolTable.COL_VALOR4, medicion.getValor4());
        contentValues.put(DataBaseContract.MedicionesVolTable.COL_VALOR5, medicion.getValor5());
        contentValues.put(DataBaseContract.MedicionesVolTable.COL_VALOR6, medicion.getValor6());
        contentValues.put(DataBaseContract.MedicionesVolTable.COL_VALOR7, medicion.getValor7());
        contentValues.put(DataBaseContract.MedicionesVolTable.COL_VALOR8, medicion.getValor8());
        contentValues.put(DataBaseContract.MedicionesVolTable.COL_VALOR9, medicion.getValor9());

        //Para actualizar un elemento se utiliza el médodo:
        //   update(nombre_tabla, valores, criterio_seleccion, parametros_criterio_seleccion)
        String criterioSeleccion = String.format("%s=?", DataBaseContract.MedicionesVolTable._ID);
        //array de string con los parámetros del criterio de selección
        String[] paramCriterioSeleccion = {String.valueOf(medicion.getId())};
        //Realizo la actualización
        int updated = db.update(
                DataBaseContract.MantenimientosTable.TABLE_NAME,
                contentValues,
                criterioSeleccion,
                paramCriterioSeleccion);
        return updated > 0; // true si se actualizó algo
    }

    public boolean deleteMedicion(int id) {
        //obtengo instancia de la base de datos para escritura
        SQLiteDatabase db = dataBase.getWritableDatabase();
        //Para borrar se utilizar el método:
        //  delete(nombre_tabla, criterio_seleccion, parametros_criterio_seleccion
        //Defino la string con el criterio de seleccion parametrizado.
        String criterioSeleccion = String.format("%s=?", DataBaseContract.MedicionesVolTable._ID);
        //array de string con los valores de los parámetros para completar la query parametrizada
        String[] paramCriterioSeleccion = {String.valueOf(id)};
        //ejecuto el borrado
        int deleted = db.delete(
                DataBaseContract.MantenimientosTable.TABLE_NAME, criterioSeleccion, paramCriterioSeleccion);
        return deleted > 0;
    }

    public boolean existMedicion(Medicion m) {
        boolean exist = false;
        for (Medicion medicion: selectMediciones()) {
            if (medicion.equals(m)) {
                exist = true;
                break;
            }
        }
        return exist;
    }

    public int getIdOfMedicion(Medicion m) {
        int idMedicion = -1;
        for (Medicion medicion: selectMediciones()) {
            if (medicion.equals(m)) {
                idMedicion = medicion.getId();
                break;
            }
        }
        return idMedicion;
    }

    public float getVolumenExtraccionReal(int idMedicion, float largoGuillotina) {
        float medicionMedia = selectMedicionWithId(idMedicion).getValorMedio();
        return medicionMedia * largoGuillotina * 0.5f * 3600;
    }
}
