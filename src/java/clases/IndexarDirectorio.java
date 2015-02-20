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
        //quedo aqui 86499
        for (int i = 6538; i < files.length; i++) {
            try {
                File file = files[i];
                if (file.getName().endsWith(".pdf") && file.length() > 11930889) {
                    IndexarDocumento indiceArchivos = new IndexarDocumento();
                    long inicioConexion = System.currentTimeMillis();
                    InputStream archivoI = new FileInputStream(file);
                    long finConexion = System.currentTimeMillis();
                    System.out.println("inputStream en:" + (finConexion - inicioConexion));
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
            }
        }

    }

    public static void main(String arg[]) throws IOException, CryptographyException, InvalidPasswordException {
        IndexarDirectorio dir = new IndexarDirectorio();
        IndexarDocumento index = new IndexarDocumento();
        // index.crearIndice("/opt/indice/");
        // index.cerrar();
        // dir.recorrerDirectorioArchivo("/opt/documentos pdfs/","/opt/indice/");
        System.out.println(index.getWriter("/opt/indice/").maxDoc());
        //dir.recorrerDirectorio("/opt/documentos pdfs/", "/opt/indice/");
    }
}
