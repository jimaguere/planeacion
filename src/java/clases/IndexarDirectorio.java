/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import entidad.Documento;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.swing.filechooser.FileFilter;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.util.Version;
import org.apache.pdfbox.exceptions.CryptographyException;
import org.apache.pdfbox.exceptions.InvalidPasswordException;

/**
 *
 * @author mateo
 */
public class IndexarDirectorio {

    File dir;

    public void recorrerDirectorioArchivo(String directorio, String indice) throws IOException {
        dir = new File(directorio);
        File[] files = dir.listFiles();
        for (int i = 89; i < files.length; i++) {
            File file = files[i];
            if (file.getName().endsWith(".pdf") && file.length() <= 11930889 && file.getName().contains("85070")) {
                System.out.println(i);
                return;
            }
        }
    }

    public void recorrerDirectorio(String directorio, String indice) throws IOException, CryptographyException, InvalidPasswordException {
        dir = new File(directorio);
        File[] files = dir.listFiles();
        int contador = 1;
        String idNoIndexados = "";
        //quedo aqui 84749
        for (int i = 307; i < files.length; i++) {
            try {
                File file = files[i];
                if (file.getName().endsWith(".pdf")) {
                    IndexarDocumento indiceArchivos = new IndexarDocumento();
                    InputStream archivoI = new FileInputStream(file);

                    ExtracPdfText texto = new ExtracPdfText(archivoI);
                    Documento doc = new Documento();
                    doc.setId(Integer.parseInt(file.getName().substring(0, file.getName().lastIndexOf(".")).replaceAll(" ", "").trim()));
                    doc.setTitulo(texto.extraerTitulo());
                    doc.setAutores("grias");
                    indiceArchivos.setDocumento(doc);
                    indiceArchivos.setContenido(texto.extraerTexto());
                    indiceArchivos.indexarTexto(indice);
                    archivoI.close();
                    texto.cerrar();
                    System.out.println(contador + " -------- " + doc.getId());
                    contador++;
                }
            } catch (Exception e) {
                idNoIndexados = idNoIndexados + files[i].getName() + "____" + e.toString() + "\n";
            }
        }
        System.out.println("archivos No indexados");
        System.out.println(idNoIndexados);

    }

    public void crearArchivo(String nombreArchivo, String contenido) {
        FileWriter fw = null; // la extension al archivo
        try {
            fw = new FileWriter(nombreArchivo);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter salArch = new PrintWriter(bw);
            salArch.write(contenido);
            salArch.close();
        } catch (IOException ex) {
        }
    }
    
    public void archivosNoIndexados(String archivo){
         FileWriter fw = null; // la extension al archivo
        try {
            fw = new FileWriter("opt/mateo/no_idexados.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter salArch = new PrintWriter(bw);
            salArch.println(archivo);
            salArch.close();
        } catch (IOException ex) {
        }
    }
    

    public void guardarTextoPdfTxt(String carpetaPdf, String carpetaTxt) {
        File origenPdf = new File(carpetaPdf);
        File[] archivosPdf = origenPdf.listFiles();
        for (int i = 75; i < archivosPdf.length; i++) {
            try {
                File file = archivosPdf[i];
                if (file.getName().endsWith(".pdf") && file.length() > 11930889) {
                    InputStream archivoI = new FileInputStream(file);
                    ExtracPdfText texto = new ExtracPdfText(archivoI);
                    String contenido = texto.extraerTexto();
                    crearArchivo(carpetaTxt+file.getName().toLowerCase().replaceAll("pdf", "txt"), contenido);
                    System.out.println(i + " -------- " + file.getName());
                }
            } catch (Exception e) {
                this.archivosNoIndexados(archivosPdf[i].getName());
            }
        }

    }
    
    public void luceneEspaniol(){
        SpanishAnalyzer esp=new SpanishAnalyzer(Version.LUCENE_46);
        System.out.println(esp.getStopwordSet().toString());
    }

    public static void main(String arg[]) throws IOException, CryptographyException, InvalidPasswordException {
        IndexarDirectorio dir = new IndexarDirectorio();
        IndexarDocumento index = new IndexarDocumento();
        //index.crearIndice("/opt/indice_espaniol/");
        //index.cerrar();
        // dir.recorrerDirectorioArchivo("/opt/documentos pdfs/","/opt/indice/");
        //System.out.println(index.getWriter("/opt/indice_espaniol/").maxDoc());
        //dir.recorrerDirectorio("/opt/documentos pdfs/", "/opt/indice_espaniol/");
         dir.guardarTextoPdfTxt("/opt/documentos pdfs/", "/opt/mateo/tesisTxt/");
       // dir.luceneEspaniol();
    }
}