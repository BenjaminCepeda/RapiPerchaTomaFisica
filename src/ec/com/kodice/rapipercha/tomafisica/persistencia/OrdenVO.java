/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.com.kodice.rapipercha.tomafisica.persistencia;

import ec.com.kodice.rapipercha.administracion.persistencia.LocalVO;
import java.time.LocalDateTime;

/**
 * Esta clase contiene los atributos y metodos de un EmpleadoVO
 * @author Benjamin Cepeda
 * @version v1.0
 * @date 2020/12/28 
 */
public class OrdenVO {
    private int codigo;
    private LocalVO localVO = new LocalVO();
    private int usuarioCodigo;
    private LocalDateTime fechaGeneracion;
    private LocalDateTime fechaARealizar;
    private String codigoExternoOrden;
    private int codigoUsuarioGeneracion;
    private String estado;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public LocalVO getLocalVO() {
        return localVO;
    }

    public void setLocalVO(LocalVO localVO) {
        this.localVO = localVO;
    }

    public int getUsuarioCodigo() {
        return usuarioCodigo;
    }

    public void setUsuarioCodigo(int usuarioCodigo) {
        this.usuarioCodigo = usuarioCodigo;
    }

    public LocalDateTime getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDateTime fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public LocalDateTime getFechaARealizar() {
        return fechaARealizar;
    }

    public void setFechaARealizar(LocalDateTime fechaARealizar) {
        this.fechaARealizar = fechaARealizar;
    }

    public String getCodigoExternoOrden() {
        return codigoExternoOrden;
    }

    public void setCodigoExternoOrden(String codigoExternoOrden) {
        this.codigoExternoOrden = codigoExternoOrden;
    }

    public int getCodigoUsuarioGeneracion() {
        return codigoUsuarioGeneracion;
    }

    public void setCodigoUsuarioGeneracion(int codigoUsuarioGeneracion) {
        this.codigoUsuarioGeneracion = codigoUsuarioGeneracion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public OrdenVO(int codigo, int localCodigo, int usuarioCodigo, 
            LocalDateTime fechaGeneracion, LocalDateTime fechaARealizar, 
            String codigoExternoOrden, int codigoUsuarioGeneracion, 
            String estado) {
        this.codigo = codigo;
        this.localVO.setCodigo(localCodigo);
        this.usuarioCodigo = usuarioCodigo;
        this.fechaGeneracion = fechaGeneracion;
        this.fechaARealizar = fechaARealizar;
        this.codigoExternoOrden = codigoExternoOrden;
        this.codigoUsuarioGeneracion = codigoUsuarioGeneracion;
        this.estado = estado;
    }

    public OrdenVO() {
    }
    
    

}
