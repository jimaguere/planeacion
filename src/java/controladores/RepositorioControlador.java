/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import clases.IndexarDocumento;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author mateo
 */
@ManagedBean
@ApplicationScoped
public class RepositorioControlador {

    /**
     * Creates a new instance of RepositorioControlador
     */
    private String rutaOntologia;
    private boolean repositorio;

    public String getRutaOntologia() {
        return rutaOntologia;
    }

    public void setRutaOntologia(String rutaOntologia) {
        this.rutaOntologia = rutaOntologia;
    }

    public boolean isRepositorio() {
        return repositorio;
    }

    public void setRepositorio(boolean repositorio) {
        this.repositorio = repositorio;
    }
    
    

    public void crearRepositorio() {
        try {
            if(rutaOntologia.lastIndexOf("/")!=rutaOntologia.length()-1 || rutaOntologia.contains(" ")){
                 FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "La ruta debe terminar en / y o no tener espacios en blanco ", ""));
                 return;
            }
            IndexarDocumento index = new IndexarDocumento();
            index.crearIndice(rutaOntologia);
            index.cerrar();
            this.repositorio=true;
               FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Repositorio Creado ", ""));
        } catch (Exception e) {
               FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al crear repositorio ", ""));
     
        }
    }
    
    @PostConstruct
    public void iniciarServer(){
        this.repositorio=false;
    }

    public RepositorioControlador() {
    }
}
