package cooklyst.tubes2wbdsoap.models;


import cooklyst.tubes2wbdsoap.utils.SubsStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "subscription")
@Data
@NoArgsConstructor
public class Subscription implements Serializable {
    @Id
    @Column(nullable = false, name = "creator_id")
    private int creatorID;

    @Id
    @Column(nullable = false, name = "subscriber_id")
    private int subscriberID;

    @Column(nullable = false, name = "status")
    private SubsStatus status = SubsStatus.PENDING;
}