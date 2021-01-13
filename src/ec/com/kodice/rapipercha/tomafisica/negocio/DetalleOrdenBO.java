/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.com.kodice.rapipercha.tomafisica.negocio;





import ec.com.kodice.rapipercha.tomafisica.persistencia.DetalleOrdenDAO;
import ec.com.kodice.rapipercha.tomafisica.persistencia.DetalleOrdenVO;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 * Esta clase contiene los atributos y metodos de capa de negocio de una Orden
 * @author Benjamin Cepeda
 * @version v1.0
 * @date 2021/01/03 
 */
public class DetalleOrdenBO {
    DetalleOrdenDAO detalleOrdenDAO = null;

    public DetalleOrdenBO() {
        detalleOrdenDAO = new DetalleOrdenDAO();
    }

    public int crear(DetalleOrdenVO detalleOrdenVO) throws Exception {
        int codigoGenerado = 0;
        try {
            codigoGenerado = detalleOrdenDAO.crear(detalleOrdenVO);
        } catch (Exception e) {
            throw new Exception(e.getMessage()
                    + "\n[" + this.getClass().getName() + "]");
        }
        return (codigoGenerado);
    }

    public DetalleOrdenVO buscar(int codigo) throws Exception {
        DetalleOrdenVO detalleOrdenVO = null;
        try {
            detalleOrdenVO = detalleOrdenDAO.buscar(codigo);
        } catch (Exception e) {
            throw new Exception(e.getMessage()
                    + "\n[" + this.getClass().getName() + "]");
        }
        return (detalleOrdenVO);
    }

    public List<DetalleOrdenVO> buscarTodos() throws Exception {
       List<DetalleOrdenVO> listaElementos = null;
       try {
           listaElementos = detalleOrdenDAO.buscarTodos();

       } catch (Exception e) {
           throw new Exception(e.getMessage()
                   + "\n[" + this.getClass().getName() + "]");
       }
       return (listaElementos);
   }

    public int actualizar(DetalleOrdenVO detalleOrdenVO) throws Exception {
        int filasAfectadas = 0;
        try {
            filasAfectadas = detalleOrdenDAO.actualizar(detalleOrdenVO);
        } catch (Exception e) {
            throw new Exception(e.getMessage()
                    + "\n[" + this.getClass().getName() + "]");
        }
        return (filasAfectadas);
    }

    public int eliminar(int codigo) throws Exception {
        int filasAfectadas = 0;
        try {
            filasAfectadas = detalleOrdenDAO.eliminar(codigo);
        } catch (Exception e) {
            throw new Exception(e.getMessage()
                    + "\n[" + this.getClass().getName() + "]");
        }
        return (filasAfectadas);
    }

    public DefaultTableModel generaModeloDatosTabla(int ordenCodigo,
            Object[] titulosCabecera) 
            throws Exception {
        DefaultTableModel dtmListaElementos = new DefaultTableModel();
        List<DetalleOrdenVO> listaElementos = null;      
        dtmListaElementos.setColumnIdentifiers(titulosCabecera);
        try {
            listaElementos = detalleOrdenDAO.buscarPorOrden(
                    ordenCodigo);
            if (listaElementos != null) {
                for (DetalleOrdenVO detalleOrdenVO : listaElementos) {
                   dtmListaElementos.addRow(new Object[]{
                      detalleOrdenVO.getCodigo(), 
                      detalleOrdenVO.getProducto().getDescripcion(),
                      detalleOrdenVO.getProducto().getCodigoExternoProducto(),
                      detalleOrdenVO.getCantidadMinima(),
                      detalleOrdenVO.getExistencia(),                    
                      detalleOrdenVO.getCantidadRevisada(),
                      detalleOrdenVO.getCantidadMalEstado(),
                      detalleOrdenVO.getCantidadVencido()});
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage()
                    + "\n[" + this.getClass().getName() + "]");
        }
        return (dtmListaElementos);
    }

}
