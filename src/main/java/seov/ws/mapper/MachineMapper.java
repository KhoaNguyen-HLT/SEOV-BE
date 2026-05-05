package seov.ws.mapper;

import org.mapstruct.Mapper;
//import seov.se_app.machine.dto.request.MachineCreateRequest;
import seov.ws.dto.request.MachineRequest;
import seov.ws.entity.Machines;

@Mapper(componentModel = "spring")
public interface MachineMapper {
    Machines toMachine(MachineRequest request);

}
