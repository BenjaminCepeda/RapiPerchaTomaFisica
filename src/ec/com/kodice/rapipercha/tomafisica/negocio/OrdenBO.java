/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.com.kodice.rapipercha.tomafisica.negocio;

import ec.com.kodice.rapipercha.administracion.negocio.UsuarioBO;
import ec.com.kodice.rapipercha.tomafisica.persistencia.OrdenDAO;
import ec.com.kodice.rapipercha.tomafisica.persistencia.OrdenVO;
import ec.com.kodice.rapipercha.administracion.persistencia.UsuarioVO;
import java.time.LocalDate;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 * Esta clase contiene los atributos y metodos de capa de negocio de una Orden
 * @author Benjamin Cepeda
 * @version v1.0
 * @date 2021/01/03 
 */
public class OrdenBO {
    OrdenDAO ordenDAO = null;

    public OrdenBO() {
        ordenDAO = new OrdenDAO();
    }

    public int crear(OrdenVO ordenVO) throws Exception {
        int codigoGenerado = 0;
        try {
            codigoGenerado = ordenDAO.crear(ordenVO);
        } catch (Exception e) {
            throw new Exception(e.getMessage()
                    + "\n[" + this.getClass().getName() + "]");
        }
        return (codigoGenerado);
    }

    public OrdenVO buscar(int codigo) throws Exception {
        OrdenVO ordenVO = null;
        try {
            ordenVO = ordenDAO.buscar(codigo);
        } catch (Exception e) {
            throw new Exception(e.getMessage()
                    + "\n[" + this.getClass().getName() + "]");
        }
        return (ordenVO);
    }
    
      public int buscarUltimoCodigo() throws Exception {
        OrdenVO ordenVO = null;
        int ultimoCodigoOrden=0;
        try {
            ultimoCodigoOrden = ordenDAO.buscarultimocodigo();
        } catch (Exception e) {
            throw new Exception(e.getMessage()
                    + "\n[" + this.getClass().getName() + "]");
        }
        return (ultimoCodigoOrden);
    }

    public List<OrdenVO> buscarTodos() throws Exception {
        List<OrdenVO> listaElementos = null;
        try {
            listaElementos = ordenDAO.buscarTodos();

        } catch (Exception e) {
            throw new Exception(e.getMessage()
                    + "\n[" + this.getClass().getName() + "]");
        }
        return (listaElementos);
    }

    public int actualizar(OrdenVO ordenVO) throws Exception {
        int filasAfectadas = 0;
        try {
            filasAfectadas = ordenDAO.actualizar(ordenVO);
        } catch (Exception e) {
            throw new Exception(e.getMessage()
                    + "\n[" + this.getClass().getName() + "]");
        }
        return (filasAfectadas);
    }

    public int eliminar(int codigo) throws Exception {
        int filasAfectadas = 0;
        try {
            filasAfectadas = ordenDAO.eliminar(codigo);
        } catch (Exception e) {
            throw new Exception(e.getMessage()
                    + "\n[" + this.getClass().getName() + "]");
        }
        return (filasAfectadas);
    }

    public int cerrarOrden(int codigo) throws Exception {
        int filasAfectadas = 0;
        try {
            filasAfectadas = ordenDAO.cerrarOrden(codigo);
        } catch (Exception e) {
            throw new Exception(e.getMessage()
                    + "\n[" + this.getClass().getName() + "]");
        }
        return (filasAfectadas);
    }

    public DefaultTableModel generaModeloDatosTabla(int usuarioCodigo,
            LocalDate fechaARealizar,
            Object[] titulosCabecera) 
            throws Exception {
        DefaultTableModel dtmListaElementos = new DefaultTableModel();
        List<OrdenVO> listaElementos = null;
        UsuarioVO usuarioGeneroOrden = new UsuarioVO();
        UsuarioBO usuarioBO = new UsuarioBO();
        dtmListaElementos.setColumnIdentifiers(titulosCabecera);
        try {
            listaElementos = ordenDAO.buscarPorUsuarioFechaRealizar(
                    usuarioCodigo, fechaARealizar);
            if (listaElementos != null) {
                for (OrdenVO ordenVO : listaElementos) {
                    usuarioGeneroOrden = usuarioBO.buscar(ordenVO.getCodigoUsuarioGeneracion());
                    dtmListaElementos.addRow(new Object[]{
                      ordenVO.getCodigo(), ordenVO.getFechaARealizar(),
                      ordenVO.getLocalVO().getCentroExpendioVO().getRazonSocial(),
                      ordenVO.getLocalVO().getNombre(),
                      ordenVO.getLocalVO().getDireccion(),
                      usuarioGeneroOrden.getNombre(),
                      ordenVO.getFechaGeneracion(),
                      ordenVO.getEstado()});
                   
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage()
                    + "\n[" + this.getClass().getName() + "]");
        }
        return (dtmListaElementos);
    }

}
