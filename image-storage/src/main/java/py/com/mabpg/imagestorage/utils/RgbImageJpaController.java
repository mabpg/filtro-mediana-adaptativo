package py.com.mabpg.imagestorage.utils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import py.com.mabpg.imagestorage.models.RgbImage;
import py.com.mabpg.imagestorage.models.RgbWindow;
import py.com.mabpg.imagestorage.models.Window;
import py.com.mabpg.imagestorage.utils.exceptions.PreexistingEntityException;

/**
 *
 * @author Derlis Argüello
 */
public class RgbImageJpaController implements Serializable {

    private EntityManagerFactory emf = Persistence
            .createEntityManagerFactory("py.com.mabpg_image-storage_jar_1.0-SNAPSHOTPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RgbImage rgbImage) throws PreexistingEntityException, Exception {

        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(rgbImage);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRgbImage(rgbImage.getIdRgbImage()) != null) {
                throw new PreexistingEntityException("RgbImage " + rgbImage + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public RgbImage findRgbImage(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RgbImage.class, id);
        } finally {
            em.close();
        }
    }
    
    public List<RgbImage> getImageTest(int minIndex, int maxIndex){
        EntityManager em = getEntityManager();
        return (List<RgbImage>)em.createNamedQuery("RgbImage.getImageTest")
                .setParameter("minIndex", Double.valueOf(minIndex))
                .setParameter("maxIndex", Double.valueOf(maxIndex))
                .getResultList();
    }
    
    public List<RgbImage> getNoiseImageByNoise(String noiseName, Double description){
        EntityManager em = getEntityManager();
        return (List<RgbImage>)em.createNamedQuery("RgbImage.getNoiseImageByNoise")
                .setParameter("noiseName", noiseName)
                .setParameter("description", description)
                .getResultList();
    }
    
    public List<Window> getWindowsList(int idRgbImage, int roiWindow) throws Exception {
        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery("RgbImage.getWindosList")
                .setParameter("idRgbImage", idRgbImage)
                .setParameter("windowCount", roiWindow);
        
        List<RgbWindow> windowsList = (List<RgbWindow>)query.getResultList();
        if(windowsList.isEmpty()){
            throw new Exception("No se encontraron datos para la imagen " + idRgbImage + " y cantidad de ventanas " + roiWindow);
        }
        List<Window> wList = new ArrayList<>();
        Window window;
        List<RgbWindow> subList;
        //inicializamos los rois
        for (int i = 0; i < windowsList.size(); i+=3) {
            subList = windowsList.subList(i, i+3);
            window = new Window(subList);
            wList.add(window);
        }
           
        return wList;
    }
}
