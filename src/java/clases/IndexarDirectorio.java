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
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;

/**
 *
 * @author mateo
 */
public class IndexarDirectorio {

    File dir ;
    
    public void recorrerDirectorio(String directorio,String indice) throws IOException{       
        dir=new File(directorio);
        File[] files = dir.listFiles();
   
        int contador=1;
        for (File file : files){ 
            if (file.getName().endsWith(".pdf")) { 
                IndexarDocumento indiceArchivos=new IndexarDocumento();
                InputStream archivoI = new FileInputStream(file);
                ExtracPdfText texto=new ExtracPdfText(archivoI);
                Documento doc=new Documento();
                doc.setId(Integer.parseInt(file.getName().substring(0,file.getName().lastIndexOf(".")).replaceAll(" ","").trim()));
                doc.setTitulo(texto.extraerTitulo());
                doc.setAutores("grias");
                indiceArchivos.setDocumento(doc);
                indiceArchivos.setContenido(texto.extraerTexto());
                indiceArchivos.indexarTexto(indice);
                archivoI.close();
                texto.cerrar();   
                System.out.println(contador+" -------- "+doc.getId());
                contador++;
            }
             
        }
     
    }
    
    public static void main(String arg[]) throws IOException{
        IndexarDirectorio dir=new IndexarDirectorio();
        IndexarDocumento index=new IndexarDocumento();
        index.crearIndice("/opt/indice/");
        index.cerrar();
        dir.recorrerDirectorio("/opt/documentos pdfs/","/opt/indice/" );
    }
       
}
