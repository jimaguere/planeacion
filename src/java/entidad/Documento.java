/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author mateo
 */
@Cacheable(false)
@Entity
@Table(name = "documento", catalog = "planeacion", schema = "public")
@NamedQueries({
    @NamedQuery(name = "Documento.findAll", query = "SELECT d FROM Documento d"),
    @NamedQuery(name = "Documento.findById", query = "SELECT d FROM Documento d WHERE d.id = :id"),
    @NamedQuery(name = "Documento.findByTitulo", query = "SELECT d FROM Documento d WHERE d.titulo = :titulo"),
    @NamedQuery(name = "Documento.findByAutores", query = "SELECT d FROM Documento d WHERE d.autores = :autores"),
    @NamedQuery(name = "Documento.findByCampos", query="SELECT d from Documento d WHERE d.autores=:autores and d.titulo=:titulo and d.fecha=:fecha"),
    @NamedQuery(name = "Documento.findByRango", query="SELECT d from Documento d ORDER BY d.fecha DESC")
})
public class Documento implements Serializable {
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Size(max = 2147483647)
    @Column(name = "ruta", length = 2147483647)
    private String ruta;
    @Column(name = "vistos")
    private Integer vistos;
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="DOCUMENTO_ID_GENERATOR", sequenceName="documento_id_seq",allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DOCUMENTO_ID_GENERATOR")
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Size(max = 2147483647)
    @Column(name = "titulo", length = 2147483647)
    private String titulo;
    @Size(max = 2147483647)
    @Column(name = "autores", length = 2147483647)
    private String autores;

    public Documento() {
    }

    public Documento(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutores() {
        return autores;
    }

    public void setAutores(String autores) {
        this.autores = autores;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Documento)) {
            return false;
        }
        Documento other = (Documento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Documento[ id=" + id + " ]";
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public Integer getVistos() {
        return vistos;
    }

    public void setVistos(Integer vistos) {
        this.vistos = vistos;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
}
