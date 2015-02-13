/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import Dao.DocumentoFacade;
import Dao.VocabularioFacade;
import entidad.Documento;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 *
 * @author mateo
 */
@ManagedBean(name = "buscadorControlador")
@SessionScoped
public class BuscadorControlador {

    private String rutaIndice;
    private FSDirectory dir;
    private IndexSearcher searcher;
    @EJB
    VocabularioFacade vocabularioFacadel;
    @EJB
    DocumentoFacade docuemntoFacade;
    private String tipoBusqueda;
    private String cadenaBusqueda;
    private String contenido;
    private List<Documento> listaDocuementos;
    private Documento documentoSeleccionado;

    public String getTipoBusqueda() {
        return tipoBusqueda;
    }

    public void setTipoBusqueda(String tipoBusqueda) {
        this.tipoBusqueda = tipoBusqueda;
    }

    public String getCadenaBusqueda() {
        return cadenaBusqueda;
    }

    public void setCadenaBusqueda(String cadenaBusqueda) {
        this.cadenaBusqueda = cadenaBusqueda;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public List<Documento> getListaDocuementos() {
        return listaDocuementos;
    }

    public void setListaDocuementos(List<Documento> listaDocuementos) {
        this.listaDocuementos = listaDocuementos;
    }

    public Documento getDocumentoSeleccionado() {
        return documentoSeleccionado;
    }

    /**
     * Creates a new instance of BuscadorControlador
     */
    public BuscadorControlador() {
        this.tipoBusqueda = "1";
    }

    @PostConstruct
    public void iniciar() throws IOException {
        RepositorioControlador repositorio = (RepositorioControlador) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("repositorioControlador");
        if (!repositorio.isRepositorio()) {
            return;
        }
        rutaIndice = repositorio.getRutaOntologia();
        dir = FSDirectory.open(new File(rutaIndice));
        searcher = new IndexSearcher(IndexReader.open(dir));
        searcher.setSimilarity(new LMDirichletSimilarity((float) 1));
        this.tipoBusqueda = "1";
        this.cadenaBusqueda = "";
        this.contenido = "1";
    }

    public List<String> completeGeneral(String query) {
        query = query.replaceAll("'", "");
        List<String> resul = new ArrayList<String>();
        List<Object[]> listaWords;
        String[] listaTitulo = query.split(" ");
        String res = "";
        for (int i = 0; i < listaTitulo.length - 1; i++) {
            if (i == 0) {
                res = listaTitulo[i];
            } else {
                res = res + " " + listaTitulo[i];
            }
        }
        listaWords = this.vocabularioFacadel.findAllJaroWordsComplet(listaTitulo[listaTitulo.length - 1]);
        if (listaWords.isEmpty()) {
            return resul;
        }
        for (int i = 0; i < listaTitulo.length; i++) {
            if (i > 5) {
                break;
            }
            if (res.equals("")) {
                resul.add(listaWords.get(i)[0].toString());
            } else {
                resul.add(res + " " + listaWords.get(i)[0].toString());
            }
        }
        return resul;
    }

    public String depurarContenidoTodo() {
        String busquedaContenido = "(\"" + this.cadenaBusqueda + "\")";
        return busquedaContenido;
    }

    public String depurarContenidoAlguno() {
        String[] busquedaContenido = this.cadenaBusqueda.split(" ");
        String buscar = "";
        for (int i = 0; i < busquedaContenido.length; i++) {
            buscar = buscar + "" + busquedaContenido[i] + " ";
        }
        return buscar.substring(0, buscar.length() - 1);
    }

    public String depurarContenidoTodoCorrec() {
        String[] busquedaContenido = this.cadenaBusqueda.split(" ");
        String buscar = "";
        for (int i = 0; i < busquedaContenido.length; i++) {
            buscar = buscar + "+" + busquedaContenido[i] + "~ ";
        }
        return buscar.substring(0, buscar.length() - 1);
    }

    public String depurarContenidoAlgunoCorrec() {
        String[] busquedaContenido = this.cadenaBusqueda.split(" ");
        String buscar = "";
        for (int i = 0; i < busquedaContenido.length; i++) {
            buscar = buscar + "" + busquedaContenido[i] + "~ ";
        }
        return buscar.substring(0, buscar.length() - 1);
    }

    public void generarResultados(TopDocs hits) throws IOException {
        this.listaDocuementos = new ArrayList<Documento>();

        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            try {
                Document doc = searcher.doc(scoreDoc.doc);

                Documento documento = new Documento();
                this.listaDocuementos.add(documento);
                documento.setId(Integer.parseInt(doc.get("id").replaceAll(".pdf", "").replace(".PDF", "")));
                documento.setTitulo(doc.get("tituloCampo"));
                documento.setAutores(doc.get("autorCampo"));
            } catch (Exception e) {
            }
        }
        String url = "./Resultado.xhtml";
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.getExternalContext().redirect(url);
    }

    public void buscar() throws ParseException, IOException {
        if (this.cadenaBusqueda == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("No Encontraron resultados para su busqueda", ""));
            return;
        }
        String cont = this.cadenaBusqueda;
        if (this.contenido.equals("1")) {
            cont = this.depurarContenidoTodo();
        } else {
            cont = this.depurarContenidoAlguno();
        }

        QueryParser parser = null;
        if (this.tipoBusqueda.equals("1")) {
            parser = new QueryParser(Version.LUCENE_46,
                    "contenido",
                    new StandardAnalyzer(
                    Version.LUCENE_46));
        } else if (this.tipoBusqueda.equals("2")) {
            parser = new QueryParser(Version.LUCENE_46,
                    "titulo",
                    new StandardAnalyzer(
                    Version.LUCENE_46));
        } else if (this.tipoBusqueda.equals("3")) {
            parser = new QueryParser(Version.LUCENE_46,
                    "autor",
                    new StandardAnalyzer(
                    Version.LUCENE_46));
        }
        parser.setAllowLeadingWildcard(true);
        Query query = parser.parse(cont);
        TopDocs hits = searcher.search(query, 30);
        if (hits.totalHits == 0) {
            cont = this.cadenaBusqueda;
            if (!this.contenido.equals("1") || !this.tipoBusqueda.equals("1")) {
                cont = this.depurarContenidoAlgunoCorrec();
                query = parser.parse(cont);
                hits = searcher.search(query, 30);
            }
        }
        this.generarResultados(hits);
    }

    public String asignarDocumento(Documento doc) {
        this.documentoSeleccionado = this.docuemntoFacade.find(doc.getId());
        this.documentoSeleccionado.setVistos(documentoSeleccionado.getVistos() + 1);
        this.docuemntoFacade.edit(this.documentoSeleccionado);
        return "Documento.xhtml";
    }
}
