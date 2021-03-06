/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Ricardo Timaran Pereira Jimmy Mateo Guerrero
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import Dao.VocabularioFacade;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.exceptions.CryptographyException;
import org.apache.pdfbox.exceptions.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.BadSecurityHandlerException;
import org.apache.pdfbox.util.PDFTextStripper;

/**
 *
 * @author mateo
 */
public class ExtracPdfText {

    private File archivoPdf;
    //private PDFText pdfText;
    private final PDDocument doc;

    public ExtracPdfText(InputStream archivo) throws IOException {
        long inicioConexion=System.currentTimeMillis();
        doc = PDDocument.load(archivo);
        long finConexion=System.currentTimeMillis();
       // System.out.println("constructor en:"+(finConexion-inicioConexion)); 

        //pdfText = new PDFText(archivo, null);
    }

    public File getArchivoPdf() {
        return archivoPdf;
    }

    public void setArchivoPdf(File archivoPdf) {
        this.archivoPdf = archivoPdf;
    }

    public String extraerTexto() throws IOException {
       long inicioConexion=System.currentTimeMillis();
        PDFTextStripper stripper = new PDFTextStripper();
        stripper.setStartPage(1);
        stripper.setEndPage(stripper.getEndPage());
        stripper.setAddMoreFormatting(true);
        String docText = stripper.getText(doc);
        long finConexion=System.currentTimeMillis();
       // System.out.println("contenido en:"+(finConexion-inicioConexion)); 
        //return docText;
        return docText.replaceAll("", "").replaceAll("~", "").replaceAll("", "").replaceAll(" ", "").replaceAll("`", "").replaceAll("", "").replaceAll("", "").replaceAll(" ", "").replaceAll("", "").replaceAll("", "").replaceAll("", "");
    }

    public List<String> extraerPalabras(VocabularioFacade vocabulario, String contenido) {
        Set<String> palabras = new HashSet<String>();
        String[] palabrasContenido = contenido.split(" ");

        for (int i = 0; i < palabrasContenido.length; i++) {
            if (vocabulario.findAllStopWords(palabrasContenido[i]) == null) {
                palabras.add(palabrasContenido[i]);
            }
        }

        List<String> palabrasDef = new ArrayList<String>(palabras);
        return palabrasDef;
    }

    public void cerrar() throws IOException {
        doc.close();
    }

    public void extarerUnaPagina() throws IOException {
        PDFTextStripper stripper = new PDFTextStripper();
        stripper.setStartPage(3);
        stripper.setEndPage(3);
        System.out.println(stripper.getText(doc));

    }

    public String extraerTitulo() throws IOException {
        long inicioConexion=System.currentTimeMillis();
        doc.setAllSecurityToBeRemoved(true);

        PDFTextStripper stripper = new PDFTextStripper();
        stripper.setStartPage(1);
        stripper.setEndPage(1);
        stripper.setAddMoreFormatting(true);

        String titulo = stripper.getText(doc);
        long finConexion=System.currentTimeMillis();
       // System.out.println("titulo en:"+(finConexion-inicioConexion)); 
        String[] resultado = titulo.split("\n");
        String nuevoTitulo = "";
        boolean b = false;
        int contador = 0;
        for (int i = 0; i < resultado.length; i++) {
            if (resultado[i].trim().length() > 0) {
                b = true;
            }
            if (resultado[i].trim().length() == 0 && b) {
                contador++;
                if (contador > 10) {
                    break;
                }
            } else {
                nuevoTitulo = nuevoTitulo + " " + resultado[i];
            }
        }
        //return nuevoTitulo;
        return nuevoTitulo.trim().replaceAll("", "").replaceAll("~", "").replaceAll("", "").replaceAll(" ", "").replaceAll("`", "").replaceAll("", "").replaceAll("", "").replaceAll("", "").replaceAll("", "").replaceAll("", "");
    }

    public String extraerPalabrasClave() throws IOException {
        PDFTextStripper stripper = new PDFTextStripper();
        return stripper.getWordSeparator();
    }
    //http://www.adictosaltrabajo.com/tutoriales/tutoriales.php?pagina=quartz
    //http://www.programandoconcafe.com/2011/02/java-automatizacion-de-procesos-o.html
    //http://garabatoslinux.net/tareas-programadas-en-java-quartz-scheduler-y-cronexpressions.html
    //http://sg.com.mx/revista/18/una-introduccion-quartz-calendarizacion-tareas-java#.VOIEM_6QmMA



    public static void main(String arg[]) throws IOException, BadSecurityHandlerException, CryptographyException, InvalidPasswordException, COSVisitorException {
        InputStream archivoI = new FileInputStream("/opt/documentos pdfs/69963.pdf");
        ExtracPdfText texto = new ExtracPdfText(archivoI);
        //texto.decencriptar();
        System.out.println(texto.extraerTexto());
        texto.cerrar();
        
    }
}