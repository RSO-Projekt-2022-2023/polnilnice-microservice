package si.fri.rso.polnilnice.models.converters;

import si.fri.rso.polnilnice.lib.Polnilnice;
import si.fri.rso.polnilnice.models.entities.PolnilniceEntity;

public class PolnilniceConverter {

    public static Polnilnice toDto(PolnilniceEntity entity) {

        Polnilnice dto = new Polnilnice();
        dto.setPolnilniceId(entity.getId());
        dto.setCreated(entity.getCreated());
        dto.setCoord_north(entity.getCoord_north());
        dto.setCoord_east(entity.getCoord_east());
        dto.setChargers(entity.getChargers());
        dto.setAvailable(entity.getAvailable());

        return dto;

    }

    public static PolnilniceEntity toEntity(Polnilnice dto) {

        PolnilniceEntity entity = new PolnilniceEntity();
        entity.setCreated(dto.getCreated());
        entity.setCoord_north(dto.getCoord_north());
        entity.setCoord_east(dto.getCoord_east());
        entity.setChargers(dto.getChargers());
        entity.setAvailable(dto.getAvailable());

        return entity;

    }

}
