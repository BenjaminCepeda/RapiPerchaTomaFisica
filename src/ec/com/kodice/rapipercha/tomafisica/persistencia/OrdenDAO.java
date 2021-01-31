/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.kodice.rapipercha.tomafisica.persistencia;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import ec.com.kodice.connection.CustomConnection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase contiene metodos de acceso a datos de un Provedor
 *
 * @author Benjamin Cepeda
 * @version v1.0
 * @date 2020/12/03
 */
public class OrdenDAO {

    /**
     * Permite crear un nuevo Proveedor
     *
     * @param ordenVO POJO con los atributos de Orden
     * @return Codigo del registro creado
     * @throws Exception
     */
    public int crear(OrdenVO ordenVO) throws Exception {
        Connection conexion = null;
        PreparedStatement sentencia = null;
        int codigoGenerado = 0;
        try {
            conexion = CustomConnection.getConnection();
            String consulta = "INSERT INTO TORDENES"
                    + "(loc_codigo, usu_codigo, ord_fecha_generacion, "
                    + "ord_fecha_arealizar, ord_codigo_externo_orden, "
                    + "ord_codigo_usuario_generacion, ord_estado) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            sentencia = conexion.prepareStatement(consulta,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            sentencia.setInt(1, ordenVO.getLocalVO().getCodigo());
            sentencia.setInt(2, ordenVO.getUsuarioCodigo());
            sentencia.setObject(3, ordenVO.getFechaGeneracion());
            sentencia.setObject(4, ordenVO.getFechaARealizar());
            sentencia.setString(5, ordenVO.getCodigoExternoOrden());
            sentencia.setInt(6, ordenVO.getCodigoUsuarioGeneracion());
            sentencia.setString(7, ordenVO.getEstado());
            sentencia.executeUpdate();
            ResultSet resultado = sentencia.getGeneratedKeys();
            while (resultado.next()) {
                codigoGenerado = resultado.getInt(1);
            }
        } 
        catch(Exception e){
            conexion.close();
            throw new Exception(e.getMessage() + "\n[" + this.getClass().getName()
                    + "] ");
        }    
        finally{
            try {
                conexion.close();
            } catch (SQLException e){
                throw new Exception(e.getMessage() + "\n[" 
                        + this.getClass().getName() + "] ");
            }
        }        
        return (codigoGenerado);
    }

    /**
     * Permite leer una Orden en base de su codigo
     *
     * @param codigo Codigo del registro a ser leído
     * @return POJO con los atributos de Orden
     * @throws Exception
     */
    public OrdenVO buscar(int codigo) throws Exception {
        Connection conexion = null;
        PreparedStatement sentencia = null;
        OrdenVO ordenVO = null;
        try {
            conexion = CustomConnection.getConnection();
            String consulta = "SELECT ord_codigo, loc_codigo, usu_codigo, "
                    + "ord_fecha_generacion, ord_fecha_arealizar, "
                    + "ord_codigo_externo_orden, ord_codigo_usuario_generacion, "
                    + "ord_estado "
                    + "FROM TORDENES WHERE ord_codigo = ?";
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, codigo);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                ordenVO = new OrdenVO(resultado.getInt("ord_codigo"),
                        resultado.getInt("loc_codigo"),
                        resultado.getInt("usu_codigo"),
                        resultado.getTimestamp("ord_fecha_generacion").toLocalDateTime(),
                        resultado.getTimestamp("ord_fecha_arealizar").toLocalDateTime(),
                        resultado.getString("ord_codigo_externo_orden"),
                        resultado.getInt("ord_codigo_usuario_generacion"),
                        resultado.getString("ord_estado"));
            }
        } 
        catch(Exception e){
            conexion.close();
            throw new Exception(e.getMessage() + "\n[" + this.getClass().getName()
                    + "] ");
        }    
        finally{
            try {
                conexion.close();
            } catch (SQLException e){
                throw new Exception(e.getMessage() + "\n[" 
                        + this.getClass().getName() + "] ");
            }
        }        
        return (ordenVO);
    }
    
    
    
    
    public int buscarultimocodigo() throws Exception {
        Connection conexion = null;
        PreparedStatement sentencia = null;
        OrdenVO ordenVO = null;
        int ultimoCodigo=0;
        try {
            conexion = CustomConnection.getConnection();
            String consulta = "SELECT MAX(`ORD_CODIGO`) AS `ORD_CODIGO` FROM tordenes";
            sentencia = conexion.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                ultimoCodigo = (resultado.getInt("ord_codigo"));
            }
        } 
        catch(Exception e){
            conexion.close();
            throw new Exception(e.getMessage() + "\n[" + this.getClass().getName()
                    + "] ");
        }    
        finally{
            try {
                conexion.close();
            } catch (SQLException e){
                throw new Exception(e.getMessage() + "\n[" 
                        + this.getClass().getName() + "] ");
            }
        }        
        return (ultimoCodigo);
    }

    /**
     * Devuelve el listado de Ordenes
     *
     * @return Lista de Ordenes
     * @throws Exception
     */
    public List<OrdenVO> buscarTodos() throws Exception {
        Connection conexion = null;
        PreparedStatement sentencia = null;
        List<OrdenVO> listaElementos = null;
        OrdenVO ordenVO = null;
        try {
            conexion = CustomConnection.getConnection();
            String consulta = "SELECT ord_codigo, loc_codigo, usu_codigo, "
                    + "ord_fecha_generacion, ord_fecha_arealizar, "
                    + "ord_codigo_externo_orden, ord_codigo_usuario_generacion, "
                    + "ord_estado "
                    + "FROM TORDENES ";
            sentencia = conexion.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                if (listaElementos == null) {
                    listaElementos = new ArrayList<OrdenVO>();
                }
                ordenVO = new OrdenVO(resultado.getInt("ord_codigo"),
                        resultado.getInt("loc_codigo"),
                        resultado.getInt("usu_codigo"),
                        resultado.getTimestamp("ord_fecha_generacion").toLocalDateTime(),
                        resultado.getTimestamp("ord_fecha_arealizar").toLocalDateTime(),
                        resultado.getString("ord_codigo_externo_orden"),
                        resultado.getInt("ord_codigo_usuario_generacion"),
                        resultado.getString("ord_estado"));
                listaElementos.add(ordenVO);
            }
        } 
        catch(Exception e){
            conexion.close();
            throw new Exception(e.getMessage() + "\n[" + this.getClass().getName()
                    + "] ");
        }    
        finally{
            try {
                conexion.close();
            } catch (SQLException e){
                throw new Exception(e.getMessage() + "\n[" 
                        + this.getClass().getName() + "] ");
            }
        }        
        return (listaElementos);
    }

    /**
     * Devuelve el listado de Ordenes
     *
     * @return Lista de Ordenes
     * @throws Exception
     */
    public List<OrdenVO> buscarPorUsuarioFechaRealizar(int usuarioCodigo, 
            LocalDate fechaARealizar) throws Exception {
        Connection conexion = null;
        PreparedStatement sentencia = null;
        List<OrdenVO> listaElementos = null;
        OrdenVO ordenVO = null;
        try {
            conexion = CustomConnection.getConnection();
            String consulta = "SELECT ord_codigo, L.loc_codigo, usu_codigo, "
                    + "ord_fecha_generacion, ord_fecha_arealizar, "
                    + "ord_codigo_externo_orden, "
                    + "ord_codigo_usuario_generacion, ord_estado, "
                    + "L.loc_nombre, loc_direccion, "
                    + "C.cen_codigo, C.cen_ruc, C.cen_razon_social, "
                    + "C.cen_nombre_comercial "
                    + "FROM TORDENES "
                    + "INNER JOIN TLOCALES L ON TORDENES.loc_codigo = l.loc_codigo "
                    + "INNER JOIN TCENTRO_EXPENDIO C ON L.cen_codigo = C.cen_codigo "
                    + "WHERE ord_codigo_usuario_generacion = ? and "
                    + "DATE_FORMAT(ord_fecha_arealizar,'%Y%m%d') = "
                    + "DATE_FORMAT( ?, '%Y%m%d') "
                    + "ORDER BY ord_fecha_arealizar";
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, usuarioCodigo);
            sentencia.setObject(2, fechaARealizar);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                if (listaElementos == null) {
                    listaElementos = new ArrayList<OrdenVO>();
                }
                ordenVO = new OrdenVO(resultado.getInt("ord_codigo"),
                        resultado.getInt("loc_codigo"),
                        resultado.getInt("usu_codigo"),
                        resultado.getTimestamp("ord_fecha_generacion").toLocalDateTime(),
                        resultado.getTimestamp("ord_fecha_arealizar").toLocalDateTime(),
                        resultado.getString("ord_codigo_externo_orden"),
                        resultado.getInt("ord_codigo_usuario_generacion"),
                        resultado.getString("ord_estado"));
                ordenVO.getLocalVO().setDireccion(resultado.getString("loc_direccion") );
                ordenVO.getLocalVO().setNombre(resultado.getString("loc_nombre") );                
                ordenVO.getLocalVO().getCentroExpendioVO().setCodigo(resultado.getInt("cen_codigo"));
                ordenVO.getLocalVO().getCentroExpendioVO().setRazonSocial(resultado.getString("cen_razon_social"));
                ordenVO.getLocalVO().getCentroExpendioVO().setNombreComercial(resultado.getString("cen_nombre_comercial"));
                listaElementos.add(ordenVO);
            }
        } 
        catch(Exception e){
            conexion.close();
            throw new Exception(e.getMessage() + "\n[" + this.getClass().getName()
                    + "] ");
        }    
        finally{
            try {
                conexion.close();
            } catch (SQLException e){
                throw new Exception(e.getMessage() + "\n[" 
                        + this.getClass().getName() + "] ");
            }
        }        
        return (listaElementos);
    }

    /**
     * Permite actualizar un registro de Orden
     *
     * @param ordenVO POJO con los atributos de Proveedor
     * @return Numero de registros actualizados
     * @throws Exception
     */
    public int actualizar(OrdenVO ordenVO) throws Exception {
        Connection conexion = null;
        PreparedStatement sentencia = null;
        int filasAfectadas = 0;
        try {
            conexion = CustomConnection.getConnection();
            String consulta = "UPDATE TPROVEEDORES "
                    + "SET loc_codigo = ?, "
                    + "usu_codigo = ?, "
                    + "ord_fecha_arealizar = ?, "
                    + "ord_codigo_externo_orden = ?, "
                    + "ord_codigo_usuario_generacion = ?, "
                    + "ord_estado = ? "
                    + "WHERE ord_codigo = ?";
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, ordenVO.getLocalVO().getCodigo());
            sentencia.setInt(2, ordenVO.getUsuarioCodigo());
            sentencia.setObject(3, ordenVO.getFechaARealizar());
            sentencia.setString(4, ordenVO.getCodigoExternoOrden());
            sentencia.setInt(5, ordenVO.getCodigoUsuarioGeneracion());
            sentencia.setString(6, ordenVO.getEstado());
            sentencia.setInt(7, ordenVO.getCodigo());
            filasAfectadas = sentencia.executeUpdate();
        } 
        catch(Exception e){
            conexion.close();
            throw new Exception(e.getMessage() + "\n[" + this.getClass().getName()
                    + "] ");
        }    
        finally{
            try {
                conexion.close();
            } catch (SQLException e){
                throw new Exception(e.getMessage() + "\n[" 
                        + this.getClass().getName() + "] ");
            }
        }        
        return (filasAfectadas);
    }

    /**
     * Permte eliminar una instancia de Orden
     *
     * @param codigo Codigo de Orden a eliminar
     * @return Numero de registros eliminados
     * @throws Exception
     */
    public int eliminar(int codigo) throws Exception {
        Connection conexion = null;
        PreparedStatement sentencia = null;
        int filasAfectadas = 0;
        try {
            conexion = CustomConnection.getConnection();
            String consulta = "DELETE FROM TORDENES "
                    + "WHERE ord_codigo = ?";
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, codigo);
            filasAfectadas = sentencia.executeUpdate();
            conexion.close();
        } 
        catch(Exception e){
            conexion.close();
            throw new Exception(e.getMessage() + "\n[" + this.getClass().getName()
                    + "] ");
        }    
        finally{
            try {
                conexion.close();
            } catch (SQLException e){
                throw new Exception(e.getMessage() + "\n[" 
                        + this.getClass().getName() + "] ");
            }
        }        
        return (filasAfectadas);

    }

}
