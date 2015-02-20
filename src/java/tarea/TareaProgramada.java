/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tarea;

import Dao.DocumentoFacade;
import entidad.Documento;
import java.util.List;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author mateo
 */
public class TareaProgramada implements Job{

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        DocumentoFacade docEjb=(DocumentoFacade)jec.getJobDetail().getJobDataMap().get("ejb");
        System.out.println("doc:"+docEjb.count());
        List<Documento>lista=docEjb.findAll();
        for(Documento doc:lista){
            System.out.println(doc.getTitulo());
        }
    }
    
}
