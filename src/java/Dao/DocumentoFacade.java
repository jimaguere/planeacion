/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import entidad.Documento;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author mateo
 */
@Stateless
public class DocumentoFacade extends AbstractFacade<Documento> {
    @PersistenceContext(unitName = "PlaneacionPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DocumentoFacade() {
        super(Documento.class);
    }
    
    public Documento findByDocumento(Documento documento){
        try{
            Query q=getEntityManager().createNamedQuery("Documento.findByCampos",Documento.class);
            q.setParameter("autores", documento.getAutores());
            q.setParameter("titulo", documento.getTitulo());
            q.setParameter("fecha", documento.getFecha());
            return (Documento) q.getSingleResult();
        }catch(Exception e){}
        return null;
    }
    
    public List<Documento> findByRango(int li, int max) {
        Query cq = getEntityManager().createNamedQuery("Documento.findByRango");
        cq.setFirstResult(li);
        cq.setMaxResults(max);
        return cq.getResultList();
    }
    
}
