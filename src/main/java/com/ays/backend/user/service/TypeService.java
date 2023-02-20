package com.ays.backend.user.service;

import com.ays.backend.user.exception.DeviceNotFoundException;
import com.ays.backend.user.model.DeviceType;
import com.ays.backend.user.model.Type;
import com.ays.backend.user.repository.TypeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TypeService {

    private final TypeRepository typeRepository;


    public Set<Type> addTypeToUser(Set<String> strDevices) {
        Set<Type> types = new HashSet<>();

        if (strDevices != null) {
            types = strDevices.stream()
                    .map(role -> {
                        switch (role) {
                            case "DEVICE_1":
                                return typeRepository.findByName(DeviceType.DEVICE_1)
                                        .orElseGet(() -> new Type(DeviceType.DEVICE_1));

                            case "DEVICE_2":
                                return typeRepository.findByName(DeviceType.DEVICE_2)
                                        .orElseGet(() -> new Type(DeviceType.DEVICE_2));

                            default:
                                return typeRepository.findByName(DeviceType.DEVICE_1)
                                        .orElseGet(() -> new Type(DeviceType.DEVICE_1));
                        }
                    })
                    .collect(Collectors.toSet());
        } else {
            Set<Type> finalTypes = types;
            typeRepository.findByName(DeviceType.DEVICE_1)
                    .ifPresentOrElse(types::add, () -> finalTypes.add(new Type(DeviceType.DEVICE_1)));
        }

        saveTypes(types);

        return types;
    }

    public void saveTypes(Set<Type> types){
        typeRepository.saveAll(types);
    }

}
