package seov.se_app.device.mapper;

import org.mapstruct.Mapper;
import seov.se_app.device.dto.request.DeviceCreateRequest;
import seov.se_app.device.entity.Device;

@Mapper(componentModel = "spring")
public interface DeviceMapper {
    Device todevice( DeviceCreateRequest request);
}
