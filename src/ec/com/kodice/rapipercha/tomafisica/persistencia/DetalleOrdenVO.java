/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.com.kodice.rapipercha.tomafisica.persistencia;

import ec.com.kodice.rapipercha.administracion.persistencia.ProductoVO;
import java.time.LocalDateTime;

/**
 * Esta clase contiene los atributos y metodos de un DetalleOrdenVO
 * @author Benjamin Cepeda
 * @version v1.0
 * @date 2021/01/12 
 */
public class DetalleOrdenVO {
    private int codigo;
    private int ordenCodigo;
    private ProductoVO producto = new ProductoVO();
    private float cantidadMinima;
    private float existencia;
    private float cantidadRevisada;
    private float cantidadMalEstado;
    private float cantidadVencido;
    private LocalDateTime fechaProceso;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getOrdenCodigo() {
        return ordenCodigo;
    }

    public void setOrdenCodigo(int ordenCodigo) {
        this.ordenCodigo = ordenCodigo;
    }


    public ProductoVO getProducto() {
        return producto;
    }

    public void setProducto(ProductoVO producto) {
        this.producto = producto;
    }

    public float getCantidadMinima() {
        return cantidadMinima;
    }

    public void setCantidadMinima(float cantidadMinima) {
        this.cantidadMinima = cantidadMinima;
    }

    public float getExistencia() {
        return existencia;
    }

    public void setExistencia(float existencia) {
        this.existencia = existencia;
    }

    public float getCantidadRevisada() {
        return cantidadRevisada;
    }

    public void setCantidadRevisada(float cantidadRevisada) {
        this.cantidadRevisada = cantidadRevisada;
    }

    public float getCantidadMalEstado() {
        return cantidadMalEstado;
    }

    public void setCantidadMalEstado(float cantidadMalEstado) {
        this.cantidadMalEstado = cantidadMalEstado;
    }

    public float getCantidadVencido() {
        return cantidadVencido;
    }

    public void setCantidadVencido(float cantidadVencido) {
        this.cantidadVencido = cantidadVencido;
    }

    public LocalDateTime getFechaProceso() {
        return fechaProceso;
    }

    public void setFechaProceso(LocalDateTime fechaProceso) {
        this.fechaProceso = fechaProceso;
    }

    public DetalleOrdenVO(int codigo, int ordenCodigo, int productoCodigo, 
            float cantidadMinima, float existencia, float cantidadRevisada, 
            float cantidadMalEstado, float cantidadVencido, 
            LocalDateTime fechaProceso) {
        this.codigo = codigo;
        this.setOrdenCodigo(ordenCodigo);
        this.getProducto().setCodigo( productoCodigo);
        this.cantidadMinima = cantidadMinima;
        this.existencia = existencia;
        this.cantidadRevisada = cantidadRevisada;
        this.cantidadMalEstado = cantidadMalEstado;
        this.cantidadVencido = cantidadVencido;
        this.fechaProceso = fechaProceso;
    }
    
    public DetalleOrdenVO(int ordenCodigo, int productoCodigo, 
            float cantidadMinima, float existencia, float cantidadRevisada, 
            float cantidadMalEstado, float cantidadVencido, 
            LocalDateTime fechaProceso) {
        this.setOrdenCodigo(ordenCodigo);
        this.getProducto().setCodigo( productoCodigo);
        this.cantidadMinima = cantidadMinima;
        this.existencia = existencia;
        this.cantidadRevisada = cantidadRevisada;
        this.cantidadMalEstado = cantidadMalEstado;
        this.cantidadVencido = cantidadVencido;
        this.fechaProceso = fechaProceso;
    }
    

    public DetalleOrdenVO() {
    }
    
}
