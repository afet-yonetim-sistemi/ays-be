package com.ays.backend.user.service;

import com.ays.backend.user.exception.DeviceNotException;
import com.ays.backend.user.model.EType;
import com.ays.backend.user.model.Type;
import com.ays.backend.user.repository.TypeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class TypeService {

    private final TypeRepository typeRepository;


    public Set<Type> addTypeToUser(Set<String> strDevices ) {

        Set<Type> types = new HashSet<>();

        if (strDevices  != null) {
            strDevices.forEach(role -> {

                switch (role) {
                    case "DEVICE_1":

                        Type deviceOne = null;

                        if(typeRepository.findByName(EType.DEVICE_1).isEmpty()){
                            deviceOne = new Type(EType.DEVICE_1);
                        }else{
                            deviceOne = typeRepository.findByName(EType.DEVICE_1)
                                    .orElseThrow(() -> new DeviceNotException("Error: Device 1 is not found."));
                        }

                        types.add(deviceOne);
                        break;

                    case "DEVICE_2":

                        Type deviceTwo = null;

                        if(typeRepository.findByName(EType.DEVICE_2).isEmpty()){
                            deviceTwo = new Type(EType.DEVICE_2);
                        }else{
                            deviceTwo = typeRepository.findByName(EType.DEVICE_1)
                                    .orElseThrow(() -> new DeviceNotException("Error: Device 1 is not found."));
                        }

                        types.add(deviceTwo);

                        break;

                    default:

                        Type deviceDefault = null;

                        if(typeRepository.findByName(EType.DEVICE_1).isEmpty()){
                            deviceDefault = new Type(EType.DEVICE_1);
                        }else{
                            deviceDefault = typeRepository.findByName(EType.DEVICE_1)
                                    .orElseThrow(() -> new DeviceNotException("Error: VOLUNTARY Role is not found."));
                        }

                        types.add(deviceDefault);

                }

            });
        }else{

            typeRepository.findByName(EType.DEVICE_1).ifPresentOrElse(types::add, () -> types.add(new Type(EType.DEVICE_1)));
        }


        saveTypes(types);

        return types;
    }

    public void saveTypes(Set<Type> types){
        typeRepository.saveAll(types);
    }

}
