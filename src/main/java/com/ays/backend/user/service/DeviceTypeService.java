package com.ays.backend.user.service;

import java.util.Set;

import com.ays.backend.user.model.entities.DeviceType;

/**
 * Business logic service for device type operations
 */
public interface DeviceTypeService {
    /**
     * Adds device type to a user
     *
     * @param devices list of devices to be added to the user
     * @return set of devices that are added to the user
     */
    Set<DeviceType> addDeviceTypeToUser(Set<String> devices);

    /**
     * Saves the device types
     *
     * @param deviceTypes list of device types to be saved to the database.
     */
    void saveDeviceTypes(Set<DeviceType> deviceTypes);
}
