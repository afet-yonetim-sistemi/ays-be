package com.ays.backend.user.service.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.ays.backend.user.model.entities.DeviceType;
import com.ays.backend.user.model.enums.DeviceNames;
import com.ays.backend.user.repository.DeviceTypeRepository;
import com.ays.backend.user.service.DeviceTypeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
class DeviceTypeServiceImpl implements DeviceTypeService {

    private final DeviceTypeRepository deviceTypeRepository;

    public Set<DeviceType> addDeviceTypeToUser(Set<String> devices) {
        Set<DeviceType> types = new HashSet<>();

        if (devices != null) {
            types = devices.stream()
                    .map(role -> {
                        switch (role) {
                            case "DEVICE_1":
                                return deviceTypeRepository.findByName(DeviceNames.DEVICE_1)
                                        .orElseGet(() -> new DeviceType(DeviceNames.DEVICE_1));

                            case "DEVICE_2":
                                return deviceTypeRepository.findByName(DeviceNames.DEVICE_2)
                                        .orElseGet(() -> new DeviceType(DeviceNames.DEVICE_2));

                            default:
                                return deviceTypeRepository.findByName(DeviceNames.DEVICE_1)
                                        .orElseGet(() -> new DeviceType(DeviceNames.DEVICE_1));
                        }
                    })
                    .collect(Collectors.toSet());
        } else {
            Set<DeviceType> finalTypes = types;
            deviceTypeRepository.findByName(DeviceNames.DEVICE_1)
                    .ifPresentOrElse(types::add, () -> finalTypes.add(new DeviceType(DeviceNames.DEVICE_1)));
        }

        saveDeviceTypes(types);

        return types;
    }

    public void saveDeviceTypes(Set<DeviceType> deviceTypes) {
        deviceTypeRepository.saveAll(deviceTypes);
    }
}
