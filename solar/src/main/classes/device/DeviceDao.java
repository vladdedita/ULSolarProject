package main.classes.device;

import org.springframework.data.repository.CrudRepository;

public interface DeviceDao extends CrudRepository<Device, Long> {

    Device findByUserId(Integer id);
    Device findByNameAndUserId(String name, Integer id);
    Device findById(Integer id);
}

