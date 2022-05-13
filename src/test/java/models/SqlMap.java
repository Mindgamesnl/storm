package models;

import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.markers.Column;
import com.craftmend.storm.api.markers.Table;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Table(name = "map_test")
public class SqlMap extends StormModel {

    @Column
    private String mapName;

    @Column(
            storeAsBlob = true
    )
    private Map<String, String> keyValue = new HashMap<>();

}
