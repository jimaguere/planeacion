/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import entidad.Vocabulario;
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
public class VocabularioFacade extends AbstractFacade<Vocabulario> {

    @PersistenceContext(unitName = "PlaneacionPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VocabularioFacade() {
        super(Vocabulario.class);
    }

    public Object findAllStopWords(String query) {
        try {
            javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
            cq.select(cq.from(Vocabulario.class));
            return getEntityManager().createNativeQuery("SELECT ts_lexize('public.simple_dict',"
                    + "'" + query + "');").getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Object[]> findAllJaroWordsComplet(String query) {
        try {
            javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
            cq.select(cq.from(Vocabulario.class));
            return getEntityManager().createNativeQuery("select distinct palabra,"
                    + " jarowinkler((palabra) ,'" + query + "') as aceptacion"
                    + " from vocabulario"
                    + " where"
                    + " jarowinkler(palabra,'" + query + "')>0.6 order by aceptacion desc").getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public void crearVocabulario(String palabra){
        try{
            Query q=getEntityManager().createNativeQuery("insert into vocabulario(palabra) values(?)");
            q.setParameter(1, palabra);
            q.executeUpdate();
        }catch(Exception e){}
    }
}
