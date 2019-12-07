package services;

import entities.Point;
import entities.User;
import lombok.SneakyThrows;
import model.Graphic;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class PointService {
    @PersistenceContext(unitName = "хибернате козел")
    private EntityManager em;
    @EJB
    private Graphic graphic;

    public void save(Point point){
        point.setResult(graphic.isInArea(point));
        em.persist(point);
    }

    @SuppressWarnings("unchecked")
    public List<Point> getAllPoints(User user) {
        Query query = em.createQuery(("select p from Point p WHERE p.user = :id"));
        query.setParameter("id", user);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Point> getAllPointsRecalculated(User user, double r) {
        Query query = em.createQuery(("select p from Point p WHERE p.user = :id"));
        query.setParameter("id", user);

        List<Point> resultList = query.getResultList();
        List<Point> recalculatedList = new ArrayList<>();

        for (Point p : resultList) {
            Point recalculatedPoint = new Point(p.getId(), p.getX(), p.getY(), r, graphic.isInArea(p.getX(), p.getY(), r), user);
            recalculatedList.add(recalculatedPoint);
        }

        return recalculatedList;
    }
}
