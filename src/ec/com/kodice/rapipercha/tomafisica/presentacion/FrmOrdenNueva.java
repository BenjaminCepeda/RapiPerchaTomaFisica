/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.com.kodice.rapipercha.tomafisica.presentacion;

import ec.com.kodice.rapipercha.administracion.negocio.CentrodeExpendioBO;
import ec.com.kodice.rapipercha.administracion.negocio.EmpleadoBO;
import ec.com.kodice.rapipercha.administracion.negocio.LocalBO;
import ec.com.kodice.rapipercha.administracion.negocio.ProductoBO;
import ec.com.kodice.rapipercha.administracion.persistencia.CentroExpendioVO;
import ec.com.kodice.rapipercha.administracion.persistencia.EmpleadoVO;
import ec.com.kodice.rapipercha.administracion.persistencia.LocalVO;
import ec.com.kodice.rapipercha.administracion.persistencia.ProveedorVO;
import ec.com.kodice.rapipercha.tomafisica.negocio.DetalleOrdenBO;
import ec.com.kodice.rapipercha.tomafisica.negocio.OrdenBO;
import ec.com.kodice.rapipercha.tomafisica.persistencia.DetalleOrdenVO;
import ec.com.kodice.rapipercha.tomafisica.persistencia.OrdenVO;
import ec.com.kodice.rapipercha.util.UtilPresentacion;
import java.io.File;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.time.LocalDateTime;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
/**
 *
 * Esta clase contiene atributos y métodos del formulario FrmPerfilAdministracion
 * @author Benjamin Cepeda
 * @version v1.0
 * @date 2020/12/06
 */
public class FrmOrdenNueva extends javax.swing.JDialog {
    private int codigoActual = 0;
    private EmpleadoVO empleadoLogueado= null;
    private ProveedorVO proveedorEmpleadoLogueado = null;

    public void setCodigoActual(int codigoActual) {
        this.codigoActual = codigoActual;
    }

    /** Creates new form FrmOrdenNueva */
    public FrmOrdenNueva(int codigoActual, boolean soloLectura,EmpleadoVO empleadoLogueado, 
            ProveedorVO proveedorEmpleadoLoguedo) {
        initComponents();
        this.setLocationRelativeTo(null);
        this.empleadoLogueado = empleadoLogueado;
        this.proveedorEmpleadoLogueado=proveedorEmpleadoLoguedo;
        this.lblNombreEmpresa.setText(proveedorEmpleadoLogueado.getNombreComercial()
            + " - "+ proveedorEmpleadoLogueado.getRazonSocial());
        
        this.lblNombreEmpleado1.setText(empleadoLogueado.getNombres() + " " +
                empleadoLogueado.getApellidos());       
        this.codigoActual = codigoActual;
        if (soloLectura){
            cargarDatosSoloLectura(codigoActual);
            seteaControles(soloLectura);
        } else{
           cargarDatos(); 
                     
        }
            
        
        
    }
    
    public FrmOrdenNueva(EmpleadoVO empleadoLogueado, 
            ProveedorVO proveedorEmpleadoLoguedo) {
        initComponents();
        this.empleadoLogueado = empleadoLogueado;
        this.proveedorEmpleadoLogueado=proveedorEmpleadoLoguedo;
         this.lblNombreEmpresa.setText(proveedorEmpleadoLogueado.getNombreComercial()
            + " - "+ proveedorEmpleadoLogueado.getRazonSocial());
        
        this.lblNombreEmpleado1.setText(empleadoLogueado.getNombres() + " " +
                empleadoLogueado.getApellidos());       
        this.setLocationRelativeTo(null);
        this.codigoActual = 0;
        cargarDatos();
        if (cmbCentroExpendio.getModel()!=null){
           LocalBO localBO=new LocalBO();
           CentroExpendioVO centroexpendioVO = new CentroExpendioVO();
           centroexpendioVO=(CentroExpendioVO)cmbCentroExpendio.getSelectedItem();
           try {
           cmbLocales.setModel(localBO.generaModeloDatosCombo(centroexpendioVO.getCodigo()));
           CargaDatosProductosporLocal();
             } catch (Exception e) {
            
             UtilPresentacion.mostrarMensajeError(this, e.getMessage());
            }
            finally{
            localBO = null;   
           
            }
           
          }
        
        dtpFecha.datePicker.setDateToToday();
        dtpFecha.timePicker.setTimeToNow();
    }
    
  
    
    private void seteaControles(boolean soloLectura){
        btnGrabar.setEnabled(!soloLectura);
        cmbEmpleado.enable(soloLectura);
        cmbCentroExpendio.enable(soloLectura);
        cmbLocales.enable(soloLectura);
    }
    
    
    
    private void cargarDatos(){
              
        EmpleadoBO empleadoBO = new EmpleadoBO();
        CentrodeExpendioBO centrodeexpendioBO=new CentrodeExpendioBO();
        try {
            cmbEmpleado.setModel(empleadoBO.generaModeloDatosCombo(proveedorEmpleadoLogueado.getCodigo()));
            cmbCentroExpendio.setModel(centrodeexpendioBO.generaModeloDatosCombo(proveedorEmpleadoLogueado.getCodigo()));
            
        } catch (Exception e) {
            
             UtilPresentacion.mostrarMensajeError(this, e.getMessage());
        }
        finally{
            empleadoBO = null;   
            centrodeexpendioBO=null;
        }
        
     
    }
    
      private void cargarDatosSoloLectura(int codigoOrden){
              
        EmpleadoBO empleadoBO = new EmpleadoBO();
        CentrodeExpendioBO centrodeexpendioBO=new CentrodeExpendioBO();
        OrdenVO ordenVO= null;
        ordenVO= new OrdenVO();
        OrdenBO ordenBO=new OrdenBO();
        LocalBO localBO=new LocalBO();
        LocalVO localVO=null;
        localVO =new LocalVO();
        try {
            ordenVO=ordenBO.buscar(codigoOrden);
            cmbEmpleado.setModel(empleadoBO.generaModeloDatosComboUnSoloItem(ordenVO.getUsuarioCodigo()));
            if (ordenVO.getFechaARealizar()!=null)
                    dtpFecha.setDateTimeStrict(ordenVO.getFechaARealizar());
            txtCodExterno.setText(ordenVO.getCodigoExternoOrden());
            
            localVO=localBO.buscar(ordenVO.getLocalVO().getCodigo());
            cmbLocales.setModel(localBO.generaModeloDatosComboUnSoloItem(localVO.getCodigo()));
            cmbCentroExpendio.setModel(centrodeexpendioBO.generaModeloDatosComboUnSoloItem(localVO.getCentroExpendioVO().getCodigo()));
            
            CargaDatosDetallOrden();
            
            //cmbCentroExpendio.setModel(centrodeexpendioBO.generaModeloDatosCombo(proveedorEmpleadoLogueado.getCodigo()));
        } catch (Exception e) {
            
             UtilPresentacion.mostrarMensajeError(this, e.getMessage());
        }
        finally{
            empleadoBO = null;   
            centrodeexpendioBO=null;
        }
        
     
    }
    
    
    public void creachecboxentabla (int column, JTable table){
        TableColumn tc = table.getColumnModel().getColumn(column);
        tc.setCellEditor(table.getDefaultEditor(Boolean.class));
        tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
        
    }
    
    public boolean IsSelected (int row, int column, JTable table){
        return table.getValueAt(row, column)!= null;
    }
    
    public void guardadetalle (int codigoOrden,int codProducto, float cantidadMinima, float Existencia){
        DetalleOrdenBO detalleOrdenBO=null;
        DetalleOrdenVO detalleordenVO=null;
        detalleOrdenBO=new DetalleOrdenBO();
        OrdenVO ordenVO = null;
        OrdenBO ordenBO =null;
        ordenBO = new OrdenBO();
        
        detalleordenVO=new DetalleOrdenVO(codigoOrden, codProducto,cantidadMinima , 
                Existencia, 0, 0, 
                0, null);
        try {
            
            detalleOrdenBO.crear(detalleordenVO);
        } 
        catch (Exception e) {
            UtilPresentacion.mostrarMensajeError(this, e.getMessage());
            
        }
        
        
    }
    
    
    public void CargaDatosProductosporLocal () {
        ProductoBO productoBO=new ProductoBO();
        LocalVO localVO=new LocalVO();
         try {
            localVO=(LocalVO)cmbLocales.getSelectedItem();
            tblProductosenorden.setModel(productoBO.generaModeloDatosTabla(localVO.getCodigo(), 
                    this.proveedorEmpleadoLogueado.getCodigo(),new Object[]{"CODIGO", "DESCRIPCIÓN", "COD.EXTERNO",
                        "CATIDAD MÍNIMA","EXISTENCIA","SELECCIONAR"}));
            creachecboxentabla(5, tblProductosenorden);
            tblProductosenorden.setVisible(true);
        } catch (Exception e) {
            
             UtilPresentacion.mostrarMensajeError(this, e.getMessage());
        }
        finally{
            productoBO = null;   
       
        }
    }
         
    public void CargaDatosDetallOrden() {
        DetalleOrdenBO detalleOrdenBO=new DetalleOrdenBO();
        LocalVO localVO=new LocalVO();
         try {
            tblProductosenorden.setModel(detalleOrdenBO.generaModeloDatosTabla(codigoActual, 
                        new Object[]{"CODIGO", "DESCRIPCIÓN", "COD.EXTERNO",
                        "CATIDAD MÍNIMA","EXISTENCIA"}));
            //creachecboxentabla(5, tblProductosenorden);
            tblProductosenorden.setVisible(true);
        } catch (Exception e) {
            
             UtilPresentacion.mostrarMensajeError(this, e.getMessage());
        }
        finally{
            detalleOrdenBO = null;   
       
        }
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlDetalle1 = new javax.swing.JPanel();
        lblCodigo1 = new javax.swing.JLabel();
        txtCodigo1 = new javax.swing.JTextField();
        lblPerfil1 = new javax.swing.JLabel();
        cmbPerfil1 = new javax.swing.JComboBox<>();
        lblNombre2 = new javax.swing.JLabel();
        txtNombre1 = new javax.swing.JTextField();
        lblNombre3 = new javax.swing.JLabel();
        pswClave1 = new javax.swing.JPasswordField();
        lblConfirmacion1 = new javax.swing.JLabel();
        pswConfirmacion1 = new javax.swing.JPasswordField();
        lblEstado1 = new javax.swing.JLabel();
        cmbEstado1 = new javax.swing.JComboBox<>();
        pnlDetalle2 = new javax.swing.JPanel();
        lblCodigo2 = new javax.swing.JLabel();
        txtCodigo2 = new javax.swing.JTextField();
        lblPerfil2 = new javax.swing.JLabel();
        cmbPerfil2 = new javax.swing.JComboBox<>();
        lblNombre4 = new javax.swing.JLabel();
        txtNombre2 = new javax.swing.JTextField();
        lblNombre5 = new javax.swing.JLabel();
        pswClave2 = new javax.swing.JPasswordField();
        lblConfirmacion2 = new javax.swing.JLabel();
        pswConfirmacion2 = new javax.swing.JPasswordField();
        lblEstado2 = new javax.swing.JLabel();
        cmbEstado2 = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        pnlCabecera = new javax.swing.JPanel();
        lblLogoRapipercha = new javax.swing.JLabel();
        lblTitulo = new javax.swing.JLabel();
        lblNombreEmpleado = new javax.swing.JLabel();
        lblNombreEmpleado1 = new javax.swing.JLabel();
        lblTituloEmpresa = new javax.swing.JLabel();
        lblNombreEmpresa = new javax.swing.JLabel();
        pnlDetalle = new javax.swing.JPanel();
        lblPerfil = new javax.swing.JLabel();
        cmbCentroExpendio = new javax.swing.JComboBox<>();
        lblEstado = new javax.swing.JLabel();
        cmbLocales = new javax.swing.JComboBox<>();
        pnlDetalle3 = new javax.swing.JPanel();
        lblPerfil3 = new javax.swing.JLabel();
        cmbEmpleado = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        dtpFecha = new com.github.lgooddatepicker.components.DateTimePicker();
        jLabel5 = new javax.swing.JLabel();
        txtCodExterno = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        pnlPie = new javax.swing.JPanel();
        btnGrabar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        lblLogoKodice = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProductosenorden = new javax.swing.JTable();

        pnlDetalle1.setAlignmentX(0.0F);
        pnlDetalle1.setAlignmentY(0.0F);

        lblCodigo1.setText("Dirección del Local:");
        lblCodigo1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtCodigo1.setEnabled(false);
        txtCodigo1.setMinimumSize(new java.awt.Dimension(7, 22));
        txtCodigo1.setPreferredSize(new java.awt.Dimension(7, 22));
        txtCodigo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigo1ActionPerformed(evt);
            }
        });

        lblPerfil1.setText("Centro de Expendio");
        lblPerfil1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        cmbPerfil1.setMinimumSize(new java.awt.Dimension(30, 25));
        cmbPerfil1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbPerfil1ActionPerformed(evt);
            }
        });

        lblNombre2.setText("Nombre:");
        lblNombre2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtNombre1.setMinimumSize(new java.awt.Dimension(7, 22));
        txtNombre1.setPreferredSize(new java.awt.Dimension(7, 22));

        lblNombre3.setText("Clave:");
        lblNombre3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        pswClave1.setMinimumSize(new java.awt.Dimension(7, 22));
        pswClave1.setPreferredSize(new java.awt.Dimension(7, 22));

        lblConfirmacion1.setText("Confirme clave:");
        lblConfirmacion1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        pswConfirmacion1.setMinimumSize(new java.awt.Dimension(7, 22));
        pswConfirmacion1.setPreferredSize(new java.awt.Dimension(7, 22));

        lblEstado1.setText("Local:");
        lblEstado1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout pnlDetalle1Layout = new javax.swing.GroupLayout(pnlDetalle1);
        pnlDetalle1.setLayout(pnlDetalle1Layout);
        pnlDetalle1Layout.setHorizontalGroup(
            pnlDetalle1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDetalle1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDetalle1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDetalle1Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(pnlDetalle1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblNombre3)
                            .addComponent(lblNombre2)
                            .addComponent(lblConfirmacion1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlDetalle1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtNombre1, javax.swing.GroupLayout.DEFAULT_SIZE, 597, Short.MAX_VALUE)
                            .addComponent(pswClave1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pswConfirmacion1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(pnlDetalle1Layout.createSequentialGroup()
                        .addGroup(pnlDetalle1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblPerfil1)
                            .addComponent(lblCodigo1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlDetalle1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCodigo1, javax.swing.GroupLayout.PREFERRED_SIZE, 597, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlDetalle1Layout.createSequentialGroup()
                                .addComponent(cmbPerfil1, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblEstado1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbEstado1, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlDetalle1Layout.setVerticalGroup(
            pnlDetalle1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDetalle1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDetalle1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDetalle1Layout.createSequentialGroup()
                        .addGroup(pnlDetalle1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPerfil1)
                            .addComponent(cmbPerfil1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblEstado1)
                            .addComponent(cmbEstado1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlDetalle1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCodigo1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCodigo1)))
                    .addGroup(pnlDetalle1Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addGroup(pnlDetalle1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblNombre2)
                            .addComponent(txtNombre1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlDetalle1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblNombre3)
                            .addComponent(pswClave1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlDetalle1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblConfirmacion1)
                            .addComponent(pswConfirmacion1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlDetalle2.setAlignmentX(0.0F);
        pnlDetalle2.setAlignmentY(0.0F);

        lblCodigo2.setText("Dirección del Local:");
        lblCodigo2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtCodigo2.setEnabled(false);
        txtCodigo2.setMinimumSize(new java.awt.Dimension(7, 22));
        txtCodigo2.setPreferredSize(new java.awt.Dimension(7, 22));
        txtCodigo2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigo2ActionPerformed(evt);
            }
        });

        lblPerfil2.setText("Centro de Expendio");
        lblPerfil2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        cmbPerfil2.setMinimumSize(new java.awt.Dimension(30, 25));
        cmbPerfil2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbPerfil2ActionPerformed(evt);
            }
        });

        lblNombre4.setText("Nombre:");
        lblNombre4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtNombre2.setMinimumSize(new java.awt.Dimension(7, 22));
        txtNombre2.setPreferredSize(new java.awt.Dimension(7, 22));

        lblNombre5.setText("Clave:");
        lblNombre5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        pswClave2.setMinimumSize(new java.awt.Dimension(7, 22));
        pswClave2.setPreferredSize(new java.awt.Dimension(7, 22));

        lblConfirmacion2.setText("Confirme clave:");
        lblConfirmacion2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        pswConfirmacion2.setMinimumSize(new java.awt.Dimension(7, 22));
        pswConfirmacion2.setPreferredSize(new java.awt.Dimension(7, 22));

        lblEstado2.setText("Local:");
        lblEstado2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout pnlDetalle2Layout = new javax.swing.GroupLayout(pnlDetalle2);
        pnlDetalle2.setLayout(pnlDetalle2Layout);
        pnlDetalle2Layout.setHorizontalGroup(
            pnlDetalle2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDetalle2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDetalle2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDetalle2Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(pnlDetalle2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblNombre5)
                            .addComponent(lblNombre4)
                            .addComponent(lblConfirmacion2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlDetalle2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtNombre2, javax.swing.GroupLayout.DEFAULT_SIZE, 597, Short.MAX_VALUE)
                            .addComponent(pswClave2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pswConfirmacion2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(pnlDetalle2Layout.createSequentialGroup()
                        .addGroup(pnlDetalle2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblPerfil2)
                            .addComponent(lblCodigo2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlDetalle2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCodigo2, javax.swing.GroupLayout.PREFERRED_SIZE, 597, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlDetalle2Layout.createSequentialGroup()
                                .addComponent(cmbPerfil2, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblEstado2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbEstado2, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlDetalle2Layout.setVerticalGroup(
            pnlDetalle2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDetalle2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDetalle2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDetalle2Layout.createSequentialGroup()
                        .addGroup(pnlDetalle2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPerfil2)
                            .addComponent(cmbPerfil2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblEstado2)
                            .addComponent(cmbEstado2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlDetalle2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCodigo2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCodigo2)))
                    .addGroup(pnlDetalle2Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addGroup(pnlDetalle2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblNombre4)
                            .addComponent(txtNombre2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlDetalle2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblNombre5)
                            .addComponent(pswClave2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlDetalle2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblConfirmacion2)
                            .addComponent(pswConfirmacion2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setResizable(false);

        jPanel1.setAlignmentX(0.0F);
        jPanel1.setAlignmentY(0.0F);
        jPanel1.setPreferredSize(new java.awt.Dimension(800, 600));

        pnlCabecera.setAlignmentX(0.0F);
        pnlCabecera.setAlignmentY(0.0F);
        pnlCabecera.setBackground(new java.awt.Color(64, 124, 202));
        pnlCabecera.setMaximumSize(new java.awt.Dimension(32767, 90));
        pnlCabecera.setMinimumSize(new java.awt.Dimension(0, 90));
        pnlCabecera.setPreferredSize(new java.awt.Dimension(518, 90));

        lblLogoRapipercha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ec/com/kodice/rapipercha/imagenes/logo-rapipercha.png"))); // NOI18N

        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("Orden de Toma Física");
        lblTitulo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));

        lblNombreEmpleado.setText("NOMBRE EMPLEADO:");
        lblNombreEmpleado.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblNombreEmpleado.setForeground(new java.awt.Color(255, 255, 255));

        lblNombreEmpleado1.setText("NOMBRE EMPLEADO");
        lblNombreEmpleado1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblNombreEmpleado1.setForeground(new java.awt.Color(255, 255, 255));

        lblTituloEmpresa.setText("EMPRESA:");
        lblTituloEmpresa.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblTituloEmpresa.setForeground(new java.awt.Color(255, 255, 255));

        lblNombreEmpresa.setText("NOMBRE EMPRESA");
        lblNombreEmpresa.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblNombreEmpresa.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pnlCabeceraLayout = new javax.swing.GroupLayout(pnlCabecera);
        pnlCabecera.setLayout(pnlCabeceraLayout);
        pnlCabeceraLayout.setHorizontalGroup(
            pnlCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCabeceraLayout.createSequentialGroup()
                .addComponent(lblLogoRapipercha)
                .addGap(205, 205, 205)
                .addGroup(pnlCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlCabeceraLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(pnlCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlCabeceraLayout.createSequentialGroup()
                                .addComponent(lblNombreEmpleado)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblNombreEmpleado1, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlCabeceraLayout.createSequentialGroup()
                                .addComponent(lblTituloEmpresa)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblNombreEmpresa)))))
                .addContainerGap(188, Short.MAX_VALUE))
        );
        pnlCabeceraLayout.setVerticalGroup(
            pnlCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCabeceraLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCabeceraLayout.createSequentialGroup()
                        .addComponent(lblTitulo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTituloEmpresa)
                            .addComponent(lblNombreEmpresa))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                        .addGroup(pnlCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNombreEmpleado)
                            .addComponent(lblNombreEmpleado1)))
                    .addComponent(lblLogoRapipercha))
                .addContainerGap())
        );

        pnlDetalle.setAlignmentX(0.0F);
        pnlDetalle.setAlignmentY(0.0F);

        lblPerfil.setText("Centro de Expendio");
        lblPerfil.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        cmbCentroExpendio.setMinimumSize(new java.awt.Dimension(30, 25));
        cmbCentroExpendio.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbCentroExpendioItemStateChanged(evt);
            }
        });
        cmbCentroExpendio.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                cmbCentroExpendioPopupMenuWillBecomeVisible(evt);
            }
        });
        cmbCentroExpendio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cmbCentroExpendioMouseClicked(evt);
            }
        });
        cmbCentroExpendio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCentroExpendioActionPerformed(evt);
            }
        });

        lblEstado.setText("Local:");
        lblEstado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        cmbLocales.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbLocalesItemStateChanged(evt);
            }
        });
        cmbLocales.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cmbLocalesMouseClicked(evt);
            }
        });
        cmbLocales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbLocalesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlDetalleLayout = new javax.swing.GroupLayout(pnlDetalle);
        pnlDetalle.setLayout(pnlDetalleLayout);
        pnlDetalleLayout.setHorizontalGroup(
            pnlDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDetalleLayout.createSequentialGroup()
                .addGroup(pnlDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPerfil)
                    .addComponent(lblEstado))
                .addGap(10, 10, 10)
                .addGroup(pnlDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbCentroExpendio, 0, 710, Short.MAX_VALUE)
                    .addComponent(cmbLocales, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlDetalleLayout.setVerticalGroup(
            pnlDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDetalleLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPerfil)
                    .addComponent(cmbCentroExpendio, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEstado)
                    .addComponent(cmbLocales, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(77, 77, 77))
        );

        pnlDetalle3.setAlignmentX(0.0F);
        pnlDetalle3.setAlignmentY(0.0F);

        lblPerfil3.setText("Empleado");
        lblPerfil3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        cmbEmpleado.setMinimumSize(new java.awt.Dimension(30, 25));
        cmbEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbEmpleadoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlDetalle3Layout = new javax.swing.GroupLayout(pnlDetalle3);
        pnlDetalle3.setLayout(pnlDetalle3Layout);
        pnlDetalle3Layout.setHorizontalGroup(
            pnlDetalle3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDetalle3Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(lblPerfil3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cmbEmpleado, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlDetalle3Layout.setVerticalGroup(
            pnlDetalle3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDetalle3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlDetalle3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPerfil3)
                    .addComponent(cmbEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28))
        );

        jLabel2.setText("Asignación Empleado Perchador");
        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dtpFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(62, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(dtpFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 7, Short.MAX_VALUE))
        );

        jLabel5.setText("Código Externo de Orden");
        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        jLabel3.setText("Asignación Fecha a realizar");
        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        jLabel4.setText("Asignación Local de Orden");
        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlCabecera, javax.swing.GroupLayout.DEFAULT_SIZE, 884, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pnlDetalle3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(txtCodExterno, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(pnlDetalle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(35, 35, 35)
                    .addComponent(jLabel2)
                    .addContainerGap(738, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(pnlCabecera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(pnlDetalle3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCodExterno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(111, 111, 111)
                    .addComponent(jLabel2)
                    .addContainerGap(229, Short.MAX_VALUE)))
        );

        pnlPie.setMaximumSize(new java.awt.Dimension(32767, 90));
        pnlPie.setMinimumSize(new java.awt.Dimension(0, 90));
        pnlPie.setPreferredSize(new java.awt.Dimension(780, 90));

        btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ec/com/kodice/rapipercha/imagenes/Disquette.png"))); // NOI18N
        btnGrabar.setText("Grabar");
        btnGrabar.setAlignmentY(0.0F);
        btnGrabar.setBackground(new java.awt.Color(64, 124, 202));
        btnGrabar.setBorderPainted(false);
        btnGrabar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnGrabar.setForeground(new java.awt.Color(255, 255, 255));
        btnGrabar.setPreferredSize(new java.awt.Dimension(125, 57));
        btnGrabar.setToolTipText("");
        btnGrabar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnGrabarMouseClicked(evt);
            }
        });
        btnGrabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrabarActionPerformed(evt);
            }
        });

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ec/com/kodice/rapipercha/imagenes/Badge-cancel.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.setAlignmentY(0.0F);
        btnSalir.setBackground(new java.awt.Color(64, 124, 202));
        btnSalir.setBorderPainted(false);
        btnSalir.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnSalir.setForeground(new java.awt.Color(255, 255, 255));
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        lblLogoKodice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ec/com/kodice/rapipercha/imagenes/logo-kodice.png"))); // NOI18N

        javax.swing.GroupLayout pnlPieLayout = new javax.swing.GroupLayout(pnlPie);
        pnlPie.setLayout(pnlPieLayout);
        pnlPieLayout.setHorizontalGroup(
            pnlPieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPieLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnGrabar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblLogoKodice)
                .addGap(48, 48, 48))
        );
        pnlPieLayout.setVerticalGroup(
            pnlPieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPieLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlPieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblLogoKodice, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlPieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnGrabar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnSalir)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setText("Asignación de Productos a la Orden");
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        tblProductosenorden.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Cod. Producto", "Descripción", "Cod. Alterno", "Cant. Minima", "Existencia"
            }
        ));
        jScrollPane1.setViewportView(tblProductosenorden);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 633, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlPie, javax.swing.GroupLayout.DEFAULT_SIZE, 884, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 884, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlPie, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed
        
        OrdenVO ordenVO = null;
        OrdenBO ordenBO =null;
        DetalleOrdenBO detalleOrdenBO=null;
        DetalleOrdenVO detalleordenVO=null;
        ordenBO = new OrdenBO();
        EmpleadoBO empleadoBO=null;
        EmpleadoVO empleadoVO=null;
        empleadoVO=new EmpleadoVO();
        CentroExpendioVO centroexpendioVO=null;
        LocalVO localVo=null;
        int respuestaOperacion = 0;
        boolean camposValidos;
        String nombreEstado ="ACTIVA";
        camposValidos = 
            (!txtCodExterno.getText().isEmpty() && 
                !txtCodExterno.getText().trim().equals("") && 
                !dtpFecha.datePicker.getText().isEmpty() && 
                !dtpFecha.datePicker.getText().trim().equals(""));               
        if (camposValidos){
                
                localVo=(LocalVO)cmbLocales.getSelectedItem();
                empleadoVO=(EmpleadoVO)cmbEmpleado.getSelectedItem();
                //nombreEstado = cmbEstado.getItemAt(cmbEstado.getSelectedIndex());
                //usuarioVO = new UsuarioVO(codigoActual, txtNombre.getText(),
                  //      pswClave.getText(),nombreEstado); 
                          ordenVO = new OrdenVO(localVo.getCodigo(), 
                          empleadoVO.getCodigo(),LocalDateTime.now(),dtpFecha.getDateTimeStrict(), txtCodExterno.getText(), empleadoLogueado.getCodigo(), nombreEstado); 
                try{
                    if (codigoActual==0){
                       
                        if(tblProductosenorden.getRowCount()>0){
                        respuestaOperacion = ordenBO.crear(ordenVO);
                        
                        int codigoorden = ordenBO.buscarUltimoCodigo();
                        int codProducto=0;
                        float cantidadMinima=0;
                        float existencia=0;
                        for (int i=0; i<tblProductosenorden.getRowCount();i++){
                            if(IsSelected(i,5, tblProductosenorden)){
                                codProducto=Integer.parseInt(tblProductosenorden.getValueAt(i, 0).toString());
                                cantidadMinima=Float.parseFloat(tblProductosenorden.getValueAt(i, 3).toString());
                                existencia=Float.parseFloat(tblProductosenorden.getValueAt(i, 4).toString());
                                guardadetalle(codigoorden,codProducto,cantidadMinima,existencia);
                            }
                        }
                        } else{
                            JOptionPane.showMessageDialog(null, "La tabla de productos está vacía");
                        }
                        
                        
                        //txtCodigo.setText(String.valueOf(respuestaOperacion));
                    }else
                        respuestaOperacion = ordenBO.actualizar(ordenVO);
                    
                    if (respuestaOperacion>0)
                        JOptionPane.showMessageDialog(null, "Registro guardado con éxito");
                        this.setVisible(false);
                        this.dispose();
                }
                catch(Exception e){
                    UtilPresentacion.mostrarMensajeError(this, e.getMessage());
                }
                finally{
                    ordenVO = null;
                    ordenBO = null;
                  
                    this.setVisible(false);
                    this.dispose();
                }
            }
            
        else {
            UtilPresentacion.mostrarMensajeValidacionIncorrecta(this, 
                "Ingrese los datos requeridos en el formulario");
        }
        
        
        
    }//GEN-LAST:event_btnGrabarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void cmbCentroExpendioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCentroExpendioActionPerformed
        // TODO add your handling code here:
        LocalBO localBO=new LocalBO();
        CentroExpendioVO centroexpendioVO = new CentroExpendioVO();
         try {
            centroexpendioVO=(CentroExpendioVO)cmbCentroExpendio.getSelectedItem();
            cmbLocales.setModel(localBO.generaModeloDatosCombo(centroexpendioVO.getCodigo()));
        
        } catch (Exception e) {
            
             UtilPresentacion.mostrarMensajeError(this, e.getMessage());
        }
        finally{
            localBO = null;   
       
        }
        
        
    }//GEN-LAST:event_cmbCentroExpendioActionPerformed

    private void txtCodigo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigo1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigo1ActionPerformed

    private void cmbPerfil1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPerfil1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbPerfil1ActionPerformed

    private void txtCodigo2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigo2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigo2ActionPerformed

    private void cmbPerfil2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPerfil2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbPerfil2ActionPerformed

    private void cmbEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbEmpleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbEmpleadoActionPerformed

    private void cmbLocalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbLocalesActionPerformed
        // TODO add your handling code here:
       CargaDatosProductosporLocal();
        
    }//GEN-LAST:event_cmbLocalesActionPerformed

    private void cmbCentroExpendioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbCentroExpendioMouseClicked
        // TODO add your handling code here:
   
        
        
       
    }//GEN-LAST:event_cmbCentroExpendioMouseClicked

    private void cmbCentroExpendioItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbCentroExpendioItemStateChanged
        // TODO add your handling code here:
        LocalBO localBO=new LocalBO();
        CentroExpendioVO centroexpendioVO = new CentroExpendioVO();
         try {
            centroexpendioVO=(CentroExpendioVO)cmbCentroExpendio.getSelectedItem();
            cmbLocales.setModel(localBO.generaModeloDatosCombo(centroexpendioVO.getCodigo()));
            tblProductosenorden.setVisible(false);
        } catch (Exception e) {
            
             UtilPresentacion.mostrarMensajeError(this, e.getMessage());
        }
        finally{
            localBO = null;   
       
        }
    }//GEN-LAST:event_cmbCentroExpendioItemStateChanged

    private void cmbLocalesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbLocalesMouseClicked
        // TODO add your handling code here:
       
       /* ProductoBO productoBO=new ProductoBO();
        LocalVO localVO=new LocalVO();
         try {
            localVO=(LocalVO)cmbLocales.getSelectedItem();
            tblProductosenorden.setModel(productoBO.generaModeloDatosTabla(localVO.getCodigo(), 
                    this.proveedorEmpleadoLogueado.getCodigo(),new Object[]{"CODIGO", "DESCRIPCIÓN", "COD.EXTERNO",
                        "CATIDAD MÍNIMA","EXISTENCIA","SELECCIONAR"}));
            creachecboxentabla(5, tblProductosenorden);
        } catch (Exception e) {
            
             UtilPresentacion.mostrarMensajeError(this, e.getMessage());
        }
        finally{
            productoBO = null;   
       
        }
       */
        
    }//GEN-LAST:event_cmbLocalesMouseClicked

    private void cmbLocalesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbLocalesItemStateChanged
        // TODO add your handling code here:
        
         CargaDatosProductosporLocal();
         
    }//GEN-LAST:event_cmbLocalesItemStateChanged

    private void btnGrabarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGrabarMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGrabarMouseClicked

    private void cmbCentroExpendioPopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cmbCentroExpendioPopupMenuWillBecomeVisible
        // TODO add your handling code here:
        
    }//GEN-LAST:event_cmbCentroExpendioPopupMenuWillBecomeVisible

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmOrdenNueva.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmOrdenNueva.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmOrdenNueva.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmOrdenNueva.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new FrmOrdenNueva().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGrabar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> cmbCentroExpendio;
    private javax.swing.JComboBox<String> cmbEmpleado;
    private javax.swing.JComboBox<String> cmbEstado1;
    private javax.swing.JComboBox<String> cmbEstado2;
    private javax.swing.JComboBox<String> cmbLocales;
    private javax.swing.JComboBox<String> cmbPerfil1;
    private javax.swing.JComboBox<String> cmbPerfil2;
    private com.github.lgooddatepicker.components.DateTimePicker dtpFecha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCodigo1;
    private javax.swing.JLabel lblCodigo2;
    private javax.swing.JLabel lblConfirmacion1;
    private javax.swing.JLabel lblConfirmacion2;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblEstado1;
    private javax.swing.JLabel lblEstado2;
    private javax.swing.JLabel lblLogoKodice;
    private javax.swing.JLabel lblLogoRapipercha;
    private javax.swing.JLabel lblNombre2;
    private javax.swing.JLabel lblNombre3;
    private javax.swing.JLabel lblNombre4;
    private javax.swing.JLabel lblNombre5;
    private javax.swing.JLabel lblNombreEmpleado;
    private javax.swing.JLabel lblNombreEmpleado1;
    private javax.swing.JLabel lblNombreEmpresa;
    private javax.swing.JLabel lblPerfil;
    private javax.swing.JLabel lblPerfil1;
    private javax.swing.JLabel lblPerfil2;
    private javax.swing.JLabel lblPerfil3;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTituloEmpresa;
    private javax.swing.JPanel pnlCabecera;
    private javax.swing.JPanel pnlDetalle;
    private javax.swing.JPanel pnlDetalle1;
    private javax.swing.JPanel pnlDetalle2;
    private javax.swing.JPanel pnlDetalle3;
    private javax.swing.JPanel pnlPie;
    private javax.swing.JPasswordField pswClave1;
    private javax.swing.JPasswordField pswClave2;
    private javax.swing.JPasswordField pswConfirmacion1;
    private javax.swing.JPasswordField pswConfirmacion2;
    private javax.swing.JTable tblProductosenorden;
    private javax.swing.JTextField txtCodExterno;
    private javax.swing.JTextField txtCodigo1;
    private javax.swing.JTextField txtCodigo2;
    private javax.swing.JTextField txtNombre1;
    private javax.swing.JTextField txtNombre2;
    // End of variables declaration//GEN-END:variables

}
