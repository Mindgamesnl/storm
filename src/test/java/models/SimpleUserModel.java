package models;

import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.markers.Column;
import com.craftmend.storm.api.markers.Table;
import lombok.Data;

import java.util.UUID;

@Data
@Table(name = "user")
public class SimpleUserModel extends StormModel {

    @Column
    private String userName;

    @Column
    private Integer score;

    @Column
    private UUID minecraftUserId = UUID.randomUUID();

    @Column(
            name = "email",
            defaultValue = "default@craftmend.com"
    )
    private String emailAddress;

}
