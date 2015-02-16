/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import entidad.Documento;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
            if (file.getName().endsWith(".pdf") && file.length() <= 11930889 && file.getName().contains("67496")) {
                System.out.println(i);
                return;
            }

        }

    }

    public void recorrerDirectorio(String directorio, String indice) throws IOException, CryptographyException, InvalidPasswordException {
        dir = new File(directorio);
        File[] files = dir.listFiles();
        int contador = 1;
        for (int i = 1964; i < files.length; i++) {
            try {
                File file = files[i];
                if (file.getName().endsWith(".pdf") && file.length() <= 11930889) {
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
            } catch (Exception e) {}

        }

    }

    public static void main(String arg[]) throws IOException, CryptographyException, InvalidPasswordException {
        IndexarDirectorio dir = new IndexarDirectorio();
        //IndexarDocumento index=new IndexarDocumento();
        // index.crearIndice("/opt/indice/");
        // index.cerrar();
        //dir.recorrerDirectorioArchivo("/opt/documentos pdfs/","/opt/indice/");
        dir.recorrerDirectorio("/opt/documentos pdfs/", "/opt/indice/");
    }
}
