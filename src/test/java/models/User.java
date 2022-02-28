package models;

import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.enums.ColumnType;
import com.craftmend.storm.api.markers.Column;
import com.craftmend.storm.api.markers.Table;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Table(name = "storm_user")
public class User extends StormModel {

    @Column
    private String userName;

    @Column
    private Integer score;

    @Column
    private UUID minecraftUserId = UUID.randomUUID();

    @Column(
            type = ColumnType.ONE_TO_MANY,
            references = {SocialPost.class},
            matchTo = "poster"
    )
    private List<SocialPost> posts;

    @Column(
            name = "email",
            defaultValue = "default@craftmend.com"
    )
    private String emailAddress;

}
