
package com.grupo3.HealthCooperationWeb.repositorios;

/*import com.egg.eggNews.entidades.ObraSocial;*/
import com.grupo3.HealthCooperationWeb.entidades.ObraSocial;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ObraSocialRepository extends JpaRepository<ObraSocial, String> {
    @Query("SELECT o, COUNT(o) AS cantidad FROM ObraSocial o GROUP BY o ORDER BY cantidad DESC")
    List<Object[]> encontrarObrasSocialesMasUtilizadas();
}

