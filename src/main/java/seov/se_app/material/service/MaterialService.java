package seov.se_app.material.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import seov.se_app.material.dto.request.MaterialRqNoRequest;
import seov.se_app.material.entity.InventoryTransactionFlow;
import seov.se_app.material.entity.MaterialRequest;
import seov.se_app.material.repository.InventoryTransactionFlowRepository;
import seov.se_app.material.repository.MaterialRequestRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MaterialService {
    @Autowired
    private InventoryTransactionFlowRepository inventoryTransactionFlowRepo;

    @Autowired
    private MaterialRequestRepository materialRequestRepository;
    public InventoryTransactionFlow getTransactionFlow(String flowCode){
        return inventoryTransactionFlowRepo.getTransactionFlow(flowCode);
    }


    public MaterialRequest CreatMaterialRequest(MaterialRqNoRequest request) {
        MaterialRequest materialRequest = new MaterialRequest();
        materialRequest.setRequestNo(request.getRequestNo());
        materialRequest.setFlowCode(request.getFlowCode());
        materialRequest.setFlowName(request.getFlowName());
        materialRequest.setTransactionType(request.getTransactionType());
        return materialRequestRepository.save(materialRequest);

    }



}
