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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase contiene metodos de acceso a datos de un Detalle de Orden
 *
 * @author Benjamin Cepeda
 * @version v1.0
 * @date 2020/12/03
 */
public class DetalleOrdenDAO {

    /**
     * Permite crear un nuevo Detalle de una Orden
     *
     * @param detalleOrdenVO POJO con los atributos de DetalleOrden
     * @return Codigo del registro creado
     * @throws Exception
     */
    public int crear(DetalleOrdenVO detalleOrdenVO) throws Exception {
        Connection conexion = null;
        PreparedStatement sentencia = null;
        int codigoGenerado = 0;
        try {
            conexion = CustomConnection.getConnection();
            String consulta = "INSERT INTO TDETALLE_ORDENES"
                    + "(ord_codigo, prod_codigo, "
                    + "det_cantidad_minima,  det_existencia, "
                    + "det_cantidad_revisada, det_cantidad_mal_estado,"
                    + "det_cantidad_vencido, det_fecha_proceso) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
            sentencia = conexion.prepareStatement(consulta,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            sentencia.setInt(1, detalleOrdenVO.getOrdenCodigo());
            sentencia.setInt(2, detalleOrdenVO.getProducto().getCodigo());
            sentencia.setFloat(3, detalleOrdenVO.getCantidadMinima());
            sentencia.setFloat(4, detalleOrdenVO.getExistencia());
            sentencia.setFloat(5, detalleOrdenVO.getCantidadRevisada());
            sentencia.setFloat(6, detalleOrdenVO.getCantidadMalEstado());
            sentencia.setFloat(7, detalleOrdenVO.getCantidadVencido());
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
     * Permite leer un Detalle de Orden en base de su codigo
     *
     * @param codigo Codigo del registro a ser leído
     * @return POJO con los atributos de Detalle de Orden
     * @throws Exception
     */
    public DetalleOrdenVO buscar(int codigo) throws Exception {
        Connection conexion = null;
        PreparedStatement sentencia = null;
        DetalleOrdenVO detalleOrdenVO = null;
        try {
            conexion = CustomConnection.getConnection();
            String consulta = "SELECT det_codigo, ord_codigo, prod_codigo, "
                    + "det_cantidad_minima,  det_existencia, "
                    + "det_cantidad_revisada, det_cantidad_mal_estado,"
                    + "det_cantidad_vencido, det_fecha_proceso "
                    + "FROM TDETALLE_ORDENES WHERE det_codigo = ?";
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, codigo);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                detalleOrdenVO = new DetalleOrdenVO(
                        resultado.getInt("det_codigo"),
                        resultado.getInt("ord_codigo"),
                        resultado.getInt("prod_codigo"),
                        resultado.getFloat("det_cantidad_minima"),
                        resultado.getFloat("det_existencia"),
                        resultado.getFloat("det_cantidad_revisada"),
                        resultado.getFloat("det_cantidad_mal_estado"),
                        resultado.getFloat("det_cantidad_vencido"),
                        resultado.getTimestamp("det_fecha_proceso").
                                toLocalDateTime());
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
        return (detalleOrdenVO);
    }

    /**
     * Devuelve el listado de Detalles de la Orden
     *
     * @return Listado de Detalle de la Orden
     * @throws Exception
     */
    public List<DetalleOrdenVO> buscarTodos() throws Exception {
        Connection conexion = null;
        PreparedStatement sentencia = null;
        List<DetalleOrdenVO> listaElementos = null;
        DetalleOrdenVO detalleOrdenVO = null;
        try {
            conexion = CustomConnection.getConnection();
            String consulta = "SELECT det_codigo, ord_codigo, prod_codigo, "
                    + "det_cantidad_minima,  det_existencia, "
                    + "det_cantidad_revisada, det_cantidad_mal_estado,"
                    + "det_cantidad_vencido, det_fecha_proceso "
                    + "FROM TDETALLE_ORDENES ";
            sentencia = conexion.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                if (listaElementos == null) {
                    listaElementos = new ArrayList<DetalleOrdenVO>();
                }
                detalleOrdenVO = new DetalleOrdenVO(
                        resultado.getInt("det_codigo"),
                        resultado.getInt("ord_codigo"),
                        resultado.getInt("prod_codigo"),
                        resultado.getFloat("det_cantidad_minima"),
                        resultado.getFloat("det_existencia"),
                        resultado.getFloat("det_cantidad_revisada"),
                        resultado.getFloat("det_cantidad_mal_estado"),
                        resultado.getFloat("det_cantidad_vencido"),
                        resultado.getTimestamp("det_fecha_proceso").
                                toLocalDateTime());
                listaElementos.add(detalleOrdenVO);
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
     * Devuelve el Detalle de una Orden
     *
     * @return Lista de Detalle de Ordenes
     * @throws Exception
     */
    public List<DetalleOrdenVO> buscarPorOrden(int ordenCodigo) 
            throws Exception {
        Connection conexion = null;
        PreparedStatement sentencia = null;
        List<DetalleOrdenVO> listaElementos = null;
        DetalleOrdenVO detalleOrdenVO = null;
        LocalDateTime fechaProceso = null;
        try {
            conexion = CustomConnection.getConnection();
            String consulta = "SELECT det_codigo, ord_codigo, P.prod_codigo, "
                    + "det_cantidad_minima,  det_existencia, "
                    + "det_cantidad_revisada, det_cantidad_mal_estado,"
                    + "det_cantidad_vencido, det_fecha_proceso, "
                    + "p.prod_descripcion, "
                    + "p.prod_codigo_externo_producto "
                    + "FROM TDETALLE_ORDENES  "
                    + "INNER JOIN TPRODUCTOS P ON "
                    + "TDETALLE_ORDENES.prod_codigo = P.prod_codigo "
                    + "WHERE ord_codigo = ?";
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, ordenCodigo);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                if (listaElementos == null) {
                    listaElementos = new ArrayList<DetalleOrdenVO>();
                }
                if (resultado.getTimestamp("det_fecha_proceso")!=null)
                    fechaProceso = resultado.getTimestamp("det_fecha_proceso")
                            .toLocalDateTime();
                            
                detalleOrdenVO = new DetalleOrdenVO(
                        resultado.getInt("det_codigo"),
                        resultado.getInt("ord_codigo"),
                        resultado.getInt("prod_codigo"),
                        resultado.getFloat("det_cantidad_minima"),
                        resultado.getFloat("det_existencia"),
                        resultado.getFloat("det_cantidad_revisada"),
                        resultado.getFloat("det_cantidad_mal_estado"),
                        resultado.getFloat("det_cantidad_vencido"),
                        fechaProceso);
                detalleOrdenVO.getProducto().setDescripcion(
                        resultado.getString("prod_descripcion"));
                detalleOrdenVO.getProducto().setCodigoExternoProducto(
                        resultado.getString("prod_codigo_externo_producto"));
                listaElementos.add(detalleOrdenVO);
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
     * Permite actualizar un detalle de la Orden
     *
     * @param detalleOrdenVO POJO con los atributos de Detalle de Orden
     * @return Numero de registros actualizados
     * @throws Exception
     */
    public int actualizar(DetalleOrdenVO detalleOrdenVO) throws Exception {
        Connection conexion = null;
        PreparedStatement sentencia = null;
        int filasAfectadas = 0;
        try {
            conexion = CustomConnection.getConnection();
            String consulta = "UPDATE DETALLE_ORDENES "
                    + "SET ord_codigo = ?, "
                    + "prod_codigo = ?, "
                    + "det_cantidad_minima= ?, "
                    + "det_existencia= ?, "
                    + "det_cantidad_revisada= ?, "
                    + "det_cantidad_mal_estado= ?, "
                    + "det_cantidad_vencido= ? "
                    + "det_fecha_proceso= NOW() "
                    + "WHERE det_codigo = ?";
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, detalleOrdenVO.getOrdenCodigo());
            sentencia.setInt(2, detalleOrdenVO.getProducto().getCodigo());
            sentencia.setFloat(3, detalleOrdenVO.getCantidadMinima());
            sentencia.setFloat(4, detalleOrdenVO.getExistencia());
            sentencia.setFloat(5, detalleOrdenVO.getCantidadRevisada());
            sentencia.setFloat(6, detalleOrdenVO.getCantidadMalEstado());
            sentencia.setFloat(7, detalleOrdenVO.getCantidadVencido());
            sentencia.setFloat(8, detalleOrdenVO.getCodigo());
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
     * Permte eliminar una instancia de Detalle de Orden
     *
     * @param codigo Codigo de Detalle de Orden a eliminar
     * @return Numero de registros eliminados
     * @throws Exception
     */
    public int eliminar(int codigo) throws Exception {
        Connection conexion = null;
        PreparedStatement sentencia = null;
        int filasAfectadas = 0;
        try {
            conexion = CustomConnection.getConnection();
            String consulta = "DELETE FROM TDETALLE_ORDENES"
                    + "WHERE det_codigo = ?";
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
