package models;

import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.enums.KeyType;
import com.craftmend.storm.api.markers.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialPost extends StormModel {

    @Column(
            notNull = true
    )
    private String content;

    @Column(
            keyType = KeyType.FOREIGN,
            references = {User.class}
    )
    private Integer poster;

}
