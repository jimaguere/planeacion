/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import Dao.DocumentoFacade;
import Dao.VocabularioFacade;
import Dto.AutorDto;
import clases.ExtracPdfText;
import clases.IndexarDocumento;
import entidad.Documento;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author mateo
 */
@ManagedBean
@SessionScoped
public class DocumentoControlador {

    private static Logger logger = Logger.getLogger(DocumentoControlador.class.getName());
    private boolean skip;
    private Documento documento;
    private UploadedFile documentoPdf;
    private String titulo;
    private String contenido;
    private List<AutorDto> listaAutor;
    private String ruta;
    private List<String> palbrasDocumento;
    private List<Documento> listaDocumentos;
    private int li;
    private int sup;
    private int antLi;
    private int limite;
    private boolean cambio;
    private boolean operacion;
    @EJB
    private DocumentoFacade docuemntoFacade;
    @EJB
    private VocabularioFacade vocabularioFacade;

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public UploadedFile getDocumentoPdf() {
        return documentoPdf;
    }

    public void setDocumentoPdf(UploadedFile documentoPdf) {
        this.documentoPdf = documentoPdf;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public List<AutorDto> getListaAutor() {
        return listaAutor;
    }

    public void setListaAutor(List<AutorDto> listaAutor) {
        this.listaAutor = listaAutor;
    }

    public List<Documento> getListaDocumentos() {
        return listaDocumentos;
    }

    public void setListaDocumentos(List<Documento> listaDocumentos) {
        this.listaDocumentos = listaDocumentos;
    }

    public int getLi() {
        return li;
    }

    public void setLi(int li) {
        this.li = li;
    }

    public int getSup() {
        return sup;
    }

    public void setSup(int sup) {
        this.sup = sup;
    }

    public int getAntLi() {
        return antLi;
    }

    public void setAntLi(int antLi) {
        this.antLi = antLi;
    }

    public int getLimite() {
        return limite;
    }

    public void setLimite(int limite) {
        this.limite = limite;
    }

    public boolean isCambio() {
        return cambio;
    }

    public void setCambio(boolean cambio) {
        this.cambio = cambio;
    }

    public boolean isOperacion() {
        return operacion;
    }

    public void setOperacion(boolean operacion) {
        this.operacion = operacion;
    }
    
    

    /**
     * Creates a new instance of DocumentoControlador
     */
    public String onFlowProcess(FlowEvent event) {
        logger.info("Current wizard step:" + event.getOldStep());
        logger.info("Next step:" + event.getNewStep());

        if (skip) {
            skip = false;	//reset in case user goes back
            return "confirm";
        } else {
            return event.getNewStep();
        }
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {
        try {
            this.documentoPdf = event.getFile();
            ExtracPdfText texto = new ExtracPdfText(this.documentoPdf.getInputstream());
            this.titulo = texto.extraerTitulo();
            this.contenido = texto.extraerTexto();
            this.palbrasDocumento = texto.extraerPalabras(vocabularioFacade, this.titulo);
            texto.cerrar();
            FacesMessage msg = new FacesMessage("Archivo " + event.getFile().getFileName() + "", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        } catch (FileNotFoundException ex) {
            this.contenido = "";
            Logger.getLogger(DocumentoControlador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            this.contenido = "";
            Logger.getLogger(DocumentoControlador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            this.contenido = "";
            Logger.getLogger(DocumentoControlador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (java.lang.NoClassDefFoundError ex) {
            this.contenido = "";
            Logger.getLogger(DocumentoControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error En Cargar " + event.getFile().getFileName() + " Favor Intentar Con Otro Archivo", "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void removerAutor(int i) {
        listaAutor.remove(i);
    }

    public void nuevoAutor() {
        AutorDto autor = new AutorDto();
        listaAutor.add(autor);
    }

    public boolean validarAutores(List<AutorDto> estudiantes) {
        if (estudiantes == null) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Información de autores incompleta ", ""));
            return false;
        }
        if (estudiantes.isEmpty()) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Información de autores incompleta ", ""));
            return false;
        }

        for (int i = 0; i < estudiantes.size(); i++) {
            if (estudiantes.get(i).getNombres() == null || estudiantes.get(i).getApellidos() == null) {
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Información de autores incompleta ", ""));
                return false;
            }
            if (estudiantes.get(i).getNombres().trim().length() == 0 || estudiantes.get(i).getApellidos().trim().length() == 0) {
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Información de autores incompleta ", ""));
                return false;
            }
        }
        return true;
    }

    public boolean guardarArchivo(Integer nombre) {
        try {
            if (this.documentoPdf == null) {
                return true;
            }
            //File documento=new File(ruta+"resources/pdf/"+nombre);
            ServletContext servContx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String rutaAlt=(String) servContx.getRealPath("/")+"resources/pdf/";
            OutputStream documento = new FileOutputStream(new File(rutaAlt + nombre + ".pdf"));
            InputStream archivoIn = this.documentoPdf.getInputstream();
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = archivoIn.read(bytes)) != -1) {
                documento.write(bytes, 0, read);
            }
            archivoIn.close();
            documento.flush();
            documento.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void crearDocumento() {
        try {
            if (!validarAutores(listaAutor)) {
                return;
            }
            this.documento = new Documento();
            this.documento.setTitulo(this.titulo);
            this.documento.setAutores("");
            this.documento.setVistos(0);
            this.documento.setRuta(ruta);
            this.documento.setFecha(new Date());

            for (AutorDto autorDto : this.listaAutor) {
                this.documento.setAutores(this.documento.getAutores() + "" + autorDto.getNombres() + " " + autorDto.getApellidos() + "\n");
            }
            docuemntoFacade.create(documento);
            this.documento = docuemntoFacade.findByDocumento(this.documento);
            if (guardarArchivo(documento.getId())) {
                IndexarDocumento index = new IndexarDocumento();
                index.setDocumento(documento);
                index.setContenido(contenido);
                index.indexarTexto(ruta);
                String nombreAutores[] = this.documento.getAutores().split(" ");
                this.palbrasDocumento.addAll(Arrays.asList(nombreAutores));
                for (String palabra : palbrasDocumento) {
                    try {
                        this.vocabularioFacade.crearVocabulario(palabra);
                    } catch (Exception e) {
                    }
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Registro Exitoso", ""));
                this.iniciar();

            } else {
                docuemntoFacade.remove(documento);
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Al Crear Documento ", ""));
        }
    }
    
    public void modificarDocumento(){
        try {
            if (!validarAutores(listaAutor)) {
                return;
            }
            this.documento.setTitulo(this.titulo);
            this.documento.setAutores("");
            this.documento.setVistos(0);
            this.documento.setRuta(ruta);
            this.documento.setFecha(new Date());

            for (AutorDto autorDto : this.listaAutor) {
                this.documento.setAutores(this.documento.getAutores() + "" + autorDto.getNombres() + " " + autorDto.getApellidos() + "\n");
            }
            docuemntoFacade.edit(documento);
            if (guardarArchivo(documento.getId())) {
                IndexarDocumento index = new IndexarDocumento();
                index.setDocumento(documento);
                index.setContenido(contenido);
                index.modificarIndice(ruta);
                String nombreAutores[] = this.documento.getAutores().split(" ");
                this.palbrasDocumento.addAll(Arrays.asList(nombreAutores));
                for (String palabra : palbrasDocumento) {
                    try {
                        this.vocabularioFacade.crearVocabulario(palabra);
                    } catch (Exception e) {
                    }
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Actualización Exitosa", ""));
            } 

        } catch (Exception e) {
            System.out.println("error:"+e.toString());
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Al Crear Documento ", ""));
        }
    }
    
    public void eliminarDocumento(Documento documento){
        try{
            this.docuemntoFacade.remove(documento);
            IndexarDocumento index = new IndexarDocumento();
            index.setDocumento(documento);
            index.eliminarDocumento(ruta);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Documento Eliminado", ""));
        }catch(Exception e){
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Al Eliminar DOcumento ", ""));
        }
        
    }

    public void sig() {
        if (cambio) {
            listaDocumentos = this.docuemntoFacade.findByRango(li, 10);
            antLi = li;
            li = li + 10;
            limite = limite + 10;
            if (li >= sup) {
                li = sup;
                limite = sup;
            }

        } else {
            listaDocumentos = this.docuemntoFacade.findByRango(li + 10, 10);
            li = li + 20;
            limite = limite + 10;
            if (li >= sup) {
                li = sup;
                limite = sup;
            }
        }
        cambio = true;
    }

    public void ant() {
        if (li == sup) {
            listaDocumentos = this.docuemntoFacade.findByRango(antLi - 10, 10);
            li = antLi - 10;
            limite = antLi;
        } else {
            if (!cambio) {
                listaDocumentos = this.docuemntoFacade.findByRango(li - 10, 10);
                li = li - 10;
                limite = limite - 10;
            } else {
                listaDocumentos = this.docuemntoFacade.findByRango(li - 20, 10);
                li = li - 20;
                limite = limite - 10;
            }
        }
        cambio = false;
    }

    public String asignarDocumento(Documento documento) {
        ServletContext servContx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String rutaAlt=(String) servContx.getRealPath("/")+"resources/pdf/";
        this.documentoPdf = null;
        this.documento = documento;
        this.titulo = documento.getTitulo();
        this.listaAutor = new ArrayList<AutorDto>();
        this.palbrasDocumento=new ArrayList<String>();
        String[] autores = documento.getAutores().split("\n");
        for (int i = 0; i < autores.length; i++) {
            AutorDto autor = new AutorDto();
            if (autores[i].split(" ").length == 2) {
                autor.setNombres(autores[i].split(" ")[0]);
                autor.setApellidos(autores[i].split(" ")[1]);
            }
            if (autores[i].split(" ").length == 4) {
                autor.setNombres(autores[i].split(" ")[0] + " " + autores[i].split(" ")[1]);
                autor.setApellidos(autores[i].split(" ")[2] + " " + autores[i].split(" ")[3]);
            }
            if (autores[i].split(" ").length == 3) {
                autor.setNombres(autores[i].split(" ")[0]);
                autor.setApellidos(autores[i].split(" ")[1] + " " + autores[i].split(" ")[2]);
            }
            this.listaAutor.add(autor);
            this.operacion=false;
        }
        InputStream archivoI;
        try {
            archivoI = new FileInputStream(rutaAlt + this.documento.getId() + ".pdf");
            ExtracPdfText texto = new ExtracPdfText(archivoI);
            this.contenido = texto.extraerTexto();
            this.palbrasDocumento = texto.extraerPalabras(vocabularioFacade, this.titulo);
            texto.cerrar();
        } catch (FileNotFoundException ex) {
            this.contenido = "";
            Logger.getLogger(DocumentoControlador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            this.contenido = "";
            Logger.getLogger(DocumentoControlador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            this.contenido = "";
            Logger.getLogger(DocumentoControlador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (java.lang.NoClassDefFoundError ex) {
            this.contenido = "";
            Logger.getLogger(DocumentoControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "CrearDocumento.xhtml";
    }

    @PostConstruct
    public void iniciar() {
        this.contenido = "";
        this.titulo = "";
        this.documento = new Documento();
        this.listaAutor = new ArrayList<AutorDto>();
        this.documentoPdf = null;
        RepositorioControlador repositorio = (RepositorioControlador) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("repositorioControlador");
        ruta = repositorio.getRutaOntologia();
        listaDocumentos = this.docuemntoFacade.findByRango(0, 10);
        cambio = true;
        li = 10;
        limite = 10;
        sup = this.docuemntoFacade.count();
        this.operacion=true;
    }

    public DocumentoControlador() {
    }
}
