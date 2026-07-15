package seov.se_app.pe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import seov.se_app.pe.entity.BomData;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface BomDataRepository extends JpaRepository<BomData, Long> {
//    Optional<BomData> findByItemCode(String itemCode);
    @Query(value = """
    select A.product_code , A.product_name , A.material_code , A.material_name, A.custom_mode , A.norm_sei , A.norm_seov , A.eng_unit  
    from bom_data a where a.product_code = :design_number order by a.custom_mode
""", nativeQuery = true)
    List<Map<String, Object>> getBomData(String design_number);

    List<BomData> findByProductCodeIn(List<String> productCodes);


}
